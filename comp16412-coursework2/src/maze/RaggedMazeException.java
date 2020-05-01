package maze;
/**
 * Defines a exception which is thrown when maze is ragged
 * @author Joshwin Sundarraj
 * 
 */
public class RaggedMazeException extends InvalidMazeException {
    /**
     * Constructor for a RaggedMazeException
     * NO parameters taken
     */
    public RaggedMazeException(){
        super("Error: Rows of tiles are not the same length");
    }
    /**
     * Constructor for a RaggedMzeException
     * with a given error message
     * 
     * @param message error message provided
     */
    public RaggedMazeException(String message) {
        super(message);
    }

}
