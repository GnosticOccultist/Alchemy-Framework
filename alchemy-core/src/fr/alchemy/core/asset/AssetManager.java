package fr.alchemy.core.asset;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import fr.alchemy.core.asset.binary.BinaryManager;
import fr.alchemy.core.asset.binary.Exportable;
import fr.alchemy.core.asset.cache.Asset;
import fr.alchemy.core.asset.cache.AssetCache;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

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
	 * The asset cache.
	 */
	private final AssetCache cache = new AssetCache();
	/**
	 * The list of root folders to search the assets for.
	 */
	private List<Path> roots = new ArrayList<Path>();
	
	/**
	 * Loads a JavaFX asset using an {@link InputStream} to create a 
	 * new instance of the specified asset type.
	 * 
	 * @param type The JavaFX asset type to load.
	 * @param name The name of the asset file.
	 * @return	   The asset instance corresponding to the requested type.
	 */
	public <A> A loadFXAsset(final Class<A> type, final String name) {
		try {
			final InputStream is = openInStream(name);
			if(is != null) {
				return type.getConstructor(InputStream.class).newInstance(is);
			}
		} catch (Exception e) {
			try {
				return type.getConstructor(String.class).newInstance(getClass().getResource(name).toExternalForm());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Saves the specified {@link Exportable} to the provided binary file.
	 * 
	 * @param exportable The exportable instance to save.
	 * @param path		 The path for the file on the disk.
	 */
	public void saveAsset(final Exportable exportable, final String path) {
		final BinaryManager exporter = BinaryManager.newManager(this);
		
		try (final OutputStream out = openOutStream(path)) {
			exporter.export(exportable, out);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Loads an {@link Exportable} value stored into the provided binary file.
	 * 
	 * @param exportable The exportable instance to save.
	 * @param path		 The path for the file on the disk.
	 */
	public Exportable loadAsset(final String path) {
		final BinaryManager importer = BinaryManager.newManager(this);
		
		try (final InputStream is = Files.newInputStream(Paths.get(locateInternal(path).getPath().substring(1)), StandardOpenOption.READ)) {
			return importer.insert(is, Paths.get(locateInternal(path).getPath().substring(1)));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * Loads a <code>Texture</code> from the specified file name.
	 * It will search the asset internally and in every specified root folders.
	 * If the asset isn't found it will return null.
	 * 
	 * @param name The name of the texture file.
	 * @return	   The texture object or null if not found.
	 */
	public Texture loadTexture(final String name) {
		final Asset asset = cache.acquire(name);
		if(asset != null && asset instanceof Texture) {
			return ((Texture) asset).copy();
		}
		
		final Texture texture = new Texture(loadFXAsset(Image.class, name), name);
		cache.cache(name, texture);
		return texture;
	}
	
	/**
	 * Loads a <code>Image</code> from the specified file name.
	 * It will search the asset internally and in every specified root folders.
	 * If the asset isn't found it will return null.
	 * 
	 * @param name The name of the image file.
	 * @return	   The image object or null if not found.
	 */
	public Image loadImage(final String name) {
		final Asset asset = cache.acquire(name);
		if(asset != null && asset instanceof Texture) {
			return ((Texture) asset).getImage();
		}
		
		final Image image = loadFXAsset(Image.class, name);
		cache.cache(name, new Texture(image, name));
		return image;
	}
	
	/**
	 * Loads a <code>ImageView</code> from the specified file name and return it as an icon.
	 * It will search the asset internally and in every specified root folders.
	 * If the asset isn't found it will return null.
	 * 
	 * @param name The name of the image file.
	 * @return	   The image object or null if not found.
	 */
	public ImageView loadIcon(final String name) {
		try {
			final InputStream is = openInStream(name);
			if(is != null) {
				return new ImageView(new Image(is, 16, 16, false, true));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Loads a <code>Sound</code> from the specified file name.
	 * It will search the asset internally and in every specified root folders.
	 * If the asset isn't found it will return null.
	 * <p>
	 * After instanciation, you can call {@link Sound#play()} to play the sound effect.
	 * 
	 * @param name The asset name.
	 * @return	   The loaded sound or null if not found.
	 */
	public Sound loadSound(final String name) {
		final Asset asset = cache.acquire(name);
		if(asset != null && asset instanceof Sound) {
			return (Sound) asset;
		}
		
		final Sound sound = new Sound(loadFXAsset(AudioClip.class, name));
		cache.cache(name, sound);
		return sound;
	}
	
	/**
	 * Loads a <code>Sound</code> from the specified file name.
	 * It will search the asset internally and in every specified root folders.
	 * If the asset isn't found it will return null.
	 * <p>
	 * After instanciation, you can call {@link Sound#play()} to play the sound effect.
	 * 
	 * @param name The asset name.
	 * @return	   The loaded sound or null if not found.
	 */
	public Music loadMusic(final String name) {
		final Asset asset = cache.acquire(name);
		if(asset != null && asset instanceof Music) {
			return (Music) asset;
		}
		
		final Music music = new Music(loadFXAsset(Media.class, name));
		cache.cache(name, music);
		return music;
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
	
	/**
	 * Registers a new root directory for the <code>AssetManager</code>
	 * to look for asset to load.
	 * 
	 * @param root The root directory to search the asset in.
	 */
	public void registerRootDirectory(final String root) {
		roots.add(Paths.get(root));
	}
	
	private InputStream openInStream(String name) throws IOException {
		InputStream is = openInStreamFromRoot(name);
		if(is != null) {
			return is;
		} 
		is = openInStreamInternal(name);
		return is;
	}
	
	private OutputStream openOutStream(String name) throws IOException {
		OutputStream os = openOutStreamFromRoot(name);
		if(os != null) {
			return os;
		} 
		os = openOutStreamInternal(name);
		return os;
	}
	
	private InputStream openInStreamInternal(String name) throws IOException {
		final URL url = locateInternal(name);
		if(url != null) {
			final URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			return connection.getInputStream();
		}
		return null;
	}
	
	private OutputStream openOutStreamInternal(String name) throws IOException {
		final URL url = locateInternal(name);
		return Files.newOutputStream(Paths.get(url.getPath().substring(1)));	
	}
	
	private InputStream openInStreamFromRoot(String name) throws IOException {
		if(roots.isEmpty()) {
			return null;
		}
		
		for(int i = 0; i < roots.size(); i++) {
			final Path resolve = roots.get(i).resolve(name);
			if(Files.exists(resolve)) {
				return Files.newInputStream(resolve, StandardOpenOption.READ);		
			}
		}
		return null;
	}
	
	private OutputStream openOutStreamFromRoot(String name) throws IOException {
		if(roots.isEmpty()) {
			return null;
		}
		
		for(int i = 0; i < roots.size(); i++) {
			final Path resolve = roots.get(i).resolve(name);
			if(Files.exists(resolve)) {
				return Files.newOutputStream(resolve);		
			}
		}
		return null;
	}
	
	private URL locateInternal(String name) {
		if (name.startsWith("/")) {
            name = name.substring(1);
        }
        
        final URL url = getClass().getResource("/" + name);

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
