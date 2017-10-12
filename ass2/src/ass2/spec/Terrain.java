package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;



/**
 * COMMENT: Comment HeightMap
 *
 * @author malcolmr
 */
public class Terrain {

    private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlight;

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
        // the point can be: left or right of the hypotenuse or on a side of the grid --> 4 cases

        double leftX = Math.floor(x);
        double rightX = Math.ceil(x);
        double backZ = Math.floor(z); // neg. z direction (away from us. for the drawing above it is the up direction)
        double forwardZ = Math.ceil(z);
        double hypotenuse = (leftX + backZ) - z;

        if (x == (int)x) { // interpolate z axis
            altitude = linearInterpolationZ(z, backZ, forwardZ, x, x);

        } else if (z == (int)z) { // interpolate x axis
        		altitude = linearInterpolationX(x, leftX, rightX, z, z);

        } else if (x < hypotenuse) { // interpolate left triangle
            altitude = bilinearInterpolation(x, leftX, rightX, z, forwardZ, backZ, hypotenuse);

        } else { // interpolate right triangle
        		altitude = bilinearInterpolation(x, rightX, leftX, z, backZ, forwardZ, hypotenuse);

        }

        return altitude;
    }

    public double linearInterpolationZ(double z, double backZ, double forwardZ, double x, double p1) {
    		double part1 = ( (z - backZ) / (forwardZ - backZ) ) * getGridAltitude((int)x, (int)forwardZ) ;
    		double part2 = ( (forwardZ - z) / (forwardZ - backZ) ) * getGridAltitude((int)x, (int)backZ) ;

    		return  (part1 + part2);
    }

    public double linearInterpolationX(double x, double leftX, double rightX, double z, double p1) {
    		// needed extra input for bilinear interpolation p1
    		// p1 = z when doing linear interpolation, p1 = leftX or rightX when doing bilinear interpolation
		double part1 = ( (x - leftX) / (rightX - leftX) ) * getGridAltitude((int)rightX, (int)p1) ;
		double part2 = ( (rightX - x) / (rightX - leftX) ) * getGridAltitude((int)leftX, (int)z) ;

		return  (part1 + part2);
}

    public double bilinearInterpolation(double x, double x1, double x2, double z, double z1, double z2, double hyp) {
    		double part1 = ( (x - x1) / (hyp - x1) ) * linearInterpolationZ(z, z1, z2, x1, x2);
		double part2 = ( (hyp - x) / (hyp - x1) ) * linearInterpolationZ(z, z1, z1, x1, x1);

    		return (part1 + part2);
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

 // ================= OUR CODE ================= //

    public void draw(GL2 gl) {
//		GL2 gl = drawable.getGL().getGL2();
    	drawTerrain(gl);
    	gl.glPolygonMode(GL.GL_FRONT_AND_BACK,GL2.GL_FILL);


    }

    public void drawTerrain(GL2 gl) {
    	gl.glPushMatrix();

    	gl.glColor4d(0, 1, 1, 1);
//    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);

    	Dimension size = this.size();
    	double height = size.getHeight();
    	double width = size.getWidth();
//    	int count = 0;

    	for (int z = 0; z < (height-1); z++) {
			gl.glBegin(GL2.GL_TRIANGLE_STRIP);

    		for (int x = 0; x < (width-1); x++) {
    			double[] p1 = {x, getGridAltitude(x,z), z};
    			double[] p2 = {x+1, getGridAltitude(x+1,z), z};
    			double[] p3 = {x, getGridAltitude(x,z+1), z+1};
    			double[] p4 = {x+1, getGridAltitude(x+1,z+1), z+1};

    			double[] norm1 = Util.getNormal(p1, p2, p3);
    			double[] norm2 = Util.getNormal(p2, p4 ,p3);

//    			gl.glNormal3dv(norm1,0);
    			gl.glVertex3dv(p1,0);
    			gl.glVertex3dv(p2,0);
    			gl.glVertex3dv(p3,0);
//    			gl.glNormal3dv(norm2,0);
    			gl.glVertex3dv(p4,0);

//    			System.out.println("#####");
//    			printArray(p1);
//    			printArray(p2);
//    			printArray(p3);
//    			printArray(p4);
//    			System.out.println("#####");


//    			if (x == (width -2)){
//    				gl.glVertex3dv(p2,0);
//    			}




//    			System.out.println("??????????");
//
//    			System.out.println("x: " + x + ", " +
//    							   "z: " + z + ", " +
//    							   "x+1: " + (x+1) + ", "+
//    								"z+1: " + (z+1) + ", " +
//    								"##: " + getGridAltitude(x,z) + ", " +
//    								"##: " + getGridAltitude(x+1,z) + ", " +
//    								"##: " + getGridAltitude(x,z+1) + ", " +
//    								"##: " + getGridAltitude(x+1,z+1) + ", ");
//    			System.out.println("??????????");
    		}
			gl.glEnd();

    	}


    	gl.glPopMatrix();

    }

    private void printArray(double[] arr) {
	    	for (int i = 0; i < arr.length; i++) {
	    		System.out.print(arr[i] + ", ");
	    	}
	    	System.out.println();
    }

// =================Gladys' Attempt does not work =( ================= //
//    /**
//     * Draws the terrain.
//     * @param gl
//     */
//    public void draw(GL2 gl) {
//
//    		gl.glPushMatrix();
//        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
//
//        	Dimension size = this.size();
//        	double height = size.getHeight();
//        	double width = size.getWidth();
//
//        	for (int z = 0; z < (height-1); ++z) {
//        		for (int x = 0; x < (width-1); ++x) {
//        			// Draw 2 triangles to make the mesh
//        			double[] p1 = {x, getGridAltitude(x, z),  z};
//        			double[] p2 = {x, getGridAltitude(x, z+1), z+1};
//        			double[] p3 = {x+1, getGridAltitude(x+1, z), z};
//
//        			double[] norm1 = Util.getNormal(p1, p2, p3);
//        			gl.glNormal3dv(norm1,0);
//
//        			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
//        			{
//        				gl.glColor4d(0, 1, 1, 1);
//	        			gl.glVertex3dv(p1,0);
//	        			gl.glVertex3dv(p2,0);
//	        			gl.glVertex3dv(p3,0);
//        			}
//        			gl.glEnd();
//
//        			double[] p4 = {x + 1, getGridAltitude(x+1, z), z};
//        			double[] p5 = {x, getGridAltitude(x, z+1), z+1};
//        			double[] p6 = {x+1, getGridAltitude(x+1, z+1), z+1};
//
//        			double[] norm2 = Util.getNormal(p4, p5, p6);
//        			gl.glNormal3dv(norm2,0);
//
//        			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
//        			{
//        				gl.glColor4d(0, 1, 1, 1);
//	        			gl.glVertex3dv(p4,0);
//	        			gl.glVertex3dv(p5,0);
//	        			gl.glVertex3dv(p6,0);
//        			}
//        			gl.glEnd();
//        		}
//        	}
//        	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
//        	gl.glPopMatrix();
//
//    }


}
