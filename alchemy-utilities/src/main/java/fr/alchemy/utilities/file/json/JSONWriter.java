package fr.alchemy.utilities.file.json;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

class JSONWriter {

	private static final int CONTROL_CHARACTERS_END = 0x001f;

	private static final char[] QUOT_CHARS = { '\\', '"' };
	private static final char[] BS_CHARS = { '\\', '\\' };
	private static final char[] LF_CHARS = { '\\', 'n' };
	private static final char[] CR_CHARS = { '\\', 'r' };
	private static final char[] TAB_CHARS = { '\\', 't' };
	// In JavaScript, U+2028 and U+2029 characters count as line endings and must be
	// encoded.
	private static final char[] UNICODE_2028_CHARS = { '\\', 'u', '2', '0', '2', '8' };
	private static final char[] UNICODE_2029_CHARS = { '\\', 'u', '2', '0', '2', '9' };
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	protected final Writer writer;

	JSONWriter(Writer writer) {
		this.writer = writer;
	}

	protected void writeLiteral(String value) throws IOException {
		writer.write(value);
	}

	protected void writeNumber(String value) throws IOException {
		writer.write(value);
	}

	protected void writeString(String value) throws IOException {
		writer.write('"');
		writeJSONString(value);
		writer.write('"');
	}

	protected void writeArrayOpen() throws IOException {
		writer.write('[');
	}

	protected void writeArrayClose() throws IOException {
		writer.write(']');
	}

	protected void writeArraySeparator() throws IOException {
		writer.write(',');
	}

	protected void writeObjectOpen() throws IOException {
		writer.write('{');
	}

	protected void writeObjectClose() throws IOException {
		writer.write('}');
	}

	protected void writeMemberName(String name) throws IOException {
		writer.write('"');
		writeJSONString(name);
		writer.write('"');
	}

	protected void writeMemberSeparator() throws IOException {
		writer.write(':');
	}

	protected void writeObjectSeparator() throws IOException {
		writer.write(',');
	}

	protected void writeJSONString(String value) throws IOException {
		int length = value.length();
		int start = 0;
		for (int index = 0; index < length; ++index) {
			char[] replacement = getReplacementChars(value.charAt(index));
			if (replacement != null) {
				writer.write(value, start, index - start);
				writer.write(replacement);
				start = index + 1;
			}
		}
		writer.write(value, start, length - start);
	}

	private static char[] getReplacementChars(char ch) {
		if (ch > '\\') {
			if (ch < '\u2028' || ch > '\u2029') {
				// The lower range contains 'a' .. 'z'. Only 2 checks required.
				return null;
			}
			return ch == '\u2028' ? UNICODE_2028_CHARS : UNICODE_2029_CHARS;
		}
		if (ch == '\\') {
			return BS_CHARS;
		}
		if (ch > '"') {
			// This range contains '0' .. '9' and 'A' .. 'Z'. Need 3 checks to get here.
			return null;
		}
		if (ch == '"') {
			return QUOT_CHARS;
		}
		if (ch > CONTROL_CHARACTERS_END) {
			return null;
		}
		if (ch == '\n') {
			return LF_CHARS;
		}
		if (ch == '\r') {
			return CR_CHARS;
		}
		if (ch == '\t') {
			return TAB_CHARS;
		}
		return new char[] { '\\', 'u', '0', '0', HEX_DIGITS[ch >> 4 & 0x000f], HEX_DIGITS[ch & 0x000f] };
	}
	
	static class PrettyJSONWriter extends JSONWriter {

		private final char[] indentChars;
	    private int indent;
		
		PrettyJSONWriter(Writer writer) {
			super(writer);
			this.indentChars = new char[2];
			Arrays.fill(indentChars, ' ');
		}
		
		@Override
		protected void writeArrayOpen() throws IOException {
			indent++;
			writer.write('[');
			writeNewLine();
		}
		
		@Override
		protected void writeArrayClose() throws IOException {
			indent--;
			writeNewLine();
			writer.write(']');
		}
		
		@Override
		protected void writeArraySeparator() throws IOException {
			writer.write(',');
			if(!writeNewLine()) {
				writer.write(' ');
			}
		}
		
		@Override
		protected void writeObjectOpen() throws IOException {
			indent++;
			writer.write('{');
			writeNewLine();
		}
		
		@Override
		protected void writeObjectClose() throws IOException {
			indent--;
			writeNewLine();
			writer.write('}');
		}
		
		@Override
		protected void writeMemberSeparator() throws IOException {
			writer.write(':');
			writer.write(' ');
		}
		
		@Override
		protected void writeObjectSeparator() throws IOException {
			writer.write(',');
			if(!writeNewLine()) {
				writer.write(' ');
			}
		}
		
		private boolean writeNewLine() throws IOException {
			if(indentChars == null) {
				return false;
			}
			
			writer.write('\n');
			for (int i = 0; i < indent; i++) {
				writer.write(indentChars);
			}
			return true;
		}
	}
}
