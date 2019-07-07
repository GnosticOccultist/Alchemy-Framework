package fr.alchemy.editor.core.asset;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * <code>AssetCache</code> is a wrapper class containing the table of loaded assets ordered
 * by their paths.
 * It allows you to access a specific asset previously loaded without having to call a new
 * {@link InputStream} as it is stored in cache-memory.
 * <p>
 * Eventually you can call {@link #clear()} to empty the cache.
 * 
 * @author GnosticOccultist
 */
public final class AssetCache {
	/**
	 * The table containing the assets classed by path.
	 */
	private final Map<Path, IAsset> cache = new HashMap<Path, IAsset>();
	
	/**
	 * Caches the specified asset with its path as the key.
	 * 
	 * @param path  The path to acquire the asset.
	 * @param asset The asset to cache.
	 * @return		The previously associated value to the key or null.
	 */
	public IAsset cache(final String path, final IAsset asset) {
		return cache.put(Paths.get(path), asset);
	}
	
	/**
	 * Caches the specified asset with its path as the key.
	 * 
	 * @param path  The path to acquire the asset.
	 * @param asset The asset to cache.
	 * @return		The previously associated value to the key or null.
	 */
	public IAsset cache(final Path path, final IAsset asset) {
		return cache.put(path, asset);
	}
	
	/**
	 * Acquires the cached asset with the provided key or null 
	 * if it doesn't exist.
	 * 
	 * @param path The path to use as the key.
	 * @return	   The asset from the cache.
	 */
	public IAsset acquire(final String path) {
		return cache.get(Paths.get(path));
	}
	
	/**
	 * Acquires the cached asset with the provided key or null 
	 * if it doesn't exist.
	 * 
	 * @param path The path to use as the key.
	 * @return	   The asset from the cache.
	 */
	public IAsset acquire(final Path path) {
		return cache.get(path);
	}
	
	/**
	 * Uncaches the asset mapped to the specified key from the <code>AssetCache</code>.
	 * 
	 * @param path The key path of the asset to remove.
	 * 
	 * @return The previously mapped key to the path.
	 */
	public boolean uncache(final String path) {
		return cache.remove(path, acquire(path));
	}
	
	/**
	 * Uncaches the asset mapped to the specified key from the <code>AssetCache</code>.
	 * 
	 * @param path The key path of the asset to remove.
	 * 
	 * @return The previously mapped key to the path.
	 */
	public boolean uncache(final Path path) {
		return cache.remove(path, acquire(path));
	}
	
	/**
	 * Uncaches the asset mapped to the specified key from the <code>AssetCache</code>.
	 * 
	 * @param path  The key path of the asset to remove.
	 * @param asset The asset to remove.
	 * @return 		The previously mapped key to the path.
	 */
	public boolean uncache(final Path path, final IAsset asset) {
		return cache.remove(path, asset);
	}
	
	/**
	 * Cleanup the assets and clears the cache from its content.
	 */
	public void clear() {
		cache.values().forEach(IAsset::cleanup);
		cache.clear();
	}
}