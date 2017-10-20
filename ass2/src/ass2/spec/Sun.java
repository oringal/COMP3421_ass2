package ass2.spec;

import com.jogamp.opengl.GL2;

public class Sun {
	
	private double[] sunlight;
	private double radius;
	private int shaderprogram;
	private int intensity;
	
	public Sun(double[] s, double r, int p) {
		sunlight = s;
		radius = r;
		shaderprogram = p;
		intensity = 1;
	}
	
	public void drawSun(GL2 gl) {
		
	}
	
	

}
