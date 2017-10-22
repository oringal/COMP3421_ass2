package ass2.spec;

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
	
	private int bufferIds[] = new int[4];
	
	private void defineTextures(GL2 gl) {
		String fileName = "evilMinionIcon.png";
		String fileExt = "png";

		tex = new Texture(gl, fileName, fileExt, true);
	}
	
	public void setup (GL2 gl) {
		// set texture environment parameters
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE); 
		
		// Generate 1 VBO buffer and get its ID
        gl.glGenBuffers(1, bufferIds, 0);
        
        // bind a named buffer object
        // This buffer is now the current array buffer
        // array buffers hold vertex attribute data
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferIds[0]);

        //gl.glBufferData(GL.GL_ARRAY_BUFFER, (headVertices.length*4) + (headTex.length*4) + (headNormals.length*4), null, GL2.GL_STATIC_DRAW);
        
		
	}







}
