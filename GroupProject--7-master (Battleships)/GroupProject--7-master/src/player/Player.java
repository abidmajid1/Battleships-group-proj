package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.Grid;
import shapes.UnitCube;
import ships.Battleship;
import ships.Carrier;
import ships.Cruiser;
import ships.Destroyer;
import ships.Position;
import ships.Ship;

public class Player {

	private boolean human;
	private List<Ship> ships;
	private List<UnitCube> shipLocations;
	private Grid grid;
	private Position previousHit;

	public Player(boolean human) {
		this.human = human;
		this.setPreviousHit(null);
		ships = new ArrayList<>(5);
		shipLocations = new ArrayList<>(5);
		grid = new Grid(human);
		addShips();
	}
	
	public int tryAdjacentX() {
		int x = previousHit.getX();
		do {
			if(new Random().nextBoolean()) {
				x++;
			} else {
				x--;
			}
		} while(x >= 1 && x < getGrid().getCubes().length-1);
		return x;
	}
	
	public int tryAdjacentY() {
		int y = previousHit.getY();
		do {
			if(new Random().nextBoolean()) {
				y++;
			} else {
				y--;
			}
		} while(y >= 1 && y < getGrid().getCubes()[0].length-1);
		return y;
	}

	private void addShips() {
		ships.add(new Destroyer());
		ships.add(new Cruiser());
		ships.add(new Cruiser());
		ships.add(new Battleship());
		ships.add(new Carrier());
	}

	public Grid getGrid() {
		return grid;
	}

	public boolean isHuman() {
		return human;
	}

	public List<Ship> getShips() {
		return ships;
	}

	public void drawGrid() {
		grid.drawBoard();
	}

	public void randomisePositions() {

		boolean vert = false;
		int x = 0, y = 0;
		Random rand = new Random();

		boolean valid = false;

		for (Ship ship : ships) {

			do {

				x = rand.nextInt(Grid.DEFAULT_X);
				y = rand.nextInt(Grid.DEFAULT_Y);
				vert = rand.nextBoolean();
				valid = testValidPosition(ship, x, y, vert);

			} while (!valid);

			ship.setX(x);
			ship.setY(y);
			ship.setVertical(vert);

			for (int i = 0; i < ship.getLength(); i++) {
				if (vert) {
					ship.addPosition(x, y + i);
				} else {
					ship.addPosition(x + i, y);
				}
			}
		}
	}

	// Return true if valid
	// Return false if not valid
	private boolean testValidPosition(Ship s, int x, int y, boolean vert) {

		boolean valid = true;

		for (int i = 0; i < s.getLength(); i++) {
			for (Ship ship : ships) {
				for (Position p : ship.getPositions()) {
					if (vert) {
						//Add to y
						if(p.equals(new Position(x, y+i)) || s.getLength() + y > getGrid().getCubes()[x].length) {
							valid = false;
						} 
					} else {
						//Add to x
						if(p.equals(new Position(x+i, y)) || s.getLength() + x > getGrid().getCubes().length ) {
							valid = false;
						} 
					}
				}
			}
		}

		return valid;
	}

	public void addShipLocation(UnitCube unitCube) {
		shipLocations.add(unitCube);
	}

	public boolean defeated() {

		int numberDestroyed = 0;

		for (Ship s : ships) {
			if (s.isDestroyed())
				numberDestroyed++;
		}

		return numberDestroyed >= 5;
	}

	public Position getPreviousHit() {
		return previousHit;
	}

	public void setPreviousHit(Position previousHit) {
		this.previousHit = previousHit;
	}

}
