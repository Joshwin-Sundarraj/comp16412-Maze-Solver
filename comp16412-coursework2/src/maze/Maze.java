package maze;

import java.util.*;
import java.io.*;
import java.io.FileNotFoundException;


/**
 * Class which defines Maze and contains the Direction enumeration and coordinate class
 * 
 * @author Joshwin Sundarraj
 */
public class Maze implements Serializable {

	/**
	 * Enumeration which defines all possible directions to traverse the maze
	 */
	public enum Direction {
		NORTH,
		EAST,
		SOUTH,
		WEST
	}

	/**
	 * Class which defines positioning by using two integer values to form coordinates
	 * 
	 * @author Joshwin Sundarraj
	 */
	public class Coordinate implements Serializable{

		private int x;
		private int y;

		/**
		 * Constructor intialises the x & y postion
		 * 
		 * @param x_in the x position
		 * @param y_in the y position
		 */
		public Coordinate(int x_in, int y_in) {
			x = x_in;
			y = y_in;
		}
		
		/**
		 * Method to return the x-position
		 * 
		 * @return integer value of x-position 
		 */
		public int getX() {
			return x;
		}
		
		/**
		 * Method to return the y-position
		 * 
		 * @return integer value of y-position 
		 */
		public int getY() {
			return y;	
		}
		
		/**
		 * Method to convert integer values representing coordinates into a single string form
		 * 
		 * @return string form of coordinates
		 */
		public String toString() {
			return String.format("(%d, %d)", x, y);
		}
	
	}
	
	private Tile entrance;
	private Tile exit;
	private List<List<Tile>> tiles;

	/**
	 * Constructor which intialises the tiles 2-D list, entrance and exit.
	 */
	private Maze() {
		tiles = new ArrayList<List<Tile>>();
		entrance = null;
		exit = null;
	}
	
	/**
	 * Method which reads from a file and converts the character representation of a maze into a 2-D list of tiles.
	 * It does so by assigning a tile of a specific type based on the character.
	 * 
	 * @param s file path of the file containing the maze
	 * @return returns the new maze object created
	 * @throws InvalidMazeException thrown when there is not exactly one exit and entrance
	 * 								and all rows are not of the same length
	 * @throws FileNotFoundException thrown when the provided file does not exist
	 * @throws IOException thrown when an invalid file path is provided
	 */
	public static Maze fromTxt(String s) throws InvalidMazeException,
										 FileNotFoundException, IOException {
		try 
		{
			if (!s.contains(".txt")) {
				throw new IOException("Invalid file type");
			}
			File fileCheck = new File(s);

			if(!fileCheck.exists() || fileCheck.isDirectory()) { 
    			throw new FileNotFoundException();
			}

			if (fileCheck.length() == 0) {
				throw new EOFException("Cannot load from empty file");
			}

			FileReader file = new FileReader(s);
			BufferedReader fileStream = new BufferedReader(file);

			String line;
			int numberOfExits = 0;
			int numberOfEntrances = 0;
			int noOfCharsFirstLine = -1;
			Tile toBeEntrance = null;
			Tile toBeExit = null;
			Maze newMaze = new Maze();
			List<List<Tile>> newTiles = newMaze.getTiles();

			while((line = fileStream.readLine()) != null){
				char[] ch  = line.toCharArray();
				if (noOfCharsFirstLine == -1) {
					noOfCharsFirstLine = ch.length;
				}
				else if (noOfCharsFirstLine != ch.length){
					throw new RaggedMazeException();
				}

				List<Tile> lineTiles = new ArrayList<Tile>();

				for (int i=0; i<ch.length; i++) {
					Tile currentTile = Tile.fromChar(ch[i]);
					lineTiles.add(currentTile);
					if (ch[i] == 'e') {
						numberOfEntrances++;
						toBeEntrance = currentTile;
					}
					else if (ch[i] == 'x') {
						numberOfExits++;
						toBeExit = currentTile;
					}
				}
				newTiles.add(lineTiles);
			}

			fileStream.close();
			file.close();

			if (numberOfEntrances == 0) {
				throw new NoEntranceException();
			}
			else if (numberOfEntrances > 1) {
				throw new MultipleEntranceException();
			}
			else if (numberOfExits == 0) {
				throw new NoExitException();
			}
			else if (numberOfExits > 1) {
				throw new MultipleExitException();
			}
			else{
				newMaze.setEntrance(toBeEntrance);
				newMaze.setExit(toBeExit);
			}
			return newMaze;
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("File could not be found.");
		}
	}
	
