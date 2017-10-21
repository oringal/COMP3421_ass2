package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;

import java.nio.file.Paths;


/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, KeyListener{
	
	/* Debugger mode */
	public final static boolean debug = false;


	private Terrain myTerrain;
	private Texture textures[];
	
	private Camera camera;
	private Avatar avatar;
	private int shaderProgram;
	
	private static boolean leftKey;
	private static boolean rightKey;
	private static boolean upKey;
	private static boolean downKey;
	private static boolean firstPerson;
	
	public final static int GRASS = 0;
	public final static int ROAD = 1;
	public final static int AVATARBODY = 2;
	public final static int AVATARHEAD = 3;
	public final static int AVATARFEET = 4;
	public final static int BRANCH = 5;
	public final static int LEAF = 6;
	public final static int AVATARRLEG = 7;
	public final static int AVATARLLEG = 8;





	public Game(Terrain terrain) {
		super("Assignment 2");
		myTerrain = terrain;
		textures = new Texture[10];
		camera = new Camera(myTerrain);
		avatar = new Avatar(camera);
		
		leftKey = false;
		rightKey = false;
		upKey = false;
		downKey = false;
	}

	/** 
	 * Run the game.
	 *
	 */
	public void run() {
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLJPanel panel = new GLJPanel(caps); 
		panel.addGLEventListener(this);

		// Add an animator to call 'display' at 60fps        
		FPSAnimator animator = new FPSAnimator(60);
		animator.add(panel);
		animator.start();

		getContentPane().add(panel);
		setSize(800, 600);        
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);     
		
		Game obj = new Game(myTerrain);
		panel.addKeyListener(obj);
	}

	/**
	 * Load a level file and display it.
	 * 
	 * @param args - The first argument is a level file in JSON format
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Terrain terrain = LevelIO.load(new File(args[0]));
		Game game = new Game(terrain);
		game.run();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glUseProgram(shaderProgram);
				
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		myTerrain.setLight(gl, 0);
		gl.glEnable(GL2.GL_LIGHT0);

		//double alt = myTerrain.altitude(camera.getPosition()[0], camera.getPosition()[2]);

		avatar.draw(gl, textures);
		
		myTerrain.draw(gl,textures);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK,GL2.GL_FILL);

		keyControls();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		/* Enable Lighting */
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0); 

		gl.glEnable(GL2.GL_NORMALIZE);

		/* Cull Back Faces */
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glCullFace(GL2.GL_BACK);

		/* Turn on 2d Textures */
		gl.glEnable(GL2.GL_TEXTURE_2D);
		loadTextures(gl);

		String path = Paths.get(".").toAbsolutePath().normalize().toString();
		String vs = path + "/ass2/src/ass2/spec/PhongVertex.glsl";
		String fs = path + "/ass2/src/ass2/spec/PhongFragment.glsl";
		try {
			shaderProgram = Shader.initShaders(gl, vs, fs);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}
	
	private void loadTextures(GL2 gl) {
		String grassFile = "textures/grass.png";
		String grassExt = "png";
		
		String roadFile = "textures/road.png";
		String roadExt = "png";

		String avatarHeadFile = "textures/minionHead.png";
		String avatarHeadExt = "png";
		
		String avatarBodyFile = "textures/minionBody.png";
		String avatarBodyExt = "png";
		
		String avatarFeetFile = "textures/minionFeet.png";
		String avatarFeetExt = "png";
		
		String avatarRLegFile = "textures/minionRLeg.png";
		String avatarRLegExt = "png";
		
		String avatarLLegFile = "textures/minionLLeg.png";
		String avatarLLegExt = "png";
		
		String branchFile = "textures/branch.png";
		String branchExt = "png";
		
		String leafFile = "textures/leaf.png";
		String leafExt = "png";
				
		textures[GRASS] = new Texture(gl,grassFile,grassExt,true);
		textures[ROAD] = new Texture(gl,roadFile,roadExt,true);
		textures[AVATARHEAD] = new Texture(gl, avatarHeadFile, avatarHeadExt, true);
		textures[AVATARBODY] = new Texture(gl, avatarBodyFile, avatarBodyExt, true);
		textures[AVATARFEET] = new Texture(gl, avatarFeetFile, avatarFeetExt, true);

		textures[BRANCH] = new Texture(gl, branchFile, branchExt, true);
		textures[LEAF] = new Texture(gl, leafFile, leafExt, true);
		
// bug: Caught IndexOutOfBoundsException: Required 7694 remaining bytes in buffer, only had 7548 on thread AWT-EventQueue-0
//		textures[AVATARRLEG] = new Texture(gl, avatarRLegFile, avatarRLegExt, true);
//		textures[AVATARLLEG] = new Texture(gl, avatarLLegFile, avatarLLegExt, true);

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
		GLU glu = new GLU();

		if (height == 0) {
			height = 1;
		}

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(60, width/height, 1, 20);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
		
	private void keyControls() {
		
		if (leftKey) {
			//System.out.print("left");
			avatar.turnLeft();
		}
		if (rightKey) {
			//System.out.print("right");
			avatar.turnRight();
		}
		if (upKey) {
			//System.out.print("up");
			avatar.moveForward();
		}
		if (downKey) {
			//System.out.print("down");
			avatar.moveBackward();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
	
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightKey = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftKey = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			upKey = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			downKey = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_F) {
			firstPerson = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_F) {
			camera.toggleFirstPerson();
		}
		if(e.getKeyCode() == KeyEvent.VK_T) {
			if (e.isShiftDown()) myTerrain.decTreeDepth();
			else myTerrain.incTreeDepth();
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftKey = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightKey = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			upKey = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			downKey = false;
		}	
		if (e.getKeyCode() == KeyEvent.VK_F) {
			firstPerson = false;
		}
	}
}
