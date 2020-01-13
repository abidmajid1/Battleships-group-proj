package entities;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import render.FloatBuffer;
import render.Normal;
import render.Vertex;

public class Water {
	
	private Random generator = new Random();
	private float waveHeightControl;
	private int cols, rows;
	private Vertex[][] vertices;
	private float[][] newZValues, zValueDifferences;
	
/**
* Initialises the water object ready for use
* @param gridWidth A float that represents the width of the grid
* @param gridHeight A float that represents the height of the grid
* @param gridScale A float that represents the scale factor of the water grid's size
* @param waveHeightControl A float that controls the height of the waves
*/
	public Water(int gridWidth, int gridHeight, float gridScale, float waveHeightControl) {
		generator = new Random();
		cols = Math.round(gridWidth/gridScale);
		rows = Math.round(gridHeight/gridScale);
		vertices = new Vertex[cols][rows];
		newZValues = new float[cols][rows];
		zValueDifferences = new float[cols][rows];
		this.waveHeightControl = waveHeightControl;
		this.generateZs();
		
		for(int  y = 0; y < rows; y++) {
			for(int x = 0; x<(cols); x++) {
				vertices[x][y] = new Vertex(x*gridScale, y*gridScale, newZValues[x][y]);
			}
		}
	}
	
/**
 * Generates the differences of the old and new values ready for the incremental step
 * 
 */
	public void generateNextWaterStep() {
		this.generateZs();
		for(int  y = 0; y < rows; y++) {
			for(int x = 0; x<(cols); x++) {
				Vertex current = vertices[x][y];
				float currentZ = current.getZ();
				float newZ = newZValues[x][y];
				
				if(currentZ < newZ) {
					zValueDifferences[x][y] = newZ - currentZ;
				}
				else if(currentZ > newZ) {
					zValueDifferences[x][y] = -(currentZ - newZ);
				}
				else {
					zValueDifferences[x][y] = 0;
				}
			}	
		}
	}
	
/**
 * Updates the water texture by 1 incremental step towards the new z value. This method should be called 'steps' times
 * before generating the next water step
 * @param steps The number of steps to break the transition down into
 * 
 */
	public void incrementalStep(int steps) {
		for(int  y = 0; y < rows; y++) {
			for(int x = 0; x<(cols); x++) {
				float change = zValueDifferences[x][y] / steps;
				Vertex current = vertices[x][y];
				current.setZ(current.getZ() + change);
			}	
		}
	}
	
/**
 * Draws and displays the water texture with it's current vertex values
 * 
 */
	public void displayWater() {

		float shininess = 3.0f;
		float specular[] = { 0.3f, 0.3f, 0.5f, 1.0f };
		float diffuse[] = { 0.0f, 0.0f, 1.0f, 1.0f };
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, shininess);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(specular));
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse));

		for(int y=0; y<rows-1; y++) {    				
			GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
			Vertex v1, v2, v3, v4;
			for(int x=0; x<cols-1; x++) {	 
				 v1 = vertices[x][y];
				 v2 = vertices[x][y+1];
				 v3 = vertices[x+1][y];
				 v4 = vertices[x+1][y+1];
				  
				 new Normal(v1.toVector(),v2.toVector(),v3.toVector()).submit();
				 v1.submit();
				 v2.submit();
				 v3.submit();
								 
				 new Normal(v2.toVector(),v4.toVector(),v3.toVector()).submit();
				 v2.submit();
				 v4.submit();
				 v3.submit();
			}
			GL11.glEnd();	
		}
	}
	
/**
 * Internal method for generating new z values
 * 	
 */
	
	private void generateZs() {
		float newZValue = 0, base = 0;
		for(int  y = 0; y < rows; y++) {
			for(int x = 0; x<(cols); x++) {

				
				base = generator.nextFloat();			
				boolean sign = generator.nextBoolean(); 	
				if(sign) newZValue = base/waveHeightControl; 
				else newZValue = base/waveHeightControl; 
				base = newZValue;
				newZValues[x][y] = newZValue;
			}
		}
	}
}
