package fr.alchemy.editor.core.asset;

/**
 * <code>IAsset</code> is an interface to represent an object designed to be an asset.
 * It can be cached inside {@link AssetCache} and reusable, copyable or destroyable.
 * 
 * @author GnosticOccultist
 */
public interface IAsset {
	
	/**
	 * @return The file path of the asset.
	 */
	String getFile();
	
	/**
	 * Clean the object when no longer needed or to be resetted.
	 */
	void cleanup();
}