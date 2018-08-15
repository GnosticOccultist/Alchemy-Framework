package fr.alchemy.core.scene.component;

import java.io.IOException;

import fr.alchemy.core.asset.binary.BinaryReader;
import fr.alchemy.core.asset.binary.BinaryWriter;
import fr.alchemy.core.scene.entity.Entity;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

/**
 * <code>NameComponent</code> allows the <code>Entity</code> to own a name 
 * which can be displayed with its <code>VisualComponent</code>.
 * 
 * @author GnosticOccultist
 */
public class NameComponent extends Component {
	
	/**
	 * The name of the <code>Entity</code>.
	 */
	private String name;
	/**
	 * The label containing the name of the <code>Entity</code>.
	 */
	private Label nameLabel = new Label();
	
	public NameComponent() {
		this("Unknown");
	}
	
	public NameComponent(final String name) {
		this(name, false);
	}
	
	public NameComponent(final String name, final boolean visible) {
		setName(name);
		setVisible(visible);
		this.nameLabel.setLayoutY(-20);
	}
	
	@Override
	public void onAttached(final Entity entity) {
		super.onAttached(entity);
		
		attachLabel();
	}
	
	@Override
	public void onDetached(final Entity entity) {
		detachLabel();
		
		super.onDetached(entity);
	}
	
	@Override
	public void enable() {
		setVisible(true);
	}
	
	@Override
	public void disable() {
		setVisible(false);
	}
	
	/**
	 * Sets the <code>NameComponent</code> visible on the screen. 
	 * 
	 * @param visible Whether to set the component visible.
	 */
	public void setVisible(final boolean visible) {
		this.nameLabel.setVisible(visible);
	}
	
	/**
	 * Attach the name label to the <code>VisualComponent</code> of the owner.
	 */
	private void attachLabel() {
		getOwner().getComponent(VisualComponent.class).getView().addNode(nameLabel);
	}
	
	/**
	 * Detach the name label from the <code>VisualComponent</code> of the owner.
	 */
	private void detachLabel() {
		getOwner().getComponent(VisualComponent.class).getView().removeNode(nameLabel);
	}
	
	/**
	 * @return The name of the <code>Entity</code> or 'Unknown' 
	 * 		   if the name is empty or null.
	 */
	public String getName() {
		if(name.isEmpty() || name == null) {
			return "Unknown";
		}
		
		return name;
	}
	
	/**
	 * Sets the name of the <code>Entity</code>.
	 * 
	 * @param name The name of the entity.
	 */
	public void setName(final String name) {
		this.name = name;
		this.nameLabel.setText(getName());
	}
	
	/**
	 * Sets the color of the String label name.
	 * 
	 * @param color The color for the name.
	 */
	public void setColor(final Paint color) {
		nameLabel.setTextFill(color);
	}
	
	@Override
	public String toString() {
		return "Name: " + name;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj instanceof NameComponent) {
			return name.equals(((NameComponent) obj).name);
		} else if (obj instanceof String) {
			return name.equals((String) obj);
		}
		return false;
	}
	
	@Override
	public void export(final BinaryWriter writer) throws IOException {
		super.export(writer);
		writer.write("name", name);
	}
	
	@Override
	public void insert(final BinaryReader reader) throws IOException {
		super.insert(reader);
		setName(reader.readString("name", "Unknown"));
	}
}
