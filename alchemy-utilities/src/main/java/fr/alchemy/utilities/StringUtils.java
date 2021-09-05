package fr.alchemy.utilities;

/**
 * <code>StringUtils</code> provides utility functions to manipulate, extract or create {@link String}.
 * 
 * @version 0.2.0
 * @since 0.2.0
 * 
 * @author GnosticOccultist
 */
public class StringUtils {

	/**
	 * An empty string instance. Make sure this isn't modified.
	 */
	public static final String EMPTY = "";
	/**
	 * The EOL (End Of Line) character to specify the end of a line of text.
	 * Note that it's value isn't system dependant like {@link System#lineSeparator()}.
	 */
	public static final String EOL = "\n";
	
	/**
	 * Private constructor to inhibit instantiation of <code>StringUtils</code>.
	 */
	private StringUtils() {}
	
	/**
	 * Return whether the provided {@link String} contains at least one of the provided characters.
	 * 
	 * @param value		 The string value to check (not null).
	 * @param characters The characters to search for.
	 * @return			 Whether the string is composed of at least one of those characters.
	 */
	public static boolean contains(String value, char... characters) {
		Validator.nonNull(value, "The string can't be null!");
		
		if(value.isEmpty()) {
			return false;
		}
		
		for(char c : value.toCharArray()) {
			if(!contains(c, characters)) {
				continue;
			}
			
			return true;
		}
		return false;
	}
	
	/**
	 * Return whether the provided character is part of the provided characters.
	 * 
	 * @param character  The character to look for.
	 * @param characters The characters to check.
	 * @return			 Whether the character is contained in the provided ones.
	 */
	public static boolean contains(char character, char... characters) {
		for(char c : characters) {
			if(c == character) {
				return true;
			}
		}
		return false;
	}
}
