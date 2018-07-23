package fr.alchemy.core.scene;

import fr.alchemy.core.scene.component.Transform;
import fr.alchemy.core.scene.entity.Entity;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;

/**
 * <code>Viewport</code> represents the sight range in the <code>AlchemyScene</code>.
 * The view can be bound to follow a specific {@link Entity entity} like a player or can be translated
 * to execute some cinematic action.
 * 
 * @author GnosticOccultist
 */
public final class Viewport {
	/**
	 * The X-axis property.
	 */
	private final DoubleProperty x = new SimpleDoubleProperty();
	/**
	 * The Y-axis property.
	 */
	private final DoubleProperty y = new SimpleDoubleProperty();
	/**
	 * The zoom property.
	 */
	private final DoubleProperty zoom = new SimpleDoubleProperty(1.0);
	
	/**
	 * Binds the <code>Viewport</code> origin to the provided <code>Entity</code> position. 
	 * A delta distance can be set between the origin and the entity position with
	 * the provided x and y values.
	 * 
	 * @param entity The entity to bind the viewport to.
	 * @param x		 The delta-X value.
	 * @param y		 The delta-Y value.
	 */
	public void bindToEntity(final Entity entity, final double x, final double y) {
		xProperty().bind(entity.getComponent(Transform.class).posXProperty().negate().add(x));
		yProperty().bind(entity.getComponent(Transform.class).posYProperty().negate().add(y));
	}
	
	/**
	 * Unbinds the <code>Viewport</code>.
	 */
	public void unbind() {
		xProperty().unbind();
		yProperty().unbind();
		zoomProperty().unbind();
	}
	
	/**
	 * @return The X-axis property.
	 */
	public DoubleProperty xProperty() {
		return x;
	}

	/**
	 * @return The X-axis value.
	 */
	public double getX() {
		return x.get();
	}
	
	/**
	 * @return The Y-axis property.
	 */
	public DoubleProperty yProperty() {
		return y;
	}

	/**
	 * @return The Y-axis value.
	 */
	public double getY() {
		return y.get();
	}
	
	/**
	 * @return The origin point of the <code>Viewport</code>.
	 */
	public Point2D getOrigin() {
		return new Point2D(getX(), getY());
	}
	
	/**
	 * @return The zoom property.
	 */
	public DoubleProperty zoomProperty() {
		return zoom;
	}
	
	/**
	 * Zoom the <code>Viewport</code> to the specified factor.
	 * 
	 * @param zoom The zooming factor.
	 */
	public void zoom(final double zoom) {
		zoomProperty().set(getZoom() + zoom); 
	}

	/**
	 * @return The zooming value.
	 */
	public double getZoom() {
		return zoom.get();
	}
}
