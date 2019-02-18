package fr.alchemy.utilities.file.json;

import fr.alchemy.utilities.Validator;

/**
 * <code>JSONNumber</code> is an implementation of {@link JSONValue} for
 * numbers.
 * 
 * @author GnosticOccultist
 */
class JSONNumber extends JSONValue {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * The readed number value.
	 */
	private final String value;
	
	JSONNumber(String value) {
		Validator.nonNull(value);
		this.value = value;
	}
	
	@Override
	public boolean isNumber() {
		return true;
	}
	
	@Override
	public int asInt() {
		return Integer.parseInt(value, 10);
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
		JSONNumber other = (JSONNumber) o;
		return value.equals(other.value);
	}
	
	@Override
	public String toString() {
		return value;
	}
}
