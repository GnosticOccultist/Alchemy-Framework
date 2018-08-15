package fr.alchemy.core.asset.cache;

/**
 * <code>Asset</code> is an interface to represent an object designed to be an asset.
 * It can be cached inside {@link AssetCache} and reusable, copyable or destroyable.
 * 
 * @author GnosticOccultist
 */
public interface Asset {
	
	/**
	 * Clean the object when no longer needed or to be resetted.
	 */
	void cleanup();
}
