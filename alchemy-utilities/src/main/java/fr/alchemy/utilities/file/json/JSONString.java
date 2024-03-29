package fr.alchemy.utilities.file.json;

import java.io.IOException;

import fr.alchemy.utilities.Validator;

/**
 * <code>JSONString</code> is an implementation of {@link JSONValue} for
 * strings.
 * 
 * @author GnosticOccultist
 */
class JSONString extends JSONValue {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * The readed string value.
	 */
	private final String value;
	
	JSONString(String value) {
		Validator.nonEmpty(value, "The string value can't be null or empty!");
		this.value = value;
	}
	
	@Override
	void write(JSONWriter writer) throws IOException {
		writer.writeString(value);
	}
	
	@Override
	public boolean isString() {
		return true;
	}
	
	@Override
	public String asString() {
		return value;
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
	        return false;
		}
		if (getClass() != o.getClass()) {
	        return false;
		}
		JSONString other = (JSONString) o;
		return value.equals(other.value);
	}
	
	@Override
	public String toString() {
		return value;
	}
}