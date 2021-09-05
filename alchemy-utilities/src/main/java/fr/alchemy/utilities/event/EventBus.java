package fr.alchemy.utilities.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.collections.array.Array;
import fr.alchemy.utilities.collections.array.StampedLockArray;
import fr.alchemy.utilities.logging.FactoryLogger;
import fr.alchemy.utilities.logging.Logger;

/**
 * <code>EventBus</code> dispatches events to listeners and provides a way for listeners to register
 * themselves. This is similar in concept to Guava's EventBus with some notable differences:
 * <ul>
 * <li>Type and event objects are separate which means the same event class can be used 
 * for multiple event types.
 * <li>The event bus is singleton which provides a single central place for event 
 * dispatch and registration.
 * <li>Listeners can be added with an EventListener interface OR by using reflective 
 * methods (similar to Guava's event bus).
 * </ul>
 * 
 * @version 0.2.0
 * @since 0.1.0
 * 
 * @see #addEventListener(EventType, EventListener)
 * @see #addListenerMethods(Object, EventType...)
 * 
 * @author GnosticOccultist
 */
public class EventBus {
	
	/**
	 * The logger for homonculus events.
	 */
	private static final Logger logger = FactoryLogger.getLogger("alchemy.events");
	
	/**
	 * The registered listeners list.
	 */
	private final Listeners all = new Listeners();
	/**
	 * The lock used for threading safety.
	 */
	private final Lock lock = new ReentrantLock();
	/**
	 * The table containing the listeners ranged by their type.
	 */
	private final Map<EventType, Listeners> listenerMap = Collections.synchronizedMap(new ConcurrentHashMap<>());
	
	/**
	 * Instantiates a new <code>EventBus</code> with no listeners.
	 */
	public EventBus() {}
	
	/**
	 * Publishes the specified event to the <code>EventBus</code>, delivering it 
	 * to all listeners registered for the particular {@link EventType}.
	 * 
	 * @param <E> The event's type.
	 * 
	 * @param type  The type of event to be delivered.
	 * @param event The event to publish.
	 */
	public <E> void publishEvent(EventType<E> type, E event) {
		logger.debug("Published event [" + type + ", " + event + "]");
		
		// Deliver to any global listeners first and we don't factor
		// them into the delivery check. The global list is usually used
		// for thing like lifecycle logging and not actual event handling.
		deliver(null, event, all);
		
		boolean delivered = deliver(type, event, getListeners(type));
		
		if(!delivered) {
			logger.debug("Undelivered event type:" + type + " Event:" + event);
		}
	}
	
	/**
	 * Publishes the specified event to the <code>EventBus</code>, delivering it asynchronously
	 * to all listeners registered for the particular {@link EventType}.
	 * <p>
	 * The method uses the {@link ForkJoinPool#commonPool()} to deliver the event asynchronously.
	 * 
	 * @param <E> The event's type.
	 * 
	 * @param type  The type of event to be delivered asynchronously.
	 * @param event The event to publish asynchronously.
	 */
	public <E> void publishAsyncEvent(EventType<E> type, E event) {
		logger.debug("Published event [ type= " + type + ", event= " + event + "]");
		
		// Deliver to any global listeners first and we don't factor
		// them into the delivery check. The global list is usually used
		// for thing like lifecycle logging and not actual event handling.
		deliverAsync(null, event, all);
		
		boolean delivered = deliverAsync(type, event, getListeners(type));
		
		if(!delivered) {
			logger.debug("Undelivered event type:" + type + " Event:" + event);
		}
	}
	
	/**
	 * Adds an {@link EventListener} object that will be notified about events of the
	 * specified {@link EventType}.
	 * 
	 * @param <E> The event's type.
	 * 
	 * @param type	   The type of event.
	 * @param listener The event listener to register.
	 */
	public <E> void addEventListener(EventType<E> type, EventListener<? super E> listener) {
		getListeners(type).add(listener);
	}
	
