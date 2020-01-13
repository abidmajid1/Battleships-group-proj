package shapes;

import org.lwjgl.opengl.GL11;

import render.Normal;
import render.Vertex;

public class UnitPolygon {
	public static void drawUnitPolygon() {
		Vertex v1 = new Vertex(0.1f, 0.1f, 0.0f);
		Vertex v2 = new Vertex(-0.1f, 0.1f, 0.1f);
		Vertex v3 = new Vertex(-0.1f, 0.1f, -0.1f);
		Vertex v4 = new Vertex(-0.1f, -0.1f, 0.0f);

		// top face
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v2.toVector(), v3.toVector(), v4.toVector()).submit();

			v2.submit();
			v3.submit();
			v4.submit();

		}
		GL11.glEnd();

		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v3.toVector(), v1.toVector(), v4.toVector()).submit();

			v3.submit();
			v1.submit();
			v4.submit();

		}
		GL11.glEnd();

		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v2.toVector(), v4.toVector(), v1.toVector()).submit();

			v2.submit();
			v4.submit();
			v1.submit();

		}
		GL11.glEnd();

		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v1.toVector(), v3.toVector(), v2.toVector()).submit();

			v1.submit();
			v3.submit();
			v2.submit();

		}
		GL11.glEnd();
	}
}
