package maze.routing;

// import java.io.File;
// import java.io.FileReader;
// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.FileNotFoundException;
// import java.io.PrintWriter;
// import java.io.FileWriter;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import maze.Maze;
import maze.Tile;
import maze.Tile.Type;
import maze.Maze.Coordinate;
import maze.Maze.Direction;

/**
 * Class which deals with finding, loading and saving route
 * 
 * @author Joshwin Sundarraj
 */
public class RouteFinder implements Serializable {
	private static final long serialversionUID = 129348938L;

	private Maze maze;
	private Stack<Tile> route;
	private boolean goingToFinish;
	private boolean finished;
	private Tile entrance;
	private Tile exit;
	private Tile currentTile;
	private Tile previousTile;
	private List<Tile> visited;
	private List<Tile> backTrackedTiles;
	private boolean backTracking;

	private List<List<Tile>> tiles;
	

	/**
	 * Constructor which creates a new RouteFinder object.
	 * Intiliases the maze to the given maze and initialises tiles,entrance and exit based on the maze.
	 * Initialises the three lists for the route and the boolean variables.
	 * 
	 * @param maze_in given maze for which a route can be found.
	 */
	public RouteFinder(Maze maze_in) {
		maze = maze_in;
		tiles = maze.getTiles();
		entrance = maze.getEntrance();
		exit = maze.getExit();
		currentTile = entrance;
		route = new Stack<Tile>();
		visited = new ArrayList<Tile>();
		backTrackedTiles = new ArrayList<Tile>();
		backTracking = false;
		goingToFinish = false;
		previousTile = null;
	}
	
	/**
	 * Method which returns current state of maze belonging to the object
	 * 
	 * @return maze which is being traversed.
	 */
	public Maze getMaze() {
		return maze;
	}

	/**
	 * Method which returns the current state of the route 
	 * in list form from a stack form
	 * 
	 * @return a list of tiles which form the current route
	 */
	public List<Tile> getRoute() {
		// Convert stack to list 
		List<Tile> routeList = new ArrayList<>();
		for (Tile each: route){
			routeList.add(each);
		}
		return routeList;
	}
	
	/**
	 * Method which returns whether a route has been found
	 * 
	 * @return returns true if route has been completed else returns false
	 */
	public boolean isFinished() {
		return finished;
	}
	
	/**
	 * Method which loads currently saved route from a .route file
	 * by loading the state into a new RouteFinder object.
	 * This is done by deserialization of an object
	 * 
	 * @param s filepath of route to be loaded
	 * @return return the new RouteFinder object with intialised data from file
	 * @throws FileNotFoundException thrown when file which does not exist has been provided
	 * @throws IOException thrown when invalid file is provided
	 * @throws ClassNotFoundException thrown when object cannot be deserialized
	 */
	public static RouteFinder load(String s) throws FileNotFoundException, IOException,
											ClassNotFoundException{
		try {
			if (!s.contains(".route")){
				throw new IOException("Invalid file type");
			}

			File fileCheck = new File(s);

			if(!fileCheck.exists() || fileCheck.isDirectory()) { 
    			throw new FileNotFoundException();
			}

			if (fileCheck.length() == 0) {
				throw new EOFException("Cannot load from empty file");
			}

			FileInputStream file = new FileInputStream(s); 
            ObjectInputStream in = new ObjectInputStream(file); 

            // Method for deserialization of object 
            RouteFinder result = (RouteFinder)in.readObject(); 
  
            in.close(); 
            file.close(); 
            System.out.println("Object has been deserialized\n"); 
			
			return result;
			
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("File could not be found.");
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException("File could not be deserialized.");
		}	
	}
	

	/**
	 * Method which saves the current state of the RouteFinder object in a file specified by the file path.
	 * It does this by serialising the object into a file.
	 * 
	 * @param s file path of the file which will have the serialised object
	 * @throws IOException thrown when an invalid file type is given
	 */
	public void save(String s) throws IOException {
		try {
			FileOutputStream file = new FileOutputStream(s); 
            ObjectOutputStream out = new ObjectOutputStream(file); 
  
            out.writeObject(this); 
  
            out.close(); 
            file.close(); 
  
            System.out.println("Object has been serialized\n");
			System.out.println("Successfully wrote to the file.");
    	} catch (IOException e) {
			throw new IOException("An error occurred.");
		}
	}
	
	/**
	 * Method which adds one more step to the route.
	 * It does this by analysing all the adjacent tiles with respect to the current tile.
	 * If there is no way to move forward in the maze it will backtrack.
	 * A route is not found if there is no way to move from the entrance.
	 * 
	 * @return returns true if the route has been completed, otherwise false 
	 * @throws NoRouteFoundException thrown if there is no route that can be detected
	 */
	public boolean step() throws NoRouteFoundException {
		// Make list of tiles for each direction
		// Get all adjacent tiles and enter into list
		// Loop through and if that element is not null, check if type is exit 
		// If exit is there, set finished to true and the "currentTile" variable to that tile
		// else Loop through again and if that element is not null, check which are navigable
		// And choose first navigable one to currentTile

		if (!isFinished()) {

			if (goingToFinish){
				route.push(exit);
				finished = true;
				return true;
			}

			List<Tile> adjTiles = new ArrayList<Tile>();
			if (!route.contains(currentTile)) {
				route.push(currentTile);
				visited.add(currentTile);
			}
			for(Direction d: Direction.values()){
				Tile nextTile = maze.getAdjacentTile(currentTile, d);
				if (nextTile != null && nextTile.isNavigable()){
					if (nextTile == exit){
						currentTile = nextTile;
						goingToFinish = true;
						return false;
					}

					if (!visited.contains(nextTile)) {
						adjTiles.add(nextTile);	
					}			
				} // if
			} //for

			if (adjTiles.size() == 0) {

				if (currentTile == entrance) {
					throw new NoRouteFoundException(); 
				}
				previousTile = currentTile;
				backTracking = true;
				currentTile = route.get(route.size() - 2);
				if (backTracking) {
					backTrackedTiles.add(previousTile);
					route.pop();
				}
				return false; 
			}
			else {
				currentTile = adjTiles.get(0);
				if (backTracking) {
					backTrackedTiles.add(previousTile);
					backTracking = false;
				}
				return false; 
			}
		}
		else {
			return true;
		}
	
	}
	
	/**
	 * Method which converts the current state of the maze and route into a string representation
	 * 
	 * @return returns the state of maze and route in string form
	 */
	public String toString() {
		String mazeString = "";
		List<Tile> routeList = getRoute();

		for (int i=0; i<tiles.size(); i++) {
			List<Tile> currentRow = tiles.get(i);
			for (int j=0; j<currentRow.size(); j++) {
				Tile curTile = currentRow.get(j);
				String currentChar = null; 
				
				if (curTile == entrance) {
					currentChar = curTile.toString();
				}
				else if (backTrackedTiles.contains(curTile)){
					currentChar = "-";
				}
				else if (routeList.contains(curTile)) {
					currentChar = "*";
				}
				else {
					currentChar = curTile.toString();
				}
				mazeString = mazeString + currentChar + " ";
			}
			mazeString = mazeString + String.format("\n");
		}

		return mazeString;
	}
	
}
