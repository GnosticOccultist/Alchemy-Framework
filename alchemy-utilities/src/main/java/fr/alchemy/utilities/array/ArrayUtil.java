package fr.alchemy.utilities.array;

import java.lang.reflect.Array;

public final class ArrayUtil {
	
	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
	
	/**
	 * Private constructor to inhibit instantiation of <code>ArrayUtil</code>.
	 */
	private ArrayUtil() {}
	
	/**
	 * Create a new array with the specified type and of the given size.
	 * 
	 * @param <E> The element's type contained in the array.
	 * 
	 * @param type The type of element which will be contained in the array.
	 * @param size The size of the array to create.
	 * @return	   The new array.
	 */
	@SuppressWarnings("unchecked")
	public static <E> E[] create(Class<E> type, int size) {
		return (E[]) Array.newInstance(type, size);
	}
	
    @SuppressWarnings("unchecked")
	public static <T> T[] copyOf(T[] original, int added) {

        Class<? extends Object[]> newType = original.getClass();
        T[] copy = (T[]) create(newType.getComponentType(), original.length + added);

        System.arraycopy(original, 0, copy, 0, Math.min(original.length, copy.length));

        return copy;
    }
	
	/**
	 * Clear all the elements present in the provided array.
	 * 
	 * @param <E> The element's type contained in the array.
	 * 
	 * @param array The array to clear.
	 */
	public static <E> void clear(E[] array) {
		for(int i = 0; i < array.length; i++) {
			array[i] = null;
		}
	}

	/**
	 * Convert the array into a string representation.
	 * 
	 * @param array The array to convert.
	 * @return		The string converted array.
	 */
	public static <E> String toString(AbstractArray<E> array) {
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
}