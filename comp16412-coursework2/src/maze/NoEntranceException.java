package maze;
/**
 * Defines a exception which is thrown when NO entrances is detected
 * 
 * @author Joshwin Sundarraj
 */
public class NoEntranceException extends InvalidMazeException {
    /**
     * Constructor for a NoEntranceException
     * NO parameters taken
     */
    public NoEntranceException(){
        super("Error: No entrance found");
    }
    /**
     * Constructor for a NoEntranceException
     * with a given error message
     * 
     * @param message error message provided
     */
    public NoEntranceException(String message) {
        super(message);
    }

}
