package fr.alchemy.editor.core.ui.component.asset.tree.elements;

import java.nio.file.Paths;

import javafx.scene.control.ProgressIndicator;

/**
 * <code>LoadingElement</code> is an implementation of {@link AssetElement}, which
 * is simply used to display a {@link ProgressIndicator} meaning that the asset hierarchy
 * is currently being loaded.
 * 
 * @author GnosticOccultist
 */
public class LoadingElement extends AssetElement {
	
	/**
	 * The single-instance of the loading element.
	 */
	private static final LoadingElement INSTANCE = new LoadingElement();
	
	/**
	 * Return the single-instance of the <code>LoadingElement</code>.
	 * 
	 * @return The loading element.
	 */
	public static LoadingElement getInstance() {
		return INSTANCE;
	}
	
	/**
	 * The <code>LoadingElement</code> is a single-instanced class.
	 * If you want to access it, use {@link #getInstance()}.
	 */
	private LoadingElement() {
		super(Paths.get("/"));
	}
}
