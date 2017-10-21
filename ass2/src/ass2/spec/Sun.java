package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class Sun {
	private static final double SIZE = 1;
	private static final int SLICES = 32;
	private static final double lightSlices = 250;

	
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
	private double lightStep;
	private float timeStep = 0.005f;

	
	private double terrainWidth;
	private double terrainLength;
	
	public Sun(float[] s, float r, int p, double tw, double tl) {
		sunlight = s;
		dynamicSun = s;
		radius = r;
		shaderprogram = p;
		intensity = 1.0f;
		animate = true;
		skyTime = 1.0f;
		lightStep = 0;
		terrainWidth = tw;
		terrainLength = tl;
	}
	
	public void drawSun(GL2 gl, Texture[] tex) {
	
		gl.glPushMatrix(); {
			GLU glu = new GLU();
			GLUquadric quad = glu.gluNewQuadric();
			
			float sunAmbAndDifL[] = { 2.5f, 2.5f, 2.5f, 1.0f };
			
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, sunAmbAndDifL, 0);
			
			float[] pos = getPosition();

			gl.glTranslatef(pos[0], pos[1], pos[2]);
			
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.SUN].getTextureId());
			glu.gluQuadricTexture(quad, true);
			glu.gluSphere(quad, SIZE, SLICES, SLICES);
		}

		gl.glPopMatrix();
		
	}
	
	private void renderSky(GL2 gl) {
		float[] color = {
				intensity * daySkyColor[0] + (1-intensity) * nightSkyColor[0],
				intensity * daySkyColor[1] + (1-intensity) * nightSkyColor[1],
				intensity * daySkyColor[2] + (1-intensity) * nightSkyColor[2],
				intensity * daySkyColor[3] + (1-intensity) * nightSkyColor[3],
		};
		gl.glClearColor(color[0], color[1], color[2], color[3]);
	}
	
	private float[] getDifSpecLight() {
		float[] light = {
				intensity * dayLight[0] + (1-intensity) * nightLight[0],
				intensity * dayLight[1] + (1-intensity) * nightLight[1],
				intensity * dayLight[2] + (1-intensity) * nightLight[2],
				intensity * dayLight[3] + (1-intensity) * nightLight[3],
		};
		return light;
	}
	
	public void setLight(GL2 gl) {
		gl.glUniform1f(gl.glGetUniformLocation(shaderprogram, "intensity"), intensity);
		renderSky(gl);
		
		float[] pos = getPosition();
		
//        gl.glClearColor(daySkyColor[0], daySkyColor[1], daySkyColor[2], daySkyColor[3]);
		
		float ambLight0[] = {0.3f,0.3f,0.3f,1.0f};
		float difSpecLight0[] = getDifSpecLight();
//		float specLight0[] = {1.0f,1.0f,1.0f,1.0f};
        gl.glEnable(GL2.GL_LIGHT0);
		
		/* Light 0 Properties */ 
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambLight0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, difSpecLight0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, difSpecLight0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos ,0);

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
	
	public void switchAnimate() {
		animate = !animate;
	}
	
	public boolean checkAnimate() {
		return animate;
	}
	
	public void changeSun() {
		double angle = lightStep * (2 * Math.PI / lightSlices);
		dynamicSun[0] = (float) (terrainWidth/2 + 10 * Math.cos(angle));
		dynamicSun[1] = (float) (terrainWidth/2 + 10 * Math.sin(angle));
		dynamicSun[2] = 1;
		lightStep++;
	}
	
	public void update() {
		intensity += timeStep;

		if (intensity > 1 || intensity <= 0.4f) {
			timeStep = -timeStep;
		}	
		changeSun();
	
	}
	
	

}
