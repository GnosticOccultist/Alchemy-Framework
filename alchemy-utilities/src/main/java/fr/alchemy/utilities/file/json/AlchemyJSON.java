package fr.alchemy.utilities.file.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.file.json.JSONLiteral.JSONFalseLiteral;
import fr.alchemy.utilities.file.json.JSONLiteral.JSONNullLiteral;
import fr.alchemy.utilities.file.json.JSONLiteral.JSONTrueLiteral;

public final class AlchemyJSON {

	/**
	 * The JSON literal representing a <code>null</code> value.
	 */
	public static final JSONValue NULL = new JSONNullLiteral();
	/**
	 * The JSON literal representing a <code>true</code> value.
	 */
	public static final JSONValue TRUE = new JSONTrueLiteral();
	/**
	 * The JSON literal representing a <code>false</code> value.
	 */
	public static final JSONValue FALSE = new JSONFalseLiteral();
	
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
	
	public static JSONObject object() {
		return new JSONObject();
	}
	
	public static JSONArray array() {
		return new JSONArray();
	}
	
	public static JSONValue value(String value) {
		return value == null ? NULL : new JSONString(value);
	}
	
	public static JSONValue value(boolean value) {
		return value ? TRUE : FALSE;
	}
}