	/**
	 * Removes the {@link EventListener} object that will be notified about events of the
	 * specified {@link EventType}.
	 * 
	 * @param <E> The event's type.
	 * 
	 * @param type	   The type of event.
	 * @param listener The event listener to unregister.
	 */
	public <E> void removeEventListener(EventType<E> type, EventListener<? super E> listener) {
		getListeners(type).remove(listener);
	}
	
	/**
	 * Removes the {@link EventListener} object that will be notified about events of any
	 * {@link EventType}.
	 * 
	 * @param <E> The event's type.
	 * 
	 * @param listener The event listener to unregister from every types.
	 */
	public <E> void clearEventListener(EventListener<E> listener) {
		for(EventType type : listenerMap.keySet()) {
			listenerMap.get(type).remove(listener);
		}
	}
	
	/**
	 * Adds a generic listener which will have its events delivered through reflection based
	 * on the {@link EventType} names. For example, if the expected event type is {@link ErrorEvent#FATAL_ERROR},
	 * then the event type name is "FatalError" and the expected method name will be either "onFatalError" 
	 * or "fatalError" with a single argument which is the concerned event.
	 * <p>
	 * Note that it is ok if the method isn't public if the current security settings allow overriding method
	 * accessibility.
	 * 
	 * @param listener The listener to register methods from.
	 * @param types	   The events type to create listener for.
	 * 
	 * @throws IllegalArgumentException Thrown if any of the dispatch methods is missing.
	 */
	@SuppressWarnings("deprecation")
	public void addListenerMethods(Object listener, EventType...types) {
		Class clazz = listener.getClass();
		for(EventType type : types) {
			try {
				Method method = findMethod(clazz, type);
				if(!method.isAccessible()) {
					method.setAccessible(true);
				}
				getListeners(type).add(new MethodDispatcher(listener, method));
			} catch (NoSuchMethodException ex) {
				throw new IllegalArgumentException("Event method not found for: " + 
						type + " on object: " + listener, ex);
			}
		}
	}
	
	/**
	 * Removes a generic listener which have been registered using {@link #addListenerMethods(Object, EventType...)}
	 * from any of the specified registered {@link EventType}.
	 * 
	 * @param listener The event listener to unregister.
	 * @param types	   The types of event.
	 */
	@SuppressWarnings("unchecked")
	public void removeListenerMethods(Object listener, EventType...types) {
		for(EventType type : types) {
			Listeners listeners = getListeners(type);
			for(EventListener l : listeners.getArray()) {
				if(!(l instanceof MethodDispatcher)) {
					continue;
				}
				MethodDispatcher md = (MethodDispatcher) l;
				if(md.delegate == listener || md.delegate.equals(listener)) {
					removeEventListener(type, md);
				}
			}
		}
	}
	
	public void addListenerAnnotatedMethods(Object listener, EventType...types) {
		for(Method method : listener.getClass().getDeclaredMethods()) {
			
			// The compiler sometimes creates synthetic bridge methods as part of the
			// type erasure process. As of JDK8, these methods now include the same 
			// annotations as the original declarations. They should be ingnored from 
			// the registering process.
			if(method.isBridge()) {
				continue;
			}
			
			if(method.isAnnotationPresent(EventSubscriber.class)) {
				Class<?>[] parametersTypes = method.getParameterTypes();
				if(parametersTypes.length == 0) {
					throw new IllegalArgumentException("Method " + method + " has @EventSubscriber "
							+ "annotation but has no arguments. Such methods must require at "
							+ "least one argument.");
				}
				
				EventSubscriber annotation = method.getAnnotation(EventSubscriber.class);
				String[] typeNames = annotation.types();
				if(typeNames.length != types.length) {
					throw new IllegalArgumentException("Method " + method + " has @EventSubscriber "
							+ "annotation to handle " + typeNames.length + " event types!");
				}
				
				for(int i = 0; i < types.length; i++) {
					if(types[i].equals(typeNames[i])) {
						getListeners(types[i]).add(new MethodDispatcher(listener, method));
					}
				}
			}
		}
	}
	
