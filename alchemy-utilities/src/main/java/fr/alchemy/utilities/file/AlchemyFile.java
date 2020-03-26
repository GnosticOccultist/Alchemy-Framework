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

import fr.alchemy.utilities.file.io.ProgressInputStream;
import fr.alchemy.utilities.file.io.ProgressInputStream.ProgressListener;

/**
 * <code>AlchemyFile</code> is a more complex implementation of {@link File} to manage
 * {@link InputStream} or {@link OutputStream}.
 * 
 * @version 0.1.0
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
    
    public AlchemyFile() {
		this("");
	}

    /**
     * Instantiates a new <code>AlchemyFile</code> with the
     * specified path.
     */
    public AlchemyFile(String path) {
        this.path = path;
        this.extension = FileUtils.getExtension(path);
    }
    
    /**
     * Return the path of the file.
     * 
     * @return The path of the file.
     */
    public String getPath() {
		return path;
	}
    
    /**
     * Change the path of the <code>AlchemyFile</code>, only virtually,
     * to the provided one, updating its extension as well.
     * <p>
     * It return the previously used path for the file.
     * 
     * @param path The new path to set.
     * @return	   The old path.
     */
    public String changePath(String path) {
    	String oldPath = getPath();
    	this.path = path;
    	this.extension = FileUtils.getExtension(path);
    	
    	return oldPath;
    }
    
    /**
     * Return the folder path of the file.
     * 
     * @return The folder path.
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
     * Return the extension of the file.
     * 
     * @return The extension of the file.
     */
    public String getExtension() {
		return extension;
	}
    
    /**
     * Acquire the internal URL of the <code>AlchemyFile</code>, 
     * which can be used to obtain in {@link InputStream}.
     * 
     * @return The URL of the file.
     */
    public URL acquireInternalURL() {
    	
    	String name = path.startsWith("/") ? path : "/" + path;
    	
		URL url = AlchemyFile.class.getResource(name);
		
        if (url == null) {
            return null;
        }
        
        if (url.getProtocol().equals("file")) {
        	try {
        		String path = new File(url.toURI()).getCanonicalPath();
        		
            	// In Windows, convert '\' to '/'.
            	if (File.separatorChar == '\\') {
                    path = path.replace('\\', '/');
                }
        	} catch (URISyntaxException | IOException e) {
        		e.printStackTrace();
        	}
        }
        return url;
    }
    
    /**
     * Open an {@link InputStream} using the URL of the file obtained
     * by {@link #acquireInternalURL()}.
     * 
     * @return The input stream of the file.
     */
    public InputStream openStream() {
    	URL url = acquireInternalURL();
		if(url != null) {
			try {
				URLConnection connection = url.openConnection();
				connection.setUseCaches(false);
				return connection.getInputStream();
			} catch (IOException e) {
				System.err.println("Error while opening input stream for file: " + toString());
				e.printStackTrace();
			}
		}
		return null;
    }
    
    /**
     * Open a {@link ProgressInputStream} using the URL of the file obtained
     * by {@link #acquireInternalURL()}.
     * 
     * @return The progress input stream of the file.
     */
    public ProgressInputStream openProgressStream() {
    	return openProgressStream(null);
    }
    
    /**
     * Open a {@link ProgressInputStream} using the URL of the file obtained by {@link #acquireInternalURL()},
     * and the given {@link ProgressListener} to keep track of bytes read.
     * 
     * @return The progress input stream of the file.
     */
    public ProgressInputStream openProgressStream(ProgressListener listener) {
    	URL url = acquireInternalURL();
		if(url != null) {
			try {
				URLConnection connection = url.openConnection();
				connection.setUseCaches(false);
				
				int size = connection.getContentLength();
				return new ProgressInputStream(connection.getInputStream(), asPath(), size, listener);
			} catch (IOException e) {
				System.err.println("Error while opening input stream for file: " + toString());
				e.printStackTrace();
			}
		}
		return null;
    }
    
    /**
     * Return the <code>AlchemyFile</code> as a {@link Path} instance using a corresponding {@link URI}.
     * 
     * @return A path instance representing the file.
     */
    public Path asPath() {
    	try {
    		URL url = acquireInternalURL();
			return Paths.get(url.toURI());
		} catch (URISyntaxException e) {
			System.err.println("Error while parsing String to URI for file: " + toString());
			e.printStackTrace();
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
