package fr.alchemy.core.scene;

import java.io.IOException;
import java.io.OutputStream;

import fr.alchemy.core.asset.Texture;
import fr.alchemy.core.asset.binary.BinaryReader;
import fr.alchemy.core.asset.binary.Exportable;
import fr.alchemy.utilities.ByteUtils;

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
	 * Internal use only for serialization.
	 */
	public SceneLayer() {}
	
	public SceneLayer(final int index) {
		this("Undefined", index);
	}
	
	public SceneLayer(final String name, final int index) {
		this.name = name;
		this.index = index;
	}
	
	/**
	 * @return The name of the layer.
	 */
	public String name() {
		return name;
	}
	
	/**
	 * @return The index value for the layer.
	 */
	public int index() {
		return index;
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
	public void export(final OutputStream os) throws IOException {
		os.write(ByteUtils.toBytes(getClass().getName().length()));
		os.write(ByteUtils.toBytes(getClass().getName()));
		
		os.write(ByteUtils.toBytes("name".length()));
		os.write(ByteUtils.toBytes("name"));
		os.write(ByteUtils.toBytes(name.length()));
		os.write(ByteUtils.toBytes(name));
		
		os.write(ByteUtils.toBytes("index".length()));
		os.write(ByteUtils.toBytes("index"));
		os.write(ByteUtils.toBytes(index));
	}

	@Override
	public void insert(final BinaryReader reader) throws IOException {
		name = reader.readString("name");
		index = reader.readInt("index");
	}
}
