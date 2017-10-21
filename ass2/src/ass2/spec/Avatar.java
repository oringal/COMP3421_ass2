package ass2.spec;

import com.jogamp.opengl.GL2;

import ass2.spec.Camera;
import ass2.spec.Texture;

public class Avatar {

	private static final double WALKING_SPEED = 1;
	private static final double AVARTAR_HEIGHT = 2.5f;
	private static final int SLICES = 32;

	private double moveZ = 0;
	
	private Camera camera;

	public Avatar(Camera camera) {
		this.camera = camera;
	}

	public void draw(GL2 gl, Texture[] tex) {

		camera.render(gl);

		if (camera.isFirstPerson()) {
			return;
		}
		
		gl.glPushMatrix();{
			
			double[] cameraPosition = camera.getPosition();
			// moves back and forth with camera
			gl.glTranslated(cameraPosition[0], cameraPosition[1] + AVARTAR_HEIGHT, cameraPosition[2]);
			// turns with camera
			gl.glRotated(camera.getAngle(), 0, 1, 0);
			
			drawCylinder(gl, tex);
		}
		gl.glPopMatrix();

	}
	
	private void drawCylinder(GL2 gl, Texture[] tex) {
		// used lecture code week 8 TexCylindar
		
		double angleIncrement = (Math.PI * 2.0) / SLICES;
		double zFront = -1;
		double zBack = -2;
		
		gl.glPushMatrix();
		
		gl.glRotated(-90, 1, 0, 0);
		
		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.AVATARHEAD].getTextureId());
		gl.glBegin(GL2.GL_POLYGON);{
			for (int i = 0; i < SLICES; i++) {
				
					double angle0 = i*angleIncrement;
	
					gl.glNormal3d(0.0, 1.0, 0.0);
					gl.glTexCoord2d(0.5+0.3*Math.cos(angle0),0.5+0.3*Math.sin(angle0));
					gl.glVertex3d(0.3*Math.cos(angle0), 0.3*Math.sin(angle0), zFront);
			}	
		} gl.glEnd();

		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.AVATARBODY].getTextureId());
		gl.glBegin(GL2.GL_QUAD_STRIP);{      
			for (int i=0; i<= SLICES; i++) {
				
				double angle0 = i*angleIncrement;
				double xPos0 = 0.3*Math.cos(angle0);
				double yPos0 = 0.3*Math.sin(angle0);
				double sCoord = 2.0/SLICES * i; //Or * 2 to repeat label

				gl.glNormal3d(xPos0, yPos0, 0);
				gl.glTexCoord2d(sCoord,1);
				gl.glVertex3d(xPos0,yPos0,zFront);
				gl.glTexCoord2d(sCoord,0);
				gl.glVertex3d(xPos0,yPos0,zBack);	
			}
		} gl.glEnd();    

		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.AVATARFEET].getTextureId());
		gl.glBegin(GL2.GL_POLYGON);{
			for (int i = 0; i < SLICES; i++) {
				
				double angle0 = -i*angleIncrement;

				gl.glNormal3d(0.0, -1.0, 0.0);
				gl.glTexCoord2d(0.5+0.3*Math.cos(angle0),0.5+0.3*Math.sin(angle0));
				gl.glVertex3d(0.3*Math.cos(angle0), 0.3*Math.sin(angle0), zBack);
			}
		} gl.glEnd();
		
		gl.glPopMatrix();
		
		drawRLeg(gl, tex);
		drawLLeg(gl, tex);
	}
	
	private void drawLLeg(GL2 gl, Texture[] tex) {
		
		double offSet = 0.2;
		
		gl.glPushMatrix();
		
		gl.glTranslated(0, -2.5, 0);
		
		//gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.AVATARLLEG].getTextureId());
		gl.glBegin(GL2.GL_QUADS); { 
		    // Top face (y = 1.0f)
		    // Define vertices in counter-clockwise (CCW) order with normal pointing out
			//gl.glColor3f(0.0f, 1.0f, 0.0f);     // Green
			//gl.glColor3f(0.3f, 0.3f, 0.3f);  // yellow
			gl.glVertex3f( (float)(0.1f - offSet), 1.0f, -0.1f);
			gl.glVertex3f( (float)(0.0f - offSet), 1.0f, -0.1f);
			gl.glVertex3f( (float)(0.0f - offSet), 1.0f,  0.0f);
			gl.glVertex3f( (float)(0.1f - offSet), 1.0f,  0.0f);
		 
		      // Bottom face (y = -1.0f)
			//gl.glColor3f(1.0f, 0.5f, 0.0f);     // Orange
			//gl.glColor3f(0.3f, 0.3f, 0.3f);  // yellow
			gl.glVertex3f( (float)(0.1f - offSet),  0.0f, -0.1f);
			gl.glVertex3f( (float)(0.0f - offSet),  0.0f, -0.1f);
			gl.glVertex3f( (float)(0.0f - offSet),  0.0f,  0.0f);
			gl.glVertex3f( (float)(0.1f - offSet),  0.0f,  0.0f);
		 
		      // Front face  (z = 1.0f)
			//gl.glColor3f(1.0f, 0.0f, 0.0f);     // Red
//			gl.glColor3f(0.5f, 0.5f, 0.0f);  // yellow
			gl.glVertex3f( (float)(0.1f - offSet),  0.0f, 0.0f);
			gl.glVertex3f( (float)(0.1f - offSet),  1.0f, 0.0f);
			gl.glVertex3f( (float)(0.0f - offSet),  1.0f, 0.0f);
			gl.glVertex3f( (float)(0.0f - offSet),  0.0f, 0.0f);
		 
		      // Back face (z = -1.0f)
			//gl.glColor3f(0.3f, 0.3f, 0.3f);     // Yellow
			gl.glVertex3f( (float)(0.1f - offSet),  0.0f, -0.1f);
			gl.glVertex3f( (float)(0.1f - offSet),  1.0f, -0.1f);
			gl.glVertex3f( (float)(0.0f - offSet),  1.0f, -0.1f);
			gl.glVertex3f( (float)(0.0f - offSet),  0.0f, -0.1f);
		 
		      // Left face (x = -1.0f)
			//gl.glColor3f(0.0f, 0.0f, 1.0f);     // Blue
			//gl.glColor3f(0.3f, 0.3f, 0.3f);  // yellow
			gl.glVertex3f( (float)(0.0f - offSet),  0.0f, -0.1f);
			gl.glVertex3f( (float)(0.0f - offSet),  1.0f, -0.1f);
			gl.glVertex3f( (float)(0.0f - offSet),  1.0f,  0.0f);
			gl.glVertex3f( (float)(0.0f - offSet),  0.0f,  0.0f);
		 
		      // Right face (x = 1.0f)
			//gl.glColor3f(0.3f, 0.3f, 0.3f);     // Magenta
			//gl.glColor3f(0.3f, 0.3f, 0.3f);  // yellow
			gl.glVertex3f( (float)(0.1f - offSet),  0.0f,  0.0f);
			gl.glVertex3f( (float)(0.1f- offSet),  0.0f, -0.1f);
			gl.glVertex3f( (float)(0.1f - offSet),  1.0f, -0.1f);
			gl.glVertex3f( (float)(0.1f - offSet),  1.0f,  0.0f);
		} gl.glEnd(); 
		
		gl.glPopMatrix();
		
	}
		
	private void drawRLeg(GL2 gl, Texture[] tex) {
		
		double offSet = - 0.1;
		
		gl.glPushMatrix();
		
		gl.glTranslated(0, -2.5, 0);
		
		//gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.AVATARRLEG].getTextureId());
		gl.glBegin(GL2.GL_QUADS); { 
		      // Top face (y = 1.0f)
		      // Define vertices in counter-clockwise (CCW) order with normal pointing out
//			gl.glColor3f(0.0f, 1.0f, 0.0f);     // Green
			gl.glVertex3f( (float)(0.1f - offSet), 1.0f, -0.1f);
			gl.glVertex3f( (float)(0.0f - offSet), 1.0f, -0.1f);
			gl.glVertex3f( (float)(0.0f - offSet), 1.0f,  0.0f);
			gl.glVertex3f( (float)(0.1f - offSet), 1.0f,  0.0f);
		 
		      // Bottom face (y = -1.0f)
//			gl.glColor3f(1.0f, 0.5f, 0.0f);     // Orange
			gl.glVertex3f( (float)(0.1f - offSet),  0.0f, -0.1f);
			gl.glVertex3f( (float)(0.0f - offSet),  0.0f, -0.1f);
			gl.glVertex3f( (float)(0.0f - offSet),  0.0f,  0.0f);
			gl.glVertex3f( (float)(0.1f - offSet),  0.0f,  0.0f);
		 
		      // Front face  (z = 1.0f)
//			gl.glColor3f(1.0f, 0.0f, 0.0f);     // Red
			gl.glVertex3f( (float)(0.1f - offSet),  0.0f, 0.0f);
			gl.glVertex3f( (float)(0.1f - offSet),  1.0f, 0.0f);
			gl.glVertex3f( (float)(0.0f - offSet),  1.0f, 0.0f);
			gl.glVertex3f( (float)(0.0f - offSet),  0.0f, 0.0f);
		 
		      // Back face (z = -1.0f)
//			gl.glColor3f(0.3f, 0.3f, 0.3f);     // Yellow
			gl.glVertex3f( (float)(0.1f - offSet),  0.0f, -0.1f);
			gl.glVertex3f( (float)(0.1f - offSet),  1.0f, -0.1f);
			gl.glVertex3f( (float)(0.0f - offSet),  1.0f, -0.1f);
			gl.glVertex3f( (float)(0.0f - offSet),  0.0f, -0.1f);
		 
		      // Left face (x = -1.0f)
//			gl.glColor3f(0.0f, 0.0f, 1.0f);     // Blue
			gl.glVertex3f( (float)(0.0f - offSet),  0.0f, -0.1f);
			gl.glVertex3f( (float)(0.0f - offSet),  1.0f, -0.1f);
			gl.glVertex3f( (float)(0.0f - offSet),  1.0f,  0.0f);
			gl.glVertex3f( (float)(0.0f - offSet),  0.0f,  0.0f);
		 
		      // Right face (x = 1.0f)
//			gl.glColor3f(0.3f, 0.3f, 0.3f);     // Magenta
			gl.glVertex3f( (float)(0.1f - offSet),  0.0f,  0.0f);
			gl.glVertex3f( (float)(0.1f- offSet),  0.0f, -0.1f);
			gl.glVertex3f( (float)(0.1f - offSet),  1.0f, -0.1f);
			gl.glVertex3f( (float)(0.1f - offSet),  1.0f,  0.0f);
		} gl.glEnd(); 
		
		gl.glPopMatrix();
	}
	
	public void moveForward() {
		moveZ = moveZ + WALKING_SPEED;
		camera.moveForward();
	}

	public void moveBackward() {
		moveZ = moveZ - WALKING_SPEED;
		camera.moveBackward();
	}

	public void turnLeft() {
		camera.turnLeft();
	}

	public void turnRight() {
		camera.turnRight();
	}
}
