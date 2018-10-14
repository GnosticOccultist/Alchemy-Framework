package fr.alchemy.utilities.file;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import fr.alchemy.utilities.Validator;

public final class FileUtils {
	
	/**
	 * The separator character in a path.
	 */
	public static final String SEPARATOR = "/";
	/**
	 * An internal only file to limit the number of instantiations.
	 */
	private static AlchemyFile file = new AlchemyFile("");
	
	/**
	 * Return the potential extension of the file, or
	 * an empty string if null.
	 * 
	 * @param path The path of the file to get the extension.
	 * @return	   The file's extension.
	 */
	public static String getExtension(String path) {
		int index = path.lastIndexOf(".");
		if (index <= 0 || index == path.length() - 1) {
            return "";
        } else {
            return path.substring(index + 1).toLowerCase();
        }
	}
	
	/**
	 * Open an {@link InputStream} from the provided path of a file.
	 * <p>
	 * The path cannot be null or empty.
	 * 
	 * @param path The path of the file to get an input stream.
	 * @return	   The input stream to the file.
	 */
	public static InputStream openStream(String path) {
		Validator.nonEmpty(path, 
				"The path for the file cannot be null or empty!");
		
		if(!file.getPath().equals(path)) {
			file.changePath(path);
		}
		
		return file.openStream();
	}
	
	/**
	 * Open a {@link BufferedReader} from the provided path of a file.
	 * <p>
	 * The path cannot be null or empty.
	 * 
	 * @param path The path of the file to get the buffered reader.
	 * @return	   The buffered reader from the file.
	 */
	public static BufferedReader read(String path) {
		Validator.nonEmpty(path, 
				"The path for the file cannot be null or empty!");
		
		if(!file.getPath().equals(path)) {
			file.changePath(path);
		}
		
		return new BufferedReader(new InputStreamReader(openStream(path)));
	}
}
