package ass2.spec;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

/**
 * 
 * @author Antheny and Gladys
 * week 7 triangleVBO lecture code as a starting point/for understanding
 */

public class Enemy {

	private float size = 0.5f;
	private float[] vertices = {
			// Top
			size, size, -size, 
			-size, size, -size,
			-size,size,size,
			size,size,size,

			// bottom
			size,-size,size,
			-size,-size,size,
			-size,-size,-size,
			size,-size,-size,

			// front
			size,size,size,
			-size,size,size,
			-size,-size,size,
			size,-size,size,

			// back
			size,-size,-size,
			-size,-size,-size,
			-size,size,-size,
			size,size,-size,

			// left
			-size,size,size,
			-size,size,-size,
			-size,-size,-size,
			-size,-size,size,

			// right
			size,size,-size,
			size,size,size,
			size,-size,size,
			size,-size,-size
	};

	private float[] normals = {
			// top
			0f, 1f, 0f,
			0f, 1f, 0f,
			0f, 1f, 0f,
			0f, 1f, 0f,

			// bottom
			0f, -1f, 0f,
			0f, -1f, 0f,
			0f, -1f, 0f,
			0f, -1f, 0f,

			// front
			0f, 0f, 1f,
			0f, 0f, 1f,
			0f, 0f, 1f,
			0f, 0f, 1f,

			// back
			0f, 0f, -1f,
			0f, 0f, -1f,
			0f, 0f, -1f,
			0f, 0f, -1f,

			// left
			-1f, 0f, 0f,
			-1f, 0f, 0f,
			-1f, 0f, 0f,
			-1f, 0f, 0f,

			// right
			1f, 0f, 0f,
			1f, 0f, 0f,
			1f, 0f, 0f,
			1f, 0f, 0f,
	};

	private float[] textureCoords = {

			0, 0,
			1, 0,
			1, 1,
			0, 1,

			0, 0,
			1, 0,
			1, 1,
			0, 1,

			0, 1,
			1, 1,
			1, 0,
			0, 0,

			0, 0,
			1, 0,
			1, 1,
			0, 1,

			0, 1,
			1, 1,
			1, 0,
			0, 0,

			0, 1,
			1, 1,
			1, 0,
			0, 0
	};

	private Texture[] tex;
	private Camera camera;
	private Terrain terrain;
	private double move;
	private double angle;
	private static final double WALKING_SPEED = 0.2;
	private static final double TURNING_SPEED = 2;
	public boolean moveWith;

	private int bufferIds[] = new int[4];
	//These are not vertex buffer objects, they are just java containers
	FloatBuffer verticesBuffer = Buffers.newDirectFloatBuffer(vertices);
	FloatBuffer textureCoordsBuffer = Buffers.newDirectFloatBuffer(textureCoords);
	FloatBuffer normalsBuffer = Buffers.newDirectFloatBuffer(normals);

	public Enemy (Camera camera, Terrain terrain) {
		this.camera = camera;
		this.terrain = terrain;
		tex = new Texture[5];
		move = 0;
		angle = 0;
		moveWith = false;
	}

	private void defineTextures(GL2 gl) {
		String fileName = "textures/evilMinionIcon.png";
		String fileName1 = "textures/purpleSquare.png";
		String fileExt = "png";



		tex[0] = new Texture(gl, fileName, fileExt, true);
		tex[1] = new Texture(gl, fileName1, fileExt, true);
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
				(vertices.length*4) + (normals.length*4) + (textureCoords.length*4), 
				null, 
				GL2.GL_STATIC_DRAW);

		//Now load vertices data
		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 
				0, //From byte offset 0
				vertices.length*4, 
				verticesBuffer);

		// load normals data
		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 
				vertices.length*4, 
				normals.length*4, //Load after the vertices data
				normalsBuffer);

		// load textureCoords data
		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 
				(vertices.length*4 + normals.length*4), 
				textureCoords.length*4, 
				textureCoordsBuffer);

		defineTextures(gl);
	}

	public void draw(GL2 gl) {
		
//		if (camera.isFirstPerson()) {
//			return;
//		}
		
		gl.glPushMatrix();{
//			if (moveWith) {
//				positionMe(gl);
//				System.out.print(moveWith);
//			}
			
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
			gl.glNormalPointer(GL.GL_FLOAT, 0, vertices.length*4);
			gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, (vertices.length*4 + normals.length*4));
			
			int offset = 0;
	
			// front face (other faces will just be purple)
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[1].getTextureId());
			gl.glDrawArrays(GL2.GL_QUADS, offset, 4);
			offset += 4;
	
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[1].getTextureId());
			gl.glDrawArrays(GL2.GL_QUADS, offset, 4);
			offset += 4;
	
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[1].getTextureId());
			gl.glDrawArrays(GL2.GL_QUADS, offset, 4);
			offset += 4;
	
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[1].getTextureId());
			gl.glDrawArrays(GL2.GL_QUADS, offset, 4);
			offset += 4;
	
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[1].getTextureId());
			gl.glDrawArrays(GL2.GL_QUADS, 0, 4);
			offset += 4;
	
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[1].getTextureId());
			gl.glDrawArrays(GL2.GL_QUADS, offset, 4);
	
			gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
			gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
	
			// Un-bind the buffer
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
		
		} gl.glPopMatrix();
	}

//	private void positionMe(GL2 gl) {
//			double[] cameraPosition = camera.getPosition();
//			gl.glTranslated(cameraPosition[0] - 2, cameraPosition[1]  + 1, cameraPosition[2]);
//			gl.glRotated(camera.getAngle(), 0, 1, 0);
//	}
	
//	public void toggleMoveWith() {
//		moveWith = !moveWith;
//	}

//	public void moveEnemyForward(GL2 gl) {
//		
//		double[] cameraPosition = camera.getPosition();
//		gl.glTranslated(cameraPosition[0] - 2, cameraPosition[1]  + 1, cameraPosition[2]);
//		
//	}
//
//	public void moveEnemyBackward(GL2 gl) {
//		
//		double[] cameraPosition = camera.getPosition();
//		gl.glTranslated(cameraPosition[0] - 2, cameraPosition[1]  + 1, cameraPosition[2]);
//		
//	}
//
//	public void turnEnemyLeft(GL2 gl) {
//		angle = (angle + TURNING_SPEED) % 360;
//		gl.glRotated(camera.getAngle() + angle, 0, 1, 0);
//		
//	}
//
//	public void turnEnemyRight(GL2 gl) {
//		angle = (angle - TURNING_SPEED) % 360;
//		gl.glRotated(camera.getAngle() - angle, 0, 1, 0);
//		
//	}
}
