package fr.alchemy.utilities.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.functions.BiModifierAction;

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
	 * Return the potential extension of the file, or
	 * an empty string if null.
	 * 
	 * @param path The path of the file to get the extension.
	 * @return	   The file's extension.
	 */
	public static String getExtension(Path path) {
		
		if(Files.isDirectory(path)) {
			return "";
		}
		
		return getExtension(Objects.toString(path.getFileName()));
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
	
	/**
	 * Open a {@link ReadableByteChannel} from the provided path of a file.
	 * <p>
	 * The path cannot be null or empty.
	 * 
	 * @param path The path of the file to get the readable byte channel.
	 * @return	   The readable byte channel from the file.
	 */
	public static ReadableByteChannel readByteChannel(String path) {
		Validator.nonEmpty(path, 
				"The path for the file cannot be null or empty!");
		
		if(!file.getPath().equals(path)) {
			file.changePath(path);
		}
		
		return Channels.newChannel(openStream(path));
	}
	
	/**
	 * Converts the provided file into the provided {@link ByteBuffer}. If the buffer is too small, it will execute the 
	 * given resizing action.
	 * 
	 * @param resource     The resource to convert into a byte buffer.
	 * @param buffer	   The buffer to fill with the resource.
	 * @param resizeAction The buffer resizing action.
	 * @return			   The filled byte buffer.
	 */
	public static ByteBuffer toByteBuffer(String resource, ByteBuffer buffer, BiModifierAction<ByteBuffer, Integer> resizeAction) {
		
		try (ReadableByteChannel channel = readByteChannel(resource)) {
            	
			while (true) {
				int bytes = channel.read(buffer);
				if (bytes == -1) {
					break;
				}
				if (buffer.remaining() == 0) {
					buffer = resizeAction.modify(buffer, buffer.capacity() * 2);
				}
			}
		} catch (IOException e) {
			System.err.println("Failed to read byte channel for file: " + resource);
			e.printStackTrace();
		}
        
        buffer.flip();
        return buffer;
    }
}
