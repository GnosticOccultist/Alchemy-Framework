package fr.alchemy.utilities.file.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import fr.alchemy.utilities.Validator;

public final class JSONParser {
	
	private static final int MAX_NESTING_LEVEL = 1000;

	
	public JSONValue value;
	
	/**
	 * The default buffer size, by default 1024.
	 */
	private static final int DEFAULT_BUFFER_SIZE = 1024;
	
	/**
	 * The reader to parse the JSON file.
	 */
	private final Reader reader;
	/**
	 * The internal buffer of the parser.
	 */
	private char[] buffer;
	/**
	 * The buffer offset.
	 */
	private int bufferOffset;
	/**
	 * The reader index.
	 */
	private int index;
	/**
	 * The
	 */
	private int fill;
	/**
	 * The line.
	 */
	private int line;
	/**
	 * The line offset.
	 */
	private int lineOffset;
	/**
	 * The current.
	 */
	private int current;
	/**
	 * The capture buffer.
	 */
	private StringBuilder captureBuffer;
	/**
	 * The capture start index.
	 */
	private int captureStart;

	private int nestingLevel;
	
	/**
	 * Instantiates a new <code>JSONParser</code> with the provided
	 * {@link Reader} to parse the file.
	 * 
	 * @param reader The rader to parse the JSON file.
	 * @return		 The JSON parser instance.
	 */
	protected static JSONParser newParser(Reader reader) {
		return new JSONParser(reader);
	}
	
	/**
	 * Private constructor to inhibit instantiation of <code>JSONParser</code>.
	 * Use {@link AlchemyJSON#parse(Reader)} instead to load from a {@link Reader}.
	 * 
	 * @param reader The reader to parse the JSON file.
	 */
	private JSONParser(Reader reader) {
		Validator.nonNull(reader);
		this.reader = reader;
	}
	
	/**
	 * Reads the entire input from the given {@link Reader} and parses it as JSON. The input must contain
	 * a valid JSON value, optionally padded with whitespace.
	 * <p>
	 * Characters are read in chunks into an input buffer of {@link #DEFAULT_BUFFER_SIZE}. Hence, wrapping a reader
	 * in an additional {@link BufferedReader} likely won't inmprove reading performance.
	 * </p>
	 * 
	 * @throws IOException
	 */
	public void parse() throws IOException {
		parse(DEFAULT_BUFFER_SIZE);
	}

	/**
	 * Reads the entire input from the given {@link Reader} and parses it as JSON. The input must contain
	 * a valid JSON value, optionally padded with whitespace.
	 * <p>
	 * Characters are read in chunks into an input buffer of the provided size. Hence, wrapping a reader
	 * in an additional {@link BufferedReader} likely won't inmprove reading performance.
	 * </p>
	 * 
	 * @param bufferSize The size of the input buffer in characters.
	 * @throws IOException Thrown if an I/O error occurs in the reader.
	 */
	public void parse(int bufferSize) throws IOException {
		Validator.nonNegative(bufferSize);
		
		if(buffer == null) {
			this.buffer = new char[bufferSize];
		}
		reset();
		read();
		trim();
		readValue();
		trim();
		if(!end()) {
			int offset = bufferOffset + index - 1;
			int column = offset - lineOffset + 1;
			throw new IOException(offset + " " + column + " ");
		}
	}
	
	private void read() throws IOException {
		if(index == fill) {
			if(captureStart != -1) {
				captureBuffer.append(buffer, captureStart, fill - captureStart);
				captureStart = 0;
			}
			bufferOffset += fill;
			fill = reader.read(buffer, 0, buffer.length);
			index = 0;
			if(fill == -1) {
				current = -1;
				index++;
				return;
			}
		}
		if(current == '\n') {
			line++;
			lineOffset = bufferOffset + index;
		}
		current = buffer[index++];
	}
	
	private void readValue() throws IOException {
		switch (current) {
			case 'n':
				readNull();
				break;
			case '"':
				readString();
				break;
			case '[':
				readArray();
				break;
			case '{':
				readObject();
				break;
			case '-':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				readNumber();
		        break;
			default:
			    int offset = bufferOffset + index - 1;
			    int column = offset - lineOffset + 1;
				throw new UnsupportedOperationException(Character.getName(current) + " " + line + " " + column);
		}
	}
	
	  private void readArray() throws IOException {
		    JSONArray array = new JSONArray();
		    read();
		    if (++nestingLevel > MAX_NESTING_LEVEL) {
		      throw new IOException("Nesting too deep");
		    }
		    trim();
		    if (readChar(']')) {
		      nestingLevel--;
		      value = array;
		      return;
		    }
		    do {
		      trim();
		      readValue();
		      array.add(value);
		      trim();
		    } while (readChar(','));
		    if (!readChar(']')) {
		    	throw new IOException("',' or ']'");
		    }
		    nestingLevel--;
		    value = array;
		  }

