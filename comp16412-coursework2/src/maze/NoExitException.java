package maze;
/**
 * Defines a exception which is thrown when No exit is detected
 * 
 * @author Joshwin Sundarraj
 */
public class NoExitException extends InvalidMazeException {
    /**
     * Constructor for a NoExitException
     * NO parameters taken
     */
    public NoExitException(){
        super("Error: No exit found");
    }
    /**
     * Constructor for a NoExitException
     * with a given error message
     * 
     * @param message error message provided
     */
    public NoExitException(String message) {
        super(message);
    }

}