	/**
	 * Method which calculates the tile adjacent to the current tile in a specific direction.
	 * It does this by adding an offset to the coordinates.
	 * 
	 * @param t the current tile
	 * @param d the direction of the desired adjacent tile
	 * @return returns the tile which is adjacent in a specific direction
	 */
	public Tile getAdjacentTile(Tile t, Direction d) {
		Coordinate tileLocation = getTileLocation(t);
		Coordinate adjLocation = null;
		switch (d) {
			case NORTH:
				if (tileLocation.getY() == (tiles.size()-1))
					return null;
				else
					adjLocation = new Coordinate(tileLocation.getX(), tileLocation.getY() + 1);
				break;
			case SOUTH:
				if (tileLocation.getY() == 0)
					return null;
				else
					adjLocation = new Coordinate(tileLocation.getX(), tileLocation.getY() - 1);
				break;
			case EAST:
				int noOfColumns = tiles.get(0).size();
				if (tileLocation.getY() == (noOfColumns - 1))
					return null;
				else
					adjLocation = new Coordinate(tileLocation.getX() + 1, tileLocation.getY());
				break;
			case WEST:
				if (tileLocation.getX() == 0)
					return null;
				else
					adjLocation = new Coordinate(tileLocation.getX() - 1, tileLocation.getY());
				break;		
			default:
				break;
		}
		return getTileAtLocation(adjLocation);//getTileLocation(adjLocation); //change made here
	}
	
	/**
	 * Method to return the entrance tile
	 * 
	 * @return returns the entrance tile of the maze
	 */
	public Tile getEntrance() {
		return entrance;
	}
	
		/**
	 * Method to return the exit tile
	 * 
	 * @return returns the exit tile of the maze
	 */
	public Tile getExit() {
		return exit;
	} 
	
	/**
	 * Returns the tile of the maze at the specified coordinate
	 * 
	 * @param c coordinate of the desired tile
	 * @return returns the tile at the specified coordinate
	 */
	public Tile getTileAtLocation(Coordinate c) {
		return tiles.get(tiles.size() - 1 - c.getY()).get(c.getX());
	}
	
	/**
	 * Returns the coordinate of a specified tile.
	 * It does this by searching the 2-D list of tiles until the specified tile is found
	 * 
	 * @param t the tile at the desired coordinate
	 * @return returns the coordinate of the specified tile which is in the 2-D list
	 *         returns null if the tile is not in the 2-D list
	 */
	public Coordinate getTileLocation(Tile t) {
		int x = -1;
		int y = -1;
		boolean found = false;
		for (int i=0; i<tiles.size(); i++) {
			List<Tile> currentRow = tiles.get(i);
			for (int j=0; j<currentRow.size(); j++) {
				if (currentRow.get(j).equals(t)) {
					x = j;
					found = true;
					break;
				}
			}
			if (found) {
				y = (tiles.size() - 1 - i);
				break;
			}
		}

		if (found) {
			return new Coordinate(x, y);
		}
		else{
			return null;
		}
	}
	
	/**
	 * Method to access the 2-D list of tiles
	 * 
	 * @return returns the 2-D list of tiles representing the maze
	 */
	public List<List<Tile>> getTiles() {
		return tiles;
	}
	
	/**
	 * Method to set the entrance of the maze to the specified tile
	 * 
	 * @param t tile to potentially be the entrance of the maze
	 * @throws MultipleEntranceException thrown when the entrance has already been set
	 * @throws IllegalArgumentException thrown when the provided tile is not in the maze
	 */
	private void setEntrance(Tile t) throws MultipleEntranceException, IllegalArgumentException{
		if (getTileLocation(t) == null) {
			throw new IllegalArgumentException("Entrance tile not in maze");
		}
		else if (entrance == null) {
			entrance = t;
		}
		else {
			throw new MultipleEntranceException();
		}
	}
	
	/**
	 * Method set the exit of the maze to the specified tile
	 * 
	 * @param t tile to potentially be the exit of the maze
	 * @throws MultipleExitException thrown when the exit has already been set
	 * @throws IllegalArgumentException thrown when the provided tile is not in the maze
	 */
	private void setExit(Tile t) throws MultipleExitException, IllegalArgumentException{
		if (getTileLocation(t) == null) {
			throw new IllegalArgumentException("Exit tile not in maze");
		}
		else if (exit == null) {
			exit = t;
		}
		else {
			throw new MultipleExitException();
		}
	}
	
	/**
	 * Method to convert the 2-D list of tiles into a string.
	 * This is done by checking the type associated with each tile
	 * 
	 * @return returns the string which represents the 2-D maze as characters
	 */
	public String toString() {
		String mazeString = "";
		int noOfColumns = -1;

		for (int i=0; i<tiles.size(); i++) {
			List<Tile> currentRow = tiles.get(i);
			noOfColumns = currentRow.size() - 1;
			mazeString = mazeString + (tiles.size() - 1 - i) + "		";
			for (int j=0; j<currentRow.size(); j++) {
				String currentChar = currentRow.get(j).toString();
				mazeString = mazeString + currentChar + " ";
			}
			mazeString = mazeString + String.format("\n");
		}

		mazeString = mazeString + String.format("\n");
		mazeString = mazeString + " " + "		";

		for (int i=0; i<= noOfColumns; i++) {
			mazeString = mazeString + i + " ";
		}

		return mazeString;
	}
	
}
