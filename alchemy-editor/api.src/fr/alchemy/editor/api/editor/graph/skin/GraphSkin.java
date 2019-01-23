package fr.alchemy.editor.api.editor.graph.skin;

import java.util.function.Consumer;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.element.GraphElement;
import fr.alchemy.utilities.Validator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.Node;

/**
 * <code>GraphSkin</code> is the base abstract class for all skins implementation.
 * It contains the minimal common logic required for a skin implementation of a specific {@link GraphElement}.
 * 
 * @param <E> The graph element type the skin is designed for.
 * 
 * @author GnosticOccultist
 */
public abstract class GraphSkin<E extends GraphElement> {
	
	/**
	 * The selection property of this skin.
	 */
	private final BooleanProperty selectedProperty = new BooleanPropertyBase() {
		
		@Override
		protected void invalidated() {
			selectionChanged(get());
		}
		
		@Override
		public String getName() {
			return "selected";
		}
		
		@Override
		public Object getBean() {
			return GraphSkin.this;
		}
	};
	
	/**
	 * The current graph node editor.
	 */
	private GraphNodeEditor editor;
	/**
	 * The skinned graph element.
	 */
	private final E element;
	/**
	 * The consumer to handle graph skin movement.
	 */
	private Consumer<GraphSkin<?>> onPositionMoved;
	
	/**
	 * Instantiates a new <code>GraphSkin</code> for the specified {@link GraphElement}.
	 * 
	 * @param element The element to create the skin for.
	 */
	protected GraphSkin(E element) {
		this.element = element;
	}
	
	/**
	 * Return the root {@link Node} used by the <code>GraphSkin</code>.
	 * 
	 * @return The root-node of the graph skin.
	 */
	public abstract Node getRoot();
	
	/**
	 * Called after the <code>GraphSkin</code> is removed. Override this method to
	 * implement a cleanup code for the skin.
	 */
	public void cleanup() {
		onPositionMoved = null;
		editor = null;
	}
	
	/**
	 * Updates whether the <code>GraphSkin</code> is in a selected state.
	 * <p>
	 * This method will be automatically called by the <code>SelectionTracker</code>
	 * when needed.
	 * </p>
	 */
	public void updateSelection() {
		setSelected(editor == null ? false : editor.isSelected(element));
	}
	
	/**
	 * Called whenever the selection state has changed for the <code>GraphSkin</code>.
	 * 
	 * @param selected Whether the skin is selected.
	 */
	protected abstract void selectionChanged(final boolean selected);
	
	/**
	 * Return the selection property of the <code>GraphSkin</code>, which determines 
	 * whether the skin is selected. 
	 * 
	 * @return The selected property of the skin.
	 */
	public ReadOnlyBooleanProperty selectedProperty() {
		return selectedProperty;
	}
	
	/**
	 * Return whether the <code>GraphSkin</code> is selected.
	 * 
	 * @return Whether the skin is selected.s
	 */
	public boolean isSelected() {
		return selectedProperty.get();
	}
	
	/**
	 * Sets whether the <code>GraphSkin</code> is selected.
	 * <p>
	 * <b>Shouldn't</b> be called directly, the selection state is managed by the selection
	 * manager of the {@link GraphNodeEditor}.
	 * 
	 * @param selected Whether the skin is selected.
	 */
	protected void setSelected(final boolean selected) {
		selectedProperty.set(selected);
	}
	
	/**
	 * <b>INTERNAL USE ONLY</b>
	 * Called when the position of the <code>GraphSkin</code> has been changed.
	 */
	public final void positionMoved() {
		final Consumer<GraphSkin<?>> inform = onPositionMoved;
		if(inform != null) {
			inform.accept(this);
		}
	}
	
	/**
	 * <b>INTERNAL USE ONLY</b>
	 * Sets the consumer to invoke when the position of the <code>GraphSkin</code> 
	 * has been changed.
	 * 
	 * @param onPositionMoved The position changed consumer.
	 */
	public final void setOnPositionMoved(final Consumer<GraphSkin<?>> onPositionMoved) {
		this.onPositionMoved = onPositionMoved;
	}
	
	/**
	 * Return the {@link GraphNodeEditor} that the <code>GraphSkin</code> is a part of.
	 * 
	 * @return The graph editor that the graph skin is a part of.
	 */
	protected GraphNodeEditor getGraphEditor() {
		return editor;
	}
	
	/**
	 * Sets the {@link GraphNodeEditor} that the <code>GraphSkin</code> is a part of.
	 * The provided editor can't be null.
	 * 
	 * @param editor The graph editor that the graph skin is a part of.
	 */
	public void setGraphEditor(GraphNodeEditor editor) {
		Validator.nonNull(editor);
		this.editor = editor;
	}
	
	/**
	 * Return the {@link GraphElement} represented by the <code>GraphSkin</code>.
	 * 
	 * @return The element represented by the graph skin.
	 */
	public final E getElement() {
		return element;
	}
}
