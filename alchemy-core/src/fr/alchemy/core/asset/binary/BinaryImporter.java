package fr.alchemy.core.asset.binary;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import fr.alchemy.core.asset.AssetManager;
import fr.alchemy.utilities.ByteUtils;
import fr.alchemy.utilities.Instantiator;
import fr.alchemy.utilities.file.FileExtensions;
import fr.alchemy.utilities.logging.FactoryLogger;
import fr.alchemy.utilities.logging.Logger;

public class BinaryImporter {
	
    private AssetManager assetManager;

    //Key - alias, object - bco
    private Map<String, BinaryClassObject> classes
             = new HashMap<String, BinaryClassObject>();
    //Key - id, object - the savable
    private Map<Integer, Exportable> contentTable
            = new HashMap<Integer, Exportable>();
    //Key - savable, object - capsule
    private Map<Exportable, BinaryReader> capsuleTable
             = new IdentityHashMap<Exportable, BinaryReader>();

    //Key - id, opject - location in the file
    private Map<Integer, Integer> locationTable
             = new HashMap<Integer, Integer>();

    public static boolean debug = false;

    private byte[] dataArray;
    private int aliasWidth;
    private int version;

	private final Logger logger = FactoryLogger.getLogger("alchemy.binary");
	
	/**
	 * Inserts an {@link Exportable} using the {@link OutputStream}.
	 * This method should be called via {@link AssetManager#loadAsset(Exportable, String)} only.
	 * 
	 * @param exportable   The exportable class instance.
	 * @param os		   The output stream.
	 * @throws IOException Thrown if an exception occured when writing to the file.
	 */
	public Exportable load(final InputStream is, final Path path, ByteArrayOutputStream baos) throws IOException {
		contentTable.clear();
		BufferedInputStream bis = new BufferedInputStream(is);
		
		// Read header and version
		final byte[] header = ByteUtils.readBytes(bis, FileExtensions.ALCHEMY_FILE_HEADER.length);
		if(!Arrays.equals(FileExtensions.ALCHEMY_FILE_HEADER, header)) {
			throw new IOException("Wrong file header: " + new String(header) + ". Can't continue!");
		}
		
		version = ByteUtils.readInteger(bis);
		if(version > FileExtensions.ALCHEMY_FILE_VERSION) {
			throw new IOException("The binary file '" + path.getFileName() + "' has a newer file version (v." + version  
					+ ") than expected (v." + FileExtensions.ALCHEMY_FILE_VERSION + ")! Are you coming from the future ?");
		}
		
		int numClasses = ByteUtils.readInteger(bis);
		int bytes = 4;
		aliasWidth = (int) ((Math.log(numClasses) / Math.log(256)) + 1);
		
		classes.clear();
		for(int i = 0; i < numClasses; i++) {
			String alias = readString(bis, aliasWidth);
			
			int[] classHierarchyVersions;
            int classHierarchySize = bis.read();
            classHierarchyVersions = new int[classHierarchySize];
            for (int j = 0; j < classHierarchySize; j++){
                classHierarchyVersions[j] = ByteUtils.readInteger(bis);
            }
           
            // read classname and classname size
            int classLength = ByteUtils.readInteger(bis);
            String className = readString(bis, classLength);
            
            BinaryClassObject bco = new BinaryClassObject();
            bco.alias = alias.getBytes();
            bco.className = className;
            bco.classHierarchyVersions = classHierarchyVersions;
            
            int fields = ByteUtils.readInteger(bis);
            bytes += (8 + aliasWidth + classLength);

            bco.nameFields = new HashMap<String, BinaryClassField>(fields);
            bco.aliasFields = new HashMap<Byte, BinaryClassField>(fields);
            for (int x = 0; x < fields; x++) {
                byte fieldAlias = (byte)bis.read();
                byte fieldType = (byte)bis.read();

                int fieldNameLength = ByteUtils.readInteger(bis);
                String fieldName = readString(bis, fieldNameLength);
                BinaryClassField bcf = new BinaryClassField(fieldName, fieldAlias, fieldType);
                bco.nameFields.put(fieldName, bcf);
                bco.aliasFields.put(fieldAlias, bcf);
                bytes += (6 + fieldNameLength);
            }
            classes.put(alias, bco);
		}
		
        int numLocs = ByteUtils.readInteger(bis);
        bytes = 4;

        capsuleTable.clear();
        locationTable.clear();
        for(int i = 0; i < numLocs; i++) {
            int id = ByteUtils.readInteger(bis);
            int loc = ByteUtils.readInteger(bis);
            locationTable.put(id, loc);
            bytes += 8;
        }
        
        int numbIDs = ByteUtils.readInteger(bis); // XXX: NOT CURRENTLY USED
        int id = ByteUtils.readInteger(bis);
        bytes += 8;

        if(baos == null) {
        	baos = new ByteArrayOutputStream(bytes);
        } else {
        	baos.reset();
        }
        int size = -1;
        byte[] cache = new byte[4096];
        while((size = bis.read(cache)) != -1) {
            baos.write(cache, 0, size);
        }
        bis = null;

        dataArray = baos.toByteArray();
        baos = null;

        Exportable rVal = readObject(id);
		
        dataArray = null;
        return rVal;
	}
	
    public Exportable readObject(int id) {

        if(contentTable.get(id) != null) {
            return contentTable.get(id);
        }

        try {
            int loc = locationTable.get(id);

            String alias = readString(aliasWidth, loc);
            loc+=aliasWidth;

            BinaryClassObject bco = classes.get(alias);

            if(bco == null) {
                logger.error("readObject(int id) NULL class object: " + alias);
                return null;
            }

            int dataLength = ByteUtils.readInteger(dataArray, loc);
            loc+=4;

            Exportable out = null;
//            if (assetManager != null) {
//                out = SavableClassUtil.fromName(bco.className, assetManager.getClassLoaders());
//            } else {
                out = Instantiator.fromName(bco.className);
//            }

            BinaryReader cap = new BinaryReader(this, out, bco);
            cap.setContent(dataArray, loc, loc+dataLength);

            capsuleTable.put(out, cap);
            contentTable.put(id, out);

            out.insert(this);

            capsuleTable.remove(out);

            return out;

        } catch (IOException e) {
            logger.error("readObject(int id)", e);
            return null;
        }
    }
    
    protected String readString(int length, int offset) throws IOException {
        byte[] data = new byte[length];
        for(int j = 0; j < length; j++) {
            data[j] = dataArray[j+offset];
        }

        return new String(data);
    }

    public BinaryReader getCapsule(Exportable id) {
        return capsuleTable.get(id);
    }
	
    protected String readString(InputStream f, int length) throws IOException {
        byte[] data = new byte[length];
        for(int j = 0; j < length; j++) {
            data[j] = (byte)f.read();
        }

        return new String(data);
    }
}
