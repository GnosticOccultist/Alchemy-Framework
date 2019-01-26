package fr.alchemy.editor.core.ui.editor.graph;

import com.ss.rlib.common.util.dictionary.ObjectDictionary;

import fr.alchemy.editor.api.editor.graph.element.GraphConnection;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphElement;
import fr.alchemy.editor.api.editor.graph.element.GraphJoint;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;
import fr.alchemy.editor.api.editor.graph.skin.GraphSkin;
import fr.alchemy.editor.core.ui.editor.graph.skin.TitledGraphConnectionSkin;
import fr.alchemy.editor.core.ui.editor.graph.skin.TitledGraphConnectorSkin;
import fr.alchemy.editor.core.ui.editor.graph.skin.TitledGraphJointSkin;
import fr.alchemy.editor.core.ui.editor.graph.skin.TitledGraphNodeSkin;
import fr.alchemy.editor.core.ui.editor.graph.skin.TitledGraphTailSkin;
import javafx.util.Callback;

/**
 * <code>GraphSkinRegistry</code> is the registry class to store the various {@link GraphSkin} constructor based on their type
 * described in a <code>String</code>.
 * <p>
 * Call {@link #register(String, Callback)} to register a new graph skin type or {@link #newSkin(String, GraphElement)} to create 
 * a new instance graph skin of the given type and with the specified {@link GraphElement}.
 * 
 * @author GnosticOccultist
 */
public final class GraphSkinRegistry {
	
	/**
	 * The single instance of this registry.
	 */
	private static final GraphSkinRegistry INSTANCE = new GraphSkinRegistry();
	
	/**
	 * Return the single instance of the <code>GraphSkinRegistry</code>.
	 * 
	 * @return The graph skin registry.
	 */
	public static GraphSkinRegistry skin() {
		return INSTANCE;
	}
	
	/**
	 * The table of registered skins ordered by their tag.
	 */
	private final ObjectDictionary<String, Callback<? extends GraphElement, ? extends GraphSkin>> skins = 
			ObjectDictionary.ofType(String.class, Callback.class);
	
	/**
	 * Don't instantiate a new <code>GraphSkinRegistry</code>.
	 * Use {@link #skin()} to get the single instance.
	 */
	private GraphSkinRegistry() {
		register("titled.node", graph -> new TitledGraphNodeSkin((GraphNode) graph));
		register("titled.connector", graph -> new TitledGraphConnectorSkin((GraphConnector) graph));
		register("titled.connection", graph -> new TitledGraphConnectionSkin((GraphConnection) graph));
		register("titled.tail", graph -> new TitledGraphTailSkin((GraphConnector) graph));
		register("titled.joint", graph -> new TitledGraphJointSkin((GraphJoint) graph));
	}
	
	/**
	 * Registers the constructor for the {@link GraphSkin} with the specified skin type represented as a string.
	 * 
	 * @param skinTag	  The skin tage to find the graph skin.
	 * @param constructor The constructor to instantiate a new graph skin of this type.
	 */
	public void register(String skinTag, Callback<? extends GraphElement, ? extends GraphSkin> constructor) {
		skins.put(skinTag, constructor);
	}

	/**
	 * Creates a new {@link GraphSkin} corresponding to the provided type, if a constructor has been previously
	 * registered for this type with the {@link #register(String, Callback)} method. It will pass the given {@link GraphElement}
	 * as an argument for the constructor.
	 * <p>
	 * If no such graph skin type is present it will return a <code>null</code> value.
	 * 
	 * @param type
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends GraphSkin<?>> T newSkin(String type, GraphElement element) {
		Callback<GraphElement, GraphSkin> callback = (Callback<GraphElement, GraphSkin>) skins.get(type);
		if(callback != null) {
			return (T) callback.call(element);
		} else {
			// Search a registered skin type which is contained by the type argument, because we may have other
			// element such has 'input' or 'ouput'.
			for(String skinType : skins.keyArray(String.class)) {
				if(type.contains(skinType)) {
					callback = (Callback<GraphElement, GraphSkin>) skins.get(skinType);
					return (T) callback.call(element);
				}
			}
		}
		return null;
	}
}
