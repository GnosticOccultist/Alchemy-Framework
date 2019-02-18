package fr.alchemy.utilities.pool;

import fr.alchemy.utilities.array.Array;

public class FastPool<E> {
	
	/**
	 * The internal pool of objects.
	 */
	private final Array<E> pool;
	
	public FastPool(Class<? super E> type) {
		this.pool = Array.ofType(type);
	}
	
	public void put(E object) {
		pool.add(object);
	}
	
	public void remove(E object) {
		pool.fastRemove(object);
	}
	
	public boolean isEmpty() {
		return pool.isEmpty();
    }
	
	public E take() {
		E object = pool.pop();
		
		if(object == null) {
			return null;
		}
		
		return object;
	}
	
	@Override
	public String toString() {
		return pool.toString();
	}
}
