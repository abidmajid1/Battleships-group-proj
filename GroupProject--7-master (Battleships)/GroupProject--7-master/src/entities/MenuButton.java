package entities;

import java.awt.Font;
//import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public class MenuButton {
	
	private int x, y;
	private int width, height, id;
	private float r, g, b, a;
	private String text;
	private TrueTypeFont font;
	private Font awtFont;
	
	public MenuButton(int x, int y, int id, String text) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.text = text;
		this.width = 200;
		this.height = 40;
		this.r = 0.5f; // new Random().nextFloat();
		this.g = 0.5f; // new Random().nextFloat();
		this.b = 0.5f; // new Random().nextFloat();
		this.a = 0.5f;
		this.awtFont = new Font("Verdana", Font.BOLD, 25);
		this.font = new TrueTypeFont(awtFont, true);
		
	}
	
	public MenuButton(int x, int y, int id, String text, int width, int height)  {
		this.x = x;
		this.y = y;
		this.id = id;
		this.text = text;
		this.width = width;
		this.height = height;
		this.r = 0.5f; // new Random().nextFloat();
		this.g = 0.5f; // new Random().nextFloat();
		this.b = 0.5f; // new Random().nextFloat();
		this.a = 0.5f;
		this.awtFont = new Font("Verdana", Font.BOLD, 25);
		this.font = new TrueTypeFont(awtFont, true);
	}
	
	public void draw() {
		
		GL11.glBegin(GL11.GL_POLYGON);
		{
			GL11.glColor4f(r, g, b, a);
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x + width, y);
			GL11.glVertex2f(x + width, y + height);
			GL11.glVertex2f(x, y + height);
		}
		GL11.glEnd();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		font.drawString(x+5, y+5, text, Color.black);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	
	public boolean inBounds(int mouseX, int mouseY) {
		if(mouseX > x && mouseX < x+width && mouseY > y && mouseY < y+height) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setRGB(float r, float g, float b) {
		if(r >= 0 && r <= 1.0f) {
			this.r = r;
		}
		
		if(g >= 0 && g <= 1.0f) {
			this.g = g;
		}
		
		if(b >= 0 && b <= 1.0f) {
			this.b = b;
		}
	}

	public int getId() {
		return id;
	}
	
	public void setText(String text) {
		this.text = text;
	}
}