	/**
	 * Adds a global listener that will notified about all {@link EventType}. This can be used
	 * to do logging or debugging on a global level and is generally not useful for normal event delivery.
	 * 
	 * @param listener The global listener to register.
	 */
	public void listenAll(EventListener listener) {
		all.add(listener);
	}
	
	/**
	 * Removes a global listener previously registered with the {@link #listenAll(EventListener)}
	 * method.
	 * 
	 * @param listener The global listener to unregister.
	 */
	public void unlistenAll(EventListener listener) {
		all.remove(listener);
	}
	
	/**
	 * Delivers the provided {@link EventType} to all the given listeners.
	 * 
	 * @param type		The type of event.
	 * @param event		The event to deliver.
	 * @param listeners The listeners to deliver the event to.
	 * @return			Whether the event has been delivered to at least one listeners.
	 */
	@SuppressWarnings("unchecked")
	protected <E> boolean deliver(EventType<E> type, E event, Listeners listeners) {
        if(listeners.isEmpty()) {
            return false;
        }
        
        for(EventListener listener : listeners.getArray()) {
        	notify(type, event, listener);
        }
        
        return true;
	}
	
	/**
	 * Delivers the provided {@link EventType} to all the given listeners asynchronously.
	 * 
	 * @param type		The type of event.
	 * @param event		The event to deliver asynchronously.
	 * @param listeners The listeners to deliver the event to.
	 * @return			Whether the event has been delivered to at least one listeners.
	 */
	@SuppressWarnings("unchecked")
	protected <E> boolean deliverAsync(EventType<E> type, E event, Listeners listeners) {
        if(listeners.isEmpty()) {
            return false;
        }
        
        for(EventListener listener : listeners.getArray()) {
        	ForkJoinPool.commonPool().execute(() -> notify(type, event, listener));
        }
        
        return true;
	}
	
	/**
	 * Notify the provided {@link EventType} to the given {@link EventListener} safely.
	 * If an error occurs during the invokation of the delivery method, an {@link ErrorEvent} of type 
	 * {@link ErrorEvent#DISPATCH_ERROR} will be published.
	 * 
	 * @param type 	   The type of event.
	 * @param event	   The event to deliver asynchronously.
	 * @param listener The listener to deliver the event to.
	 */
	private <E> void notify(EventType<E> type, E event, EventListener<E> listener) {
		try {
            listener.newEvent(type, event);
        } catch(Throwable t) {
            logger.error("Error handling event:" + event + " for type:" + type + "  in handler:" + listener, t);
            if(type != ErrorEvent.DISPATCH_ERROR) {
            	publishEvent(ErrorEvent.DISPATCH_ERROR, new ErrorEvent(t, type, event));
            }
        }
	}
	
