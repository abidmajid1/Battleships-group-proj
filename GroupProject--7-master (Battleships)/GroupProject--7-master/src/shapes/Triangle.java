package shapes;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;

import render.Normal;
import render.Vertex;

public class Triangle {
	public static void drawTriangle() {
		
		Vertex v1 = new Vertex(0.5f, -0.5f, 0.0f);
		Vertex v2 = new Vertex(-0.5f, -0.5f, 0.0f);
		Vertex v3 = new Vertex(0.0f, 0.5f, 0.0f);
		
		glBegin(GL_TRIANGLES);
		{
			
			new Normal(v1.toVector(), v2.toVector(), v3.toVector()).submit();
			v1.submit();
			v2.submit();
			v3.submit();
		}
		glEnd();
	}
}
