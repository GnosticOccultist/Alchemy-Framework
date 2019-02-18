package fr.alchemy.utilities;

import java.util.Comparator;

/**
 * <code>SortUtil</code> is a utility class to sort array using various sorting algorithm
 * such as <code>Shellsort</code>.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public final class SortUtil {
	
	/**
	 * Private constructor to inhibit instantiation of <code>SortUtil</code>.
	 */
	private SortUtil() {}
	
	/**
	 * Performs an in-place shellsort (extension of insertion sort) of the provided array.
	 * 
	 * @param array The array to sort.
	 * @param left  The left index of the range to sort.
	 * @param right	The right index (inclusive) of the range to sort.
	 * @param comp  The comparator object to compare values.
	 */
    public static <T> void shellSort(final T[] array, final int left, final int right, final Comparator<? super T> comp) {
        int h;
        for (h = 1; h <= (right - 1) / 9; h = 3 * h + 1) {
            ;
        }
        for (; h > 0; h /= 3) {
            for (int i = left + h; i <= right; i++) {
                int j = i;
                final T val = array[i];
                while (j >= left + h && comp.compare(val, array[j - h]) < 0) {
                    array[j] = array[j - h];
                    j -= h;
                }
                array[j] = val;
            }
        }
    }
}
