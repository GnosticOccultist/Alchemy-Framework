package fr.alchemy.utilities.collections.array;

import java.util.Arrays;

import fr.alchemy.utilities.Validator;

/**
 * <code>AbstractArray</code> is an abstract implementation of {@link Array} used by most implementations. It defines
 * methods for settings the internal array and size.
 * 
 * @param <E> The type of elements contained in the array.
 * 
 * @version 0.1.1
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 * 
 * @see Array
 * @see FastArray
 */
public abstract class AbstractArray<E> implements Array<E> {
	
	private static final long serialVersionUID = 6816108702921817326L;
	
	/**
	 * Instantiates a new empty <code>AbstractArray</code> of the provided type and with
	 * an initial capacity of 10.
	 * 
	 * @param type The type of elements to contain (not null).
	 */
	protected AbstractArray(Class<? super E> type) {
		this(type, DEFAULT_CAPACITY);
	}
	
	/**
	 * Instantiates a new empty <code>AbstractArray</code> of the provided type and with
	 * the given initial capacity.
	 * 
	 * @param type	   The type of elements to contain (not null).
	 * @param capacity The initial capacity of the array (&ge;0).
	 */
	@SuppressWarnings("unchecked")
	protected AbstractArray(Class<? super E> type, int capacity) {
		Validator.nonNull(type, "The type can't be null!");
		Validator.nonNegative(capacity, "The size of an array can't be negative!");
		
		setArray((E[]) ArrayUtil.create(type, capacity));
	}
	
	/**
	 * Instantiates a new <code>AbstractArray</code> using the provided array to use
	 * internally. The size is set accordingly to the array length.
	 * 
	 * @param array The internal array to use (not null).
	 */
	protected AbstractArray(E[] array) {
		Validator.nonNull(array, "The internal array can't be null!");
        setArray(array);
        setSize(array.length);
    }
    
    /**
     * Sets the new internal array of the <code>AbstractArray</code>.
     * 
     * @param array The new internal array (not null).
     */
    protected abstract void setArray(E[] array);
    
    /**
     * Sets the new size of the <code>AbstractArray</code>.
     * 
     * @param size The new size of the array (&ge;0).
     */
    protected abstract void setSize(int size);
    
    /**
     * Removes all the elements from the <code>AbstractArray</code> and set its
     * size to 0.
     */
    @Override
    public void clear() {
    	if(!isEmpty()) {
    		ArrayUtil.clear(array());
    		setSize(0);
    	}
    }
    
	@Override
	@SuppressWarnings("unchecked")
    protected AbstractArray<E> clone() throws CloneNotSupportedException {
    	return (AbstractArray<E>) super.clone();
    }
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		
		if(obj == null || !(obj instanceof Array)) {
			return false;
		}
		
		Array<?> other = (Array<?>) obj;
		return size() == other.size() && Arrays.equals(array(), 0, size(), other.array(), 0, other.size());
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " size = " + size() 
				+ " :\n " + ArrayUtil.toString(this);
	}
}
