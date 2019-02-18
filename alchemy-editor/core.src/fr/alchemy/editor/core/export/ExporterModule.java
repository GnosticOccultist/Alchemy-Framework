package fr.alchemy.editor.core.export;

import java.io.IOException;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;

public interface ExporterModule {
	
	void save(TransformerHandler handler) throws SAXException, IOException;
}
