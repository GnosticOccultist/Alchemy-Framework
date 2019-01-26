package fr.alchemy.core.asset.binary;

import java.util.Map;

class BinaryClassObject {
	
	/**
	 * The table matching the fields with their name, use during export.
	 */
	Map<String, BinaryClassField> nameFields;
	/**
	 * The table matching the fields with their alias, use during importing.
	 */
	Map<Byte, BinaryClassField> aliasFields;
	
	byte[] alias;
	
	String className;
	
	int[] classHierarchyVersions;
}
