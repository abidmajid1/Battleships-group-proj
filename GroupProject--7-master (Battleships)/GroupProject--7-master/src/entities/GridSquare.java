package entities;

import org.lwjgl.opengl.GL11;

public class GridSquare {
	private int x, y;
	private int length;
	private float r, g, b, a;
	private boolean isColoured;
	private boolean placed;

	public GridSquare() {
		this.length = 50;
	}
	
	public void draw() {
		GL11.glBegin(GL11.GL_POLYGON);
		{
			GL11.glColor4f(r, g, b, a);
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x+length, y);
			GL11.glVertex2f(x+length, y+length);
			GL11.glVertex2f(x, y+length);
		}
		GL11.glEnd();
	}
	
	public boolean isPlaced() {
		return placed;
	}

	public void setPlaced(boolean placed) {
		this.placed = placed;
	}
	
	public boolean inBounds(int mouseX, int mouseY) {
		if(mouseX > x && mouseX < x+length && mouseY > y && mouseY < y+length) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setXandY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean isColoured() {
		return isColoured;
	}

	public void setColoured(boolean isColoured) {
		this.isColoured = isColoured;
	}
	
	public void setRGB(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1.0f;
	}
	
	public void setRGBA(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
}
