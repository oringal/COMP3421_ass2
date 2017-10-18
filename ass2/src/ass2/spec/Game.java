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


/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, KeyListener{
	
	/* Debugger mode */
	public final static boolean debug = true;


	private Terrain myTerrain;
	private Texture textures[];
	
	private Camera camera;
	private Avatar avatar;
	
	private static boolean leftKey;
	private static boolean rightKey;
	private static boolean upKey;
	private static boolean downKey;
	private static boolean firstPerson;
	
	public final static int GRASS = 0;
	public final static int ROAD = 1;
	public final static int AVATARBODY = 2;
	public final static int AVATARHEAD = 3;
	public final static int BRANCH = 4;
	public final static int LEAF = 5;





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
		GLJPanel panel = new GLJPanel(caps); // put caps as an input to GLJPanel
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
		
		// setup the projection matrix with the aspect ratio
		camera.projectionSetup(gl);
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		myTerrain.setLight(gl, 0);
		avatar.draw(gl, textures);		
		// setup the projection matrix with the aspect ratio
		//camera.projectionSetup(gl);
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
		
		String branchFile = "textures/branch.png";
		String branchExt = "png";
		
		String leafFile = "textures/leaf.png";
		String leafExt = "png";
				
		textures[GRASS] = new Texture(gl,grassFile,grassExt,true);
		textures[ROAD] = new Texture(gl,roadFile,roadExt,true);
		textures[AVATARHEAD] = new Texture(gl, avatarHeadFile, avatarHeadExt, true);
		textures[AVATARBODY] = new Texture(gl, avatarBodyFile, avatarBodyExt, true);
		textures[BRANCH] = new Texture(gl, branchFile, branchExt, true);
		textures[LEAF] = new Texture(gl, leafFile, leafExt, true);


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
		
		camera.setAspectRatio((float) width / (float) height);

	}
	
	//========OWN CODE========//
	
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
		if (firstPerson) {
			//System.out.print("firstPerson");
			//avatar.firstPerson()
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

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