	/**
	 * Return the list of {@link EventListener} for the specified {@link EventType},
	 * creating the list if necessary.
	 * 
	 * @param type The event type to get all listeners for (not null).
	 * @return	   The pre-existing list of listeners or a new filled one if not 
	 * 			   already present.
	 */
	protected Listeners getListeners(EventType type) {
		Validator.nonNull(type, "The event type can't be null!");
		
		Listeners list = listenerMap.get(type);
		if(list == null) {
			// Now we need to get the lock so we are the only one
			// writing.
			lock.lock();
			try {
				// Check again to see if the list was created while 
				// we waited to get the lock (safe double-checked locking).
				list = listenerMap.get(type);
				if(list != null) {
					return list;
				}
				list = new Listeners();
				listenerMap.put(type, list);
			} finally {
				lock.unlock();
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	protected Method findMethod(Class clazz, EventType type) throws NoSuchMethodException {
		
		// First try the 'one' + name version.
		String name = "on" + type.getName();
		try {
			return clazz.getDeclaredMethod(name, type.getEventClass());
		} catch (NoSuchMethodException ex) {
			// Handle the miss later.
		}
		
		// Else try with the direct name, lower-cased appropriately.
		name = type.getName();
		if(Character.isUpperCase(name.charAt(0)) && Character.isLowerCase(name.charAt(1))) {
			name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
		}
		
		try {
			return clazz.getDeclaredMethod(name, type.getEventClass());
		} catch (NoSuchMethodException ex) {
			// Handle the miss later.
		}
		
		if(clazz.getSuperclass() != null) {
			// Try within the superclass methods.
			return findMethod(clazz.getSuperclass(), type);
		}
		
		throw new NoSuchMethodException("No methods matching the syntax 'on" + type.getName() 
				+ " or " + name + " within " + clazz + " and its subclasses.");
	}
	
	private class MethodDispatcher implements EventListener {
		
		private final Object delegate;
		private final Method method;
		
		public MethodDispatcher(Object delegate, Method method) {
			Validator.nonNull(method, "The listening method can't be null!");
            this.delegate = delegate;
            this.method = method;
		}

		@Override
		public void newEvent(EventType type, Object event) {
			try {
				method.invoke(delegate, event);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
				throw new RuntimeException("Error while calling: " + method + " for event: " + event, ex);
			}
		}
	}
	
	/**
	 * <code>RunnableDispatcher</code> is an implementation of {@link EventListener} that needs to receive one or
	 * multiple events asynchronously before executing a set of {@link Runnable}.
	 * <p>
	 * To create a dispatcher, please refer to one of the two builder classes: {@link SingleEventDispatcherBuilder} and
	 * {@link CombinedEventDispatcherBuilder}.
	 * 
	 * @author GnosticOccultist
	 * 
	 * @see SingleEventDispatcherBuilder
	 * @see CombinedEventDispatcherBuilder
	 */
	private static class RunnableDispatcher implements EventListener {

		/**
		 * The event bus to which the dispatcher is listening.
		 */
		private final EventBus eventBus;
		/**
		 * The event types that needs to be received by the listeners.
		 */
		private final StampedLockArray<EventType<?>> eventTypes;
		/**
		 * A copy of the event types to unregister the listener.
		 */
		private final Array<EventType<?>> eventTypesSet;
		/**
		 * The array of handlers to run when all events have been received.
		 */
		private final Array<Runnable> handlers;
		
		RunnableDispatcher(EventBus eventBus, Array<EventType<?>> eventTypes, Array<Runnable> handlers) {
			this.eventBus = eventBus;
            this.eventTypes = new StampedLockArray<>(EventType.class);
            this.eventTypes.addAll(eventTypes);
            this.eventTypesSet = Array.ofType(EventType.class);
            this.eventTypesSet.addAll(eventTypes);
            this.handlers = handlers;
        }
		
		@Override
		@SuppressWarnings("unchecked")
		public void newEvent(EventType type, Object event) {

			logger.debug(this + " received event '" + type + "'.");
			
			eventTypes.applyInWriteLock(type, Array::remove);
			
			if(!eventTypes.isEmpty()) {
				logger.debug(this + " has still " + eventTypes);
				return;
			}
			
			handlers.forEach(Runnable::run);
			
			logger.debug("Successfully runned handlers for " + this + ".");
			
			eventTypesSet.forEach(eventType ->
            	eventBus.removeEventListener(eventType, this));
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			eventTypesSet.forEach(type -> {
				sb.append(type.getName());
				sb.append(" ");
			});
			return getClass().getSimpleName() + " [ " + sb.toString() + "]";
		}
	}
	
	/**
	 * <code>CombinedEventDispatcherBuilder</code> is a builder class to build and register {@link RunnableDispatcher}
	 * to the {@link EventBus}.
	 * <p>
	 * The created dispatcher will have to listen asynchronously to multiple {@link EventType} before executing a defined handler.
	 * 
	 * @author GnosticOccultist
	 */
	public static class CombinedEventDispatcherBuilder {
		
		/**
         * Instantiates a new <code>CombinedEventDispatcherBuilder</code> which will execute 
         * the provided {@link Runnable}.
         * <p>
         * The event will be registered to the {@link SingletonEventBus}.
         *
         * @param handler The handler of the events.
         * @return 		  A new builder for the handler (not null).
         * 
         * @see #of(EventBus, Runnable)
         */
        public static CombinedEventDispatcherBuilder of(Runnable handler) {
            return new CombinedEventDispatcherBuilder(SingletonEventBus.getInstance(), handler);
        }

        /**
         * Instantiates a new <code>CombinedEventDispatcherBuilder</code> which will execute 
         * the provided {@link Runnable}.
         *
         * @param eventBus The event bus to register the event to (not null).
         * @param handler  The handler of the events.
         * @return 		   A new builder for the handler (not null).
         * 
         * @see #of(Runnable)
         */
        public static CombinedEventDispatcherBuilder of(EventBus eventBus, Runnable handler) {
            return new CombinedEventDispatcherBuilder(eventBus, handler);
        }

        /**
		 * The event bus to register the listener to.
		 */
		private final EventBus eventBus;
        /**
         * The array of listened event types.
         */
        private final Array<EventType<?>> eventTypes;
        /**
         * The result handler for the events.
         */
        private final Runnable handler;

        /**
         * Private constructor to inhibit instantiation of <code>CombinedEventDispatcherBuilder</code>.
         * Please use {@link #of(Runnable)} or {@link #of(EventBus, Runnable)}.
         */
        private CombinedEventDispatcherBuilder(EventBus eventBus, Runnable handler) {
        	Validator.nonNull(eventBus, "The event bus can't be null!");
        	Validator.nonNull(handler, "The handler can't be null!");
        	this.eventBus = eventBus;
            this.handler = handler;
            this.eventTypes = Array.ofType(EventType.class);
        }

        /**
         * Add the provided {@link EventType} for the dispatcher to listen to once before executing 
         * its handler.
         *
         * @param eventType The additional event type to listen (not null).
         * @return			The builder for chaining purposes (not null).
         */
        public CombinedEventDispatcherBuilder add(EventType<?> eventType) {
            return add(eventType, 1);
        }

        /**
         * Add the provided {@link EventType} for the dispatcher to listen to a certain number of times 
         * before executing its handler.
         *
         * @param eventType The additional event type to listen (not null).
         * @param count		The number of times the event type has to be listen (&ge;1).
         * @return			The builder for chaining purposes (not null).
         */
        public CombinedEventDispatcherBuilder add(EventType<?> eventType, int count) {
        	Validator.nonNull(eventType, "The event type can't be null!");
        	Validator.inRange(count, 1, Integer.MAX_VALUE);
        	
            for (int i = 0; i < count; i++) {
                eventTypes.add(eventType);
            }

            return this;
        }
        
        /**
         * Add the provided {@link EventType} for the dispatcher to listen to once before executing 
         * its handler.
         *
         * @param types The additional event types to listen (not null).
         * @return		The builder for chaining purposes (not null).
         */
        public CombinedEventDispatcherBuilder addAll(EventType<?>... types) {
        	this.eventTypes.addAll(types);
        	return this;
        }

        /**
         * Creates and register a corresponding {@link RunnableDispatcher} to the {@link EventBus}.
         */
        @SuppressWarnings("unchecked")
		public void register() {

            if (eventTypes.isEmpty()) {
                throw new IllegalStateException("The list of listened events should not be empty.");
            }

            RunnableDispatcher resultHandler = new RunnableDispatcher(eventBus, eventTypes, Array.of(handler));

            Array<EventType<?>> eventTypesSet = resultHandler.eventTypesSet;
            eventTypesSet.forEach(eventType ->
                    eventBus.addEventListener(eventType, resultHandler));
        }
    }
	
	public static class SingleEventDispatcherBuilder {
		
		/**
         * Instantiates a new <code>SingleEventDispatcherBuilder</code> which will execute 
         * some registered {@link Runnable} after listening to the provided {@link EventType}.
         * <p>
         * The event will be registered to the {@link SingletonEventBus}.
         *
         * @param handler The event type to listen to (not null).
         * @return 		  A new builder for the event type (not null).
         * 
         * @see #of(EventBus, EventType)
         */
		public static SingleEventDispatcherBuilder of(EventType<?> eventType) {
			return of(SingletonEventBus.getInstance(), eventType);
		}
		
		/**
         * Instantiates a new <code>SingleEventDispatcherBuilder</code> which will execute 
         * some registered {@link Runnable} after listening to the provided {@link EventType}.
         *
         * @param eventBus The event bus to register the event to (not null).
         * @param handler  The event type to listen to (not null).
         * @return 		   A new builder for the event type (not null).
         * 
         * @see #of(EventType)
         */
		public static SingleEventDispatcherBuilder of(EventBus eventBus, EventType<?> eventType) {
			return new SingleEventDispatcherBuilder(eventBus, eventType);
		}
		
		/**
		 * The event bus to register the listener to.
		 */
		private final EventBus eventBus;
		/**
		 * The listened event type.
		 */
		private final EventType<?> eventType;
		/**
		 * The array of result handlers to execute.
		 */
		private final Array<Runnable> handlers;
		
		/**
         * Private constructor to inhibit instantiation of <code>SingleEventDispatcherBuilder</code>.
         * Please use {@link #of(EventType)} or {@link #of(EventBus, EventType)}.
         */
		private SingleEventDispatcherBuilder(EventBus eventBus, EventType<?> eventType) {
			Validator.nonNull(eventBus, "The event bus can't be null!");
			Validator.nonNull(eventType, "The event type can't be null!");
			this.eventBus = eventBus;
            this.handlers = Array.ofType(Runnable.class);
            this.eventType = eventType;
        }
		
		/**
		 * Add the provided {@link Runnable} for the dispatcher to executed after listening
		 * to the {@link EventType}.
		 * 
		 * @param handler The additional handler to execute (not null).
		 * @return		  The builder for chaining purposes (not null).
		 */
		public SingleEventDispatcherBuilder add(Runnable handler) {
			Validator.nonNull(handler, "The handler can't be null!");
			handlers.add(handler);
			return this;
		}
		
		/**
		 * Add the provided {@link Runnable} for the dispatcher to executed after listening
		 * to the {@link EventType}.
		 * 
		 * @param handler The additional handler to execute (not null).
		 * @return		  The builder for chaining purposes (not null).
		 */
		public SingleEventDispatcherBuilder addAll(Runnable... handler) {
			Validator.nonNull(handler, "The handlers can't be null!");
			handlers.addAll(handler);
			return this;
		}
		
		/**
         * Creates and register a corresponding {@link RunnableDispatcher} to the {@link EventBus}.
         */
		@SuppressWarnings("unchecked")
		public void register() {
			if(handlers.isEmpty()) {
				throw new IllegalStateException("The list of handlers should not be empty.");
			}
			
			RunnableDispatcher resultHandler = new RunnableDispatcher(eventBus, Array.of(eventType), handlers);
			
			resultHandler.eventTypesSet.forEach(eventType -> {
				eventBus.addEventListener(eventType, resultHandler);
			});
		}
	}
	
	private class Listeners {
		
		private final List<EventListener> list = new ArrayList<>();
		private volatile EventListener[] array = null;
		
		public Listeners() {
			resetArray();
		}
		
		public void add(EventListener listener) {
			// Go ahead and grab the central lock.
			lock.lock();
			try {
				list.add(listener);
				
				// Implement 'copy on write'.
				resetArray();
			} finally {
				lock.unlock();
			}
		}
		
		public void remove(EventListener listener) {
			// Go ahead and grab the central lock.
			lock.lock();
			try {
				list.remove(listener);
				
				// Implement 'copy on write'.
				resetArray();
			} finally {
				lock.unlock();
			}
		}
		
		protected boolean isEmpty() {
			// Relatively safe here because we know a little about how
			// ArrayList is implemented and our constant usage of a volatile
			// variable means this threads view of the data structure is likely
			// to be up to date.
			return list.isEmpty();
		}
		
		protected final void resetArray() {
			// Presumes we already have the lock.
			// Can't reuse the array or we create the exact threading problem
			// we were trying to avoid.
			array = list.toArray(new EventListener[list.size()]);
		}
		
		protected final EventListener[] getArray() {
			return array;
		}
	}
}
