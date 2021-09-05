package fr.alchemy.utilities.collections.array;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import fr.alchemy.utilities.Validator;

/**
 * <code>ArrayCollectors</code> is a utility class to create {@link Collector} using {@link Array} implementations.
 * It is therefore similar to the {@link Collectors} class from Java's {@link Collection}.
 * 
 * @version 0.2.0
 * @since 0.2.0
 * 
 * @author GnosticOccultist
 */
public final class ArrayCollectors {
	
	/**
	 * The set of characteristics to specify that a collector's finisher function is an identity function.
	 */
	private static final Set<Collector.Characteristics> CH_ID = Collections.unmodifiableSet(
			EnumSet.of(Collector.Characteristics.IDENTITY_FINISH)
	);
	
	/**
	 * The set of characteristics to specify that a collector's finisher function is an identity function while it 
	 * can be populated concurrently.
	 */
	private static final Set<Collector.Characteristics> CH_CONCURRENT_ID = Collections.unmodifiableSet(
			EnumSet.of(Collector.Characteristics.IDENTITY_FINISH, 
						Collector.Characteristics.UNORDERED, 
						Collector.Characteristics.CONCURRENT)
	);

	/**
	 * Creates and return a {@link Collector} that accumulates the input elements into a new {@link Array}
	 * implementation instantiated using the given {@link Supplier}.
	 * 
	 * @param <T> The type of input elements.
	 * @param <A> The type of array to store collected elements in.
	 * 
	 * @param type 	   The type of input elements (not null).
	 * @param supplier The supplier to use for creating an array (not null).
	 * @return	   	   A new collector which collects elements in encounter order into an array (not null).
	 * 
	 * @see #toArray(Class)
	 */
	public static <T, A extends Array<T>> Collector<T, A, A> toArray(Class<? super T> type, Supplier<A> supplier) {
		Validator.nonNull(type, "The type of elements can't be null!");
		Validator.nonNull(supplier, "The supplier can't be null!");
		return new SimpleIdentityCollector<>(type, supplier);
	}
	
	/**
	 * Creates and return a {@link Collector} that accumulates the input elements into an {@link Array}.
	 * There is no guarantees on the type, mutability or thread-safety of the returned array, however you can
	 * specify your own implementation using {@link #toArray(Class, Supplier)}.
	 * 
	 * @param <T> The type of input elements.
	 * @param <A> The type of array to store collected elements in.
	 * 
	 * @param type The type of input elements (not null).
	 * @return	   A new collector which collects elements in encounter order into an array (not null).
	 * 
	 * @see #toArray(Class, Supplier)
	 */
	public static <T, A extends Array<T>> Collector<T, A, A> toArray(Class<? super T> type) {
		Validator.nonNull(type, "The type of elements can't be null!");
		return new SimpleIdentityCollector<>(type);
	}
	
	/**
	 * Creates and return a {@link Collector} that accumulates the input elements into a {@link ReadOnlyArray}.
	 * 
	 * @param <T> The type of input elements.
	 * @param <A> The type of array to store collected elements in.
	 * 
	 * @param type The type of input elements (not null).
	 * @return	   A new collector which collects elements in encounter order into a read-only array (not null).
	 */
	public static <T, A extends Array<T>> Collector<T, A, ReadOnlyArray<T>> toReadOnlyArray(Class<? super T> type) {
		return new Collector<T, A, ReadOnlyArray<T>>() {
			
			@Override
			@SuppressWarnings("unchecked")
			public Supplier<A> supplier() {
				return () -> (A) Array.ofType(type);
			}

			@Override
			public BiConsumer<A, T> accumulator() {
				return Array::add;
			}

			@Override
			public BinaryOperator<A> combiner() {
				return (source, toAdd) -> {
                    source.addAll(toAdd);
                    return source;
                };
			}

			@Override
			public Function<A, ReadOnlyArray<T>> finisher() {
				return array -> array.isEmpty() ? Array.empty() : Array.of(array);
			}

			@Override
			public Set<Characteristics> characteristics() {
				return Collections.emptySet();
			}
		};
	}
	
	/**
	 * Creates and return a {@link Collector} that accumulates the input elements into a {@link ConcurrentArray}.
	 * 
	 * @param <T> The type of input elements.
	 * @param <A> The type of array to store collected elements in.
	 * 
	 * @param type The type of input elements (not null).
	 * @return	   A new collector which collects elements in encounter order into a concurrent array (not null).
	 */
	public static <T, A extends ConcurrentArray<T>> Collector<T, A, A> toConcurrentArray(Class<? super T> type) {
		return new Collector<T, A, A>() {
			
			@Override
			@SuppressWarnings("unchecked")
			public Supplier<A> supplier() {
				return () -> (A) new StampedLockArray<>(type);
			}

			@Override
			public BiConsumer<A, T> accumulator() {
				return (source, toAdd) -> source.applyInWriteLock(toAdd, Array::add);
			}

			@Override
			public BinaryOperator<A> combiner() {
				return (source, toAdd) -> {
					source.applyInWriteLock(toAdd, Array::addAll);
                    return source;
                };
			}

			@Override
			public Function<A, A> finisher() {
				return array -> array;
			}

			@Override
			public Set<Characteristics> characteristics() {
				return CH_CONCURRENT_ID;
			}
		};
	}
	
	static class SimpleIdentityCollector<T, A extends Array<T>> implements Collector<T, A, A> {
		
		/**
		 * The type of elements in the array.
		 */
		Class<? super T> type;
		/**
		 * The custom supplier to instantiate a new array.
		 */
		Supplier<A> supplier;
		
		SimpleIdentityCollector(Class<? super T> type) {
			this(type, null);
		}
		
		SimpleIdentityCollector(Class<? super T> type, Supplier<A> supplier) {
			Validator.nonNull(type, "The type of elements can't be null!");
			this.type = type;
			this.supplier = supplier;
		}

		@Override
		@SuppressWarnings("unchecked")
		public Supplier<A> supplier() {
			return supplier != null ? supplier : () -> (A) Array.ofType(type);
		}

		@Override
		public BiConsumer<A, T> accumulator() {
			return Array::add;
		}

		@Override
		public BinaryOperator<A> combiner() {
			return (source, toAdd) -> {
                source.addAll(toAdd);
                return source;
            };
		}

		@Override
		public Function<A, A> finisher() {
			return array -> array;
		}

		@Override
		public Set<Characteristics> characteristics() {
			return CH_ID;
		}
	}
}
