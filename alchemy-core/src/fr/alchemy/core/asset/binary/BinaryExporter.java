package fr.alchemy.core.asset.binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import fr.alchemy.core.asset.AssetManager;
import fr.alchemy.utilities.ByteUtils;
import fr.alchemy.utilities.file.FileExtensions;
import javafx.util.Pair;

/**
 * <code>BinaryExporter</code> is capable of exporting {@link Exportable} class instances to a file on the
 * disk in bytes format.
 * 
 * @author GnosticOccultist
 */
public final class BinaryExporter {
	
	/**
	 * The alias counter.
	 */
	private int aliasCount = 1;
	/**
	 * The ID counter.
	 */
	private int idCount = 1;
	/**
	 * The table containing the class name matching a binary class.
	 */
	private final Map<String, BinaryClassObject> classes = 
			new HashMap<String, BinaryClassObject>();
	/**
	 * The identity table containing the exportable object and an ordered
	 * pair of binary writer.
	 */
	private final IdentityHashMap<Exportable, Pair<Integer, BinaryWriter>> contentTable =
			new IdentityHashMap<Exportable, Pair<Integer, BinaryWriter>>();
	/**
	 * The list of content keys.
	 */
	private final List<Exportable> contentKeys = 
			new ArrayList<Exportable>();
	/**
	 * The table of locations.
	 */
	protected Map<Integer, Integer> locationTable = 
			new HashMap<Integer, Integer>();
	
    public static BinaryExporter getInstance() {
        return new BinaryExporter();
    }
	
	private BinaryExporter() {}
	
	/**
	 * Exports the provided {@link Exportable} using the {@link OutputStream}.
	 * This method should be called via {@link AssetManager#saveAsset(Exportable, String)} only.
	 * 
	 * @param exportable   The exportable class instance.
	 * @param os		   The output stream.
	 * @throws IOException Thrown if an exception occured when writing to the file.
	 */
	public void export(final Exportable exportable, OutputStream os) throws IOException {
		reset();
		
		// First write the file header and version format.
		os.write(FileExtensions.ALCHEMY_FILE_HEADER);
		os.write(ByteUtils.toBytes(FileExtensions.ALCHEMY_FILE_VERSION));
		
		int id = writeClassObject(exportable);
		
		// Write out tag table.
		int classTableSize = 0;
		int classNum = classes.keySet().size();
		// Fix a specific width for all aliases.
		int aliasSize = (int) ((Math.log(classNum) / Math.log(256)) + 1);
		
		os.write(ByteUtils.toBytes(classNum));
		for(String key : classes.keySet()) {
			BinaryClassObject bco = classes.get(key);

            // write alias
            byte[] aliasBytes = fixClassAlias(bco.alias,
                    aliasSize);
            os.write(aliasBytes);
            classTableSize += aliasSize;
            
            // jME3 NEW: Write class hierarchy version numbers
            os.write( bco.classHierarchyVersions.length );
            for (int version : bco.classHierarchyVersions){
                os.write(ByteUtils.toBytes(version));
            }
            classTableSize += 1 + bco.classHierarchyVersions.length * 4;
            
            // Write classname size & classname.
            byte[] classBytes = key.getBytes();
            os.write(ByteUtils.toBytes(classBytes.length));
            os.write(classBytes);
            classTableSize += 4 + classBytes.length;
            
            // For each field, write alias, type, and name...
            os.write(ByteUtils.toBytes(bco.nameFields.size()));
            for (String fieldName : bco.nameFields.keySet()) {
                BinaryClassField bcf = bco.nameFields.get(fieldName);
                os.write(bcf.alias);
                os.write(bcf.type);

                // Write classname size & classname.
                byte[] fNameBytes = fieldName.getBytes();
                os.write(ByteUtils.toBytes(fNameBytes.length));
                os.write(fNameBytes);
                classTableSize += 2 + 4 + fNameBytes.length;
            }
		}
		
		// Write out data to a seperate stream.
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int location = 0;
		// Keep track of the location for each piece.
		Map<String, List<Pair<Integer, BinaryWriter>>> alreadySaved = new HashMap<>();
		for (Exportable exp : contentKeys) {
            // look back at previous written data for matches
            String savableName = exp.getClass().getName();
            Pair<Integer, BinaryWriter> pair = contentTable.get(exp);
            List<Pair<Integer, BinaryWriter>> bucket = alreadySaved
                    .get(savableName + getChunk(pair));
            int prevLoc = findPrevMatch(pair, bucket);
            if (prevLoc != -1) {
                locationTable.put(pair.getKey(), prevLoc);
                continue;
            }

            locationTable.put(pair.getKey(), location);
            if (bucket == null) {
                bucket = new ArrayList<Pair<Integer, BinaryWriter>>();
                alreadySaved.put(savableName + getChunk(pair), bucket);
            }
            bucket.add(pair);
            byte[] aliasBytes = fixClassAlias(classes.get(savableName).alias, aliasSize);
            out.write(aliasBytes);
            location += aliasSize;
            BinaryWriter cap = contentTable.get(exp).getValue();
            out.write(ByteUtils.toBytes(cap.bytes.length));
            location += 4; // length of bytes
            out.write(cap.bytes);
            location += cap.bytes.length;
		}
		
        // write out location table
        // tag/location
        int numLocations = locationTable.keySet().size();
        os.write(ByteUtils.toBytes(numLocations));
        int locationTableSize = 0;
        for (Integer key : locationTable.keySet()) {
            os.write(ByteUtils.toBytes(key));
            os.write(ByteUtils.toBytes(locationTable.get(key)));
            locationTableSize += 8;
        }

        // Write out number of root ids - hardcoded 1 for now
        os.write(ByteUtils.toBytes(1));

        // Write out root id
        os.write(ByteUtils.toBytes(id));

        // Append stream to the output stream
        out.writeTo(os);
        
        out = null;
        os = null;
	}
	
