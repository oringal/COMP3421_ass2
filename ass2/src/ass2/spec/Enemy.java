package ass2.spec;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

/**
 * 
 * @author Antheny and Gladys
 * used the C++ code from the link below and
 * week 7 lecture code as a starting point/for understanding
 * http://www.songho.ca/opengl/gl_vbo.html
 */

public class Enemy {

	// cube ///////////////////////////////////////////////////////////////////////
	//  v6----- v5
	// /|      /|
	//v1------v0|
	//| |     | |
	//| |v7---|-|v4
	//|/      |/
	//v2------v3
	// vertex coords array for glDrawArrays() =====================================
	// A cube has 6 sides and each side has 2 triangles, therefore, a cube consists
	// of 36 vertices (6 sides * 2 tris * 3 vertices = 36 vertices). And, each
	// vertex is 3 components (x,y,z) of floats, therefore, the size of vertex
	// array is 108 floats (36 * 3 = 108).

	private float[] vertices = {
		1, 1, 1,  -1, 1, 1,  -1,-1, 1,      // v0-v1-v2 (front)
		-1,-1, 1,   1,-1, 1,   1, 1, 1,      // v2-v3-v0

		1, 1, 1,   1,-1, 1,   1,-1,-1,      // v0-v3-v4 (right)
		1,-1,-1,   1, 1,-1,   1, 1, 1,      // v4-v5-v0

		1, 1, 1,   1, 1,-1,  -1, 1,-1,       // v0-v5-v6 (top)
		-1, 1,-1,  -1, 1, 1,   1, 1, 1,      // v6-v1-v0

		-1, 1, 1,  -1, 1,-1,  -1,-1,-1,      // v1-v6-v7 (left)
		-1,-1,-1,  -1,-1, 1,  -1, 1, 1,      // v7-v2-v1

		-1,-1,-1,   1,-1,-1,   1,-1, 1,      // v7-v4-v3 (bottom)
		1,-1, 1,  -1,-1, 1,  -1,-1,-1,       // v3-v2-v7

		1,-1,-1,  -1,-1,-1,  -1, 1,-1,      // v4-v7-v6 (back)
		-1, 1,-1,   1, 1,-1,   1,-1,-1 	   // v6-v5-v4
	};

	private float[] normals = {
		0, 0, 1,   0, 0, 1,   0, 0, 1,      // v0-v1-v2 (front)
		0, 0, 1,   0, 0, 1,   0, 0, 1,      // v2-v3-v0

		1, 0, 0,   1, 0, 0,   1, 0, 0,      // v0-v3-v4 (right)
		1, 0, 0,   1, 0, 0,   1, 0, 0,      // v4-v5-v0

		0, 1, 0,   0, 1, 0,   0, 1, 0,      // v0-v5-v6 (top)
		0, 1, 0,   0, 1, 0,   0, 1, 0,      // v6-v1-v0

		-1, 0, 0,  -1, 0, 0,  -1, 0, 0,     // v1-v6-v7 (left)
		-1, 0, 0,  -1, 0, 0,  -1, 0, 0,     // v7-v2-v1

		0,-1, 0,   0,-1, 0,   0,-1, 0,      // v7-v4-v3 (bottom)
		0,-1, 0,   0,-1, 0,   0,-1, 0,      // v3-v2-v7

		0, 0,-1,   0, 0,-1,   0, 0,-1,      // v4-v7-v6 (back)
		0, 0,-1,   0, 0,-1,   0, 0,-1	    // v6-v5-v4
	};

