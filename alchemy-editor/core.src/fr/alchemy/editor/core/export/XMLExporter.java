package fr.alchemy.editor.core.export;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import fr.alchemy.editor.api.editor.graph.element.GraphConnection;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;
import fr.alchemy.editor.core.ui.editor.graph.GraphNodeEditorView;
import fr.alchemy.editor.core.ui.editor.graph.SimpleGraphNodeEditor;

public class XMLExporter {
	
	private SimpleGraphNodeEditor editor;
	
	private GraphNodeEditorView view;
	
	public XMLExporter(SimpleGraphNodeEditor editor, GraphNodeEditorView view) {
		this.editor = editor;
		this.view = view;
	}
	
	public void save(OutputStream output) throws IOException {
		try {
			AttributesImpl attrs = new AttributesImpl();
			StreamResult result = new StreamResult(output);
			TransformerHandler handler = createHandler(result);
			handler.startDocument();
			
			addAttribute("name", "graph-node", attrs);
			addAttribute("version", "0.6", attrs);
			
			startElement("project", attrs, handler);
			
			DefaultGraphNodeExporter nodeExporter = new DefaultGraphNodeExporter();
			nodeExporter.save(handler);
			
			endElement("project", handler);
			handler.endDocument();
			
			output.close();
			
		} catch (Throwable t) {
			IOException propagatedException = new IOException("Failed to save the project file");
			propagatedException.initCause(t);
			throw propagatedException;
		}
	}

	private void addAttribute(String name, String value, AttributesImpl attrs) {
		if (value != null) {
			attrs.addAttribute("", name, name, "CDATA", value);
		}
	}
		
	public GraphNodeEditorView getView() {
		return view;
	}
	
	private static AttributesImpl ourEmptyAttributes = new AttributesImpl();

	protected void startElement(String name, TransformerHandler handler) throws SAXException {
		startElement(name, ourEmptyAttributes, handler);		
	}

	protected void startElement(String name, AttributesImpl attrs, TransformerHandler handler) throws SAXException {
		handler.startElement("", name, name, attrs);
		attrs.clear();
	}

	protected void endElement(String name, TransformerHandler handler) throws SAXException {
		handler.endElement("", name, name);
	}
	
	private TransformerHandler createHandler(StreamResult result) throws TransformerConfigurationException {
		SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
	    TransformerHandler handler = factory.newTransformerHandler();
	    Transformer serializer = handler.getTransformer();
	    serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    serializer.setOutputProperty(OutputKeys.INDENT, "yes");
	    serializer.setOutputProperty(OutputKeys.METHOD, "xml");
	    serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	    handler.setResult(result);
	    return handler;
	}
	
	class DefaultGraphNodeExporter implements ExporterModule {

		@Override
		public void save(TransformerHandler handler) throws SAXException, IOException {
			AttributesImpl attribs = new AttributesImpl();
			startElement("graph-nodes", attribs, handler);

			
			GraphNode root = editor.getRootNode();
			writeTask(handler, root);
			
			GraphNode prevNode = root;
			for(GraphConnector conn : prevNode.getConnectors()) {
				for(GraphConnection con : conn.getConnections()) {
					if(!con.getTarget().getParent().equals(prevNode)) {
						writeTask(handler, con.getTarget().getParent());
					}
				}
			}
			
			endElement("graph-nodes", handler);
		}
		
		private void writeTask(TransformerHandler handler, GraphNode node) throws SAXException, IOException {
			if(node.id() == -1) {
				throw new IllegalArgumentException("The provided node '" + node + "' isn't created yet!");
			}
			
			AttributesImpl attribs = new AttributesImpl();
			addAttribute("id", String.valueOf(node.id()), attribs);
			addAttribute("type", node.getType(), attribs);
			addAttribute("ancestorIDs", getAncestorIDs(node), attribs);
			addAttribute("descendantIDs", getDescendantIDs(node), attribs);
			
			startElement("node", attribs, handler);
			
			endElement("node", handler);
		}
		
		private String getAncestorIDs(GraphNode node) {
			String ids = "";
			for(GraphConnector conn : node.getConnectors()) {
				for(GraphConnection con : conn.getConnections()) {
					if(!con.getSource().getParent().equals(node)) {
						ids += con.getSource().getParent().id(); 
					}
				}
			}
			return ids;
		}
		
		private String getDescendantIDs(GraphNode node) {
			String ids = "";
			for(GraphConnector conn : node.getConnectors()) {
				for(GraphConnection con : conn.getConnections()) {
					if(!con.getTarget().getParent().equals(node)) {
						ids += con.getTarget().getParent().id(); 
					}
				}
			}
			return ids;
		}
	}
}
