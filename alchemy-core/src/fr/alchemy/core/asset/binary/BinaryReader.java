package fr.alchemy.core.asset.binary;

import java.io.IOException;
import java.io.InputStream;

import fr.alchemy.core.asset.Texture;
import fr.alchemy.utilities.ByteUtils;

public final class BinaryReader {

	private final BinaryExporter exporter;
	
	public final byte[] bytes;
	
	public int numBytes;
	
	public BinaryReader(final BinaryExporter ex, final InputStream is, final byte[] bytes, int numBytes) {
		this.exporter = ex;
		this.bytes = bytes;
		this.numBytes = numBytes;
	}
	
	public boolean readBoolean(final String name) throws IOException {
		final int classLength = ByteUtils.convertIntFromBytes(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return false;
		}
		numBytes += classLength;
		final boolean value = ByteUtils.convertBooleanFromBytes(bytes, numBytes);
		numBytes += 1;
		return value;
	}
	
	public double readDouble(final String name) throws IOException {
		final int classLength = ByteUtils.convertIntFromBytes(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return 0;
		}
		
		numBytes += classLength;
		final long value = ByteUtils.convertLongFromBytes(bytes, numBytes);
		numBytes += 8;
		return value;
	}
	
	public int readInt(final String name) throws IOException {
		final int classLength = ByteUtils.convertIntFromBytes(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return 0;
		}
		
		numBytes += classLength;
		final int value = ByteUtils.convertIntFromBytes(bytes, numBytes);
		numBytes += 4;
		return value;
	}
	
	public String readString(final String name) throws IOException {
		final int classLength = ByteUtils.convertIntFromBytes(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return "";
		}
		
		numBytes += classLength;
		final int stringLength = ByteUtils.convertIntFromBytes(bytes, numBytes);
		numBytes += 4;
		final String string = ByteUtils.readString(bytes, stringLength, numBytes);
		numBytes += stringLength;
		return string;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Exportable> T readExportable(final Class<T> type) throws IOException {
		final int classLength = ByteUtils.convertIntFromBytes(bytes, numBytes);
		numBytes += 4;
		final String className = ByteUtils.readString(bytes, classLength, numBytes);
		if(!type.getName().equals(className)) {
			System.err.println(type.getName() + " doesn't match " + className + " . Please check if the class exists!");
			return null;
		}
		numBytes += classLength;
		final Exportable value = exporter.insertObject(className, this);
		return (T) value;
	}
	
	public Texture readTexture(final String name) throws IOException {
		final int classLength = ByteUtils.convertIntFromBytes(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return null;
		}
		
		numBytes += classLength;
		final int stringLength = ByteUtils.convertIntFromBytes(bytes, numBytes);
		numBytes += 4;
		final String string = ByteUtils.readString(bytes, stringLength, numBytes);
		numBytes += stringLength;
		return exporter.assetManager.loadTexture(string);
	}	
	
	public Texture[] readTextures(final String name) throws IOException {
		final int size = ByteUtils.convertIntFromBytes(bytes, numBytes);
		numBytes += 4;
		Texture[] textures = new Texture[size];
		for(int i = 0; i < size; i++) {
			textures[i] = readTexture(name + "_" + i); 
		}
		return textures;
	}	
}
