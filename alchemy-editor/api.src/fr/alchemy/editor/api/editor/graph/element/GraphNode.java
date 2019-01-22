package fr.alchemy.editor.api.editor.graph.element;

import java.util.List;

public interface GraphNode extends GraphElement {
	
	default double getX() {
		return 0;
	}
	
	default double getY(){
		return 0;
	}
	
	default double getWidth(){
		return 151;
	}
	
	default double getHeight(){
		return 101;
	}
	
	List<GraphConnector> getConnectors();
}
