package ass2.spec;

import com.jogamp.opengl.GL2;

public class Avatar {
	// use lecture week 8 texCylindar to draw minion
	
	private double Xangle = 0;
    private double Yangle = 0;
	private double Zangle = 0;
  
    private static final int SLICES = 32;

	private Camera camera;
	private Texture headTexture;
	private Texture bodyTexture;
	
	public Avatar(Camera camera) {
		this.camera = camera;
	}
	
	public void setup(GL2 gl) {
		headTexture = new Texture(gl, "minionHead.bmp", "bmp", true);
		//bodyTexture = new Texture(gl, "minionBody.bmp", "bmp", true);
	}
	
	public void draw(GL2 gl) {
		
	}

}
