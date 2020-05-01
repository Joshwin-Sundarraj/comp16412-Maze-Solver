package maze;
/**
 * Defines a exception which is thrown when multiple entrances are detected
 * 
 * @author Joshwin Sundarraj
 */
public class MultipleEntranceException extends InvalidMazeException {
    /**
     * Constructor for a MultipleEntranceException
     * NO parameters taken
     */
    public MultipleEntranceException(){
        super("Error: Multiple entrances found");
    }
    /**
     * Constructor for a MultipleEntranceException
     * with a given error message
     * 
     * @param message error message provided
     */
    public MultipleEntranceException(String message) {
        super(message);
    }
    
}
