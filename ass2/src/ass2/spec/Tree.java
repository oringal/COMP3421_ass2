package ass2.spec;

import com.jogamp.opengl.GL2;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree {

	private double[] myPos;
	
	private double angle;
	private static String F = "FF>[>Fl<F<Fl][-Fl-F+Fl]<[<Fl>F>Fl][+Fl+F-Fl]";
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
		depth = 2;
		branchSize = 6;
		branchDelta = 2;
		height = 0.1;
	}
	

	public double[] getPosition() {
		return myPos;
	}
	
	public String rewrite(String s, int depth) {
		
		String old = s;
		for (int d = 0; d < depth; d++) {
			String newsystem = "";
			for (int i=0; i<s.length(); i++) {
				if (s.charAt(i) == 'F') {
					newsystem += F;
				} else {
					newsystem += old.charAt(i);
				}
			}
			old = newsystem;
		}
		
		return old;
	}
	
	public void drawTree(GL2 gl, Texture[] tex) {
		
		String draw = rewrite(F, depth);
		if (Game.debug) System.out.println("the lsystem: " + draw);
		
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
//            gl.glTexCoord2d(0, 0);
            gl.glVertex3f(0, 0, 0);
//            gl.glTexCoord2d(0, 0.1);
            gl.glVertex3d(0, height, 0);
            gl.glEnd();
            
            gl.glTranslated(0, height, 0);
        } else if (c == 'l') { 
            double half = height / 2;
            // leaf texture
            gl.glBindTexture(GL2.GL_TEXTURE_2D, tex[Game.LEAF].getTextureId()); 
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
//            float[] matAmbandDif = {0.21f, 0.75f, 0.10f, 1f};
//            gl.glMaterialfv(GL2.GL_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbandDif, 0);
            
            // draw leaf
            gl.glPushMatrix();
            gl.glScaled(half, half, half);
            gl.glRotated(45, 1, 0, 0);
            {
                gl.glBegin(GL2.GL_TRIANGLE_FAN);
                gl.glTexCoord2d(0.5, 0);
                gl.glVertex3d(0, 0, 0);
                gl.glTexCoord2d(1, 0.3);
                gl.glVertex3d(1, 0.9, 0.5);
                gl.glTexCoord2d(1, 0.63);
                gl.glVertex3d(1, 1.9, 0.5);
                gl.glTexCoord2d(0.5, 1);
                gl.glVertex3d(0, 3, 0);
                gl.glTexCoord2d(0, 0.63);
                gl.glVertex3d(-1, 1.9, 0.5);
                gl.glTexCoord2d(0, 0.3);
                gl.glVertex3d(-1, 0.9, 0.5);
                gl.glEnd();
            }
            gl.glPopMatrix();
            
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
        } else if (c == '^') {
            gl.glRotated(angle, 0, 1, 0);
        } else if (c == 'v') {
            gl.glRotated(-angle, 0, 1, 0);
        } else if (c == '<') {
            gl.glRotated(angle, 0, 0, 1);
        } else if (c == '>') {
            gl.glRotated(-angle, 0, 0, 1);
        }
	}


}
