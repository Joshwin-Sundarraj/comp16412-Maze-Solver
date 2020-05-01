import maze.Maze;
import maze.Tile;
import maze.routing.NoRouteFoundException;
import maze.routing.RouteFinder;
import maze.InvalidMazeException;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import java.io.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Background;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.lang.Math;

/**
 * Class which provides the graphical inteface for a maze solving application.
 * Contains the componenents for the user to navigate the maze.
 * 
 * @author Joshwin Sundarraj
 */
public class MazeApplication extends Application {
	Maze maze;
	VBox root;
	HBox buttonBox;
	String currentFileName;
	GridPane pane = null;
	Button step = null;
	Button save = null;
	Button loadRoute = null;
	RouteFinder newRouteFinder = null;
	boolean finished;
	ImageView logo;

	/**
	 * Main method to launch the application
	 * 
	 * @param args list of command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Method which displays the components to be initially present.
	 * It displays the load maze & load route button as wells as assigning the associated event handlers.
	 * It also triggers the file explorer to be opened when the buttons are pressed.
	 * 
	 * @param s the stage in which all the componenents are to be displayed
	 */
	public void start(Stage s) {

		FileChooser fileChooser = new FileChooser();
		s.getIcons().add(new Image("maze/visualisation/logo.png"));
		s.setTitle("Maze Solver");
		Button load = new Button("Load maze");
		step = new Button("Step");
		save = new Button("Save");
		loadRoute = new Button("Load Route");

		load.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
					fileChooser.setInitialDirectory(new File("../mazes"));
                    File file = fileChooser.showOpenDialog(s);
                    if (file != null) {
                        loadMazeFunct(file, s);
                    }
                }
            }); 

		load.setWrapText(true);

		logo = new ImageView(new Image("maze/visualisation/lg.png"));

		buttonBox = new HBox(300);
		buttonBox.setSpacing(5);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(load,loadRoute);

		loadRoute.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(final ActionEvent e) {
						fileChooser.setInitialDirectory(new File("../routes"));
						File file = fileChooser.showOpenDialog(s);
						if (file != null) {
							loadRouteFunct(file, s);
						}
					}
		});

		root = new VBox(600);
		root.setBackground(Background.EMPTY);
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(buttonBox,logo);
		root.setSpacing(5);
		Scene scene = new Scene(root, 800, 800);
		scene.getStylesheets().add("/maze/visualisation/Style.css");
		// root.getChildren().add(loadRoute);
		s.setScene(scene);
		s.show();
	}

	/**
	 * Method which loads the maze which is in text file and displays it in the window.
	 * It also retrieves the maze object.
	 * It dispalys the step and save button and sets their event handlers.
	 * Maze is displayed as a GridPane.
	 * 
	 * @param file file path of the file containing the the text representation of the maze
	 * @param s the stage in which all the componenents are to be displayed
	 */
	private void loadMazeFunct(File file, Stage s) {

		try {
			if (pane != null) {
				root.getChildren().remove(pane);
			}

			if (root.getChildren().contains(logo)) {
				root.getChildren().remove(logo);
			}

			maze = Maze.fromTxt(file.getPath());

			currentFileName = file.getName();
			List<List<Tile>> mazeTiles = maze.getTiles();
			int rows = mazeTiles.size(); 
			int columns = mazeTiles.get(0).size();

			s.setTitle("Maze Solver");
			finished = false;
			newRouteFinder = new RouteFinder(maze);

			step.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(final ActionEvent e) {
						nextStep(s);
					}
			}); 

			save.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(final ActionEvent e) {
						saveRoute("../routes/" + currentFileName);
					}
			}); 

			pane = new GridPane();
			pane.setAlignment(Pos.CENTER);
			pane.setVgap(0);
			pane.setHgap(0);
			pane.getChildren().clear();

			float size = 0;
			if (rows >= columns){
				size = Math.round(650/rows);
			}else{
				size = Math.round(650/columns);
			}
			for (int i = 0; i < rows; i++) {
				List<Tile> currentRow = mazeTiles.get(i);
				for (int j = 0; j < columns; j++) {
					Tile currentTile = currentRow.get(j);
					Rectangle rectangle = new Rectangle();
					rectangle.setWidth(size);
					rectangle.setHeight(size);

					switch (currentTile.getType()) {
						case ENTRANCE:  rectangle.setFill(Color.GREEN);
										break;
						case EXIT: 		rectangle.setFill(Color.RED);
										break;
						case WALL: 		rectangle.setFill(Color.BLACK);
										break;
						default:		rectangle.setFill(Color.WHITE);
										break;
					}
					pane.setRowIndex(rectangle, i);
					pane.setColumnIndex(rectangle, j);
					pane.getChildren().addAll(rectangle);
				}
			}
			pane.setStyle("-fx-grid-lines-visible: true");
			if (step != null) {
				buttonBox.getChildren().remove(step);
			}
			buttonBox.getChildren().add(step);

			if (save != null) {
				buttonBox.getChildren().remove(save);
			}
			buttonBox.getChildren().add(save);

			if (loadRoute!= null) {
				buttonBox.getChildren().remove(loadRoute);
			}
			buttonBox.getChildren().add(loadRoute);
			root.getChildren().addAll(pane);

			s.show();

		} catch (FileNotFoundException e) {
			if (step != null) {
				buttonBox.getChildren().remove(step);
			}

			if (save != null) {
				buttonBox.getChildren().remove(save);
			}

			if (!root.getChildren().contains(logo)) {
				root.getChildren().add(logo);
			}
			s.show();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("File could not be found!");

			alert.showAndWait();
		} catch (IOException e) {
			if (step != null) {
				buttonBox.getChildren().remove(step);
			}

			if (save != null) {
				buttonBox.getChildren().remove(save);
			}

			if (!root.getChildren().contains(logo)) {
				root.getChildren().add(logo);
			}
			s.show();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());

			alert.showAndWait();
		} catch (InvalidMazeException e) {
			if (step != null) {
				buttonBox.getChildren().remove(step);
			}

			if (save != null) {
				buttonBox.getChildren().remove(save);
			}

			if (!root.getChildren().contains(logo)) {
				root.getChildren().add(logo);
			}
			s.show();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());

			alert.showAndWait();
		}
		
	}
	
	/**
	 * Method which causes the step to be made in the route and display that step.
	 * This is triggered by pressing the step button.
	 * If the route has been completed/undetected an alert with the information will pop up.
	 * 
	 * @param s the stage in which all the componenents are to be displayed
	 */
	private void nextStep(Stage s) {
		try {
			finished = newRouteFinder.step();

			if (finished) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Maze Information");
				alert.setHeaderText(null);
				alert.setContentText("Route has been found!");

				alert.showAndWait();
			}
			else {
				display(s);
			}
		} catch (NoRouteFoundException e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Maze Information");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());

			alert.showAndWait();
		}

	}

	/**
	 * Method which causes the current route on display to be saved into a .route file.
	 * It is triggered by pressing the save button.
	 * This is done by calling the save method in the RouteFinder object
	 * 
	 * @param filename file path of the file in which the details of the route will be saved
	 */
	private void saveRoute(String filename) {
		try{
			filename = filename.replace(".txt", "");
			filename = filename + ".route";
			newRouteFinder.save(filename);
		} catch (IOException e) {
			if (!root.getChildren().contains(logo)) {
				root.getChildren().add(logo);
			}
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("An error occurred.");

			alert.showAndWait();
		}
	}

	/**
	 * Method which loads the contents of the .route file and displays the maze representaion in the window.
	 * It is triggered by pressing the load route button.
	 * It calls the load method in RouteFinder and receives a new RouteFinder object.
	 * It also updates the maze variable the finished variable
	 * 
	 * @param file filepath of the .route file containing the details of the route
	 * @param s the stage in which all the componenents are to be displayed
	 */
	private void loadRouteFunct(File file, Stage s) {
		try {
			if (!file.getName().contains(".route")){
				throw new IOException("Invalid file type");
			}
			newRouteFinder = RouteFinder.load("../routes/" + file.getName());
			currentFileName = file.getName();
			currentFileName = currentFileName.replace(".route", "");
			maze = newRouteFinder.getMaze();
			finished = newRouteFinder.isFinished();
			display(s);
			if (!buttonBox.getChildren().contains(step)) {
				buttonBox.getChildren().add(step);
			}
			
			if (!buttonBox.getChildren().contains(save)) {
				buttonBox.getChildren().add(save);
			}
			s.show();
		} catch (FileNotFoundException e) {
			if (step != null) {
				buttonBox.getChildren().remove(step);
			}

			if (save != null) {
				buttonBox.getChildren().remove(save);
			}

			if (root.getChildren().contains(pane)) {
				root.getChildren().remove(pane);
				pane = null;
			}
			if (!root.getChildren().contains(logo)) {
				root.getChildren().add(logo);
			}
			s.show();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("File could not be found.");

			alert.showAndWait();
		} catch (IOException e) {
			if (step != null) {
				buttonBox.getChildren().remove(step);
			}

			if (save != null) {
				buttonBox.getChildren().remove(save);
			}

			if (root.getChildren().contains(pane)) {
				root.getChildren().remove(pane);
				pane = null;
			}
			if (!root.getChildren().contains(logo)) {
				root.getChildren().add(logo);
			}
			s.show();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());

			alert.showAndWait();
		} catch (ClassNotFoundException e) {
			if (step != null) {
				buttonBox.getChildren().remove(step);
			}

			if (save != null) {
				buttonBox.getChildren().remove(save);
			}

			if (root.getChildren().contains(pane)) {
				root.getChildren().remove(pane);
				pane = null;
			}
			if (!root.getChildren().contains(logo)) {
				root.getChildren().add(logo);
			}
			s.show();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("File could not be deserialized.");

			alert.showAndWait();
		}
	}

	/**
	 * Method which displays the maze and route based on the state in the RouteFinder object.
	 * It does this by recieving the string representation of the maze. 
	 * It converts each character into a rectangle with the respective colour.
	 * 
	 * @param s the stage in which all the componenents are to be displayed
	 */
	private void display(Stage s) {
		String mazeString = newRouteFinder.toString();
			
			List<List<String>> mazeStringGrid = new ArrayList<List<String>>();
			
			String[] arrOfStr = mazeString.split("\n"); 
			
			for (String a : arrOfStr) {
				List<String> line = Arrays.asList(a.split(" "));
				mazeStringGrid.add(line);
			}
			
			root.getChildren().remove(pane);

			pane = new GridPane();
			pane.setAlignment(Pos.CENTER);
			pane.setVgap(0);
			pane.setHgap(0);
			pane.getChildren().clear();

			float size = 0;
			if (mazeStringGrid.size() >= mazeStringGrid.get(0).size()){
				size = Math.round(650/mazeStringGrid.size());
			}else{
				size = Math.round(650/mazeStringGrid.get(0).size());
			}
			for (int i = 0; i < mazeStringGrid.size(); i++) {
				List<String> currentLine = mazeStringGrid.get(i);
				for (int j = 0; j < currentLine.size(); j++) {
					String currentChar = currentLine.get(j);
					Rectangle rectangle = new Rectangle();
					rectangle.setWidth(size);
					rectangle.setHeight(size);
					switch (currentChar) {
						case "e":  rectangle.setFill(Color.GREEN);
										break;
						case "x": 		rectangle.setFill(Color.RED);
										break;
						case "#": 		rectangle.setFill(Color.BLACK);
										break;
						case "*":		rectangle.setFill(Color.MAGENTA);
										break;
						case "-":		rectangle.setFill(Color.GRAY);
										break;
						default:		rectangle.setFill(Color.WHITE);
										break;
					}
					pane.setRowIndex(rectangle, i);
					pane.setColumnIndex(rectangle, j);
					pane.getChildren().addAll(rectangle);
				}
			}

			pane.setStyle("-fx-grid-lines-visible: true");
			root.getChildren().add(pane);
			s.show();
	} // display
}
