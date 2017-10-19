package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import ass2.spec.Camera;
import ass2.spec.Texture;

public class Avatar {
	// used lecture code week 8 TexCylindar to draw cylinder

	private static final double WALKING_SPEED = 1;
	private static final double AVARTAR_HEIGHT = 1.5;

	private double moveZ = 0;

	private static final int SLICES = 32;

	private Camera camera;

	public Avatar(Camera camera) {
		this.camera = camera;
		
	}

	public void draw(GL2 gl, Texture[] tex) {
		//GLU glu = new GLU();
		//gl.glDisable(GL2.GL_CULL_FACE);
		camera.render(gl);

		if (camera.isFirstPerson()) {
			return;
		}
		gl.glPushMatrix();
		
		double[] cameraPosition = camera.getPosition();
		// moves back and forth with camera
		gl.glTranslated(cameraPosition[0], cameraPosition[1] + AVARTAR_HEIGHT , cameraPosition[2]);
		// turns with camera
		gl.glRotated(camera.getAngle(), 0, 1, 0);
		
		// Commands to turn the cylinder.
//		gl.glRotated(90, 0.0, 0.0, 1.0);
//		gl.glRotated(90, 0.0, 1.0, 0.0);
//		gl.glRotated(90, 1.0, 0.0, 0.0);
		//gl.glTranslated(0, 0 , 3);
		//gl.glTranslated(0, 0, moveZ);
		//gl.glRotated(90, 1.0, 0.0, 0.0);
		drawCylinder(gl, tex);
		
		gl.glPopMatrix();

	}
	
	private void drawCylinder(GL2 gl, Texture[] tex) {
		
		double angleIncrement = (Math.PI * 2.0) / SLICES;
		double zFront = -1;
		double zBack = -2;
		
		gl.glPushMatrix();
		
		
		gl.glRotated(-90, 1, 0, 0);
		
		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.AVATARHEAD].getTextureId());
		gl.glBegin(GL2.GL_POLYGON);{

			for(int i = 0; i < SLICES; i++)
			{
				double angle0 = i*angleIncrement;

				gl.glNormal3d(0.0, 1.0, 0.0);
				gl.glTexCoord2d(0.5+0.5*Math.cos(angle0),0.5+0.5*Math.sin(angle0));
				gl.glVertex3d(0.5*Math.cos(angle0), 0.5*Math.sin(angle0), zFront);
				
			}
			
		}gl.glEnd();

		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.AVATARBODY].getTextureId());
		gl.glBegin(GL2.GL_QUAD_STRIP);{      
			for(int i=0; i<= SLICES; i++){
				double angle0 = i*angleIncrement;
				double angle1 = (i+1)*angleIncrement;
				double xPos0 = 0.5*Math.cos(angle0);
				double yPos0 = 0.5*Math.sin(angle0);
				double sCoord = 2.0/SLICES * i; //Or * 2 to repeat label


				gl.glNormal3d(xPos0, yPos0, 0);
				gl.glTexCoord2d(sCoord,1);
				gl.glVertex3d(xPos0,yPos0,zFront);
				gl.glTexCoord2d(sCoord,0);
				gl.glVertex3d(xPos0,yPos0,zBack);	
				

			}
			
		}gl.glEnd();    

		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.AVATARFEET].getTextureId());
		gl.glBegin(GL2.GL_POLYGON);{

			for(int i = 0; i < SLICES; i++)
			{
				double angle0 = -i*angleIncrement;

				gl.glNormal3d(0.0, -1.0, 0.0);

				gl.glTexCoord2d(0.5+0.5*Math.cos(angle0),0.5+0.5*Math.sin(angle0));
				gl.glVertex3d(0.5*Math.cos(angle0), 0.5*Math.sin(angle0), zBack);
				
			}
			
		}gl.glEnd();
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
	
//	public double[] getPosition() {
//		return camera.getPosition();
//	}
//	
//	public double getCameraRotate() {
//		return camera.getAngle();
//	}

}
