package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class Sun {
	private static final double SIZE = 1;
	private static final int SLICES = 32;


	
	private float[] daySkyColor = {0.529f, 0.808f, 0.922f,1.0f};
	private float[] nightSkyColor = {0.098f, 0.098f, 0.439f,0.0f};
	private float[] dayLight = {1.0f,1.0f,1.0f,1.0f};
	private float[] nightLight = {0.0f,0.0f,0.5f,0.0f};
	
	private float[] sunlight;
	private float[] dynamicSun;
	private float radius;
	private int shaderprogram;
	private float intensity;
	private float skyTime;
	private boolean animate;
	
	public Sun(float[] s, float r, int p) {
		sunlight = s;
		dynamicSun = s;
		radius = r;
		shaderprogram = p;
		intensity = 0.4f;
		animate = false;
		skyTime = 1.0f;
	}
	
	public void drawSun(GL2 gl, Texture[] tex) {
		

		gl.glPushMatrix(); {
			GLU glu = new GLU();
			GLUquadric quad = glu.gluNewQuadric();
			
			gl.glTranslatef(sunlight[0], sunlight[1], sunlight[2]);
			
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.SUN].getTextureId());
			glu.gluQuadricTexture(quad, true);
			glu.gluSphere(quad, SIZE, SLICES, SLICES);
		}

		gl.glPopMatrix();
		
	}
	
	public void setLight(GL2 gl) {
		gl.glUniform1f(gl.glGetUniformLocation(shaderprogram, "intensity"), intensity);
		
        gl.glClearColor(daySkyColor[0], daySkyColor[1], daySkyColor[2], daySkyColor[3]);
		
		float ambLight0[] = {0.3f,0.3f,0.3f,1.0f};
		float difLight0[] = {1.0f,1.0f,1.0f,1.0f};
		float specLight0[] = {1.0f,1.0f,1.0f,1.0f};
        gl.glEnable(GL2.GL_LIGHT0);
		
		/* Light 0 Properties */ 
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambLight0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, difLight0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specLight0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, getPosition(),0);

		float g = 0.2f; // Global Ambient intensity.
		int localViewer = 0;
		float globAmb[] = { g, g, g, 0.0f };

		// Global light properties
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb, 0); // Global ambient light.
//		gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, localViewer); // Enable local viewpoint
        gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE); // Enable two-sided lighting.

	}
	
	public float[] getPosition() {
		if (animate) return dynamicSun;
		else return sunlight;
	}
	
	

}
