package maze;

/**
 * Defines a exception which is thrown when the maze is invalid
 * 
 * @author Joshwin Sundarraj
 */
public class InvalidMazeException extends RuntimeException{
    /**
     * Constructor for a InvalidMazeException
     * NO parameters taken
     */
    public InvalidMazeException (){
        super ("Error in Maze application");
    }
    /**
     * Constructor for a InvalidMazeException
     * with a given error message
     * 
     * @param message error message provided
     */
    public InvalidMazeException (String message){
        super(message);
    }
}
