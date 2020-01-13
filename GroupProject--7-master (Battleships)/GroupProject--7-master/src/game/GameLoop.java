package game;

import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import audio.AudioMaster;
import audio.Source;
import entities.GridSquare;
import entities.MenuButton;
import entities.Missile;
import entities.Water;
import player.Player;
import render.DisplayManager;
import render.FloatBuffer;
import shapes.UnitCube;
import ships.Position;
import ships.Ship;

public class GameLoop extends DisplayManager {

	private static float f = 0.0f;

	// Call list ids

	private final int destroyerList = 2;
	private final int cruiser1List = 3;
	private final int cruiser2List = 4;
	private final int battleshipList = 5;
	private final int carrierList = 6;
	private final int playerGridList = 7;
	private final int enemyGridList = 8;
	private final int menuBgList = 9;
	private final int missileList = 10;

	// Missile controls

	private int missileSpin;
	private float missileRotate;
	private double missileX;
	private double missileY;
	private double missileZ;
	private boolean missileLaunch;
	private double t;

	// Water object and smoothness control

	private Water water;
	private int stepCount;

	// Menu button IDs

	private final int singleplayer = 100;
	private final int howToPlay = 102;
	private final int exit = 103;
	private final int settings = 104;
	private final int back = 105;
	private final int playAgain = 106;

	private Texture menuBackground;
	private Texture skyBackground;

	private boolean placeVertical = false;

	private Source source;
	private int buffer;

	// how to play text
	private TrueTypeFont font;
	private Font awtFont;
	private TrueTypeFont font2;
	private Font awtFont2;

	private State state = State.MENU;

	private List<MenuButton> mainMenuButtons = new ArrayList<>(16);
	private List<MenuButton> playAgainButton = new ArrayList<>(1);
	private List<MenuButton> backButtons = new ArrayList<>(1);
	private GridSquare[][] preGameArray = new GridSquare[10][10];
	private GridSquare[][] inGameArray = new GridSquare[10][10];

	private Player player;
	private Player computer;
	private boolean playersTurn;
	private boolean playerWin;

	private enum State {
		PRE_GAME, GAME, MENU, HOW_TO_PLAY, END_GAME;
	}

	public static void main(String[] args) {
		new GameLoop().run();
	}

	@Override
	protected void initScene() throws Exception {

		/////////////////// Missile ////////////////////////
		missileRotate = 270.0f;
		missileSpin = 0;
		missileX = -0.8f;
		missileY = 23.6f;
		missileZ = -4.5f;
		missileLaunch = false;
		t = 0;

		/////////////////// Water ////////////////////////
		water = new Water(140, 135, 1.0f, 2.0f);
		stepCount = 0;

		/////////////////// Audio ////////////////////////
		buffer = AudioMaster.loadSound(new File("res/sounds/hover.wav"));
		source = new Source();

		////////////////// Load Textures /////////////////

		menuBackground = TextureLoader.getTexture("JPG",
				ResourceLoader.getResourceAsStream("res/textures/battleships.jpg"));

		skyBackground = TextureLoader.getTexture("JPG",
				ResourceLoader.getResourceAsStream("res/textures/skyBackground.jpg"));
		////////////// End game screen buttons //////////////

		playAgainButton.add(new MenuButton(170, 520, playAgain, "Play Again"));
		playAgainButton.add(new MenuButton(400, 520, back, "Main Menu"));

		////////////// How to play screen buttons //////////////

		backButtons.add(new MenuButton(170, 520, back, "Back"));

		////////////// Add Menu Buttons //////////////

		mainMenuButtons.add(new MenuButton(100, 150, singleplayer, "Play Game"));
		mainMenuButtons.add(new MenuButton(100, 200, howToPlay, "How To Play"));
		// mainMenuButtons.add(new MenuButton(100, 200, settings, "Settings"));
		mainMenuButtons.add(new MenuButton(100, 250, exit, "Exit"));

		new MenuButton(100, 100, back, "Play Game");

		////////// Lighting set up /////////////
		float globalAmbient[] = { 0.2f, 0.2f, 0.2f, 1.0f };
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, FloatBuffer.wrap(globalAmbient));

