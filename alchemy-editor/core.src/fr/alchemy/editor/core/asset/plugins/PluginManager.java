package fr.alchemy.editor.core.asset.plugins;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import fr.alchemy.editor.api.editor.FileEditorRegistry;
import fr.alchemy.editor.api.plugin.IEditorPlugin;
import fr.alchemy.utilities.Instantiator;
import fr.alchemy.utilities.SystemUtils;
import fr.alchemy.utilities.collections.array.Array;
import fr.alchemy.utilities.file.FileUtils;
import fr.alchemy.utilities.logging.FactoryLogger;
import fr.alchemy.utilities.logging.Logger;

public class PluginManager {

	/**
	 * The editor's plugins logger.
	 */
	private static final Logger logger = FactoryLogger.getLogger("alchemy.editor.plugins");
	
	private static final PluginManager INSTANCE = new PluginManager();
	
	public static PluginManager get() {
		return INSTANCE;
	}
	
	/**
	 * The registered editor plugins.
	 */
	private final Array<IEditorPlugin> plugins = Array.ofType(IEditorPlugin.class);
	
	private PluginManager() {}
	
	public PluginManager start() {
		loadPlugins();
		initialize();
		
		return this;
	}

	private void loadPlugins() {
		Path pluginPath = SystemUtils.pathToWorkingDirectory().resolve("plugins");
		if(Files.exists(pluginPath)) {
			Array<Path> files = FileUtils.getFiles(pluginPath, "jar");
			URL[] urls = FileUtils.toURL(files);
			
			URLClassLoader classLoader = new URLClassLoader(urls);
			for(Path file : files) {
				try(JarInputStream jin = new JarInputStream(Files.newInputStream(file))) {
					for(JarEntry entry = jin.getNextJarEntry(); entry != null; entry = jin.getNextJarEntry()) {
						if(entry.isDirectory()) {
							continue;
			            }
						
						IEditorPlugin plugin = Instantiator.fromJarEntry(classLoader, entry, 
								clazz -> IEditorPlugin.class.isAssignableFrom(clazz) 
								&& !clazz.isInterface() 
								&& !Modifier.isAbstract(clazz.getModifiers()));
						
						if(plugin == null) {
							continue;
						}
						
						logger.info("Successfully found and loaded editor plugin '" + entry.getName() + "'!");
						
						plugins.add(plugin);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			FileUtils.safeClose(classLoader);
		}
	}
	
	private void initialize() {
		plugins.forEach(IEditorPlugin::initialize);
		plugins.forEach(plugin -> plugin.register(FileEditorRegistry.get()));
	}
}
