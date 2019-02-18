package fr.alchemy.utilities.file.json;

import fr.alchemy.utilities.Validator;

/**
 * <code>JSONLiteral</code> is an implementation of {@link JSONValue} for
 * literal value such as <code>null</code>, <code>true</code> or <code>false</code>.
 * 
 * @author GnosticOccultist
 */
class JSONLiteral extends JSONValue {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * The readed literal value.
	 */
	private final String value;
	
	JSONLiteral(String value) {
		Validator.nonNull(value);
		this.value = value;
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
		JSONLiteral other = (JSONLiteral) o;
		return value.equals(other.value);
	}
	
	@Override
	public String toString() {
		return value;
	}
}
