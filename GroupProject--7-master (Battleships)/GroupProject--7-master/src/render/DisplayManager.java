package render;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import audio.AudioMaster;

public abstract class DisplayManager {

	private static final int FPS_CAP = 120;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 960;

	private boolean running = true;

	protected DisplayMode displayMode;
	

	public final void run() {

		try {
			init();
			while (running) {
				
				checkInput();
				updateScene();
				
				render3D();
				render2D();
				
				Display.update();
				Display.sync(FPS_CAP);
			}

			cleanUp();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void init() throws Exception {
		createWindow();
		AudioMaster.init();
		AudioMaster.setListenerData();
		initScene();
	}

	private void createWindow() throws Exception {
		
		Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
		Display.setTitle("Battleships");
		Display.create();
		
	}

	private void checkInput() {
		
		if(Display.isCloseRequested()) {
			running = false;
		}
		
		checkSceneInput();
	}
	
	private void render3D() {
		ready3D();
		draw3D();
	}
	
	private void ready3D() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		
		GL11.glLoadIdentity();
		GLU.gluPerspective(45, (float) WIDTH / HEIGHT, 0.1f, 200);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GLU.gluLookAt(-25, 15, 20, 0, 0, 0, 0, 1, 0);
		
		GL11.glClearColor(0, 0, 0, 0);
		GL11.glClearDepth(1.0f);
		
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void render2D() {
		ready2D();
		draw2D();
	}
	
	private void ready2D() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		GLU.gluOrtho2D(0.0f, WIDTH, HEIGHT, 0.0f);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_NORMALIZE);
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	private void cleanUp() {
		cleanUpScene();
		
		Display.destroy();
		AudioMaster.cleanUp();	
	}

	protected abstract void initScene() throws Exception;

	protected abstract void checkSceneInput();

	protected abstract void updateScene();
	
	protected abstract void cleanUpScene();
	
	protected abstract void draw2D();
	
	protected abstract void draw3D();

	protected final Texture loadTexture(String path) throws Exception {
		Texture tex = TextureLoader.getTexture("BMP", ResourceLoader.getResourceAsStream(path), true);
		return tex;
	}

	protected final Texture loadTexture(String path, String imageType) throws Exception {
		Texture tex = TextureLoader.getTexture(imageType, ResourceLoader.getResourceAsStream(path), true);
		return tex;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
}
