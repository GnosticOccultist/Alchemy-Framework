package fr.alchemy.core.util;

import java.io.IOException;

import fr.alchemy.core.asset.binary.BinaryExporter;
import fr.alchemy.core.asset.binary.BinaryImporter;
import fr.alchemy.core.asset.binary.BinaryReader;
import fr.alchemy.core.asset.binary.BinaryWriter;
import fr.alchemy.core.asset.binary.Exportable;

/**
 * <code>Color</code> represents a utility class defining a 4 channel color 
 * composed of a red, blue, green and alpha components.
 * 
 * @author GnosticOccultist
 */
public final class Color implements Exportable {
	
	/**
	 * The red, green and blue components of the color.
	 */
	private float r, g, b;
	/**
	 * The alpha component of the color.
	 */
	private float alpha;
	
	public Color() {}
	
	public Color(final float r, final float g, final float b) {
		this(r, g, b, 1.0f);
	}
	
	public Color(final float r, final float g, final float b, final float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.alpha = a;
	}
	
	/**
	 * Sets the components of this <code>Color</code> to the provided one.
	 * 
	 * @param color The color to take the components from.
	 */
	public void set(final Color color) {
		this.r = color.r;
		this.g = color.g;
		this.b = color.b;
		this.alpha = color.alpha;
	}
	
	/**
	 * Sets the red, green and blue components of the <code>Color</code> to
	 * the provided ones.
	 * 
	 * @param r The red component value.
	 * @param g The green component value.
	 * @param b The blue component value.
	 */
	public void set(final float r, final float g, final float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * Sets the red, green, blue and alpha (opacity) components of the <code>Color</code> to
	 * the provided ones.
	 * 
	 * 
	 * @param r The red component value.
	 * @param g The green component value.
	 * @param b The blue component value.
	 * @param a The alpha (opacity) component value.
	 */
	public void set(final float r, final float g, final float b, final float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.alpha = a;
	}
	
	/**
	 * Translates a Java FX {@link javafx.scene.paint.Color} to an Alchemy {@link Color}.
	 * 
	 * @param color The FX color to translate.
	 * @return 		The alchemy color.
	 */
	public static Color from(final javafx.scene.paint.Color color) {
		return new Color((float) color.getRed(), (float) color.getGreen(), 
				(float) color.getBlue(), (float) (1.0f - color.getOpacity()));
	}
	
	/**
	 * Translates the <code>Color</code> to a Java FX {@link javafx.scene.paint.Color}.
	 */
	public javafx.scene.paint.Color toFXColor() {
		return new javafx.scene.paint.Color(r, g, b, alpha);
	}
	
	/**
	 * @return The red component value.
	 */
	public float getR() {
		return r;
	}
	
	/**
	 * Sets the red component value.
	 * 
	 * @param r The red component value.
	 * @return  The updated color.
	 */
	public Color setR(final float r) {
		this.r = r;
		return this;
	}
	
	/**
	 * @return The green component value.
	 */
	public float getG() {
		return g;
	}
	
	/**
	 * Sets the green component value.
	 * 
	 * @param g The green component value.
	 * @return  The updated color.
	 */
	public Color setG(final float g) {
		this.g = g;
		return this;
	}

	/**
	 * @return The blue component value.
	 */
	public float getB() {
		return b;
	}
	
	/**
	 * Sets the blue component value.
	 * 
	 * @param b The blue component value.
	 * @return  The updated color.
	 */
	public Color setB(final float b) {
		this.b = b;
		return this;
	}
	
	/**
	 * @return The alpha (opacity) component value.
	 */
	public float getAlpha() {
		return alpha;
	}
	
	/**
	 * Sets the alpha (opacity) component value.
	 * 
	 * @param alpha The alpha component value.
	 * @return  	The updated color.
	 */
	public Color setAlpha(final float alpha) {
		this.alpha = alpha;
		return this;
	}
	
	@Override
	public boolean equals(final Object o) {
		if(this == o) {
			return true;
		}
		if(o == null) {
			return false;
		}
		if(o instanceof Color) {
			Color color = (Color) o;
			if(color.r == r && color.g == g && color.b == b && color.alpha == alpha) {
				return true;
			}
		} else if(o instanceof javafx.scene.paint.Color) {
			javafx.scene.paint.Color color = (javafx.scene.paint.Color) o;
			if(color.getRed() == r && color.getGreen() == g && color.getBlue() == b && color.getOpacity() == alpha) {
				return true;
			}
		}

		return false;
	}
	
	@Override
	public String toString() {
		return "Color[r: " + r + "; g: " + g + "; b: " + b + "; alpha: " + alpha + "]";
	}

	@Override
	public void export(final BinaryExporter exporter) throws IOException {
		final BinaryWriter writer = exporter.getCapsule(this);
		writer.write(r, "red", 1.0f);
		writer.write(g, "green", 1.0f);
		writer.write(b, "blue", 1.0f);
		writer.write(alpha, "alpha", 1.0f);
	}

	@Override
	public void insert(final BinaryImporter importer) throws IOException {
		final BinaryReader reader = importer.getCapsule(this);
		reader.readFloat("red", 1.0f);
		reader.readFloat("green", 1.0f);
		reader.readFloat("blue", 1.0f);
		reader.readFloat("alpha", 1.0f);
	}	
}
