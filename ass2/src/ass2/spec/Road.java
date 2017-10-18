package ass2.spec;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {

	private List<Double> myPoints;
	private double myWidth;
	private double altitude;

	/** 
	 * Create a new road starting at the specified point
	 */
	public Road(double width, double x0, double y0) {
		myWidth = width;
		myPoints = new ArrayList<Double>();
		myPoints.add(x0);
		myPoints.add(y0);
		altitude = 1;
	}

	/**
	 * Create a new road with the specified spine 
	 *
	 * @param width
	 * @param spine
	 */
	public Road(double width, double[] spine) {
		myWidth = width;
		myPoints = new ArrayList<Double>();
		for (int i = 0; i < spine.length; i++) {
			myPoints.add(spine[i]);
		}
	}

	/**
	 * The width of the road.
	 * 
	 * @return
	 */
	public double width() {
		return myWidth;
	}

	/**
	 * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
	 * (x1, y1) and (x2, y2) are interpolated as bezier control points.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 */
	public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
		myPoints.add(x1);
		myPoints.add(y1);
		myPoints.add(x2);
		myPoints.add(y2);
		myPoints.add(x3);
		myPoints.add(y3);        
	}

	/**
	 * Get the number of segments in the curve
	 * 
	 * @return
	 */
	public int size() {
		return myPoints.size() / 6;
	}

	/**
	 * Get the specified control point.
	 * 
	 * @param i
	 * @return
	 */
	public double[] controlPoint(int i) {
		double[] p = new double[2];
		p[0] = myPoints.get(i*2);
		p[1] = myPoints.get(i*2+1);
		return p;
	}

	/**
	 * Get a point on the spine. The parameter t may vary from 0 to size().
	 * Points on the kth segment take have parameters in the range (k, k+1).
	 * 
	 * @param t
	 * @return
	 */
	public double[] point(double t) {
		int i = (int)Math.floor(t);
		t = t - i;

		i *= 6;

		double x0 = myPoints.get(i++);
		double y0 = myPoints.get(i++);
		double x1 = myPoints.get(i++);
		double y1 = myPoints.get(i++);
		double x2 = myPoints.get(i++);
		double y2 = myPoints.get(i++);
		double x3 = myPoints.get(i++);
		double y3 = myPoints.get(i++);

		double[] p = new double[2];

		p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
		p[1] = b(0, t) * y0 + b(1, t) * y1 + b(2, t) * y2 + b(3, t) * y3;        

		return p;
	}
	
	/**
	 * @param a - the altitude
	 */
	public void setAltitude(double a) {
		altitude = a;
	}
	
	public double alt() {
		return altitude;
	}

	/**
	 * Calculate the Bezier coefficients
	 * 
	 * @param i
	 * @param t
	 * @return
	 */
	private double b(int i, double t) {

		switch(i) {

		case 0:
			return (1-t) * (1-t) * (1-t);

		case 1:
			return 3 * (1-t) * (1-t) * t;

		case 2:
			return 3 * (1-t) * t * t;

		case 3:
			return t * t * t;
		}

		// this should never happen
		throw new IllegalArgumentException("" + i);
	}
	
	public void drawSelf(GL2 gl, Texture[] tex) {
		double halfWidth = myWidth/2;
		
		gl.glPushMatrix();
		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.ROAD].getTextureId());

        gl.glBegin(GL2.GL_TRIANGLE_STRIP);
        double[] upVec = new double[] {0,1,0};
        gl.glNormal3dv(upVec,0);
        
        int c = 0;
        
//        if (Game.debug) System.out.println("Altitude: " + altitude);
        
        for (double i = 0; i < size() - 0.02; i+=0.02) {
        	double[] currP = point(i);
        	double[] nextP = point(i+0.02);
        	
        	double[] vec = new double[] {nextP[0] - currP[0], 0, nextP[1] - currP[1]};
        	
        	double[] normal = Util.normalise(Util.cross(vec, upVec));
        	normal = Util.scaleVector(normal, halfWidth);
        	
        	if (c%2==0) {
            	gl.glTexCoord2d(0, 0);
                gl.glVertex3d(currP[0] - normal[0], altitude, currP[1] - normal[2]);
                
                gl.glTexCoord2d(0, 1);
                gl.glVertex3d(currP[0] + normal[0], altitude, currP[1] + normal[2]);
        	} else {
                gl.glTexCoord2d(0.1, 0);
                gl.glVertex3d(currP[0] - normal[0], altitude, currP[1] - normal[2]);
                gl.glTexCoord2d(0.1, 1);
                gl.glVertex3d(currP[0] + normal[0], altitude, currP[1] + normal[2]);
        	}
        	c++;

        }
        gl.glEnd();
        gl.glPopMatrix();
	}


}
