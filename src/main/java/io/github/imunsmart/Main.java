package io.github.imunsmart;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point class for JavaFX application, loads the fxml file and creates the root and
 * stage.
 *
 * @author Devin Arena
 * @version 1.0
 * @since 9/19/2020
 */
public class Main extends Application {

  /**
   * Start method, loads the fxml file and creates the root and stage.
   *
   * @param primaryStage the stage to initialize
   * @throws Exception Multiple methods throw an exception, such as load() IOException, if any are
   *                   thrown the program should terminate
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    // Load the fxml file, create the root and get the controller
    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("production.fxml"));
    Parent root = loader.load();
    Controller controller = loader.getController();

    // Setup the primary stage
    primaryStage.setTitle("Production");
    primaryStage.setOnCloseRequest(e -> controller.disconnect()); // disconnect from db on close
    primaryStage.setScene(new Scene(root));
    primaryStage.setResizable(false);
    primaryStage.show();
  }


  public static void main(String[] args) {
    launch(args);
  }
}
