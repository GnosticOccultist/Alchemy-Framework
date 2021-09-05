package fr.alchemy.utilities.file.json;

import java.io.IOException;

import fr.alchemy.utilities.Validator;

/**
 * <code>JSONLiteral</code> is an implementation of {@link JSONValue} for literal value such as 
 * <code>null</code>, <code>true</code> or <code>false</code>.
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
		Validator.nonEmpty(value, "The literal value can't be null or empty!");
		this.value = value;
	}
	
	@Override
	void write(JSONWriter writer) throws IOException {
		writer.writeLiteral(value);
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		
		if(o == null || getClass() != o.getClass()) {
	        return false;
		}
		
		JSONLiteral other = (JSONLiteral) o;
		return value.equals(other.value);
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	static class JSONTrueLiteral extends JSONLiteral {
		
		private static final long serialVersionUID = 1L;

		JSONTrueLiteral() {
			super("true");
		}
		
		@Override
		public boolean isTrue() {
			return true;
		}
		
		@Override
		public boolean asBoolean() {
			return true;
		}
	}
	
	static class JSONFalseLiteral extends JSONLiteral {
		
		private static final long serialVersionUID = 1L;

		JSONFalseLiteral() {
			super("false");
		}
		
		@Override
		public boolean isFalse() {
			return true;
		}
		
		@Override
		public boolean asBoolean() {
			return false;
		}
	}
	
	static class JSONNullLiteral extends JSONLiteral {
		
		private static final long serialVersionUID = 1L;

		JSONNullLiteral() {
			super("null");
		}
		
		@Override
		public boolean isNull() {
			return true;
		}
	}
}
