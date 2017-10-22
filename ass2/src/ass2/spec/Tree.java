package ass2.spec;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

/**
 * Tree class generates tree objects using the L system.
 * @author Antheny and Gladys
 */
public class Tree {

	private double[] myPos;

	private double angle;
	private static String F = "FF>[>Fl<F<Fl][-Fl-F+Fl]<[<Fl>F>Fl][+Fl+Fl--Fl]";
	private static ArrayList<String> lsystem;
	private int depth;
	private double branchSize;
	private double branchDelta;
	private double height;


	public Tree(double x, double y, double z) {
		myPos = new double[3];
		myPos[0] = x;
		myPos[1] = y;
		myPos[2] = z;

		angle = 25;
		depth = 1;
		branchSize = 10;
		branchDelta = 3;
		height = 0.1;
		lsystem = new ArrayList<String>();
		lsystem.add(F);
	}


	public double[] getPosition() {
		return myPos;
	}

	public double getDepth() {
		return depth;
	}

	public void setDepth(int d) {
		if (depth < 0) depth = 0;
		else depth = d;
	}

	public void incDepth() {
		depth++;
	}

	public void decDepth() {
		if (depth > 0) depth--;
	}

	/**
	 * Creates an L tree system given a recursion depth
	 * Calculates iteratively and stores each depth in the 
	 * lsystem array for increased performance
	 * @param depth
	 * @return
	 */
	public String rewrite(int depth) {

		int size = lsystem.size() - 1;
		String old = lsystem.get(size);

		for (; size < depth; size++) {
			String newsystem = "";
			for (int i=0; i<old.length(); i++) {
				if (old.charAt(i) == 'F') {
					newsystem += F;
				} else {
					newsystem += old.charAt(i);
				}
			}
			old = newsystem;
			lsystem.add(old);
		}

		return old;
	}

	public void drawTree(GL2 gl, Texture[] tex) {

		String draw = "";
		if (depth <= 0) {
			draw = lsystem.get(0);
		} else if (lsystem.size() <= depth) {
			draw = rewrite(depth);
		} else {
			draw = lsystem.get(depth);
		}
		// Manually add length to the trunk
		draw = "FFFFF" + draw;

		// Draw   
		gl.glDisable(GL2.GL_CULL_FACE);
		gl.glPushMatrix();
		gl.glTranslated(myPos[0], myPos[1], myPos[2]);
		{
			gl.glPushMatrix();
			for (int i=0; i<draw.length(); i++) {
				drawChar(gl, tex, draw.charAt(i));
			}
			gl.glPopMatrix();
		}
		gl.glPopMatrix();
		gl.glEnable(GL2.GL_CULL_FACE);
	}

	public void drawChar(GL2 gl, Texture[] tex, char c) {

		if (c == 'F') {
			// branch texture
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.BRANCH].getTextureId()); 
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

			// draw branch
			gl.glLineWidth((float)branchSize);
			gl.glBegin(GL2.GL_LINES);
			gl.glTexCoord2d(0, 0);
			gl.glVertex3f(0, 0, 0);
			gl.glTexCoord2d(0, 0.15);
			gl.glVertex3d(0, height, 0);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
			gl.glEnd();
			gl.glTranslated(0, height, 0);
		} else if (c == 'l') { 
			double half = height / 2;
			// leaf texture
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.LEAF].getTextureId()); 
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

			// draw leaf
			gl.glPushMatrix();
			gl.glScaled(half, half, half);
			gl.glRotated(45, 1, 0, 0);
			{
				gl.glBegin(GL2.GL_TRIANGLE_FAN);
				gl.glTexCoord2d(0.5, 0);
				gl.glVertex3d(0, 0, 0);

				gl.glTexCoord2d(1, 0.3);
				gl.glVertex3d(1, 0.8, 0.5);

				gl.glTexCoord2d(1, 0.63);
				gl.glVertex3d(1, 1.9, 0.5);

				gl.glTexCoord2d(0.5, 1);
				gl.glVertex3d(0, 3.2, 0);

				gl.glTexCoord2d(0, 0.63);
				gl.glVertex3d(-1, 1.8, 0.5);

				gl.glTexCoord2d(0, 0.3);
				gl.glVertex3d(-1, 0.9, 0.5);

				gl.glEnd();
			}
			gl.glPopMatrix();
			gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		} else if (c == '[') {
			gl.glPushMatrix();
			if (branchSize > 0) {
				branchSize -= branchDelta;
			}
		} else if (c == ']') {
			gl.glPopMatrix();
			branchSize += branchDelta;
		} else if (c == '+') {
			gl.glRotated(angle, 1, 0, 0);
		} else if (c == '-') {
			gl.glRotated(-angle, 1, 0, 0);
		} else if (c == '<') {
			gl.glRotated(angle, 0, 0, 1);
		} else if (c == '>') {
			gl.glRotated(-angle, 0, 0, 1);
		}
	}


}
