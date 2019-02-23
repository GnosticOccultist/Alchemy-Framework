package fr.alchemy.utilities.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Properties;

import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.actions.BiModifierAction;
import fr.alchemy.utilities.array.Array;

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
	 * Return all the files from the specified directory.
	 * 
	 * @param directory The directory to retrieve files from.
	 * @param folders	Whether to add folders.
	 * @return			A new array containing all files, including the root directory.
	 */
	public static Array<Path> getFiles(Path directory, boolean folders) {
		return getFiles(directory, true, folders);
	}
	
	/**
	 * Return all the files from the specified directory.
	 * 
	 * @param directory The directory to retrieve files from.
	 * @param root		Whether to add the root directory.
	 * @param folders	Whether to add folders.
	 * @return			A new array containing all files.
	 */
	public static Array<Path> getFiles(Path directory, boolean root, boolean folders) {

        Array<Path> result = Array.ofType(Path.class);
        addFilesTo(result, directory, root, folders);

        return result;
	}
	
	/**
	 * Add recursively all files from the specified directory to the provided store.
	 * 
	 * @param store		The array to store the files in.
	 * @param directory	The directory to start adding.
	 * @param root		Whether to add the root directory.
	 * @param folders	Whether to add folders.
	 */
	public static void addFilesTo(Array<Path> store, Path directory, boolean root, boolean folders) {
		if(Files.isDirectory(directory) && folders && root) {
			store.add(directory);
		}
		
		if(!Files.exists(directory)) {
			System.err.println("Unable to find folder " + directory);
			return;
		}
		
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
			for(Path path : stream) {
				if(Files.isDirectory(path)) {
					addFilesTo(store, path, true, folders);
				} else {
					store.add(path);
				}
			}
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
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
	 * Open a {@link InputStreamReader} from the provided path of a file.
	 * <p>
	 * The path cannot be null or empty.
	 * 
	 * @param path The path of the file to get the buffered reader.
	 * @return	   The input stream reader from the file.
	 */
	public static InputStreamReader readStream(String path) {
		Validator.nonEmpty(path, 
				"The path for the file cannot be null or empty!");
		
		if(!file.getPath().equals(path)) {
			file.changePath(path);
		}
		
		return new InputStreamReader(openStream(path));
	}
	
	/**
	 * Open a {@link BufferedReader} from the provided path of a file.
	 * <p>
	 * The path cannot be null or empty.
	 * 
	 * @param path The path of the file to get the buffered reader.
	 * @return	   The buffered reader from the file.
	 */
	public static BufferedReader readBuffered(String path) {
		Validator.nonEmpty(path, 
				"The path for the file cannot be null or empty!");
		
		if(!file.getPath().equals(path)) {
			file.changePath(path);
		}
		
		return new BufferedReader(readStream(path));
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
	
	public static Properties getProperties(String path, Properties properties) {
		Validator.nonEmpty(path, "The provided path can't be empty or null!");
		if(properties == null) {
			properties = new Properties();
		}
		try (InputStream stream = openStream(path)) {
			properties.load(stream);
			stream.close();
		} catch (IOException e) {
			System.err.println("Failed to load properties from input stream '" + path + "'.");
		}
		
		return properties;
	}
	
	public static Properties getProperties(Path path, Properties properties) {
		if(properties == null) {
			properties = new Properties();
		}
		
		try (InputStream stream = Files.newInputStream(path, StandardOpenOption.READ)) {
			properties.load(stream);
		} catch (IOException e) {
			System.err.println("Failed to load properties from input stream '" + path + "'.");
			e.printStackTrace();
		}
		
		return properties;
	}
}
