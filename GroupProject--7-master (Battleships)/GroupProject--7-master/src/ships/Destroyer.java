package ships;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

import render.FloatBuffer;
import shapes.IrregularPolyhedra;
import shapes.UnitCube;
import shapes.UnitPolygon;
import shapes.UnitTrapezoid;

public class Destroyer extends Ship {

	private UnitCube cube = new UnitCube();


	public Destroyer() {
		super();
		super.length = 2;
	}

	@Override
	public void draw() {
		GL11.glTranslatef((-5.4f + (super.getX() * 1.2f)), (7.0f + (super.getY() * 1.2f)), -0.5f);

		GL11.glPushMatrix();
		{
			// the front faces of the ship aren't very shiny (specular exponent)
			float shipFrontShininess = 3.0f;
			// specular reflection of the front faces of the ship
			float shipFrontSpecular[] = { 0.1f, 0.0f, 0.0f, 1.0f };
			// diffuse reflection of the front faces of the ship
			float shipFrontDiffuse[] = { 0.5f, 0.5f, 0.5f, 1.0f };

			// set the material properties for the ship
			GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, shipFrontShininess);
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(shipFrontSpecular));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(shipFrontDiffuse));

			// rotate ships upright
			GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);

			if (super.isVertical()) {
				// make ship rotate
				GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
				GL11.glTranslatef(-0.5f, 0.0f, -0.6f);
				
			}else{
				GL11.glRotatef(0.0f, 0.0f, 1.0f, 0.0f);
				GL11.glTranslatef(-1.2f, 0.0f, -0.1f);
			}

			// Scale ship
			GL11.glScalef(1.0f, 3.0f, 3.0f);

			IrregularPolyhedra.drawSomeShape();

			GL11.glPushMatrix();
			{
				GL11.glTranslatef(0.9f, 0.0f, 0.0f);
				UnitPolygon.drawUnitPolygon();
			}
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			{
				GL11.glTranslatef(-0.9f, 0.0f, 0.0f);
				GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);

				UnitPolygon.drawUnitPolygon();
			}
			GL11.glPopMatrix();

			// draw communications tower
			GL11.glPushMatrix();
			{

				GL11.glTranslatef(0.2f, 0.2f, 0.0f);
				GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
				GL11.glScalef(4.0f, 4.0f, 1.0f);
				UnitTrapezoid.drawUnitTrapezoid();

				GL11.glPushMatrix();
				{

					GL11.glTranslatef(0.0f, 0.0f, -0.02f);
					GL11.glScalef(0.05f, 0.05f, 0.05f);
					cube.drawUnitCube();
				}
				GL11.glPopMatrix();

				GL11.glPushMatrix();
				{
					GL11.glScalef(0.025f, 0.025f, 0.15f);
					cube.drawUnitCube();
				}
				GL11.glPopMatrix();

			}
			GL11.glPopMatrix();

			// draw 3 shooter cannon
			GL11.glPushMatrix();
			{
				GL11.glTranslatef(-0.5f, 0.12f, 0.0f);
				GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
				GL11.glScalef(0.15f, 0.05f, 0.15f);
				cube.drawUnitCube();

				GL11.glPushMatrix();
				{
					GL11.glTranslatef(0.0f, 0.7f, 0.0f);
					GL11.glScalef(0.7f, 0.7f, 0.7f);
					cube.drawUnitCube();
				}
				GL11.glPopMatrix();

				GL11.glPushMatrix();
				{
					GL11.glTranslatef(0.0f, 0.0f, -0.7f);
					GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
					GL11.glScalef(3.0f, 3.0f, 1.0f);
					new Sphere().draw(0.1f, 500, 5);
				}
				GL11.glPopMatrix();

				GL11.glPushMatrix();
				{
					GL11.glTranslatef(0.3f, 0.0f, -0.7f);
					GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
					GL11.glScalef(3.0f, 3.0f, 1.0f);
					new Sphere().draw(0.1f, 500, 5);
				}
				GL11.glPopMatrix();

				GL11.glPushMatrix();
				{
					GL11.glTranslatef(-0.3f, 0.0f, -0.7f);
					GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
					GL11.glScalef(3.0f, 3.0f, 1.0f);
					new Sphere().draw(0.1f, 500, 5);
				}
				GL11.glPopMatrix();

			}
			GL11.glPopMatrix();

			// draw 5 shooter cannon at back
			GL11.glPushMatrix();
			{
				GL11.glTranslatef(-0.2f, 0.12f, 0.0f);
				GL11.glScalef(0.15f, 0.05f, 0.15f);
				cube.drawUnitCube();

				GL11.glPushMatrix();
				{
					GL11.glTranslatef(0.0f, 0.7f, 0.0f);
					GL11.glScalef(0.7f, 0.7f, 0.7f);
					cube.drawUnitCube();
				}
				GL11.glPopMatrix();

				GL11.glPushMatrix();
				{
					GL11.glTranslatef(-0.3f, 0.0f, -0.7f);
					GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
					GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
					GL11.glScalef(3.0f, 3.0f, 1.0f);
					new Sphere().draw(0.1f, 500, 5);

				}
				GL11.glPopMatrix();

				GL11.glPushMatrix();
				{
					GL11.glTranslatef(0.3f, 0.0f, -0.7f);
					GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
					GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
					GL11.glScalef(3.0f, 3.0f, 1.0f);
					new Sphere().draw(0.1f, 500, 5);
				}
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				{
					GL11.glTranslatef(0.0f, 0.0f, -0.7f);
					GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
					GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
					GL11.glScalef(3.0f, 3.0f, 1.0f);
					new Sphere().draw(0.1f, 500, 5);
				}
				GL11.glPopMatrix();

				GL11.glPushMatrix();
				{
					GL11.glTranslatef(-0.15f, 0.0f, 0.7f);
					GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
					GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
					GL11.glScalef(3.0f, 3.0f, 1.0f);
					new Sphere().draw(0.1f, 500, 5);
				}
				GL11.glPopMatrix();

				GL11.glPushMatrix();
				{
					GL11.glTranslatef(0.15f, 0.0f, 0.7f);
					GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
					GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
					GL11.glScalef(3.0f, 3.0f, 1.0f);
					new Sphere().draw(0.1f, 500, 5);
				}
				GL11.glPopMatrix();

			}
			GL11.glPopMatrix();

		}
		GL11.glPopMatrix();

	}

	@Override
	public void setX(int x) {
		super.x = x;
	}


	@Override
	public void setY(int y) {
		super.y = y;
	}


	@Override
	public void setVertical(boolean vertical) {
		super.isVertical = vertical;
	}


	@Override
	public void setPlaced(boolean placed) {
		super.isPlaced = placed;
	}

}