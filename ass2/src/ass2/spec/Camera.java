package ass2.spec;

import java.awt.Dimension;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class Camera {
    private static final int FIRST_PERSON_RADIUS = 1;
    private static final int THIRD_PERSON_RADIUS = 5;
    private static final double TILT_RATE = 0.1;
    
    private static final double WALKING_SPEED = 0.18;
    private static final double TURNING_SPEED = 3;

    private static double eyeX;
    private static double eyeY;
    private static double eyeZ;
    
    private static double centerX;
    private static double centerY;
    private static double centerZ;
    
    private static double angle;
    private static boolean firstPerson;
    private static double eyeYOffset;
    private static double aspectRatio;
    private static double cameraOffset;
    
    public Terrain terrain;
	
	public Camera(Terrain terrain) {
		this.terrain = terrain;
		
        angle = 0;
        firstPerson = false;
        eyeYOffset = 2;
        cameraOffset = 0;
        
        eyeX = 5;
        eyeY = 3;
        eyeZ = 13;
	}

	public void render(GL2 gl) {
		GLU glu = new GLU();
		
		if (eyeX < 0 || eyeZ < 0 || eyeX > terrain.size().getWidth() - 1 || eyeZ > terrain.size().getHeight() - 1) {
			eyeY = 0;
		} else {
			eyeY = terrain.altitude(eyeX, eyeZ);
		}
		
		

		if (firstPerson) {
			double fpYOffset = eyeYOffset - 0.3;
			centerX = eyeX + (Math.sin(Math.toRadians((angle + 180) % 360)) * FIRST_PERSON_RADIUS);
			centerY = eyeY + 1.5;
			centerZ = eyeZ + (Math.cos(Math.toRadians((angle + 180) % 360)) * FIRST_PERSON_RADIUS);
			glu.gluLookAt(eyeX, eyeY + fpYOffset + cameraOffset, eyeZ, centerX, centerY + cameraOffset, centerZ, 0, 1, 0);
		} else {
			centerX = eyeX + (Math.sin(Math.toRadians(angle)) * THIRD_PERSON_RADIUS);
			centerY = eyeY + eyeYOffset + cameraOffset;
			centerZ = eyeZ + (Math.cos(Math.toRadians(angle)) * THIRD_PERSON_RADIUS);
			glu.gluLookAt(centerX, centerY, centerZ, eyeX, eyeY + cameraOffset, eyeZ, 0, 1, 0);

		}
	}
	
	public void moveVertical(double offset) {
	    cameraOffset = offset;
	}

	public void moveForward() {
		eyeX -= Math.sin(Math.toRadians(angle)) * WALKING_SPEED;
        eyeZ -= Math.cos(Math.toRadians(angle)) * WALKING_SPEED;
	}
	
	public void moveBackward() {
		eyeX += Math.sin(Math.toRadians(angle)) * WALKING_SPEED;
        eyeZ += Math.cos(Math.toRadians(angle)) * WALKING_SPEED;
	}
	
	public void turnLeft() {
		angle = (angle + TURNING_SPEED) % 360;
	}
	
	public void turnRight() {
		angle = (angle - TURNING_SPEED) % 360;
	}
	
	public void tiltUp() {
		eyeYOffset += TILT_RATE; 
	}
	
	public void tiltDown(){
		eyeYOffset -= TILT_RATE; 
	}
	
	
	public boolean isFirstPerson() {
		return firstPerson;
	}
	
	public double getAspectRatio() {
		return aspectRatio;
	}
	
	public double[] getPosition() {
		return new double[] {eyeX, eyeY, eyeZ};
	}
	
	public double[] getCenterPosition() {
		return new double[] {centerX, centerY, centerZ};
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void toggleFirstPerson() {
		firstPerson = !firstPerson;
	}
	
	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
		
	}
	
	public void projectionSetup(GL2 gl) {
		GLU glu = new GLU();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(60, aspectRatio, 0.01f, Float.MAX_VALUE);
	}

}
