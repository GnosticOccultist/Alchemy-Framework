package fr.alchemy.core.asset.binary;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <code>Exportable</code> is the interface for exporting a class into a file 
 * with a specified format.
 * 
 * @author GnosticOccultist
 */
public interface Exportable {
	
	/**
	 * Exports the instance of the class implementing this interface into a file
	 * of a specified format.
	 * 
	 * @param os			The output stream.
	 * @throws IOException 
	 */
	void export(final OutputStream os) throws IOException;
	
	/**
	 * Imports from a file an exportable assets which can be load into the application.
	 * 
	 * @param is			The input stream.
	 * @throws IOException
	 */
	void insert(final BinaryReader reader) throws IOException;
}
