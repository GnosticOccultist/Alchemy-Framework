package fr.alchemy.core.asset;

import fr.alchemy.core.scene.entity.Entity;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * <code>Texture</code> represents a 2D image which can be set as a graphics node for
 * an {@link Entity entity}. The size ratio and the viewport can be modified if necessary.
 * The class itself is a wrapper of the JavaFX {@link ImageView}.
 * 
 * @author GnosticOccultist
 */
public class Texture extends ImageView {
	
	/**
	 * <strong>Internal use only!</strong>
	 * <p>If you want to instantiates a new <code>Texture</code>, please
	 * use {@link AssetManager#loadTexture(String)}.
	 * 
	 * @param image The image.
	 */
	Texture(Image image) {
		super(image);
	}
}
