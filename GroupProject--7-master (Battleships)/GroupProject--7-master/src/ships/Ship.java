package ships;

import java.util.ArrayList;
import java.util.List;

public abstract class Ship {
	protected boolean isActive; //Used for knowing which ship to place
	protected boolean isPlaced;	//Used to indicate a position has been set
	protected int length;
	protected int x, y;
	protected boolean isVertical; //Describes the ships orientation
	protected boolean destroyed;
	protected List<Position> shipLocations;
	
	protected Ship() {
		this.isActive = false;
		this.destroyed = false;
		shipLocations = new ArrayList<>();
	}
	
	public abstract void draw();
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setVertical(boolean vertical) {
		this.isVertical = vertical;
	}
	
	public void setPlaced(boolean placed) {
		this.isPlaced = placed;
	}
	
	public boolean isPlaced() {
		return isPlaced;
	}
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public boolean getIsActive() {
		return isActive;
	}
	
	public int getLength() {
		return length;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public List<Position> getPositions() {
		return shipLocations;
	}
	
	public void  addPosition(int x, int y) {
		shipLocations.add(new Position(x, y));
	}
	
	public boolean isDestroyed() {

		int counter = 0;
		
		for(Position p : shipLocations) {
			if(p.getHit()) {
				counter++;
			}
		}
		
		return counter >= shipLocations.size();
	}

	public boolean isVertical() {
		return isVertical;
	}
	
	public boolean getDestroyed() {
		return destroyed;
	}
	
	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}
}


