package maze;
/**
 * Defines a exception which is thrown when multiple exit are detected
 * 
 * @author Joshwin Sundarraj
 */
public class MultipleExitException extends InvalidMazeException {
    /**
     * Constructor for a MultipleExitException
     * NO parameters taken
     */
    public MultipleExitException(){
        super("Error: Multiple exits found");
    }
    /**
     * Constructor for a MultipleExitException
     * with a given error message
     * 
     * @param message error message provided
     */
    public MultipleExitException(String message) {
        super(message);
    }

}
