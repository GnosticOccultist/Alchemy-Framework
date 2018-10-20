package fr.alchemy.core.scene;

import java.io.IOException;

import fr.alchemy.core.asset.Texture;
import fr.alchemy.core.asset.binary.BinaryReader;
import fr.alchemy.core.asset.binary.BinaryWriter;
import fr.alchemy.core.asset.binary.Exportable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * <code>SceneLayer</code> is a layer inside a scene used to group object for rendering.
 * Each layers is rendered on top of the lower one, in order to give an impression of 'depth'
 * inside a 2D scene. 
 * For example each pixels of a {@link Texture} with no alpha value, will allow for the lower layer 
 * to be visible and so on.
 * 
 * @author GnosticOccultist
 */
public class SceneLayer implements Exportable {
	
	/**
	 * The default layer which is used when no layer is specified.
	 */
	public static final SceneLayer DEFAULT = new SceneLayer("DEFAULT", 100000);
	/**
	 * The highest layer in a scene.
	 */
	public static final SceneLayer TOP = new SceneLayer("TOP", Integer.MAX_VALUE);
	/**
	 * The lowest layer in a scene.
	 */
	public static final SceneLayer BOTTOM = new SceneLayer("BOTTOM", Integer.MIN_VALUE);
	
	/**
	 * The name of the layer.
	 */
	private String name;
	/**
	 * The index of the layer.
	 */
	private int index;
	/**
	 * The visible property of the layer.
	 */
	private DoubleProperty visibility;
	
	/**
	 * Internal use only for serialization.
	 */
	public SceneLayer() {}
	
	public SceneLayer(final int index) {
		this("Undefined", index);
	}
	
	public SceneLayer(final String name, final int index) {
		this.name = name;
		this.index = index;
		this.visibility = new SimpleDoubleProperty(1.0D);
	}
	
	/**
	 * Return the name of the layer.
	 * 
	 * @return The name of the layer.
	 */
	public String name() {
		return name;
	}
	
	/**
	 * Return the index value of the layer.
	 * 
	 * @return The index value.
	 */
	public int index() {
		return index;
	}
	
	/**
	 * Return the visibility property of the layer.
	 * 
	 * @return The visibility property.
	 */
	public DoubleProperty visibilityProperty() {
		return visibility;
	}
	
	/**
	 * Return the layer visibility.
	 * 
	 * @return The layer visibility (0 &ge; visibility &le; 1).
	 */
	public double visibility() {
		return visibility.get();
	}
	
	/**
	 * @return Whether the provided name and index equals to the <code>SceneLayer</code>.
	 */
	public boolean equals(final String name, final int index) {
		if(name == this.name && index == this.index) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(final Object o) {
		if(this == o) {
			return true;
		}
		if(o == null || !(o instanceof SceneLayer)) {
			return false;
		}
		SceneLayer layer = (SceneLayer) o;
		if(layer.name() == this.name && layer.index() == this.index) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return name + "[" + index + "]";
	}

	@Override
	public void export(final BinaryWriter writer) throws IOException {
		writer.write("name", name);
		writer.write("index", index);
	}

	@Override
	public void insert(final BinaryReader reader) throws IOException {
		name = reader.readString("name", "undefined");
		index = reader.readInteger("index", SceneLayer.DEFAULT.index());
	}
}
