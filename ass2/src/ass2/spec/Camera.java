package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

/**
 * Camera class controls what the camera shows.
 * @author Antheny and Gladys
 */
public class Camera {

	private static final double WALKING_SPEED = 0.2;
	private static final double TURNING_SPEED = 2;
	private static final int THIRD_PERSON_OFFSET = 6;

	private static double eyeX;
	private static double eyeY;
	private static double eyeZ;

	private static double centerX;
	private static double centerY;
	private static double centerZ;

	private static double angle;
	private static double eyeYOffset;
	private static double firstPersoneyeYOffset;
	private static boolean firstPerson;

	public Terrain terrain;

	public Camera(Terrain terrain) {
		this.terrain = terrain;

		eyeX = 4;
		eyeY = 2;
		eyeZ = 12;

		angle = 0;
		firstPerson = false;
		eyeYOffset = 2;
		firstPersoneyeYOffset = 2;
	}

	/**
	 * Renders new camera frame.
	 * @param gl
	 */
	public void render(GL2 gl) {

		GLU glu = new GLU();

		if (eyeZ > terrain.size().getHeight() - 1 || eyeX > terrain.size().getWidth() - 1 || eyeZ < 0 || eyeX < 0) {
			// not in map
			eyeY = 0;
		} else {
			eyeY = terrain.altitude(eyeX, eyeZ);
		}

		if (firstPerson) {
			centerX = eyeX + (Math.sin(Math.toRadians((angle + 180) % 360)));
			centerY = eyeY + firstPersoneyeYOffset;
			centerZ = eyeZ + (Math.cos(Math.toRadians((angle + 180) % 360)));
			eyeY = eyeY + firstPersoneyeYOffset;
			glu.gluLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, 0, 1, 0);
		} else {
			centerX = eyeX + (Math.sin(Math.toRadians(angle)) * THIRD_PERSON_OFFSET);
			centerY = eyeY + eyeYOffset;
			centerZ = eyeZ + (Math.cos(Math.toRadians(angle)) * THIRD_PERSON_OFFSET);
			glu.gluLookAt(centerX, centerY, centerZ, eyeX, eyeY, eyeZ, 0, 1, 0);
		}
	}

	/**
	 * Calculates how far forward to move the camera.
	 */
	public void moveForward() {
		eyeX -= Math.sin(Math.toRadians(angle)) * WALKING_SPEED;
		eyeZ -= Math.cos(Math.toRadians(angle)) * WALKING_SPEED;
	}

	/**
	 * Calculates how far back to move the camera.
	 */
	public void moveBackward() {
		eyeX += Math.sin(Math.toRadians(angle)) * WALKING_SPEED;
		eyeZ += Math.cos(Math.toRadians(angle)) * WALKING_SPEED;
	}

	/**
	 * Calculates the angle to turn the camera left.
	 */
	public void turnLeft() {
		angle = (angle + TURNING_SPEED) % 360;
	}

	/**
	 * Calculates the angle to turn the camera right.
	 */
	public void turnRight() {
		angle = (angle - TURNING_SPEED) % 360;
	}

	/**
	 * Checks if it's in first person camera mode.
	 * @return
	 */
	public boolean isFirstPerson() {
		return firstPerson;
	}

	/**
	 * Get the position of the camera.
	 * @return
	 */
	public double[] getPosition() {
		return new double[] {eyeX, eyeY, eyeZ};
	}

	/**
	 * Gets camera angle
	 * @return
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Toggles between first and person camera mode.
	 */
	public void toggleFirstPerson() {
		firstPerson = !firstPerson;
	}
}
