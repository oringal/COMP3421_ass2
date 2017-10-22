package ass2.spec;
/** 
 * Some math utility functions
 * Taken from Week 4 Lecture Code
 * 
 */
public class Util {

	/**
	 * Takes two vectors and carries out the cross product.
	 * @param u
	 * @param v
	 * @return
	 */
	public static double [] cross(double u [], double v[]){
		double crossProduct[] = new double[3];
		crossProduct[0] = u[1]*v[2] - u[2]*v[1];
		crossProduct[1] = u[2]*v[0] - u[0]*v[2];
		crossProduct[2] = u[0]*v[1] - u[1]*v[0];
		return crossProduct;
	}

	/**
	 * Calculates the normal of the three points
	 * @param p0
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double[] getNormal(double[] p0, double[] p1, double[] p2){
		double u[] = {p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2]};
		double v[] = {p2[0] - p0[0], p2[1] - p0[1], p2[2] - p0[2]};

		return cross(u,v);	
	}

	/**
	 * Calculates the magnitude of a vector
	 * @param n
	 * @return
	 */
	public static double getMagnitude(double [] n){
		double mag = n[0]*n[0] + n[1]*n[1] + n[2]*n[2];
		mag = Math.sqrt(mag);
		return mag;
	}

	/**
	 * Normalises the vector
	 * @param n
	 * @return
	 */
	public static double [] normalise(double [] n){
		double  mag = getMagnitude(n);
		double norm[] = {n[0]/mag,n[1]/mag,n[2]/mag};
		return norm;
	}

	/**
	 * Scales a vector
	 * @param v
	 * @param s
	 * @return
	 */
	public static double[] scaleVector(double[] v, double s) {
		v[0] *= s;
		v[1] *= s;
		return v;
	}
}
