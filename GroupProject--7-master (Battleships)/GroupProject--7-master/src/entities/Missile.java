package entities;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import render.FloatBuffer;
import render.Normal;
import render.Vertex;

public class Missile {
	
	//draws a wing for the missile
	private static void drawWing() {
        Vertex v1 = new Vertex(0.0f,  0.0f,  0.0f);
        Vertex v2 = new Vertex(0.0f, 1.0f, 0.0f);
        Vertex v3 = new Vertex(-0.5f, 0.0f, 0.0f);
    	
        GL11.glBegin(GL11.GL_TRIANGLES);
	        {
	            new Normal(v1.toVector(),v2.toVector(),v3.toVector()).submit();
	            
	            //front face
	            v1.submit();
	            v2.submit();
	            v3.submit();
	            
	            //back face
	            v3.submit();
	            v2.submit();
	            v1.submit();
	        }
        GL11.glEnd();       
	}

	// Draws the missile
    public static void drawMissile()
    {    
    	//draw body of missile
        GL11.glPushMatrix();
	    GL11.glRotatef(-90, 1.0f, 0.0f, 0.0f);
	    new Cylinder().draw(0.4f, 0.3f, 2.0f, 10, 10);
        GL11.glPopMatrix();
        
		float shininess = 3.0f;
		float specular[] = { 0.2f, 0.2f, 0.2f, 1.0f };
		float diffuse[] = { 1.0f, 0.0f, 0.0f, 1.0f };
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, shininess);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(specular));
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse));
		
        //draw top of missile
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0f, 2.0f, 0.0f);
        GL11.glRotatef(-90, 1.0f, 0.0f, 0.0f);
        new Cylinder().draw(0.3f, 0.0f, 1.0f, 10, 10);
        GL11.glPopMatrix();
        
        //draw wings of missile
        for(int i=0; i<4; i++) {	
        GL11.glPushMatrix();
        GL11.glRotatef(90*i, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(-0.3f, -0.1f, 0.0f);
        drawWing();
        GL11.glPopMatrix();
        }
    }
}
