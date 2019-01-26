package fr.alchemy.editor.api.editor.graph.event;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.Event;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TouchEvent;

/**
 * <code>GraphEventManager</code> is reponsible of handling the {@link GraphInputGesture} events by keeping
 * track of their activity and checking that they can be activated.
 * 
 * @author GnosticOccultist
 */
public class GraphEventManager {
	
	/**
	 * The single-instance of the graph event manager.
	 */
	private static GraphEventManager INSTANCE = null;
	
	/**
	 * Return the single-instance of the <code>GraphEventManager</code>.
	 * 
	 * @return The single-instance of the manager.
	 */
	public static GraphEventManager instance() {
		if(INSTANCE == null) {
			INSTANCE = new GraphEventManager();
		}
		
		return INSTANCE;
	}
	
	/**
	 * The current gesture property.
	 */
	private final ObjectProperty<GraphInputGesture> gesture = new ObjectPropertyBase<GraphInputGesture>() {
		
		@Override
		public Object getBean() {
			return GraphEventManager.this;
		}

		@Override
		public String getName() {
			return "inputGesture";
		}
	};
	
	/**
	 * Return the currently active {@link GraphInputGesture}. 
	 * 
	 * @return The current gesture or null if none.
	 */
	public GraphInputGesture getInputGesture() {
		return gesture.get();
	}
	
	/**
	 * Activate the specified {@link GraphInputGesture}.
	 * 
	 * @param inputMode The gesture to activate.
	 */
	public void activateInputGesture(final GraphInputGesture inputMode) {
		gesture.set(inputMode);
	}
	
	/**
	 * Finishes the expected {@link GraphInputGesture} and return whether the 
	 * current gesture was the one expected and has been correctly finished.
	 * 
	 * @param expected The expected gesture to be finished.
	 * @return		   Whether the state as changed after this operation.
	 */
	public boolean finishInputGesture(final GraphInputGesture expected) {
		if(getInputGesture() == expected) {
			gesture.set(null);
			return true;
		}
		return false;
	}
	
	/**
	 * Return the {@link GraphInputGesture} property of the <code>GraphEventManager</code>.
	 * 
	 * @return The gesture property of the event manager.
	 */
	public ObjectProperty<GraphInputGesture> inputGestureProperty() {
		return gesture;
	}
	
	/**
	 * Checks whether the specified {@link GraphInputGesture} can be activated with the provided
	 * event. The state of the event is used to tell if the gesture can be activated.
	 * 
	 * @param gesture The gesture to check.
	 * @param event	  The event that is called for the gesture.
	 * @return		  Whether the gesture can be activated based on the event state.
	 */
	public boolean canActivate(final GraphInputGesture gesture, final Event event) {
		final GraphInputGesture current = getInputGesture();
		if(current == gesture) {
			return true;
		} else if(current == null) {
			final boolean isTouch = event instanceof TouchEvent 
					|| event instanceof MouseEvent && ((MouseEvent) event).isSynthesized() 
					|| event instanceof ScrollEvent && ((ScrollEvent) event).getTouchCount() > 0;
			if(!isTouch) {
				switch (gesture) {
					case CONNECT:
					case MOVE:
					case RESIZE:
					case SELECT:
						return event instanceof MouseEvent && ((MouseEvent) event).isPrimaryButtonDown();
				}
			} else {
				switch (gesture) {
					case CONNECT:
						break;
					case MOVE:
						break;
					case RESIZE:
						break;
					case SELECT:
						return true;		
				}
			}
		}
		
		return false;
	}
}
