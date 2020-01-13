package shapes;

import org.lwjgl.opengl.GL11;

import render.Normal;
import render.Vertex;

public class IrregularPolyhedra {
	public static void drawSomeShape() {
		Vertex v1 = new Vertex(0.8f, 0.1f, 0.1f);
		Vertex v2 = new Vertex(0.8f, 0.1f, -0.1f);
		Vertex v3 = new Vertex(0.8f, -0.1f, 0.0f);
		Vertex v4 = new Vertex(-0.8f, 0.1f, 0.1f);
		Vertex v5 = new Vertex(-0.8f, 0.1f, -0.1f);
		Vertex v6 = new Vertex(-0.8f, -0.1f, 0.0f);

		// left gable
		GL11.glBegin(GL11.GL_TRIANGLES);
		{
			new Normal(v3.toVector(), v2.toVector(), v1.toVector()).submit();

			v3.submit();
			v2.submit();
			v1.submit();
		}
		GL11.glEnd();

		// front slope
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v6.toVector(), v3.toVector(), v1.toVector(), v4.toVector()).submit();

			v6.submit();
			v3.submit();
			v1.submit();
			v4.submit();
		}
		GL11.glEnd();

		// back slope
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v2.toVector(), v3.toVector(), v6.toVector(), v5.toVector()).submit();

			v2.submit();
			v3.submit();
			v6.submit();
			v5.submit();
		}
		GL11.glEnd();

		// right gable
		GL11.glBegin(GL11.GL_TRIANGLES);
		{
			new Normal(v5.toVector(), v6.toVector(), v4.toVector()).submit();

			v5.submit();
			v6.submit();
			v4.submit();
		}
		GL11.glEnd();

		// top gable
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v1.toVector(), v2.toVector(), v5.toVector(), v4.toVector()).submit();

			v1.submit();
			v2.submit();
			v5.submit();
			v4.submit();
		}
		GL11.glEnd();

	}
}
