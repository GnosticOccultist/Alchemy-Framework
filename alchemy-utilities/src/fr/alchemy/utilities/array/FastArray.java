package fr.alchemy.utilities.array;

import java.util.Collection;
import java.util.Iterator;

import fr.alchemy.utilities.Validator;

public class FastArray<E> extends AbstractArray<E> {
	
	private static final long serialVersionUID = -6711056773756527290L;

	/**
	 * The unsafe array.
	 */
	protected E[] array;
	
	/**
	 * The current size of this array.
	 */
	protected int size;
	
	public FastArray(Class<? super E> type) {
		super(type);
	}
	
	public FastArray(Class<? super E> type, int size) {
		super(type, size);
	}
	
    public FastArray(E[] array) {
        super(array);
    }
    
    @Override
    public boolean add(E object) {
    	if(size == array.length) {
    		array = ArrayUtil.copyOf(array, Math.max(array.length >> 1, 1));
    	}
    	
    	return unsafeAdd(object);
    }
    
    @Override
    public boolean addAll(Collection<? extends E> collection) {
        if (collection.isEmpty()) {
            return false;
        }

        int current = array.length;
        int selfSize = size();
        int targetSize = collection.size();
        int diff = selfSize + targetSize - current;

        if (diff > 0) {
            array = ArrayUtil.copyOf(array, Math.max(current >> 1, diff));
        }

        for (E element : collection) {
            unsafeAdd(element);
        }

        return true;
    }
    
    public boolean unsafeAdd(E object) {
		array[size++] = object;
		return true;
	}
    
    @Override
    public final E get(int index) {
    	Validator.inRange(index, 0, size());
        return array[index];
    }

	@Override
    public final E[] array() {
    	return array;
    }
    
    @Override
    protected final void setArray(E[] array) {
    	this.array = array;
    }
    
    @Override
    public final int size() {
    	return size;
    }
    
    @Override
    protected final void setSize(int size) {
    	this.size = size;
    }

	@Override
	public E fastRemove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E slowRemove(int index) {
		Validator.inRange(index, 0, size());

        int numMoved = size - index - 1;
        E old = array[index];
        
        if (numMoved > 0) {
            System.arraycopy(array, index + 1, array, index, numMoved);
        }

        size -= 1;
        array[size] = null;

        return old;
	}

	@Override
	public E set(int index, E element) {
		if(index < 0 || index >= size) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		E previous = array[index];
		if(previous != null) {
			size -= 1;
		}
		
		array[index] = element;
		
		size += 1;
		
		return previous;
	}

	@Override
	public Iterator<E> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		throw new UnsupportedOperationException();
	}
}
