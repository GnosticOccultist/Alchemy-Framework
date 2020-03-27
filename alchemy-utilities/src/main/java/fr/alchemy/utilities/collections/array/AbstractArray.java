package fr.alchemy.utilities.collections.array;

import fr.alchemy.utilities.Validator;

public abstract class AbstractArray<E> implements Array<E> {
	
	private static final long serialVersionUID = 6816108702921817326L;
	/**
	 * The default size of a new array.
	 */
	protected static final int DEFAULT_SIZE = 10;
	
	public AbstractArray(Class<? super E> type) {
		this(type, DEFAULT_SIZE);
	}
	
	@SuppressWarnings("unchecked")
	public AbstractArray(Class<? super E> type, int size) {
		super();
		Validator.nonNegative(size, "The size of an array can't be negative!");
		
		setArray((E[]) ArrayUtil.create(type, size));
	}
	
    public AbstractArray(E[] array) {
        super();
        setArray(array);
        setSize(array.length);
    }
    
    @Override
    public boolean slowRemove(Object object) {
        int index = indexOf(object);
        
        if (index >= 0) {
            slowRemove(index);
        }
        
        return index >= 0;
    }
    
    /**
     * Sets the new internal array of the <code>AbstractArray</code>.
     * 
     * @param array The new internal array.
     */
    protected abstract void setArray(E[] array);
    
    /**
     * Sets the new size of the <code>AbstractArray</code>.
     * 
     * @param size The new size of the array.
     */
    protected abstract void setSize(int size);
    
    public void clear() {
    	if(!isEmpty()) {
    		ArrayUtil.clear(array());
    		setSize(0);
    	}
    }
    
    @Override
    public void free() {
    	clear();
    }
    
	@Override
	@SuppressWarnings("unchecked")
    protected AbstractArray<E> clone() throws CloneNotSupportedException {
    	return (AbstractArray<E>) super.clone();
    }
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " size = " + size() 
				+ " :\n " + ArrayUtil.toString(this);
	}
}
