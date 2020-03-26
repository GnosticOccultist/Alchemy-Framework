package fr.alchemy.utilities.collections.dictionnary;

import fr.alchemy.utilities.pool.FastReusablePool;
import fr.alchemy.utilities.pool.Reusable;

public class PoolDictionary extends FastObjectDictionary<Class, FastReusablePool> {
	
	public PoolDictionary() {
		super();
	}
	
	public PoolDictionary(Class<Reusable> type) {
		super();
		this.put(type, new FastReusablePool<>(type));
	}
}