	public int writeClassObject(Exportable object) throws IOException {
		if(object == null) {
			return -1;
		}
		
		Class<? extends Exportable> clazz = object.getClass();
		BinaryClassObject bco = classes.get(object.getClass().getName());
		// Is this class has been looked at before? in the tag-table.
		if(bco == null) {
			bco = new BinaryClassObject();
			bco.alias = generateTag();
			bco.nameFields = new HashMap<>();
			bco.classHierarchyVersions = new int[] {};
			
			// Store the newly created binary equivalent of the class.
			classes.put(clazz.getName(), bco);
		}
		
		// Is object in contentTable?
		if(contentTable.get(object) != null) {
			return (contentTable.get(object).getKey());
		}
		
		Pair<Integer, BinaryWriter> newPair = new Pair<>(idCount++, new BinaryWriter(this, bco));
		Pair<Integer, BinaryWriter> old = contentTable.put(object, newPair);
		if(old == null) {
			contentKeys.add(object);
		}
		
		object.export(this);
		newPair.getValue().finish();
		
		return newPair.getKey();
	}
	
    private byte[] fixClassAlias(byte[] bytes, int width) {
        if (bytes.length != width) {
            byte[] newAlias = new byte[width];
            for (int x = width - bytes.length; x < width; x++)
                newAlias[x] = bytes[x - bytes.length];
            return newAlias;
        }
        return bytes;
    }
    
    public BinaryWriter getCapsule(Exportable object) {
        return contentTable.get(object).getValue();
    }
    
    protected byte[] generateTag() {
        int width = (int) ((Math.log(aliasCount) / Math.log(256)) + 1);
        int count = aliasCount;
        aliasCount++;
        byte[] bytes = new byte[width];
        for (int x = width - 1; x >= 0; x--) {
            int pow = (int) Math.pow(256, x);
            int factor = count / pow;
            bytes[width - x - 1] = (byte) factor;
            count %= pow;
        }
        return bytes;
    }
    
    private String getChunk(Pair<Integer, BinaryWriter> pair) {
        return new String(pair.getValue().bytes, 0, Math.min(64, pair
                .getValue().bytes.length));
    }

    private int findPrevMatch(Pair<Integer, BinaryWriter> oldPair, List<Pair<Integer, BinaryWriter>> bucket) {
        if (bucket == null)
            return -1;
        for (int x = bucket.size(); --x >= 0;) {
        	Pair<Integer, BinaryWriter> pair = bucket.get(x);
            if (pair.getValue().equals(oldPair.getValue()))
                return locationTable.get(pair.getKey());
        }
        return -1;
    }
	
	public void reset() {
		// Reset the local vars.
		aliasCount = 1;
		idCount = 1;
		classes.clear();
		contentTable.clear();
		locationTable.clear();
		contentKeys.clear();
	}
}
