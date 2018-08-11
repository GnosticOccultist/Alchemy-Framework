package fr.alchemy.core.asset.binary;

import java.io.IOException;
import java.io.OutputStream;

import fr.alchemy.core.asset.AssetManager;

/**
 * <code>BinaryExporter</code> is capable of exporting {@link Exportable} class instances to a file on the
 * disk in bytes format.
 * 
 * @author GnosticOccultist
 */
public final class BinaryExporter {
	
	/**
	 * @return The single instance of the <code>BinaryExporter</code>.
	 */
	public static BinaryExporter getInstance() {
		return new BinaryExporter();
	}
	
	/**
	 * Exports the provided {@link Exportable} using the {@link OutputStream}.
	 * This method should be called via {@link AssetManager#saveAsset(Exportable, String)} only.
	 * 
	 * @param exportable   The exportable class instance.
	 * @param os		   The output stream.
	 * @throws IOException Thrown if an exception occured when writing to the file.
	 */
	public void export(final Exportable exportable, final OutputStream os) throws IOException {
		
		// TODO: Add an header for the file and versioning.
		
		exportable.export(os);
	}
}
