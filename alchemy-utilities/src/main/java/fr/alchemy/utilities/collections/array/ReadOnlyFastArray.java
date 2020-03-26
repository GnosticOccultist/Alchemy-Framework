package fr.alchemy.utilities.collections.array;

import java.util.Collection;
import java.util.function.Predicate;

import fr.alchemy.utilities.ReadOnlyException;

/**
 * <code>ReadOnlyFastArray</code> is an implementation of {@link FastArray} for readable-only usage, meaning 
 * all adding, removing or setting function are disabled and will throw an {@link IllegalStateException} if invoked.
 * 
 * @param <E> The type of element contained in the array.
 * 
 * @author GnosticOccultist
 */
public class ReadOnlyFastArray<E> extends FastArray<E> implements ReadOnlyArray<E> {

	private static final long serialVersionUID = 3482397723734122659L;

	public ReadOnlyFastArray(E[] array) {
		super(array);
	}
	
	@Override
	public boolean add(E object) {
		throw new ReadOnlyException("The array is readable-only!");
	}
	
	@Override
	public boolean addAll(Collection<? extends E> collection) {
		throw new ReadOnlyException("The array is readable-only!");
	}
	
	@Override
	public boolean unsafeAdd(E object) {
		throw new ReadOnlyException("The array is readable-only!");
	}
	
	@Override
	public boolean remove(Object object) {
		throw new ReadOnlyException("The array is readable-only!");
	}
	
	@Override
	public boolean removeIf(Predicate<? super E> filter) {
		throw new ReadOnlyException("The array is readable-only!");
	}
	
	@Override
	public boolean removeAll(Collection<?> target) {
		throw new ReadOnlyException("The array is readable-only!");
	}
	
	@Override
	public E fastRemove(int index) {
		throw new ReadOnlyException("The array is readable-only!");
	}
	
	@Override
	public boolean fastRemove(Object object) {
		throw new ReadOnlyException("The array is readable-only!");
	}
	
	@Override
	public boolean retainAll(Collection<?> target) {
		throw new ReadOnlyException("The array is readable-only!");
	}
	
	@Override
	public void clear() {
		throw new ReadOnlyException("The array is readable-only!");
	}
}
