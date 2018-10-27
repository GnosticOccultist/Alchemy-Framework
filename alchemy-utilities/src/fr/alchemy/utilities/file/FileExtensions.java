package fr.alchemy.utilities.file;

/**
 * <code>FileExtensions</code> is an interface containing all the useful file format described by
 * their extension.
 * 
 * @author GnosticOccultist
 */
public interface FileExtensions {
	
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
