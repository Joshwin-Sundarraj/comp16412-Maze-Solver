import maze.Maze;
import maze.routing.RouteFinder;


public class MazeDriver {

	public static void main(String[] args) {
		//Maze maze = Maze.fromTxt(args[0]);
		Maze maze = Maze.fromTxt("../mazes/maze1.txt");
		RouteFinder newRouteFinder = new RouteFinder(maze);
		System.out.println(newRouteFinder.toString());
		boolean temp = newRouteFinder.step();
		System.out.println(newRouteFinder.toString());
		temp = newRouteFinder.step();
		System.out.println(newRouteFinder.toString());
		temp = newRouteFinder.step();
		System.out.println(newRouteFinder.toString());
		temp = newRouteFinder.step();
		System.out.println(newRouteFinder.toString());
		temp = newRouteFinder.step();
		System.out.println(newRouteFinder.toString());
		temp = newRouteFinder.step();
		System.out.println(newRouteFinder.toString());
		temp = newRouteFinder.step();
		System.out.println(newRouteFinder.toString());
		temp = newRouteFinder.step();
		System.out.println(newRouteFinder.toString());
		temp = newRouteFinder.step();
		System.out.println(newRouteFinder.toString());
		temp = newRouteFinder.step();
		System.out.println(newRouteFinder.toString());
		temp = newRouteFinder.step();
		System.out.println(newRouteFinder.toString());
		temp = newRouteFinder.step();
		System.out.println(newRouteFinder.toString());

	}

}