		float diffuse0[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float ambient0[] = { 0.3f, 0.3f, 0.3f, 1.0f };
		float position0[] = { 0.0f, 2000.0f, 2000.0f, 1.0f };

		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, FloatBuffer.wrap(ambient0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, FloatBuffer.wrap(diffuse0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, FloatBuffer.wrap(position0));

		createCallLists();
	}

	private void createCallLists() {

		////////////////////// MENU CALL LISTS ///////////////////////

		// Menu background call list
		GL11.glNewList(menuBgList, GL11.GL_COMPILE);
		{
			drawMenuBackground();
		}
		GL11.glEndList();
	}

	@Override
	protected void checkSceneInput() {
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			resetAnimation();
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
			state = State.MENU;
		}

		// to delete after full implementation - for test firing missile
		if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			missileLaunch = true;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			placeVertical = true;
		} else {
			placeVertical = false;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			state = State.END_GAME;

		}
	}

	private void resetAnimation() {
		missileLaunch = false;
		missileRotate = 270;
		missileX = -0.8f;
		missileY = 23.6f;
		missileZ = -4.5f;
		t = 0;
	}

	@Override
	protected void cleanUpScene() {
		source.delete();
	}

	@Override
	protected void updateScene() {
		float increment = 0.005f;
		if (missileLaunch) {
			missileSpin += 2;

			if (t >= 0.00 && t <= 1.00)
				t += increment;
			// startX,Y,Z, midpoint, endX,Y,Z
			missileX = bezierPosition(-0.8, -3.4, -6.0, t); // depth
			missileY = bezierPosition(23.6, 14.6, 5.6, t); // left/right
			missileZ = bezierPosition(-4.5, -25.5, -4.5, t); // elevation

			if (missileRotate >= 90)
				missileRotate -= 180 / (1 / increment);
		}

		int steps = 200; // higher number = smoother water
		if (stepCount == steps) {
			water.generateNextWaterStep();
			stepCount = 0;
		}
		water.incrementalStep(steps);
		stepCount++;
	}

	private void drawGameScene() {
		GL11.glPushMatrix();
		{

			GL11.glRotatef(90.0f, 1, 0, 0);
			GL11.glTranslatef(0, 7.5f, 0);
			GL11.glPushMatrix();
			{
				player.drawGrid();
			}
			GL11.glPopMatrix();
			GL11.glTranslatef(0, -13f, 0);
			GL11.glPushMatrix();
			{
				computer.drawGrid();
			}
			GL11.glPopMatrix();

			// build ship 2
			GL11.glPushMatrix();
			{
				GL11.glCallList(destroyerList);
			}
			GL11.glPopMatrix();
			// build ship 3
			GL11.glPushMatrix();
			{
				GL11.glCallList(cruiser1List);
			}
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			{
				GL11.glCallList(cruiser2List);
			}
			GL11.glPopMatrix();
			// build ship 4
			GL11.glPushMatrix();
			{
				GL11.glCallList(battleshipList);
			}
			GL11.glPopMatrix();
			// build ship 5
			GL11.glPushMatrix();
			{
				GL11.glCallList(carrierList);
			}
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			{
				GL11.glTranslated(missileX, missileY, missileZ);
				GL11.glRotatef(missileRotate, 1, 0, 0);
				// to control the missile spin
				GL11.glRotatef(missileSpin, 0, 1, 0);
				renderMissile();
			}
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			{
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_CULL_FACE);

				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, skyBackground.getTextureID());
				GL11.glColor3f(1.0f, 1.0f, 1.0f);
				GL11.glTranslatef(25.0f, -115.0f, 2.0f);
				GL11.glScalef(1.0f, 0.17f, 0.02f);
				GL11.glRotatef(90, 0, 1, 0);
				GL11.glRotatef(8, 1, 0, 0);
				GL11.glCallList(menuBgList);

				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_CULL_FACE);
			}
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		{
			GL11.glTranslatef(-30.8f, -0.2f, -105.0f);
			GL11.glRotatef(90.0f, 1, 0, 0);
			water.displayWater();
		}
		GL11.glPopMatrix();

	}

	private void renderPreGame() {

		GL11.glPushMatrix();
		{
			int offsetX = 200;
			int offsetY = 150;

			Ship activeShip = null;
			int activeShipIndex = 0;

			float r = 0.0f;
			float g = 0.0f;
			float b = 1.0f;

			for (int s = 0; s < player.getShips().size(); s++) {
				if (player.getShips().get(s).getIsActive()) {
					activeShip = player.getShips().get(s);
					activeShipIndex = s;
					break;
				}
			}

			for (int i = 0; i < preGameArray.length; i++) {
				for (int j = 0; j < preGameArray[i].length; j++) {
					preGameArray[i][j].setColoured(false);
					preGameArray[i][j].setXandY(60 * i + offsetX, 60 * j + offsetY);
				}
			}

			if (activeShip != null) {

				for (int i = 0; i < preGameArray.length; i++) {
					for (int j = 0; j < preGameArray[i].length; j++) {

						if (!preGameArray[i][j].isColoured() && !preGameArray[i][j].isPlaced()) {
							if (preGameArray[i][j].inBounds(Mouse.getX(), HEIGHT - Mouse.getY())) {

								boolean mouseDown = Mouse.isButtonDown(0);
								boolean shouldBreak = false;

								if (mouseDown) {
									r = 0.0f;
									g = 1.0f;
									b = 0.0f;
								} else {
									r = 1.0f;
									g = 0.0f;
									b = 0.0f;
								}

								for (int s = 0; s < activeShip.getLength(); s++) {
									if (!placeVertical && !(i + activeShip.getLength() > preGameArray.length)
											&& preGameArray[i + s][j].isPlaced()) {
										shouldBreak = true;
									}

									if (placeVertical && !(j + activeShip.getLength() > preGameArray[i].length)
											&& preGameArray[i][j + s].isPlaced()) {
										shouldBreak = true;
									}
								}

								for (int s = 0; s < activeShip.getLength(); s++) {

									if (shouldBreak) {
										break;
									}

									if (!placeVertical) {
										if (!(i + activeShip.getLength() > preGameArray.length)) {
											if (mouseDown) {
												preGameArray[i + s][j].setPlaced(true);
												activeShip.addPosition((i + s), j);
											}

											preGameArray[i + s][j].setRGB(r, g, b);
											preGameArray[i + s][j].setColoured(true);
										}
									} else {
										if (!(j + activeShip.getLength() > preGameArray[i].length)) {
											if (mouseDown) {
												preGameArray[i][j + s].setPlaced(true);
												activeShip.addPosition((i), j + s);
											}
											preGameArray[i][j + s].setRGB(r, g, b);
											preGameArray[i][j + s].setColoured(true);
										}
									}
								}

								if (mouseDown) {
									activeShip.setX(i);
									activeShip.setY(j);
									activeShip.setVertical(!placeVertical);
									activeShip.setPlaced(true);
									activeShip.setActive(false);
									player.getShips().set(activeShipIndex, activeShip);

									if (player.getShips().size() <= activeShipIndex + 1) {
										activeShip = null;
									} else {
										player.getShips().get(activeShipIndex + 1).setActive(true);
									}
								}

							} else {
								preGameArray[i][j].setRGB(0.1f, 0.1f, 0.1f);
							}
						}
					}
				}
			} else {

				// Must be called before the game state to improve performance
				setUpGameCallLists();

				state = State.GAME;
			}

			for (int i = 0; i < preGameArray.length; i++) {
				for (int j = 0; j < preGameArray[i].length; j++) {
					preGameArray[i][j].draw();
				}
			}

		}
		GL11.glPopMatrix();
	}

	private void renderGameUI() {

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glColor3f(0.0f, 0.0f, 0.0f);
			GL11.glVertex2f(70, HEIGHT - 50);
			GL11.glVertex2f(375, HEIGHT - 50);
			GL11.glVertex2f(375, HEIGHT - 355);
			GL11.glVertex2f(70, HEIGHT - 355);
		}
		GL11.glEnd();

		int offsetX = 75;
		int offsetY = HEIGHT - 350;

		for (int i = 0; i < inGameArray.length; i++) {
			for (int j = 0; j < inGameArray[i].length; j++) {
				inGameArray[i][j].setColoured(false);
				inGameArray[i][j].setLength(25);
				inGameArray[i][j].setXandY(30 * i + offsetX, 30 * j + offsetY);
			}
		}

		if (playersTurn) {
			for (int i = 0; i < inGameArray.length; i++) {
				for (int j = 0; j < inGameArray[i].length; j++) {
					if (!inGameArray[i][j].isPlaced()) {
						if (!inGameArray[i][j].isColoured()) {
							if (inGameArray[i][j].inBounds(Mouse.getX(), HEIGHT - Mouse.getY())) {
								if (Mouse.isButtonDown(0)) {
									inGameArray[i][j].setRGBA(0.4f, 0.4f, 0.4f, 0.4f);
									inGameArray[i][j].setPlaced(true);
									computer.getGrid().getCubes()[i][j].setHit(true);
									boolean b = false;
									for (Ship s : computer.getShips()) {
										for (Position p : s.getPositions()) {
											if (p.equals(new Position(i, j))) {
												b = true;
												p.setHit(true);
												s.isDestroyed();
												inGameArray[i][j].setColoured(true);
												inGameArray[i][j].setRGBA(1.0f, 0.0f, 1.0f, 0.6f);
												if (computer.defeated()) {
													playerWin = true;
													state = State.END_GAME;
												}
												break;
											}
										}
									}

									playersTurn = b;
								} else {
									inGameArray[i][j].setRGB(1.0f, 1.0f, 0.0f);
								}
							} else {
								inGameArray[i][j].setRGB(1.0f, 0.0f, 0.0f);
							}
						} else {
							inGameArray[i][j].setRGBA(1.0f, 0.0f, 1.0f, 0.6f);
						}
					} 
				}
			}
		} else {

			int x, y;
			Random rand = new Random();

			do {
				if (computer.getPreviousHit() == null) {

					x = rand.nextInt(player.getGrid().getCubes().length);
					y = rand.nextInt(player.getGrid().getCubes()[x].length);
				} else {
					if (rand.nextBoolean()) {
						x = computer.tryAdjacentX();
						y = computer.getPreviousHit().getY();
					} else {
						y = computer.tryAdjacentY();
						x = computer.getPreviousHit().getX();
					}
				}
			} while (player.getGrid().getCubes()[x][y].isHit());

			player.getGrid().getCubes()[x][y].setHit(true);
			boolean b = true;

			for (Ship s : player.getShips()) {
				for (Position p : s.getPositions()) {
					if (p.getX() == x && p.getY() == y) {
						b = false;
						p.setHit(true);
						computer.setPreviousHit(new Position(x, y));
						s.isDestroyed();
						if (player.defeated()) {
							playerWin = false;
							state = State.END_GAME;
						}
						break;
					} else {
						computer.setPreviousHit(null);
					}
				}
			}

			playersTurn = b;

		}

		for (int i = 0; i < inGameArray.length; i++) {
			for (int j = 0; j < inGameArray[i].length; j++) {
				inGameArray[i][j].draw();
			}
		}
	}

	private void renderMissile() {
		GL11.glPushMatrix();
		{
			GL11.glCallList(missileList);
		}
		GL11.glPopMatrix();
	}

	// step can range from 0 to 1, more steps = smoother and slower
	private double bezierPosition(double planeCoordStart, double planeCoordControl, double planeCoordEnd, double step) {
		double finalPos = Math.pow((1 - step), 2) * planeCoordStart + (1 - step) * 2 * step * planeCoordControl
				+ step * step * planeCoordEnd;
		return finalPos;
	}

	private void setUpGameCallLists() {

		///////////////////// GAME CALL LISTS //////////////////////

		// Missile call list
		GL11.glNewList(missileList, GL11.GL_COMPILE);
		{
			Missile.drawMissile();
		}
		GL11.glEndList();

		// Players Grid call list
		GL11.glNewList(playerGridList, GL11.GL_COMPILE);
		{
			player.drawGrid();
		}
		GL11.glEndList();

		// Enemy Grid call list
		GL11.glNewList(enemyGridList, GL11.GL_COMPILE);
		{
			computer.drawGrid();
		}
		GL11.glEndList();

		// Ship2 call list
		GL11.glNewList(destroyerList, GL11.GL_COMPILE);
		{
			player.getShips().get(0).draw();

		}
		GL11.glEndList();

		// Ship3 call list
		GL11.glNewList(cruiser1List, GL11.GL_COMPILE);
		{
			player.getShips().get(1).draw();
		}
		GL11.glEndList();

		GL11.glNewList(cruiser2List, GL11.GL_COMPILE);
		{
			player.getShips().get(2).draw();
		}
		GL11.glEndList();

		// Ship4 call list
		GL11.glNewList(battleshipList, GL11.GL_COMPILE);
		{
			player.getShips().get(3).draw();
		}
		GL11.glEndList();

		// Ship5 call list
		GL11.glNewList(carrierList, GL11.GL_COMPILE);
		{
			player.getShips().get(4).draw();
		}
		GL11.glEndList();
	}

	private void resetGameVariables() {

		player = new Player(true);
		computer = new Player(false);
		computer.randomisePositions();
		playerWin = false;

		for (int i = 0; i < preGameArray.length; i++) {
			for (int j = 0; j < preGameArray[i].length; j++) {
				preGameArray[i][j] = new GridSquare();
			}
		}

		for (int i = 0; i < inGameArray.length; i++) {
			for (int j = 0; j < inGameArray[i].length; j++) {
				inGameArray[i][j] = new GridSquare();
			}
		}

		player.getShips().get(0).setActive(true);

		for (int i = 0; i < player.getGrid().getCubes().length; i++) {
			for (int j = 0; j < player.getGrid().getCubes()[i].length; j++) {
				player.getGrid().getCubes()[i][j].setHit(false);
			}
		}

		for (int i = 0; i < computer.getGrid().getCubes().length; i++) {
			for (int j = 0; j < computer.getGrid().getCubes()[i].length; j++) {
				computer.getGrid().getCubes()[i][j].setHit(false);
			}
		}

		player.getShips().get(0).setActive(true);
		playersTurn = true;
	}

	private void drawMenuBackground() {
		GL11.glBegin(GL11.GL_POLYGON);
		{
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0, 0);

			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(WIDTH, 0);

			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(WIDTH, HEIGHT);

			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(0, HEIGHT);
		}
		GL11.glEnd();
	}

	private void buttonLogic(MenuButton button) {

		int x = Mouse.getX();
		int y = HEIGHT - Mouse.getY();

		if (button.inBounds(x, y)) {
			button.setRGB(0.8f, 0.8f, 0.8f);
		} else {
			button.setRGB(0.5f, 0.5f, 0.5f);
		}

		if (button.getId() == this.singleplayer) {
			if (button.inBounds(x, y) && Mouse.isButtonDown(0)) {
				source.play(buffer);
				resetGameVariables();
				state = State.PRE_GAME;
			}
		}

		if (button.getId() == this.howToPlay) {
			if (button.inBounds(x, y) && Mouse.isButtonDown(0)) {
				state = State.HOW_TO_PLAY;
			}
		}

		if (button.getId() == this.settings) {
			if (button.inBounds(x, y) && Mouse.isButtonDown(0)) {
			}
		}
		if (button.getId() == this.exit) {
			if (button.inBounds(x, y) && Mouse.isButtonDown(0)) {
				setRunning(false);
			}
		}

		if (button.getId() == this.back) {
			if (button.inBounds(x, y) && Mouse.isButtonDown(0)) {
				state = State.MENU;
			}
		}

		if (button.getId() == this.back) {
			if (button.inBounds(x, y) && Mouse.isButtonDown(0)) {
				source.play(buffer);
				state = State.MENU;
			}
		}

		if (button.getId() == this.playAgain) {
			if (button.inBounds(x, y) && Mouse.isButtonDown(0)) {
				source.play(buffer);
				resetGameVariables();
				state = State.PRE_GAME;
			}
		}
	}

	@Override
	protected void draw2D() {
		switch (state) {
		case GAME:
			GL11.glPushMatrix(); {
			renderGameUI();
		}
			GL11.glPopMatrix();
			break;
		case MENU:
			GL11.glPushMatrix(); {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, menuBackground.getTextureID());
			GL11.glColor3f(1.0f, 1.0f, 1.0f);

			GL11.glEnable(GL11.GL_TEXTURE_2D);

			GL11.glScalef(1.1f, 2.0f, 1.0f);
			GL11.glCallList(menuBgList);

			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
			GL11.glPopMatrix();

			GL11.glPushMatrix(); {
			for (MenuButton button : mainMenuButtons) {
				button.draw();
				buttonLogic(button);
			}
		}
			GL11.glPopMatrix();
			break;
		case PRE_GAME:

			GL11.glPushMatrix(); {
			renderPreGame();
		}
			GL11.glPopMatrix();
			break;

		case HOW_TO_PLAY:

			GL11.glPushMatrix(); {

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, menuBackground.getTextureID());
			GL11.glColor3f(1.0f, 1.0f, 1.0f);

			GL11.glEnable(GL11.GL_TEXTURE_2D);

			GL11.glScalef(1.1f, 2.0f, 1.0f);
			GL11.glCallList(menuBgList);

			GL11.glDisable(GL11.GL_TEXTURE_2D);

		}
			GL11.glPopMatrix();

			GL11.glPushMatrix(); {
			GL11.glTranslatef(1, 1, 1);
			GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.5f);

			GL11.glBegin(GL11.GL_POLYGON);
			{
				GL11.glVertex2f(175, 100);
				GL11.glVertex2f(1100, 100);
				GL11.glVertex2f(1100, 500);
				GL11.glVertex2f(175, 500);
			}
			GL11.glEnd();

		}
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			for (MenuButton button : backButtons) {
				button.draw();
				buttonLogic(button);
			}
			GL11.glPopMatrix();

			awtFont = new Font("Comic Sans", Font.BOLD, 30);
			font = new TrueTypeFont(awtFont, false);
			awtFont2 = new Font("Comic Sans", Font.BOLD, 20);
			font2 = new TrueTypeFont(awtFont2, false);

			font.drawString(530, 120, "HOW TO PLAY", Color.black);

			font2.drawString(200, 170,
					"Your mission is to sink all of the enemies battleships before they sink yours.");
			font2.drawString(200, 194, "");
			font2.drawString(200, 218,
					"Each player will place their 5 ships on a grid. The ships can be placed horizontally by clicking ");
			font2.drawString(200, 242, "on a square or vertically by pressing left shift and clicking on a square.");
			font2.drawString(200, 266, "");
			font2.drawString(200, 290,
					"No ship may hang off the edge of the grid. No ship may overlap another ship. Once all of the ");
			font2.drawString(200, 314, "ships have been placed you will not be able to move them. ");
			font2.drawString(200, 338, "");
			font2.drawString(200, 362,
					"Each player will take it in turns to select a co-ordinate to fire a missile. If the cube turns");
			font2.drawString(200, 386,
					"green then a ship has been hit. If the cube turns black then it has been missed. ");
			font2.drawString(200, 410, "");
			font2.drawString(200, 434,
					"Once either all your ships or all of the enemy ships have been destroyed. The game will end.");
			break;

		case END_GAME:
			GL11.glPushMatrix();
			for (MenuButton button : playAgainButton) {
				button.draw();
				buttonLogic(button);
			}
			GL11.glPopMatrix();

			GL11.glPushMatrix(); {
			GL11.glTranslatef(1, 1, 1);
			GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);

			GL11.glBegin(GL11.GL_POLYGON);
			{
				GL11.glVertex2f(175, 100);
				GL11.glVertex2f(1100, 100);
				GL11.glVertex2f(1100, 500);
				GL11.glVertex2f(175, 500);
			}
			GL11.glEnd();

		}
			GL11.glPopMatrix();

			awtFont = new Font("Arial", Font.BOLD, 50);
			font = new TrueTypeFont(awtFont, false);

			if (playerWin) {
				font.drawString(400, 260, "You Won! Well Done!", Color.white);
			} else {
				font.drawString(260, 260, "You Lost! Better luck next time!", Color.white);
			}
			break;
		default:
			GL11.glBegin(GL11.GL_QUADS); {
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			GL11.glVertex2f(0, 0);
			GL11.glVertex2f(0, 50);
			GL11.glVertex2f(50, 50);
			GL11.glVertex2f(50, 0);
		}
			GL11.glEnd();
			break;
		}

	}

	@Override
	protected void draw3D() {
		switch (state) {
		case GAME:
			GL11.glEnable(GL11.GL_LIGHT0);

			drawGameScene();
			break;
		case PRE_GAME:

			GL11.glPushMatrix(); {
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_CULL_FACE);

			GL11.glRotatef(90.0f, 1, 0, 0);
			GL11.glTranslatef(0, -7.5f, 0);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, skyBackground.getTextureID());
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			GL11.glTranslatef(25.0f, -115.0f, 2.0f);
			GL11.glScalef(1.0f, 0.17f, 0.02f);
			GL11.glRotatef(90, 0, 1, 0);
			GL11.glRotatef(8, 1, 0, 0);
			GL11.glCallList(menuBgList);

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_CULL_FACE);
		}
			GL11.glPopMatrix();

			GL11.glEnable(GL11.GL_LIGHT0);

			GL11.glPushMatrix(); {
			GL11.glTranslatef(-30.8f, -0.2f, -105.0f);
			GL11.glRotatef(90.0f, 1, 0, 0);
			water.displayWater();
		}
			GL11.glPopMatrix();
			break;
		case END_GAME:

			GL11.glPushMatrix(); {
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_CULL_FACE);

			GL11.glRotatef(90.0f, 1, 0, 0);
			GL11.glTranslatef(0, -7.5f, 0);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, skyBackground.getTextureID());
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			GL11.glTranslatef(25.0f, -115.0f, 2.0f);
			GL11.glScalef(1.0f, 0.17f, 0.02f);
			GL11.glRotatef(90, 0, 1, 0);
			GL11.glRotatef(8, 1, 0, 0);
			GL11.glCallList(menuBgList);

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_CULL_FACE);
		}
			GL11.glPopMatrix();

			GL11.glEnable(GL11.GL_LIGHT0);

			GL11.glPushMatrix(); {
			GL11.glTranslatef(-30.8f, -0.2f, -105.0f);
			GL11.glRotatef(90.0f, 1, 0, 0);
			water.displayWater();
		}
			GL11.glPopMatrix();

			break;
		default:
			// For testing
			UnitCube cube = new UnitCube();
			GL11.glPushMatrix(); {
			GL11.glScalef(10, 10, 10);
			GL11.glRotatef(f++, 1, 1, 1);
			cube.drawUnitCube();
		}
			GL11.glPopMatrix();

			break;
		}
	}
}
