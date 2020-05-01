package maze.routing;
/**
 * Defines a exception which is thrown when a route is not found
 * 
 * @author Joshwin Sundarraj
 */
public class NoRouteFoundException extends RuntimeException{
    /**
     * Constructor for a NoRouteFoundException
     * NO parameters taken
     */
    public NoRouteFoundException (){
        super ("No route found");
    }
    /**
     * Constructor for a NoRouteFoundException
     * with a given error message
     * 
     * @param message error message provided
     */
    public NoRouteFoundException (String message){
        super(message);
    }
}
