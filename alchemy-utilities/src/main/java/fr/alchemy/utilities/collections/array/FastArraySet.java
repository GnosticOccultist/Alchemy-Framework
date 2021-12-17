package fr.alchemy.utilities.collections.array;

import java.util.Collection;

public class FastArraySet<E> extends FastArray<E> {
	
	private static final long serialVersionUID = 1L;

	public FastArraySet(Class<? super E> type) {
		super(type);
	}

	public FastArraySet(Class<? super E> type, int size) {
		super(type, size);
	}
	
	public FastArraySet(E[] array) {
        super(array);
    }
	
	@Override
	public boolean add(E element) {
		return !contains(element) && super.add(element);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> elements) {
		if (elements.isEmpty()) {
            return false;
        }

        int current = array.length;
        int selfSize = size();
        int targetSize = elements.size();
        int diff = selfSize + targetSize - current;

        if (diff > 0) {
            array = ArrayUtil.copyOf(array, Math.max(current >> 1, diff));
        }
        
        boolean changed = false;
        for (E element : elements) {
        	if (!contains(element)) {
        		array[size++] = element;
            	changed = true;
        	}
        }

        return changed;
	}
}
