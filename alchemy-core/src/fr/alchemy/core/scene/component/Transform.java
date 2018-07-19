package fr.alchemy.core.scene.component;

import fr.alchemy.core.scene.entity.Entity;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;

/**
 * <code>Transform</code> adds a position, a rotation and a scale for an 
 * {@link Entity entity} in 2D.
 * 
 * @author GnosticOccultist
 */
public final class Transform extends Component {

	/**
	 * The X position coordinate.
	 */
	private final DoubleProperty posX;
	/**
	 * The Y position coordinate.
	 */
	private final DoubleProperty posY;
	/**
	 * The rotation angle.
	 */
	private final DoubleProperty rotation;
	/**
	 * The X scale value.
	 */
	private final DoubleProperty scaleX;
	/**
	 * The Y scale value.
	 */
	private final DoubleProperty scaleY;
	
	/**
	 * Instantiates a new <code>Transform</code> with a position and a rotation
	 * of 0 and a scale of 1.
	 */
	public Transform() {
		this(0, 0);
	}
	
	/**
	 * Instantiates a new <code>Transform</code> with the provided position vector.
	 * The rotation is set to 0 and the scale to 1.
	 * 
	 * @param pos The position vector.
	 */
	public Transform(Point2D pos) {
		this(pos.getX(), pos.getY());
	}
	
	/**
	 * Instantiates a new <code>Transform</code> with the provided position vector.
	 * The rotation is set to 0 and the scale to 1.
	 * 
	 * @param posX The position X coordinate.
	 * @param posY The position Y coordinate.
	 */
	public Transform(double posX, double posY) {
		this(posX, posY, 0);
	}
	
	/**
	 * Instantiates a new <code>Transform</code> with the provided position vector and
	 * rotation value. The scale is set to 1.
	 * 
	 * @param posX 		The position X coordinate.
	 * @param posY 		The position Y coordinate.
	 * @param rotation  The rotation value.
	 */
	public Transform(double posX, double posY, double rotation) {
		this(posX, posY, rotation, 1, 1);
	}
	
	/**
	 * Instantiates a new <code>Transform</code> with the provided position vector,
	 * rotation value and scaling vector.
	 * 
	 * @param posX		The position X coordinate.
	 * @param posY		The position Y coordinate.
	 * @param rotation	The rotation value.
	 * @param scaleX	The scaling vector X coordinate.
	 * @param scaleY	The scaling vector Y coordinate.
	 */
	public Transform(double posX, double posY, double rotation, double scaleX, double scaleY) {
		this.posX = new SimpleDoubleProperty(posX);
		this.posY = new SimpleDoubleProperty(posY);
		this.rotation = new SimpleDoubleProperty(rotation);
		this.scaleX = new SimpleDoubleProperty(scaleX);
		this.scaleY = new SimpleDoubleProperty(scaleY);
	}
	
	/**
	 * @return The position X coordinate property.
	 */
	public DoubleProperty posXProperty() {
		return posX;
	}
	
	/**
	 * @return The position Y coordinate property.
	 */
	public DoubleProperty posYProperty() {
		return posY;
	}
	
	/**
	 * @return The X coordinate of the position.
	 */
	public double getPosX() {
		return posX.get();
	}
	
	/**
	 * @return The Y coordinate of the position.
	 */
	public double getPosY() {
		return posY.get();
	}
	
	/**
	 * @return The position vector.
	 */
	public Point2D getPosition() {
		return new Point2D(getPosX(), getPosY());
	}
	
	/**
	 * Sets the position to the provided values.
	 * 
	 * @param x The position X coordinate. 
	 * @param y The position Y coordinate.
	 */
	public void setPosition(final double x, final double y) {
		this.posX.set(x);
		this.posY.set(y);
	}

	/**
	 * Translates the position by adding it the provided values,
	 * basically translating the vector by another one.
	 * 
	 * @param x The X value to add.
	 * @param y The Y value to add.
	 */
	public void translate(final double x, final double y) {
		setPosition(getPosX() + x, getPosY() + y);
	}
	
	/**
	 * @return The distance between the two position from the <code>Transform</code>.
	 */
	public double distance(final Transform other) {
		return getPosition().distance(other.getPosition());
	}
	
	/**
	 * @return The rotation property.
	 */
	public DoubleProperty rotationProperty() {
		return rotation;
	}
	
	/**
	 * @return The rotation value.
	 */
	public double getRotation() {
		return rotation.get();
	}
	
	/**
	 * Sets the rotation to the specified value.
	 * 
	 * @param rotation The rotation value.
	 */
	public void setRotation(final double rotation) {
		this.rotation.set(rotation);
	}
	
	/**
	 * Rotates the rotation by adding it the specified value.
	 * 
	 * @param angle The angle to add to the rotation.
	 */
	public void rotate(final double angle) {
		rotation.set(getRotation() + angle);
	}
	
	/**
	 * @return The X coordinate property of the scaling vector.
	 */
	public DoubleProperty scaleXProperty() {
		return scaleX;
	}
	
	/**
	 * @return The Y coordinate property of the scaling vector.
	 */
	public DoubleProperty scaleYProperty() {
		return scaleY;
	}
	
	/**
	 * @return The X coordinate of the scaling vector.
	 */
	public double getScaleX() {
		return scaleX.get();
	}
	
	/**
	 * @return The Y coordinate of the scaling vector.
	 */
	public double getScaleY() {
		return scaleY.get();
	}
	
	/**
	 * @return The scaling vector.
	 */
	public Point2D getScale() {
		return new Point2D(getScaleX(), getScaleY());
	}
	
	/**
	 * Sets the scaling vector to the specified values.
	 * 
	 * @param x The X coordinate of the scaling vector. 
	 * @param y The Y coordinate of the scaling vector.
	 */
	public void setScale(final double x, final double y) {
		this.scaleX.set(x);
		this.scaleY.set(y);
	}

	/**
	 * Scales the scale by adding it the specified vector.
	 * 
	 * @param x The X value to add.
	 * @param y The Y value to add.
	 */
	public void scale(final double x, final double y) {
		setScale(getScaleX() + x, getScaleY() + y);
	}
	
	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "]: " + "posX: " + posX + " posY: " + posY +
				" rotation: " + rotation + " scaleX: " + scaleX + " scaleY: " + scaleY;
	}
}
