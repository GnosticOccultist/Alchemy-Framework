package fr.alchemy.utilities.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;

import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.file.io.ProgressInputStream;
import fr.alchemy.utilities.file.io.ProgressInputStream.ProgressListener;

/**
 * <code>AlchemyFile</code> is a more complex implementation of {@link File} to manage
 * {@link InputStream} or {@link OutputStream}.
 * 
 * @version 0.1.1
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public class AlchemyFile {
	
	/**
	 * The path of the file.
	 */
	protected String path;
	/**
	 * The extension of the file.
	 */
    protected String extension;
    
    /**
     * Instantiates a new <code>AlchemyFile</code> with an empty path. This constructor should only
     * be accessed by {@link FileUtils} to instantiate a local variable.
     */
    protected AlchemyFile() {
		this("");
	}

    /**
     * Instantiates a new <code>AlchemyFile</code> with the specified path.
     * 
     * @param The path of the file (not null).
     */
    public AlchemyFile(String path) {
        this.path = path;
        this.extension = FileUtils.getExtension(path);
    }
    
    /**
     * Return the path of the <code>AlchemyFile</code>.
     * 
     * @return The path of the file (not null).
     */
    public String getPath() {
		return path;
	}
    
    /**
     * Return the extension of the <code>AlchemyFile</code>.
     * 
     * @return The extension of the file (not null).
     */
    public String getExtension() {
		return extension;
	}
    
    /**
     * Change the path of the <code>AlchemyFile</code>, only virtually, to the provided one, updating 
     * its extension as well, and returns the previous path used by the file.
     * 
     * @param path The new path to set (not null, not empty).
     * @return	   The old path used by the file (not null).
     */
    public String changePath(String path) {
    	Validator.nonEmpty(path, "The new path can't be null or empty!");
    	
    	String oldPath = getPath();
    	if(!oldPath.equals(path)) {
    		this.path = path;
        	this.extension = FileUtils.getExtension(path);
    	}
    	
    	return oldPath;
    }
    
    /**
     * Return the folder path of the <code>AlchemyFile</code> or an empty path if the file is a root directory.
     * 
     * @return The folder path of the file (not null).
     */
    public String getFolder() {
        int idx = path.lastIndexOf(FileUtils.SEPARATOR);
        if (idx <= 0 || idx == path.length() - 1) {
            return "";
        } else {
            return path.substring(0, idx + 1);
        }
    }
    
    /**
     * Acquire the internal URL of the <code>AlchemyFile</code>, which can be used to obtain an {@link InputStream}.
     * 
     * @return The URL of the file, or null if no file with this path was found.
     */
    public URL acquireInternalURL() {
    	String name = path.startsWith("/") ? path : "/" + path;
		URL url = AlchemyFile.class.getResource(name);
		
        if(url == null) {
        	return null;
        }
        
        if (url.getProtocol().equals("file")) {
        	try {
        		String path = new File(url.toURI()).getCanonicalPath();
        		
            	// In Windows, convert '\' to '/'.
            	if (File.separatorChar == '\\') {
                    path = path.replace('\\', '/');
                }
        	} catch (URISyntaxException | IOException ex) {
        		ex.printStackTrace();
        	}
        }
        return url;
    }
    
    /**
     * Open an {@link InputStream} using the URL of the <code>AlchemyFile</code> obtained by {@link #acquireInternalURL()}.
     * 
     * @return The input stream of the file, or null if no file with this path was found.
     */
    public InputStream openStream() {
    	URL url = acquireInternalURL();
		if(url != null) {
			try {
				URLConnection connection = url.openConnection();
				connection.setUseCaches(false);
				return connection.getInputStream();
			} catch (IOException ex) {
				System.err.println("Error while opening input stream for file: " + toString());
				ex.printStackTrace();
			}
		}
		return null;
    }
    
    /**
     * Open a {@link ProgressInputStream} using the URL of the <code>AlchemyFile</code> obtained by {@link #acquireInternalURL()}.
     * 
     * @return The progress input stream of the file, or null if no file with this path was found.
     */
    public ProgressInputStream openProgressStream() {
    	return openProgressStream(null);
    }
    
    /**
     * Open a {@link ProgressInputStream} using the URL of the <code>AlchemyFile</code> obtained by {@link #acquireInternalURL()},
     * and the given {@link ProgressListener} to keep track of bytes read.
     * 
     * @param listener The listener to assign to the input stream, or null for none.
     * @return 		   The progress input stream of the file, or null if no file with this path was found.
     */
    public ProgressInputStream openProgressStream(ProgressListener listener) {
    	URL url = acquireInternalURL();
		if(url != null) {
			try {
				URLConnection connection = url.openConnection();
				connection.setUseCaches(false);
				
				int size = connection.getContentLength();
				return new ProgressInputStream(connection.getInputStream(), asPath(), size, listener);
			} catch (IOException ex) {
				System.err.println("Error while opening input stream for file: " + toString());
				ex.printStackTrace();
			}
		}
		return null;
    }
    
    /**
     * Return the <code>AlchemyFile</code> as a {@link Path} instance using a corresponding {@link URI}.
     * 
     * @return A path instance representing the file, or null if no file with this path was found.
     */
    public Path asPath() {
    	try {
    		URL url = acquireInternalURL();
			return Paths.get(url.toURI());
		} catch (URISyntaxException ex) {
			System.err.println("Error while parsing String to URI for file: " + toString());
			ex.printStackTrace();
		}
    	return null;
    }
    
    @Override
    public int hashCode() {
    	return path.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
    	if (!(o instanceof AlchemyFile)){
            return false;
        }

        return path.equals(((AlchemyFile) o).path);
    }
    
    @Override
    public String toString() {
    	return path;
    }
}
