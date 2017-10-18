package ass2.spec;

import com.jogamp.opengl.GL2;

public class Avatar {
	// used lecture code week 8 TexCylindar to draw cylinder

	private double angleX = 0;
	private double angleY = 0;
	private double angleZ = 0;

	private static final int SLICES = 32;

	private Camera camera;
//	private Texture headTexture;
//	private Texture bodyTexture;
	
	private Texture myTextures[];

	public Avatar(Camera camera) {
		this.camera = camera;
	}

	public void setup(GL2 gl) {
		//myTextures[0] = new Texture(gl, "textures/minionHead.bmp", "bmp", true);
		//myTextures[1] = new Texture(gl, "textures/minionBody.bmp", "bmp", true);
	}

	public void draw(GL2 gl) {
		// Commands to turn the cylinder.
		gl.glRotated(angleZ, 0.0, 0.0, 1.0);
		gl.glRotated(angleY, 0.0, 1.0, 0.0);
		gl.glRotated(angleX, 1.0, 0.0, 0.0);
		gl.glTranslated(0, 0, 3);

		double angleIncrement = (Math.PI * 2.0) / SLICES;
		double zFront = -1;
		double zBack = -3;

		//gl.glEnable(GL2.GL_TEXTURE_2D);
		//myTextures[0] = new Texture(gl, "textures/minionHead.bmp", "bmp", true);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());
		gl.glBegin(GL2.GL_POLYGON);{

			for(int i = 0; i < SLICES; i++)
			{
				double angle0 = i*angleIncrement;

				gl.glNormal3d(0.0, 0.0, 1);
				gl.glTexCoord2d(0.5+0.5*Math.cos(angle0),0.5+0.5*Math.sin(angle0));
				gl.glVertex3d(Math.cos(angle0), Math.sin(angle0),zFront);
			}
		}gl.glEnd();

		//myTextures[1] = new Texture(gl, "textures/minionBody.bmp", "bmp", true);
		//gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[1].getTextureId());
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

}
