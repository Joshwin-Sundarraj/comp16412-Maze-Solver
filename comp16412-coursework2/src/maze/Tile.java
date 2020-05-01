package maze;

/**
 * Class which represents a single tile and contains the Type enumeration
 * 
 * @author Joshwin Sundarraj
 */
public class Tile implements java.io.Serializable{

	/**
	 * Enumeration which defines the different types of tiles within a maze
	 */
	public enum Type {
		CORRIDOR,
		ENTRANCE,
		EXIT,
		WALL
	}
	
	private Type type;

	/**
	 * Constructor which intialises the type associated with a tile
	 * 
	 * @param type_in the type to be associated with a given tile
	 */
	public Tile(Type type_in) {
		type = type_in;
	}
	
	/**
	 * Method which returns the tile, with a specific type, based on the characyer given
	 * 
	 * @param c character to which a new tile will be created.
	 * @return returns a tile with its type based on the given character
	 * @throws InvalidMazeException thrown when invalid character is provided
	 */
	protected static Tile fromChar(char c) throws InvalidMazeException{
		Type newType = null;
		switch (c) {
			case 'e':
				newType = Type.ENTRANCE;
				break;
			case 'x':
				newType = Type.EXIT;
				break;
			case '#':
				newType = Type.WALL;
				break;
			case '.':
				newType = Type.CORRIDOR;
				break;
			default:
				throw new InvalidMazeException("Invalid character found");				
		}
		return new Tile(newType);	
	}
	
	/**
	 * Method which returns the type of the Tile object
	 * 
	 * @return returns the type of the tile object which will be one of the elements in the enumeration
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * Method which checks the type of the tile and returns whether it could be included in the route
	 * 
	 * @return returns true if the type of the tile is one which can be included in the route, otherwise false
	 */
	public boolean isNavigable() {
		switch (this.type) {
			case ENTRANCE:
			case EXIT:
			case CORRIDOR:
				return true;
			default:
				//takes into account for WALL case
				break;
		}
		return false;
	}
	
	/**
	 * Method which returns the character associated with the type of the tile object
	 * 
	 * @return returns a string representation of the character associated with the type
	 */
	public String toString() {
		switch (this.type) {
			case ENTRANCE: return "e";
			case EXIT: return "x";
			case CORRIDOR: return ".";
			case WALL: return "#";
			default:  return " ";		
		}
	}
	
}
