package shapes;

import org.lwjgl.opengl.GL11;

import render.Normal;
import render.Vertex;

public class UnitTrapezoid {
	public static void drawUnitTrapezoid() {
		Vertex v1 = new Vertex(0.01f, 0.01f, 0.01f);
		Vertex v2 = new Vertex(0.01f, -0.01f, 0.01f);
		Vertex v3 = new Vertex(-0.01f, 0.01f, 0.01f);
		Vertex v4 = new Vertex(-0.01f, -0.01f, 0.01f);
		Vertex v5 = new Vertex(-0.025f, -0.025f, -0.10f);
		Vertex v6 = new Vertex(-0.025f, 0.025f, -0.10f);
		Vertex v7 = new Vertex(0.025f, 0.025f, -0.10f);
		Vertex v8 = new Vertex(0.025f, -0.025f, -0.10f);

		// top face
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v1.toVector(), v7.toVector(), v6.toVector(), v3.toVector()).submit();

			v1.submit();
			v7.submit();
			v6.submit();
			v3.submit();

		}
		GL11.glEnd();
		// bottom face
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v2.toVector(), v4.toVector(), v5.toVector(), v8.toVector()).submit();

			v2.submit();
			v4.submit();
			v5.submit();
			v8.submit();

		}
		GL11.glEnd();
		// front face
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v1.toVector(), v3.toVector(), v4.toVector(), v2.toVector()).submit();

			v1.submit();
			v3.submit();
			v4.submit();
			v2.submit();

		}
		GL11.glEnd();
		// right face
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v7.toVector(), v1.toVector(), v2.toVector(), v8.toVector()).submit();

			v7.submit();
			v1.submit();
			v2.submit();
			v8.submit();

		}
		GL11.glEnd();
		// left face
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v3.toVector(), v6.toVector(), v5.toVector(), v4.toVector()).submit();

			v3.submit();
			v6.submit();
			v5.submit();
			v4.submit();

		}
		GL11.glEnd();
		// back face
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v7.toVector(), v8.toVector(), v5.toVector(), v6.toVector()).submit();

			v7.submit();
			v8.submit();
			v5.submit();
			v6.submit();

		}
		GL11.glEnd();
	}
}
