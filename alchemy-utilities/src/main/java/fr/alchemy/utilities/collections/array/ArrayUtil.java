package fr.alchemy.utilities.collections.array;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Supplier;

import fr.alchemy.utilities.Validator;

/**
 * <code>ArrayUtil</code> provides utility functions to manipulate or instantiates {@link Collection} and {@link Array}.
 * 
 * @version 0.1.1
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public final class ArrayUtil {
	
	/**
	 * An empty array of generic object elements.
	 */
	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
	
	/**
	 * Private constructor to inhibit instantiation of <code>ArrayUtil</code>.
	 */
	private ArrayUtil() {}
	
	/**
	 * Create a new array with the specified element type that it will contain and of the given size.
	 * 
	 * @param <E> The element's type contained in the array.
	 * 
	 * @param type The type of element which will be contained in the array (not null).
	 * @param size The size of the array to create (&ge;0).
	 * @return	   The new array (not null).
	 */
	@SuppressWarnings("unchecked")
	public static <E> E[] create(Class<E> type, int size) {
		Validator.nonNull(type, "The type of elements can't be null!");
		Validator.nonNegative(size, "The size of the array can't be negative!");
		
		return (E[]) Array.newInstance(type, size);
	}
	
	/**
	 * Create and return a copy of the given array with the provided added capacity compared to its original size.
	 * 
	 * @param <E> The element's type contained in the array.
	 * 
	 * @param original The type of element which will be contained in the array (not null).
	 * @param added    The amount of size to add to the copy (&ge;0).
	 * @return	       A copy of the given array with the desired incremented size (not null).
	 */
    @SuppressWarnings("unchecked")
	public static <E> E[] copyOf(E[] original, int added) {
    	Validator.nonNull(original, "The array of elements to copy can't be null!");
		Validator.nonNegative(added, "The amount to add to the size of the array can't be negative!");
    	
        Class<? extends Object[]> newType = original.getClass();
        E[] copy = (E[]) create(newType.getComponentType(), original.length + added);

        System.arraycopy(original, 0, copy, 0, Math.min(original.length, copy.length));

        return copy;
    }
	
	/**
	 * Clear all the elements present in the provided array.
	 * 
	 * @param <E> The element's type contained in the array.
	 * 
	 * @param array The array to clear (not null).
	 * @return 		The cleared array from all its elements (not null).
	 */
	public static <E> E[] clear(E[] array) {
		Validator.nonNull(array, "The array can't be null!");
		for(int i = 0; i < array.length; i++) {
			array[i] = null;
		}
		return array;
	}
	
	/**
	 * Fill the provided array using the given {@link Supplier} of elements.
	 * 
	 * @param <E> The element's type contained in the array.
	 * 
	 * @param array	  The array to fill (not null).
	 * @param factory The factory to fill the array with (not null).
	 * @return 		  The array filled with the supplier (not null).
	 */
	public static <E> E[] fill(E[] array, Supplier<E> factory) {
		Validator.nonNull(array, "The array can't be null!");
		Validator.nonNull(factory, "The factory can't be null!");
    	for(int i = 0; i < array.length; i++) {
    		array[i] = factory.get();
    	}
    	return array;
    }
	
	/**
	 * Performs the provided {@link Consumer} for all the elements of the given array.
	 * 
	 * @param <E> The element's type contained in the array.
	 * 
	 * @param array	   The array to perform the action with (not null).
	 * @param consumer The consumer to execute an action on each element (not null).
	 * @return 		   The array for chaining purposes (not null).
	 */
	public static <E> E[] forEach(E[] array, Consumer<E> consumer) {
		Validator.nonNull(array, "The array can't be null!");
		Validator.nonNull(consumer, "The factory can't be null!");
    	for(int i = 0; i < array.length; i++) {
    		consumer.accept(array[i]);
    	}
    	return array;
    }
	
	/**
	 * Performs an in-place shellsort (extension of insertion sort) of the provided array using
	 * the specified {@link Comparator} to sort the array elements.
	 * 
	 * @param <E> The element's type contained in the array.
	 * 
	 * @param array The array to sort (not null).
	 * @param left  The left index of the range to sort (&ge;0, &lt;array.length).
	 * @param right	The right index (inclusive) of the range to sort (&ge;0, &lt;array.length).
	 * @param comp  The comparator object to compare values (not null).
	 */
    public static <E> void shellSort(E[] array, int left, int right, Comparator<? super E> comp) {
    	Validator.nonNull(array, "The array to sort can't be null!");
    	Validator.nonNull(comp, "The comparator to sort the array can't be null!");
    	
    	Validator.inRange(left, "The left index is out of bounds!", 0, array.length - 1);
    	Validator.inRange(right, "The right index is out of bounds!", 0, array.length - 1);
    	
        int h;
        for (h = 1; h <= (right - 1) / 9; h = 3 * h + 1) {
            ;
        }
        for (; h > 0; h /= 3) {
            for (int i = left + h; i <= right; i++) {
                int j = i;
                final E val = array[i];
                while (j >= left + h && comp.compare(val, array[j - h]) < 0) {
                    array[j] = array[j - h];
                    j -= h;
                }
                array[j] = val;
            }
        }
    }
    
    /**
	 * Performs an in-place shellsort (extension of insertion sort) of the provided array using
	 * the {@link Comparable} interface to sort the array elements.
	 * 
	 * @param <E> The element's type contained in the array and implementing {@link Comparable}.
	 * 
	 * @param array The array to sort (not null).
	 * @param left  The left index of the range to sort (&ge;0, &lt;array.length).
	 * @param right	The right index (inclusive) of the range to sort (&ge;0, &lt;array.length).
	 */
    public static <E extends Comparable<E>> void shellSort(E[] array, int left, int right) {
    	Validator.nonNull(array, "The array to sort can't be null!");
    	
    	Validator.inRange(left, "The left index is out of bounds!", 0, array.length - 1);
    	Validator.inRange(right, "The right index is out of bounds!", 0, array.length - 1);
    	
        int h;
        for (h = 1; h <= (right - 1) / 9; h = 3 * h + 1) {
            ;
        }
        for (; h > 0; h /= 3) {
            for (int i = left + h; i <= right; i++) {
                int j = i;
                final E val = array[i];
                while (j >= left + h && val.compareTo(array[j - h]) < 0) {
                    array[j] = array[j - h];
                    j -= h;
                }
                array[j] = val;
            }
        }
    }

	/**
	 * Convert the provided {@link fr.alchemy.utilities.collections.array.Array Array} into a readable string representation.
	 * 
	 * @param <E> The element's type contained in the array.
	 * 
	 * @param array The array to convert (not null).
	 * @return		A string representing the given array (not null).
	 */
	public static <E> String toString(fr.alchemy.utilities.collections.array.Array<E> array) {
		Validator.nonNull(array, "The array can't be null!");
		if(array.isEmpty()) {
			return "[]";
		}
		
		String className = array.array().getClass().getSimpleName();
		final StringBuilder builder = new StringBuilder(className.substring(0, className.length() - 1));
		
        for (int i = 0, length = array.size() - 1; i <= length; i++) {

            builder.append(array.get(i).toString());

            if (i == length) {
                break;
            }

            builder.append(", ");
        }

        builder.append("]");
        return builder.toString();
	}
    
	/**
	 * Adds all the provided elements to the given {@link Collection}.
	 * 
	 * @param <E> The element's type contained in the array.
	 * 
	 * @param collection The list to fill with the array (not null).
	 * @param elements 	 The elements to add to the collection (not null).
	 * @return		   	 The collection filled with the given elements (not null).
	 */
    public static <E> Collection<E> addAll(Collection<E> collection, E[] elements) {
    	for(E element : elements) {
    		collection.add(element);
    	}
    	
    	return collection;
    }
}
