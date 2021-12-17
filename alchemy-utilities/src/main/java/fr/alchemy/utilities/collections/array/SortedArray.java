package fr.alchemy.utilities.collections.array;

import java.util.Collection;
import java.util.Comparator;

import fr.alchemy.utilities.Validator;

/**
 * <code>SortedArray</code> is an interface extending {@link Array} to implement dynamic sorted arrays using the Java's {@link Collection} interface.
 * 
 * @param <E> The type of elements contained in the array, may be comparable.
 * 
 * @version 0.3.0
 * @since 0.3.0
 * 
 * @author GnosticOccultist
 * 
 * @see Array
 * @see SortedFastArray
 */
public interface SortedArray<E> extends Array<E> {

	/**
	 * Instantiates a new {@link SortedArray} which will contain the provided element's type
	 * and with an initial capacity of 10 elements.
	 * 
	 * @param <T> The type of comparable element contained in the array.
	 * 
	 * @param type The type of elements to contain and sort (not null).
	 * @return	   A new array instance (not null).
	 */
	static <T extends Comparable<T>> SortedArray<T> ofType(Class<? super T> type) {
		Validator.nonNull(type, "The type can't be null!");
		return new SortedFastArray<T>(type);
	}
	
	/**
	 * Instantiates a new {@link SortedArray} which will contain the provided element's type
	 * and with an initial capacity of 10 elements.
	 * The array will use the given {@link Comparator} to sort its elements.
	 * 
	 * @param <T> The type of element contained in the array.
	 * 
	 * @param type 		 The type of elements to contain (not null).
	 * @param comparator The comparator to use for sorting the array (not null).
	 * @return	   		 A new array instance (not null).
	 */
	static <T> SortedArray<T> ofType(Class<? super T> type, Comparator<? super T> comparator) {
		Validator.nonNull(type, "The type can't be null!");
		Validator.nonNull(comparator, "The comparator can't be null!");
		return new SortedFastArray<T>(type, comparator);
	}
	
	/**
	 * Return the {@link Comparator} used by the <code>SortedArray</code> or
	 * {@link Comparator#naturalOrder()} if no comparator was defined.
	 * 
	 * @return The comparator used to sort the elements (not null).
	 */
	Comparator<? super E> comparator();
}
