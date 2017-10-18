package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class Avatar {
	// used lecture code week 8 TexCylindar to draw cylinder

	private static final double WALKING_SPEED = 10;
	
	private double angleX = 0;
	private double angleY = 0;
	private double angleZ = 3;
	
	private double moveX = 0;
	private double moveY = 0;
	private double moveZ = 0;

	private static final int SLICES = 32;

	private Camera camera;
//	private Texture headTexture;
//	private Texture bodyTexture;
	
//	private Texture myTextures[];

	public Avatar(Camera camera) {
		this.camera = camera;
		
	}

	public void draw(GL2 gl, Texture[] tex) {
		//GLU glu = new GLU();
		camera.render(gl);
//
//		if (camera.isFirstPerson()) {
//			return;
//		}
		
//		double[] cameraPosition = camera.getPosition();
//		gl.glTranslated(cameraPosition[0], cameraPosition[1], cameraPosition[2]);
//		gl.glRotated(camera.getAngle(), 0, 1, 0);
		
		// Commands to turn the cylinder.
		gl.glRotated(angleZ, 0.0, 0.0, 1.0);
		gl.glRotated(angleY, 0.0, 1.0, 0.0);
		gl.glRotated(angleX, 1.0, 0.0, 0.0);
		gl.glTranslated(0, 0 , 3);
		//gl.glTranslated(moveX, moveY, moveZ);

		double angleIncrement = (Math.PI * 2.0) / SLICES;
		double zFront = -1;
		double zBack = -3;

		//gl.glEnable(GL2.GL_TEXTURE_2D);
		//Draw the top of the cylinder with the canTop.bmp texture
		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.AVATARHEAD].getTextureId());
		gl.glBegin(GL2.GL_POLYGON);{

			for(int i = 0; i < SLICES; i++)
			{
				double angle0 = i*angleIncrement;

				gl.glNormal3d(0.0, 0.0, 1);
				gl.glTexCoord2d(0.5+0.5*Math.cos(angle0),0.5+0.5*Math.sin(angle0));
				gl.glVertex3d(Math.cos(angle0), Math.sin(angle0),zFront);
			}
		}gl.glEnd();

		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.AVATARBODY].getTextureId());
		gl.glBegin(GL2.GL_QUAD_STRIP);{      
			for(int i=0; i<= SLICES; i++){
				double angle0 = i*angleIncrement;
				double angle1 = (i+1)*angleIncrement;
				double xPos0 = Math.cos(angle0);
				double yPos0 = Math.sin(angle0);
				double sCoord = 2.0/SLICES * i; //Or * 2 to repeat label


				gl.glNormal3d(xPos0, yPos0, 0);
				gl.glTexCoord2d(sCoord,1);
				gl.glVertex3d(xPos0,yPos0,zFront);
				gl.glTexCoord2d(sCoord,0);
				gl.glVertex3d(xPos0,yPos0,zBack);	        	

			}
		}gl.glEnd();    

		//Draw the bottom of the cylinder also with the canTop.bmp texture :)
		//just for demonstration.
		gl.glBegin(GL2.GL_POLYGON);{

			for(int i = 0; i < SLICES; i++)
			{
				double angle0 = -i*angleIncrement;

				gl.glNormal3d(0.0, 0.0, -1);

				gl.glTexCoord2d(0.5+0.5*Math.cos(angle0),0.5+0.5*Math.sin(angle0));
				gl.glVertex3d(Math.cos(angle0), Math.sin(angle0),zBack);
			}
		}gl.glEnd();
	}
	
	public void moveForward() {
		//moveX = (moveX + WALKING_SPEED);
		camera.moveForward();
	}

	public void moveBackward() {
		//moveX = (moveX - WALKING_SPEED);
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
