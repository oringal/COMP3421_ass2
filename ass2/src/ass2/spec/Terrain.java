package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

/**
 * Generates a terrain.
 * @author Antheny and Gladys
 */

public class Terrain {
	
	//private final static boolean debug = false;

	private Dimension mySize;
	private double[][] myAltitude;
	private List<Tree> myTrees;
	private List<Road> myRoads;
	private float[] mySunlight;
	private Sun mySun;
	private int shaderprogram;


	/**
	 * Create a new terrain
	 *
	 * @param width The number of vertices in the x-direction
	 * @param depth The number of vertices in the z-direction
	 */
	public Terrain(int width, int depth) {
		mySize = new Dimension(width, depth);
		myAltitude = new double[width][depth];
		myTrees = new ArrayList<Tree>();
		myRoads = new ArrayList<Road>();
		mySunlight = new float[3];
	}

	public Terrain(Dimension size) {
		this(size.width, size.height);
	}

	public Dimension size() {
		return mySize;
	}

	public List<Tree> trees() {
		return myTrees;
	}

	public List<Road> roads() {
		return myRoads;
	}

	public float[] getSunlight() {
		return mySunlight;
	}

	/**
	 * Set the sunlight direction.
	 *
	 * Note: the sun should be treated as a directional light, without a position
	 *
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void setSunlightDir(float dx, float dy, float dz) {
		mySunlight[0] = dx;
		mySunlight[1] = dy;
		mySunlight[2] = dz;
	}


	/**
	 * Resize the terrain, copying any old altitudes.
	 *
	 * @param width
	 * @param height
	 */
	public void setSize(int width, int height) {
		mySize = new Dimension(width, height);
		double[][] oldAlt = myAltitude;
		myAltitude = new double[width][height];

		for (int i = 0; i < width && i < oldAlt.length; i++) {
			for (int j = 0; j < height && j < oldAlt[i].length; j++) {
				myAltitude[i][j] = oldAlt[i][j];
			}
		}
	}

	/**
	 * Get the altitude at a grid point
	 *
	 * @param x
	 * @param z
	 * @return
	 */
	public double getGridAltitude(int x, int z) {
		return myAltitude[x][z];
	}

	/**
	 * Set the altitude at a grid point
	 *
	 * @param x
	 * @param z
	 * @return
	 */
	public void setGridAltitude(int x, int z, double h) {
		myAltitude[x][z] = h;
	}
	
	/**
	 * Set the shader program as defined in Game.java
	 * @param s
	 */
	public void setShaderprogram(int s) {
		shaderprogram = s;
	}

	/**
	 * Get the altitude at an arbitrary point.
	 * Non-integer points should be interpolated from neighbouring grid points
	 *
	 * TO BE COMPLETED
	 *
	 * @param x
	 * @param z
	 * @return
	 */
	public double altitude(double x, double z) {

		double altitude = 0;

		// If not in frame, return.
		if (x < 0 || x > mySize.width -1 || z < 0 || z > mySize.height -1 ) {
			return altitude;
		}

		// grid
		// -------   z
		// | . / |   ^
		// |  /. |   |
		// -------   ----> x
		// the point can be: in either triangle, on z axis, on x axis, the intersection of the axis --> 4 cases

		double leftX = Math.floor(x);
		double rightX = Math.ceil(x);
		double backZ = Math.floor(z); // neg. z direction (away from us)
		double forwardZ = Math.ceil(z);

		if (x%1 != 0 && z%1 != 0) { // both not integers
			double left = altitude(leftX, z);
			double right = altitude(rightX, z);
			altitude = (x - leftX)/(rightX - leftX) * right + (rightX - x)/(rightX - leftX) * left;
		} else if (x%1 != 0) { // interpolate z axis
			double part1 = ( (x - leftX) / (rightX - leftX) ) * getGridAltitude((int)rightX, (int)z) ;
			double part2 = ( (rightX - x) / (rightX - leftX) ) * getGridAltitude((int)leftX, (int)z) ;
			altitude = part1 + part2;
		} else if (z%1 != 0) { // interpolate x axis
			double part1 = ( (z - backZ) / (forwardZ - backZ) ) * getGridAltitude((int)x, (int)forwardZ) ;
			double part2 = ( (forwardZ - z) / (forwardZ - backZ) ) * getGridAltitude((int)x, (int)backZ) ;
			altitude = part1 + part2;
		} else { // both are integers
			altitude = getGridAltitude((int) x, (int) z);
		}

		return altitude;
	}

