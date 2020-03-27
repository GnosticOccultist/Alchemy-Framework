package fr.alchemy.editor.core.asset;

import fr.alchemy.utilities.task.actions.ModifierAction;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * <code>Texture</code> represents a 2D image which can be set as a graphics node. 
 * The size ratio and the viewport can be modified if necessary.
 * The class itself is a wrapper of the JavaFX {@link ImageView}.
 * 
 * @author GnosticOccultist
 */
public class Texture extends ImageView implements IAsset {
	
	/**
	 * The file's path of the texture.
	 */
	private String file;
	
	/**
	 * <strong>Internal use only!</strong>
	 * <p>If you want to instantiates a new <code>Texture</code>, please
	 * use {@link AssetManager#loadTexture(String)}.
	 */
	Texture() {
		this(null, "undefined");
	}
	
	/**
	 * <strong>Internal use only!</strong>
	 * <p>If you want to instantiates a new <code>Texture</code>, please
	 * use {@link AssetManager#loadTexture(String)}.
	 * 
	 * @param image The image.
	 */
	Texture(final Image image, final String file) {
		super(image);
		this.file = file;
	}
	
	/**
	 * Scales the <code>Texture</code> image to the gray.
	 */
	public final void grayscale() {
		set(applyEffect(Color::grayscale));
	}
	
	/**
	 * Brighten the <code>Texture</code> image.
	 */
	public final void brighter() {
		set(applyEffect(Color::brighter));
	}
	
	/**
	 * Darken the <code>Texture</code> image.
	 */
	public final void darker() {
		set(applyEffect(Color::darker));
	}
	
	/**
	 * Lower saturation of the <code>Texture</code> image.
	 */
	public final void desaturate() {
		set(applyEffect(Color::desaturate));
	}
	
	/**
	 * Rise saturation of the <code>Texture</code> image.
	 */
	public final void saturate() {
		set(applyEffect(Color::saturate));
	}

	/**
	 * Applies a desired effect onto a <code>Texture</code> image by
	 * modifying its color.
	 * 
	 * @return The modified texture.
	 */
	public final Texture applyEffect(final ModifierAction<Color> modifier) {
		final int width = (int) getImage().getWidth();
		final int height = (int) getImage().getHeight();
		
		final PixelReader reader = getImage().getPixelReader();
		final WritableImage image = new WritableImage(width, height);
		final PixelWriter writer = image.getPixelWriter();
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				writer.setColor(x, y, modifier.modify(reader.getColor(x, y)));
			}
		}
		
		return new Texture(image, file);
	}

	public String getFile() {
		return file;
	}
	
	/**
	 * Sets the width, height and image of the provided <code>Texture</code>
	 * to this one.
	 * 
	 * @param other The other texture to copy from.
	 */
	public final void set(final Texture other) {
		setFitWidth(other.getFitWidth());
		setFitHeight(other.getFitHeight());
		setImage(other.getImage());
		file = new String(other.file);
	}
	
	/**
	 * Creates a new instance of <code>Texture</code> with the width, height and image
	 * of this one.
	 * 
	 * @return A copy of the <code>Texture</code>.
	 */
	public final Texture copy() {
		final Texture texture = new Texture(getImage(), file);
		texture.setFitWidth(getFitWidth());
		texture.setFitHeight(getFitHeight());
		texture.file = new String(file);
		return texture;
	}

	@Override
	public void cleanup() {
		setImage(null);
		file = null;
	}
}