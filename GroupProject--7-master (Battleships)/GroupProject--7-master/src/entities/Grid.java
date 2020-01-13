package entities;

import org.lwjgl.opengl.GL11;

import render.FloatBuffer;
import shapes.UnitCube;

public class Grid {
	public static final int DEFAULT_X = 10;
	public static final int DEFAULT_Y = 10;
	private static final float SCALE = 1.2f;

	private float height;
	private float width;

	private Boolean isPlayer;
	
	private UnitCube[][] grid = new UnitCube[DEFAULT_X][DEFAULT_Y];

	public Grid(boolean player) {
		this.isPlayer = player;
		this.height = getX() * SCALE;
		this.width = getY() * SCALE;
		
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				grid[i][j] = new UnitCube();
			}
		}

		drawBoard();
	}

	public void drawBoard() {	
		GL11.glPushMatrix();
		{


			GL11.glTranslatef((1 - getHeight()) / 2, (1 - getWidth()) / 2, 0.0f);

			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					GL11.glPushMatrix();
					{
						GL11.glTranslatef((float) i * SCALE, (float) j * SCALE, 0.0f);

						lighting(grid[i][j].isHit());
						grid[i][j].drawUnitCube();
					}
					GL11.glPopMatrix();
				}
			}
		}
		GL11.glPopMatrix();
	}

	private void lighting(boolean isHit) {
		if (!isHit) {
			if (isPlayer) {
				float shininess = 3.0f;
				float specular[] = { 0.2f, 0.2f, 0.2f, 0.9f };
				float diffuse[] = { 0.0f, 0.0f, 1.0f, 0.65f };

				GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, shininess);
				GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(specular));
				GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse));
			} else {
				float shininess = 3.0f;
				float specular[] = { 0.2f, 0.2f, 0.2f, 0.9f };
				float diffuse[] = { 1.0f, 0.0f, 0.0f, 0.65f };

				GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, shininess);
				GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(specular));
				GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse));
			}
		} else {
			float shininess = 3.0f;
			float specular[] = { 0.2f, 0.2f, 0.2f, 1.0f };
			float diffuse[] = { 0.0f, 0.0f, 0.0f, 0.5f };

			GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, shininess);
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(specular));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse));
		}
	}

	public int getX() {
		return DEFAULT_X;
	}

	public int getY() {
		return DEFAULT_Y;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}
	
	public Boolean isAPlayer()  {
		return isPlayer;
	}
	
	public UnitCube[][] getCubes() {
		return grid;
	}
}