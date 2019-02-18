package fr.alchemy.utilities.pool;

import java.util.function.Supplier;

public class FastReusablePool<E extends Reusable> extends FastPool<E> {
	
	public FastReusablePool(Class<? super E> type) {
		super(type);
	}
	
	@Override
	public void put(E object) {
		object.free();
		super.put(object);
	}
	
	@Override
	public E take() {
		E object = super.take();
		
		if(object != null) {
			object.reuse();
		}
		
		return object;
	}

	public E take(Supplier<E> factory) {
        E take = take();
        return take != null ? take : factory.get();
	}
}
