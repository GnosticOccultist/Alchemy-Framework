package fr.alchemy.utilities.file;

/**
 * <code>FileExtensions</code> is an interface containing all the useful file format described by
 * their extension.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public interface FileExtensions {
	
	///////////////////////////////////////////////////////////
	////////////		 ALCHEMY FILES		    //////////////
	//////////////////////////////////////////////////////////
	
	/**
	 * The current version number for alchemy files format.
	 */
	int ALCHEMY_FILE_VERSION = 1;
	/**
	 * The alchemy binary file's header. Corresponding to "Alchemy Binary" as ASCII.
	 */
	byte[] ALCHEMY_FILE_HEADER = new byte[] {0x41, 0x6c, 0x63, 0x68, 0x65, 0x6d, 0x79, 0x20, 0x42, 0x69, 0x6e, 0x61, 0x72, 0x79};
	
	///////////////////////////////////////////////////////////
	////////////	 DATA FORMAT EXTENSION		//////////////
	//////////////////////////////////////////////////////////
	
	/**
	 * The 'JavaScript Object Notation' JSON, derived from the notation of objects in JavaScript.
	 */
	String JSON_FORMAT = "json";
	/**
	 * The 'Extensible Markup Language' XML format with strong support via Unicode, easily
	 * showing a hierarchy of data structures.
	 */
	String XML_FORMAT = "xml";
	/**
	 * The Comma-seperated values format (CSV), values are seperated by ',' or ';'
	 * (columns) and lines (rows).
	 */
	String CSV_FORMAT = "csv";
	/**
	 * The Tab-seperated values format (TSV), values are seperated by 'tabs'
	 * (columns) and lines (rows).
	 */
	String TSV_FORMAT = "csv";
	/**
	 * The 'Ain't Markup Language' YAML format is a minimal syntax language.
	 */
	String[] YAML_FORMAT = new String[] { "yaml", "yml" };
	
	///////////////////////////////////////////////////////////
	////////////		TEXTURE EXTENSION		//////////////
	//////////////////////////////////////////////////////////
	
	/**
	 * The PNG texture format.
	 */
	String PNG_FORMAT = "png";
	/**
	 * The JPG texture format.
	 */
	String JPG_FORMAT = "jpg";
	/**
	 * The JPEG texture format.
	 */
	String JPEG_FORMAT = "jpeg";
	/**
	 * The TGA texture format.
	 */
	String TGA_FORMAT = "tga";
	/**
	 * The HDR texture format.
	 */
	String HDR_FORMAT = "hdr";
	/**
	 * The BMP texture format.
	 */
	String BMP_FORMAT = "bmp";
	
	/**
	 * The array containing all the texture related file extensions.
	 */
	String[] TEXTURE_FILE_EXTENSION = new String[] {PNG_FORMAT, JPG_FORMAT, JPEG_FORMAT, 
			TGA_FORMAT, HDR_FORMAT, BMP_FORMAT};
	
	///////////////////////////////////////////////////////////
	////////////		MODEL EXTENSION			//////////////
	//////////////////////////////////////////////////////////
	
	/**
	 * The OBJ model format.
	 */
	String OBJ_MODEL_FORMAT = "obj";
	/**
	 * The FBX model format.
	 */
	String FBX_MODEL_FORMAT = "fbx";
	/**
	 * The blender model format.
	 */
	String BLENDER_MODEL_FORMAT = "blend";
	/**
	 * The maya model format encoded in ASCII.
	 */
	String MAYA_MODEL_FORMAT_ASCII = "ma";
	/**
	 * The maya model format encoded in binary.
	 */
	String MAYA_MODEL_FORMAT_BINARY = "mb";
	
	///////////////////////////////////////////////////////////
	////////////		SHADER EXTENSION		//////////////
	//////////////////////////////////////////////////////////
	
	/**
	 * The generic format used for shader files.
	 */
	String GENERIC_GLSL_FORMAT = "glsl";
	/**
	 * The vertex shader file's extension.
	 */
	String VERTEX_SHADER = "vert";
	/**
	 * The fragment shader file's extension.
	 */
	String FRAGMENT_SHADER = "frag";
	/**
	 * The geometry shader file's extension.
	 */
	String GEOMETRY_SHADER = "geom";
	/**
	 * The tesselation control shader file's extension.
	 */
	String TESS_CONTROL_SHADER = "tessctrl";
	/**
	 * The tesselation evaluation shader file's extension.
	 */
	String TESS_EVALUATION_SHADER = "tesseval";
	/**
	 * The compute shader file's extension.
	 */
	String COMPUTE_SHADER = "comp";
	
	/**
	 * The array containing all the shader related file extensions.
	 */
	String[] SHADER_FILE_EXTENSIONS = new String[] {VERTEX_SHADER, FRAGMENT_SHADER, GEOMETRY_SHADER, 
			TESS_CONTROL_SHADER, TESS_EVALUATION_SHADER, COMPUTE_SHADER, GENERIC_GLSL_FORMAT};
}
