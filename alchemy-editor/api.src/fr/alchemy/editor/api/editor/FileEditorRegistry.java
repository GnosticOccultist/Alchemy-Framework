package fr.alchemy.editor.api.editor;

import java.util.Optional;

import fr.alchemy.editor.core.ui.editor.nodes.DialogNodesFileEditor;
import fr.alchemy.editor.core.ui.editor.text.PropertiesEditor;
import fr.alchemy.utilities.Instantiator;
import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.dictionnary.ObjectDictionary;
import fr.alchemy.utilities.logging.FactoryLogger;
import fr.alchemy.utilities.logging.Logger;

/**
 * <code>EditorRegistry</code> is a registry storing all {@link FileEditor} implementation to use for opening and editing a file of 
 * a specific extension.
 * <br>
 * To register your own implementation of {@link FileEditor}, please use {@link #register(String, Class)} by getting the single-instance
 * of this class with {@link #get()}.
 * 
 * @see #register(String, Class)
 * @see #get()
 * 
 * @author GnosticOccultist
 */
public final class FileEditorRegistry {
	
	/**
	 * The logger for all registries of the editor.
	 */
	private static final Logger logger = FactoryLogger.getLogger("alchemy-editor.registry");

	/**
	 * The single-instance of the registry.
	 */
	private static final FileEditorRegistry INSTANCE = new FileEditorRegistry();
	
	/**
	 * Return the <code>FileEditorRegistry</code> instance.
	 * 
	 * @return The single-instance of the registry.
	 */
	public static FileEditorRegistry get() {
		return INSTANCE;
	}
	
	/**
	 * The file editor dictionary organized by their extension.
	 */
	private final ObjectDictionary<String, Class<? extends FileEditor>> editors;
	
	/**
	 * No instantiation of the <code>FileEditorRegistry</code>, use {@link #get()} if you want to access
	 * the instance of this class.
	 */
	private FileEditorRegistry() {
		this.editors = ObjectDictionary.ofType(String.class, Class.class);
		
		register("properties", PropertiesEditor.class);
		register("nodes", DialogNodesFileEditor.class);
		
		logger.info("Sucessfully loaded " + getClass().getSimpleName() + " with " + editors.size() 
			+ " file editors -> \n" + editors.toString());
	}
	
	/**
	 * Registers the provided {@link FileEditor} type for the given extension in the <code>FileEditorRegistry</code>.
	 * 
	 * @param extension	 The extension that the file editor can open (not null, not empty).
	 * @param editorType The type of file editor to register (not null).
	 */
	public void register(String extension, Class<? extends FileEditor> editorType) {
		Validator.nonEmpty(extension, "The file extension can't be empty or null!");
		Validator.nonNull(editorType, "The file editor type can't be null!");
		
		editors.put(extension, editorType);
	}
	
	/**
	 * Creates and return an optional {@link FileEditor} instance capable of opening and editing a file of the given extension.
	 * It can also return an empty optional value, if no file editor implementation was found in the <code>FileEditorRegistry</code>.
	 * 
	 * @param extension The extension that the file editor should open (not null, not empty). 
	 * @return			An optional file editor instance, empty if none was found.
	 */
	public Optional<FileEditor> createFor(String extension) {
		Validator.nonEmpty(extension, "The file extension can't be empty or null!");
		
		FileEditor result = null;
		Class<? extends FileEditor> type = editors.get(extension);
		if(type != null) {
			result = Instantiator.fromClass(type);
		}
		
		return Optional.ofNullable(result);
	}
}
