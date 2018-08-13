package fr.alchemy.core.asset.binary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import fr.alchemy.core.asset.AssetManager;
import fr.alchemy.utilities.ByteUtils;

/**
 * <code>BinaryExporter</code> is capable of exporting {@link Exportable} class instances to a file on the
 * disk in bytes format.
 * 
 * @author GnosticOccultist
 */
public final class BinaryExporter {
	
	public final AssetManager assetManager;
	
	/**
	 * @return The single instance of the <code>BinaryExporter</code>.
	 */
	public static BinaryExporter getInstance(final AssetManager assetManager) {
		return new BinaryExporter(assetManager);
	}
	
	private BinaryExporter(final AssetManager assetManager) {
		this.assetManager = assetManager;
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
	
	/**
	 * Exports the provided {@link Exportable} using the {@link OutputStream}.
	 * This method should be called via {@link AssetManager#saveAsset(Exportable, String)} only.
	 * 
	 * @param exportable   The exportable class instance.
	 * @param os		   The output stream.
	 * @throws IOException Thrown if an exception occured when writing to the file.
	 */
	public Exportable insert(final InputStream is, final Path path) throws IOException {
		
		// TODO: Add an header for the file and versioning.
		final byte[] bytes = Files.readAllBytes(path);
		
		final int classLength = ByteUtils.readInteger(bytes, 0);
		int numBytes = 4;
		final String className = ByteUtils.readString(bytes, classLength, numBytes);
		numBytes += classLength;
	
        final BinaryReader reader = new BinaryReader(this, is, bytes, numBytes);
		Exportable obj = insertObject(className, reader);
        
		return obj;
	}
	
	public Exportable insertObject(final String className, final BinaryReader reader) throws IOException {
		Exportable obj = null;
	
		try {
			obj = createFromName(className);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		
		if(obj != null) {
			obj.insert(reader);
		}
		
		return obj;
	}
	
	public static Exportable createFromName(final String className) throws ReflectiveOperationException {
		try {
			return (Exportable) Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			System.err.println("Error: Couldn't access constructor for the class: " + className + "! " + e.getMessage());
			throw e;
		} catch (IllegalAccessException | ClassNotFoundException ex) {
			System.err.println("Error: Couldn't access the class: " + className + "! " + ex.getMessage());
			throw ex;
		}
	}
}
