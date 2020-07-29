package fr.alchemy.utilities.collections.array;

import java.util.Collection;

/**
 * <code>ReadOnlyArray</code> is an implementation of {@link Array} to mark it as readable-only
 * meaning elements can't be added, removed or set on this array.
 * 
 * @param <E> The type of element contained in the array.
 * 
 * @version 0.1.1
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public interface ReadOnlyArray<E> extends Array<E> {
	
	@Override
	@Deprecated
	boolean add(E e);
	
	@Override
	@Deprecated
	E fastRemove(int index);
	
	@Override
	@Deprecated
	boolean fastRemove(Object object);
	
	@Override
	@Deprecated
	boolean addAll(Collection<? extends E> collection);
	
	@Override
	@Deprecated
	boolean remove(Object object);
	
	@Override
	@Deprecated
	E remove(int index);
	
	@Override
	@Deprecated
	boolean removeAll(Collection<?> target);
	
	@Override
	@Deprecated
	boolean retainAll(Collection<?> target);
	
	@Override
	@Deprecated
	void clear();
}
