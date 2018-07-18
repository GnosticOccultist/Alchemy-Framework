package fr.alchemy.core.asset;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

/**
 * <code>AssetManager</code> loads all the assets needed for the <code>AlchemyApplication</code>.
 * By default, the manager will only search for assets inside the internal source folder (defined as
 * 'resources').
 * However if you want to register new directories to look for assets, please use {@link #registerRootDirectory(Path)}
 * by specifying the path to the directory.
 * 
 * @author GnosticOccultist
 */
public class AssetManager {
	/**
	 * The list of root folders to search the assets for.
	 */
	private List<Path> roots = new ArrayList<Path>();
	
	/**
	 * Loads a <code>Texture</code> from the specified file name.
	 * It will search the asset internally and in every specified root folders.
	 * If the asset isn't found it will return null.
	 * 
	 * @param name The name of the texture file.
	 * @return	   The texture object or null if not found.
	 */
	public Texture loadTexture(final String name) {
		try {
			final InputStream is = openStream(name);
			if(is != null) {
				return new Texture(new Image(is));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Registers a new root directory for the <code>AssetManager</code>
	 * to look for asset to load.
	 * 
	 * @param root The root directory to search the asset in.
	 */
	public void registerRootDirectory(final Path root) {
		roots.add(root);
	}
	
	private InputStream openStream(String name) throws IOException {
		InputStream is = openStreamFromRoot(name);
		if(is != null) {
			return is;
		} 
		is = openStreamInternal(name);
		return is;
	}
	
	private InputStream openStreamInternal(String name) throws IOException {
		URL url = locateInternal(name);
		if(url != null) {
			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			return connection.getInputStream();
		}
		return null;
	}
	
	private InputStream openStreamFromRoot(String name) throws IOException {
		if(roots.isEmpty()) {
			return null;
		}
		
		for(Path path : roots) {
			final Path resolve = path.resolve(name);
			if(Files.exists(resolve)) {
				return Files.newInputStream(resolve, StandardOpenOption.READ);		
			}
		}
		return null;
	}
	
	private URL locateInternal(String name) {
		if (name.startsWith("/")) {
            name = name.substring(1);
        }
        
        URL url = getClass().getResource("/" + name);

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
}
