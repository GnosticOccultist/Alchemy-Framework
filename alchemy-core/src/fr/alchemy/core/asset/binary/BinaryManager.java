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
public final class BinaryManager {
	
	public final AssetManager assetManager;
	
	/**
	 * Creates a new <code>BinaryManager</code> instance which is used to load binary asset file.
	 * You should use the {@link AssetManager} and call {@link AssetManager#loadAsset(String)} to load 
	 * a binary file or {@link AssetManager#saveAsset(Exportable, String)} to save an {@link Exportable} to a
	 * binary file.
	 */
	public static BinaryManager newManager(final AssetManager assetManager) {
		return new BinaryManager(assetManager);
	}
	
	private BinaryManager(final AssetManager assetManager) {
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
		
		final BinaryWriter writer = new BinaryWriter(this, os);
		writer.write(exportable);
	}
	
	/**
	 * Exports the provided {@link Exportable} using the {@link OutputStream}.
	 * This method should be called via {@link AssetManager#saveAsset(Exportable, String)} only.
	 * 
	 * @param exportable   The exportable class instance.
	 * @param os		   The output stream.
	 * @throws IOException Thrown if an exception occured when writing to the file.
	 */
	public <T extends Exportable> T insert(final InputStream is, final Path path) throws IOException {
		
		// TODO: Add an header for the file and versioning.
		final byte[] bytes = Files.readAllBytes(path);
		
		final int classLength = ByteUtils.readInteger(bytes, 0);
		int numBytes = 4;
		final String className = ByteUtils.readString(bytes, classLength, numBytes);
		numBytes += classLength;
	
        final BinaryReader reader = new BinaryReader(this, bytes, numBytes);
		T obj = insertObject(className, reader);
        
		return obj;
	}
	
	public <T extends Exportable> T insertObject(final String className, final BinaryReader reader) throws IOException {
		T obj = null;
	
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
	
	@SuppressWarnings("unchecked")
	public static <T extends Exportable> T createFromName(final String className) throws ReflectiveOperationException {
		try {
			return (T) Class.forName(className).getDeclaredConstructor().newInstance();
		} catch (InstantiationException e) {
			System.err.println("Error: Couldn't access constructor for the class: " + className + "! " + e.getMessage());
			throw e;
		} catch (IllegalAccessException | ClassNotFoundException ex) {
			System.err.println("Error: Couldn't access the class: " + className + "! " + ex.getMessage());
			throw ex;
		}
	}
}