		  private void readObject() throws IOException {
		    JSONObject object = new JSONObject();
		    read();
		    if (++nestingLevel > MAX_NESTING_LEVEL) {
		      throw new IOException("Nesting too deep");
		    }
		    trim();
		    if (readChar('}')) {
		      nestingLevel--;
		      this.value = object;
		      return;
		    }
		    do {
		      trim();
		      String name = readName();
		      trim();
		      if (!readChar(':')) {
		        throw new IOException("':'");
		      }
		      trim();
		      readValue();
		      object.add(name, value);
		      trim();
		    } while (readChar(','));
		    if (!readChar('}')) {
		      throw new IOException("',' or '}'");
		    }
		    nestingLevel--;
		   this.value = object;
		  }
		  
	
	private void readNull() throws IOException {
		read();
		readRequiredChar('u');
		readRequiredChar('l');
		readRequiredChar('l');
		value = AlchemyJSON.NULL;
	}
	
	private boolean readChar(char ch) throws IOException {
		if (current != ch) {
			return false;
		}
		read();
		return true;
	}
	
	private boolean readDigit() throws IOException {
		if (!digit()) {
			return false;
		}
		read();
		return true;
	}
	
	private String readName() throws IOException {
		if (current != '"') {
			int offset = bufferOffset + index - 1;
		    int column = offset - lineOffset + 1;
			throw new IOException("name " + offset + " " + column);
		}
		return readStringInternal();	
	}
	
	private void readString() throws IOException {
		value = new JSONString(readStringInternal());
	}
	
	private String readStringInternal() throws IOException {
		read();
		startCapture();
		while (current != '"') {
			if (current == '\\') {
				pauseCapture();
		        readEscape();
		        startCapture();
			} else if (current < 0x20) {
				throw new IOException("valid string character");
			} else {
				read();
			}
		}
		String string = endCapture();
		read();
		return string;
	}

	private void readEscape() throws IOException {
		read();
		switch (current) {
			case '"':
			case '/':
			case '\\':
				captureBuffer.append((char)current);
				break;
			case 'b':
		        captureBuffer.append('\b');
		        break;
			case 'f':
		        captureBuffer.append('\f');
		        break;
			case 'n':
		        captureBuffer.append('\n');
		        break;
			case 'r':
		        captureBuffer.append('\r');
		        break;
			case 't':
		        captureBuffer.append('\t');
		        break;
			case 'u':
				char[] hexChars = new char[4];
				for (int i = 0; i < 4; i++) {
		          read();
		          if (!hexdigit()) {
		        	  throw new IOException("hexadecimal digit");
		          }
		          hexChars[i] = (char)current;
		        }
				captureBuffer.append((char)Integer.parseInt(new String(hexChars), 16));
		        break;
		      default:
		        throw new IOException("valid escape sequence");
		    }
		    read();
		  }
	
	private void readNumber() throws IOException {
		startCapture();
		readChar('-');
		int firstDigit = current;
		if (!readDigit()) {
			throw new IOException("digit");
		}
		if (firstDigit != '0') {
			while (readDigit()) {
			}
		}
		value = new JSONNumber(endCapture());
	}
	
	private void startCapture() {
		if (captureBuffer == null) {
	      captureBuffer = new StringBuilder();
		}
		captureStart = index - 1;
	}

	private void pauseCapture() {
		int end = current == -1 ? index : index - 1;
		captureBuffer.append(buffer, captureStart, end - captureStart);
	    captureStart = -1;
	}

	private String endCapture() {
		int start = captureStart;
		int end = index - 1;
		captureStart = -1;
		if (captureBuffer.length() > 0) {
	      captureBuffer.append(buffer, start, end - start);
	      String captured = captureBuffer.toString();
	      captureBuffer.setLength(0);
	      return captured;
	    }
	    return new String(buffer, start, end - start);
	}
	
	private void readRequiredChar(char ch) throws IOException {
		if (!readChar(ch)) {
			throw new IOException("'" + ch + "'");
		}	
	}
	
	private boolean whitespace() {
		return current == ' ' || current == '\t' || current == '\n' || current == '\r';
	}	
	
	private boolean digit() {	
		return current >= '0' && current <= '9';
	}
	
	private boolean hexdigit() {
		return current >= '0' && current <= '9'
	        || current >= 'a' && current <= 'f'
	        || current >= 'A' && current <= 'F';
	}
	
	private boolean end() {
		return current == -1;
	}
	
	/**
	 * 
	 * 
	 * @throws IOException
	 */
	private void trim() throws IOException {
		while(whitespace()) {
			read();
		}
	}
	
	/**
	 * Reset the <code>JSONParser</code> and all its field. Note that the buffer
	 * is leaved untouched until {@link #read()} is called.
	 */
	private void reset() {
	    bufferOffset = 0;
	    index = 0;
	    fill = 0;
	    line = 1;
	    lineOffset = 0;
	    current = 0;
	    captureStart = -1;
	}
}
