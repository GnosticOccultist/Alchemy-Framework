package fr.alchemy.utilities.file;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
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
import fr.alchemy.utilities.collections.array.Array;
import fr.alchemy.utilities.file.io.ProgressInputStream;
import fr.alchemy.utilities.file.io.ProgressInputStream.ProgressListener;
import fr.alchemy.utilities.task.actions.BiModifierAction;
import fr.alchemy.utilities.task.actions.SafeVoidAction;

/**
 * <code>FileUtils</code> provides utilities functions concerning files and directories.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public final class FileUtils {
	
	/**
	 * The separator character in a path.
	 */
	public static final String SEPARATOR = "/";
	/**
	 * An internal only file thread specific to limit the number of instantiations.
	 */
	private static final ThreadLocal<AlchemyFile> file = ThreadLocal.withInitial(AlchemyFile::new);
	
	/**
	 * Private constructor to inhibit instantiation of <code>FileUtils</code>.
	 */
	private FileUtils() {}
	
	/**
	 * Return the potential extension of the file, or
	 * an empty string if null.
	 * 
	 * @param path The path of the file to get the extension.
	 * @return	   The file's extension.
	 */
	public static String getExtension(Path path) {
		if(path == null || Files.isDirectory(path)) {
			return "";
		}
		
		return getExtension(Objects.toString(path.getFileName()));
	}
	
	/**
	 * Return the potential extension of the file, or
	 * an empty string if null.
	 * 
	 * @param path The path of the file to get the extension.
	 * @return	   The file's extension.
	 */
	public static String getExtension(String path) {
		if(path == null) {
			return "";
		}
		
		int index = path.lastIndexOf(".");
		if (index <= 0 || index == path.length() - 1) {
            return "";
        } else {
            return path.substring(index + 1).toLowerCase();
        }
	}
	
	/**
	 * Return the potential name of the file, or
	 * an empty string if null.
	 * 
	 * @param path The path of the file to get the name.
	 * @return	   The file's name.
	 */
	public static String getFileName(Path path) {
		if(path == null || Files.isDirectory(path)) {
			return "";
		}
		
		return getFileName(Objects.toString(path.getFileName()));
	}
	
	/**
	 * Return the name of the file or an empty string if null.
	 * 
	 * @param path The path for which to get the name of the file.
	 * @return	   The file's name.
	 */
	public static String getFileName(String path) {
		if(path == null) {
			return "";
		}
		
		int endIndex = path.lastIndexOf(".");
		if(endIndex <= 0) {
            return "";
        }
		int beginIndex = path.lastIndexOf("/");
		if(beginIndex <= 0) {
            return "";
        }
		return path.substring(beginIndex + 1, endIndex);
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
        addFilesTo(result, directory, FileExtensions.UNIVERSAL_EXTENSION, root, folders);
        
        return result;
	}
	
	/**
	 * Return all the files from the specified directory matching the given extension.
	 * In order to accept any extensions, use {@link FileExtensions#UNIVERSAL_EXTENSION} as the argument.
	 * 
	 * @param directory The directory to retrieve files from.
	 * @param extension The extension of files to retrieve (not null, not empty).
	 * @return			A new array containing all files of the same extension.
	 */
	public static Array<Path> getFiles(Path directory, String extension) {
        Array<Path> result = Array.ofType(Path.class);
        addFilesTo(result, directory, extension, false, false);

        return result;
	}
	
	/**
	 * Add recursively all files matching the given extension from the specified directory to the provided store.
	 * In order to accept any extensions, use {@link FileExtensions#UNIVERSAL_EXTENSION} as the argument.
	 * 
	 * @param store		The array to store the files in.
	 * @param directory	The directory to start adding.
	 * @param extension The extension of files to retrieve (not null, not empty).
	 * @param root		Whether to add the root directory.
	 * @param folders	Whether to add folders.
	 */
	public static void addFilesTo(Array<Path> store, Path directory, String extension, boolean root, boolean folders) {
		Validator.nonEmpty(extension, "The extension to search for can't be null!");
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
					addFilesTo(store, path, extension, true, folders);
				} else if(FileExtensions.UNIVERSAL_EXTENSION.equals(extension) || getExtension(path).equals(extension)) {
					store.add(path);
				}
			}
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}
	
	/**
	 * Converts the given {@link Path} of multiple files into a {@link URL}.
	 * 
	 * @param files The array of file path to convert to URL (not null).
	 * @return	  	An array of URL corresponding to each file path, or null if a malformation has occured.
	 */
	public static URL[] toURL(Array<Path> files) {
		Validator.nonNull(files, "The array of file path can't be null!");
		URL[] urls = files.stream()
				.map(path -> FileUtils.toURL(path))
				.toArray(URL[]::new);
		return urls;
	}
	
	/**
	 * Converts the given {@link Path} of a file into a {@link URL}.
	 * 
	 * @param file The file path to convert to URL (not null).
	 * @return	   A URL corresponding to file path, or null if a malformation has occured.
	 */
	public static URL toURL(Path file) {
		Validator.nonNull(file, "The file path can't be null!");
		try {
			URL url = file.toUri().toURL();
			return url;
		} catch (MalformedURLException ex) {
			System.err.println("A malformed URL as occured for file '" + file + "'");
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Update internally the path of the {@link AlchemyFile} if needed.
	 * 
	 * @param path The path to change to.
	 * @return	   The alchemy file pointing to the new path.
	 */
	private static AlchemyFile updateInternalFile(String path) {
		AlchemyFile alchemyFile = file.get();
		if(!alchemyFile.getPath().equals(path)) {
			alchemyFile.changePath(path);
		}
		return alchemyFile;
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
		Validator.nonEmpty(path, "The path for the file cannot be null or empty!");
		return new BufferedReader(readStream(path));
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
		Validator.nonEmpty(path, "The path for the file cannot be null or empty!");
		return new InputStreamReader(openStream(path));
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
		Validator.nonEmpty(path, "The path for the file cannot be null or empty!");
		return Channels.newChannel(openStream(path));
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
		Validator.nonEmpty(path, "The path for the file cannot be null or empty!");
		return updateInternalFile(path).openStream();
	}
	
	/**
	 * Open a {@link ProgressInputStream} from the provided path of a file.
	 * <p>
	 * The path cannot be null or empty.
	 * 
	 * @param path The path of the file to get an input stream.
	 * @return	   The input stream to the file.
	 */
	public static ProgressInputStream openProgressStream(String path) {
		Validator.nonEmpty(path, "The path for the file cannot be null or empty!");
		return updateInternalFile(path).openProgressStream();
	}
	
	/**
	 * Open a {@link ProgressInputStream} from the provided path of a file using the given
	 * {@link ProgressListener} to keep track of bytes being read.
	 * <p>
	 * The path cannot be null or empty.
	 * 
	 * @param path 	   The path of the file to get an input stream.
	 * @param listener The listener to assign to the input stream, or null for none.
	 * @return		   The input stream to the file.
	 */
	public static ProgressInputStream openProgressStream(String path, ProgressListener listener) {
		Validator.nonEmpty(path, "The path for the file cannot be null or empty!");
		return updateInternalFile(path).openProgressStream(listener);
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
	
	/**
	 * Loads and return the {@link Properties} from the provided path using the given store,
	 * or a new instance if null. 
	 * 
	 * @param path		 The path of the property file to load (not null, not empty).
	 * @param properties The properties to populate with the loaded data.
	 * @return			 The store properties or a new instance one with the loaded data.
	 */
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
	
	/**
	 * Loads and return the {@link Properties} from the provided {@link Path} using the given store,
	 * or a new instance if null. 
	 * 
	 * @param path		 The path of the property file to load (not null, not empty).
	 * @param properties The properties to populate with the loaded data.
	 * @return			 The store properties or a new instance one with the loaded data.
	 */
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
	
	/**
	 * Creates all directories contained in the {@link Path} if they don't already exist.
	 * 
	 * @param directory The path from which to create the hierarchy of directories.
	 * @return			Whether the directory has been created or already existed.
	 */
	public static boolean createDirectories(Path directory) {
		try {
			if(!Files.exists(directory)) {
				Files.createDirectories(directory);
				return true;
			}
		} catch (IOException ex) {
			System.err.println("Failed to create directories from path '" + directory + "'");
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Creates a file contained in the given {@link Path} if it doesn't already exist.
	 * 
	 * @param file The path from which to create the file.
	 * @return	   Whether the file has been created or already existed.
	 */
	public static boolean createFile(Path file) {
		try {
			if(!Files.exists(file)) {
				Files.createFile(file);
				return true;
			}
		} catch (IOException ex) {
			System.err.println("Failed to create file from path '" + file + "'");
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Closing safely the given {@link Closeable} implementation without needing to catch potentially
	 * thrown {@link IOException}.
	 * 
	 * @param resource The resource to close safely (not null).
	 */
	public static <T extends Closeable> void safeClose(T resource) {
		safePerform(resource, T::close);
	}
	
	/**
	 * Performing safely the provided {@link SafeVoidAction} using the given object, without needing to 
	 * catch potentially thrown {@link Throwable}.
	 * 
	 * @param object	 The object to perform an action safely with (not null).
	 * @param safeAction The safe action to perform with the object (not null).
	 * @return			 The returned object for chaining purposes (not null).
	 */
	public static <T> T safePerform(T object, SafeVoidAction<T> safeAction) {
		Validator.nonNull(object, "The object can't be null!");
		Validator.nonNull(safeAction, "The safe action can't be null!");
		try {
			safeAction.perform(object);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return object;
	}
}
