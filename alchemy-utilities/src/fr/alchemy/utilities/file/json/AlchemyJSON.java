package fr.alchemy.utilities.file.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import fr.alchemy.utilities.Validator;

public final class AlchemyJSON {

	/**
	 * The JSON literal <code>null</code>.
	 */
	public static final JSONValue NULL = new JSONLiteral("null");
	/**
	 * The JSON literal <code>true</code>.
	 */
	public static final JSONValue TRUE = new JSONLiteral("true");
	/**
	 * The JSON literal <code>false</code>.
	 */
	public static final JSONValue FALSE = new JSONLiteral("false");
	
	/**
	 * Private constructor to inhibit instantiation of <code>AlchemyJSON</code>.
	 */
	private AlchemyJSON() {}
	
	public static JSONValue parse(String value) {
		Validator.nonEmpty(value);
		
		try {
			return parse(new StringReader(value));
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static JSONValue parse(Reader reader) throws IOException {
		Validator.nonNull(reader);
		JSONParser parser = JSONParser.newParser(reader);
		parser.parse();
		return parser.value;
	}
}
