package io.github.imunsmart;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * PopupController class, handles functionality of error popups for user input.
 *
 * @author Devin Arena
 * @version 1.0
 * @since 12/2/2020
 */
public class PopupController {

  @FXML
  private Button okayButton;

  @FXML
  private Label text;

  private Stage dialog;

  /**
   * Closes the window when okay is pressed.
   *
   * @param event the event to handle
   */
  @FXML
  public void onPress(ActionEvent event) {
    dialog.close();
  }

  /**
   * Initializes the popup dialog so it can be closed.
   *
   * @param dialog the dialog stage (to be closed when okay is pressed)
   */
  public void initialize(Stage dialog) {
    this.dialog = dialog;
  }

  /**
   * Sets the text of the label on the dialog.
   *
   * @param message the message to set the label to
   */
  public void setText(String message) {
    text.setText(message);
  }
}
