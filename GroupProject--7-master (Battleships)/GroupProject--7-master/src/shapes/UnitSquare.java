package shapes;

import org.lwjgl.opengl.GL11;

public class UnitSquare {
	public static void drawUnitSquare() {
		GL11.glBegin(GL11.GL_POLYGON);
		{
			GL11.glVertex2f(-0.5f, -0.5f);
			GL11.glVertex2f(0.5f, -0.5f);
			GL11.glVertex2f(0.5f, 0.5f);
			GL11.glVertex2f(-0.5f, 0.5f);
		}
		GL11.glEnd();
	}
}