	/**
	 * Add a tree at the specified (x,z) point.
	 * The tree's y coordinate is calculated from the altitude of the terrain at that point.
	 *
	 * @param x
	 * @param z
	 */
	public void addTree(double x, double z) {
		double y = altitude(x, z);
		Tree tree = new Tree(x, y, z);
		myTrees.add(tree);
	}

	/**
	 * Add a road.
	 *
	 * @param x
	 * @param z
	 */
	public void addRoad(double width, double[] spine) {
		Road road = new Road(width, spine);
		myRoads.add(road);
	}

	public void setLight(GL2 gl) {
		this.mySun.setLight(gl);
	}

	/**
	 * Called from Game.init(), to help initialise objects after shader program has been loaded. 
	 */
	public void terrainInit() {
		mySun = new Sun(mySunlight, shaderprogram, size().getWidth(), size().getHeight());
	}

	/**
	 * Start drawing the terrain and scene (not avatar)
	 */
	public void draw(GL2 gl, Texture[] tex) {

		setLight(gl);
		drawSun(gl,tex);

		/* Material properties for the terrain */
		float [] ad = {1.0f, 1.0f, 1.0f, 1.0f}; 
		float [] sp = {0.2f, 0.2f, 0.2f, 1.0f}; 
		float [] sh = {0f, 0f, 0f, 1.0f}; 
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, ad,0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, sp,0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, sh,0);

		drawTerrain(gl, tex);
		gl.glDisable(GL2.GL_CULL_FACE);
		drawRoads(gl,tex);
		gl.glEnable(GL2.GL_CULL_FACE);
		drawTrees(gl,tex);
		mySun.update();
	}

	/**
	 * Draw the 2 triangles and calculate their normals
	 */
	public void drawTerrain(GL2 gl, Texture[] tex) {

		gl.glPushMatrix();
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.GRASS].getTextureId());

		Dimension size = this.size();
		double height = size.getHeight();
		double width = size.getWidth();

		gl.glBegin(GL2.GL_TRIANGLES);

		for (double z = 0; z < (height-1); z+=1) {

			for (double x = 0; x < (width-1); x+=1) {
				double[] p1 = {x, altitude(x,z), z};
				double[] p2 = {x+1, altitude(x+1,z), z};
				double[] p3 = {x, altitude(x,z+1), z+1};
				double[] p4 = {x+1, altitude(x+1,z+1), z+1};

				double[] norm1 = Util.normalise(Util.getNormal(p1, p2, p3));
				double[] norm2 = Util.normalise(Util.getNormal(p2, p4 ,p3));

				gl.glNormal3dv(norm1,0);
				gl.glTexCoord2d(0, 0);
				gl.glVertex3dv(p1,0);

				gl.glTexCoord2d(0, 1);
				gl.glVertex3dv(p3,0);

				gl.glTexCoord2d(1, 0);
				gl.glVertex3dv(p2,0);
				// --------------------------------------------------------
				gl.glNormal3dv(norm2,0);
				gl.glTexCoord2d(0, 1);
				gl.glVertex3dv(p3,0);

				gl.glTexCoord2d(1, 1);
				gl.glVertex3dv(p4, 0);

				gl.glTexCoord2d(1, 0);
				gl.glVertex3dv(p2, 0);
			}
		}
		gl.glEnd();
		gl.glPopMatrix();
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
	}

	public void drawRoads(GL2 gl, Texture[] tex) {

		/* 
		 * Find the highest point to place the road on 
		 * given the terrain
		 */
		for (Road r : myRoads) {
			double maxAlt = 0;
			for (int i = 0; i < r.size(); i++) {
				double[] p = r.point(i);
				double curr = altitude(p[0], p[1]);
				maxAlt = Math.max(curr,  maxAlt);
			}
			r.setAltitude(maxAlt + 0.001);
		}
		/*
		 * Draw Roads
		 */
		for (Road r: myRoads) {
			r.drawSelf(gl, tex);
		}
	}

	public void drawTrees(GL2 gl, Texture[] tex) {
		for (Tree t : myTrees) {
			t.drawTree(gl, tex);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		}
	}

	public void drawSun(GL2 gl, Texture[] tex) {
		mySun.drawSun(gl, tex);
	}

	/*
	 * The functions below are used when key events are triggered
	 */
	public void incTreeDepth() {
		for (Tree t : myTrees)
			t.incDepth();
	}
	public void decTreeDepth() {
		for (Tree t : myTrees)
			t.decDepth();
	}

	public void switchAnimate() {
		mySun.switchAnimate();
	}

	public void switchNightMode() {
		mySun.switchNightMode();
	}


//	private void printArray(double[] arr) {
//		for (int i = 0; i < arr.length; i++) {
//			if (debug) System.out.print(arr[i] + ", ");
//		}
//		if (debug) System.out.println();
//	}

}