	private float[] colours = {
		1, 1, 1,   1, 1, 0,   1, 0, 0,      // v0-v1-v2 (front)
		1, 0, 0,   1, 0, 1,   1, 1, 1,      // v2-v3-v0

		1, 1, 1,   1, 0, 1,   0, 0, 1,      // v0-v3-v4 (right)
		0, 0, 1,   0, 1, 1,   1, 1, 1,      // v4-v5-v0

		1, 1, 1,   0, 1, 1,   0, 1, 0,      // v0-v5-v6 (top)
		0, 1, 0,   1, 1, 0,   1, 1, 1,      // v6-v1-v0

		1, 1, 0,   0, 1, 0,   0, 0, 0,      // v1-v6-v7 (left)
		0, 0, 0,   1, 0, 0,   1, 1, 0,      // v7-v2-v1

		0, 0, 0,   0, 0, 1,   1, 0, 1,      // v7-v4-v3 (bottom)
		1, 0, 1,   1, 0, 0,   0, 0, 0,      // v3-v2-v7

		0, 0, 1,   0, 0, 0,   0, 1, 0,      // v4-v7-v6 (back)
		0, 1, 0,   0, 1, 1,   0, 0, 1       // v6-v5-v4
	};
	
	private Texture tex;
	private Avatar avatar;
	private Terrain terrain;
	private static double[] position = new double[2];
	
	private int bufferIds[] = new int[4];
	//These are not vertex buffer objects, they are just java containers
    FloatBuffer verticesBuffer = Buffers.newDirectFloatBuffer(vertices);
    FloatBuffer coloursBuffer = Buffers.newDirectFloatBuffer(colours);
    FloatBuffer normalsBuffer = Buffers.newDirectFloatBuffer(normals);

    public Enemy (Avatar avatar, Terrain terrain) {
	    	this.avatar = avatar;
	    	this.terrain = terrain;
	    	position[0] = terrain.size().getWidth() / 2; 
	    	position[1] = terrain.size().getHeight() /2;
    }
    
	private void defineTextures(GL2 gl) {
		String fileName = "textures/evilMinionIcon.png";
		String fileExt = "png";

		tex = new Texture(gl, fileName, fileExt, true);
	}
	
	public void setup (GL2 gl) {
		// set texture environment parameters
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE); 
		
		// Generate 1 VBO buffer and get its ID
        gl.glGenBuffers(1, bufferIds, 0);
        
        // This buffer is now the current array buffer
        // array buffers hold vertex attribute data
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferIds[0]);

        //This is just setting aside enough empty space for all our data
        //we pass in null for the actual data for now
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, 
        		(vertices.length*Float.BYTES) + (normals.length*Float.BYTES) + (colours.length*Float.BYTES), 
        		null, 
        		GL2.GL_STATIC_DRAW);
        
        //Now load vertices data
        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 
        		0, //From byte offset 0
        		vertices.length*Float.BYTES, 
        		verticesBuffer);
        
        // load normals data
        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 
        		vertices.length*Float.BYTES, 
        		normals.length*Float.BYTES, //Load after the vertices data
        		normalsBuffer);
        
        // load colours data
        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 
        		(vertices.length*Float.BYTES + normals.length*Float.BYTES), 
        		colours.length*Float.BYTES, 
        		coloursBuffer);
        
        defineTextures(gl);
	}

	public void draw(GL2 gl) {

    		// Enable arrays: To tell the graphics pipeline that we want it to use our data
    		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
        
        //Bind the buffer we want to use
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);
    	
        // This tells OpenGL the locations for the arrays.
        gl.glVertexPointer(3, //3 coordinates per vertex 
		              	  GL.GL_FLOAT, //each co-ordinate is a float 
		                  0, //There are no gaps in data between co-ordinates 
		                  0); //Co-ordinates are at the start of the current array buffer
        gl.glNormalPointer(GL.GL_FLOAT, 0, vertices.length*Float.BYTES);
        gl.glColorPointer(3, GL.GL_FLOAT, 0, (vertices.length*Float.BYTES + normals.length*Float.BYTES));
        
        gl.glBindTexture(GL2.GL_TEXTURE_2D, tex.getTextureId());
        gl.glDrawArrays(GL2.GL_TRIANGLES, 0, 36);
        
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        
        // Un-bind the buffer
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
	}







}
