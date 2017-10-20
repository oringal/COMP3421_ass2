package ass2.spec;

import com.jogamp.opengl.GL2;

public class Light {
	
	public void setLight(GL2 gl) {
		
		float ambLight0[] = {0.3f,0.3f,0.3f,1.0f};
		float difLight0[] = {1.0f,1.0f,1.0f,1.0f};
		float specLight0[] = {1.0f,1.0f,1.0f,1.0f};
		
		/* Light 0 Properties */ 
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambLight0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, difLight0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specLight0, 0);
		

		float g = 0.2f; // Global Ambient intensity.
		int localViewer = 0;
		float globAmb[] = { g, g, g, 0.0f };

		// Global light properties
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb, 0); // Global ambient light.
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, localViewer); // Enable local viewpoint
	}

}
