package fr.alchemy.utilities.collections.array;

import java.util.Comparator;

public class SortedFastArray<E> extends FastArray<E> implements SortedArray<E> {
	
	private static final long serialVersionUID = 1L;
	
	private final Comparator<? super E> comparator;
	
	public SortedFastArray(Class<? super E> type) {
		super(type);
		this.comparator = null;
	}

	public SortedFastArray(Class<? super E> type, Comparator<? super E> comparator) {
		super(type);
		this.comparator = comparator;
	}
	
	public SortedFastArray(Class<? super E> type, int size) {
		super(type, size);
		this.comparator = null;
	}

	public SortedFastArray(Class<? super E> type, int size, Comparator<? super E> comparator) {
		super(type, size);
		this.comparator = comparator;
	}
	
	public SortedFastArray(E[] array) {
		this(array, null);
	}
	
	public SortedFastArray(E[] array, Comparator<? super E> comparator) {
        super(array);
        this.comparator = comparator;
    }
	
	@Override
	@SuppressWarnings("unchecked")
	protected boolean unsafeAdd(E element) {
		if (comparator == null && !(element instanceof Comparable)) {
			throw new IllegalStateException();
		}
		
		for (int i = 0; i < array.length; ++i) {
			E old = array[i];
			
			if (old == null) {
				array[i] = element;
				size++;
				return true;
			}
			
			int compare = comparator != null ? comparator.compare(element, old) 
					: ((Comparable<E>) element).compareTo(old);
			// Element to add is less than the old one, so insert it here.
			if (compare < 0) {
				size++;
				int numMoved = size - i - 1;
                System.arraycopy(array, i, array, i + 1, numMoved);
                array[i] = element;
                return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void set(int index, E element) {
		add(element);
	}
	
	@Override
	protected SortedFastArray<E> clone() {
		SortedFastArray<E> result = (SortedFastArray<E>) super.clone();
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Comparator<? super E> comparator() {
		return comparator != null ? comparator : (Comparator<? super E>) Comparator.naturalOrder();
	}
}
