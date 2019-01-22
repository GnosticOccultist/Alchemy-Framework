package fr.alchemy.core.asset.binary;

import java.io.IOException;

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
	 * @param exporter The exporter to write to the output stream.
	 * @throws IOException 
	 */
	void export(final BinaryExporter exporter) throws IOException;
	
	/**
	 * Imports from a file an exportable assets which can be load into the application.
	 * 
	 * @param importer The importer to read the input stream.
	 * @throws IOException
	 */
	void insert(final BinaryImporter importer) throws IOException;
}
