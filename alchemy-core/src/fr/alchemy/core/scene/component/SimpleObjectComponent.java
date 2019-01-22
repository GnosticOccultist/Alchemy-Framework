package fr.alchemy.core.scene.component;

import java.io.IOException;

import fr.alchemy.core.asset.binary.BinaryExporter;
import fr.alchemy.core.asset.binary.BinaryImporter;
import fr.alchemy.core.scene.entity.Entity;

/**
 * <code>SimpleObjectComponent</code> represents a {@link Component} defined by a single object.
 * It is basically a wrapper around the object, allowing it to be attached to an {@link Entity}.
 * 
 * @author GnosticOccultist
 */
public class SimpleObjectComponent<T> extends Component {
	/**
	 * The object defining the component.
	 */
	private T object;
	
	public SimpleObjectComponent() {}
	
	public SimpleObjectComponent(final T object) {
		this.object = object;
	}
	
	/**
	 * @return The object contained in the component.
	 */
	public T getObject() {
		return object;
	}
	
	@Override
	public String toString() {
		return "Object: " + object;
	}
	
	@Override
	public void export(final BinaryExporter exporter) throws IOException {
		super.export(exporter);
	}
	
	@Override
	public void insert(final BinaryImporter importer) throws IOException {
		super.insert(importer);
	}
}
