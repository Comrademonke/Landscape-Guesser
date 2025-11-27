package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;

public class GuessingRoomController {

  @FXML private Button returnButton;
  @FXML private Button finalGuessButton;
  @FXML private Label titleLabel;
  @FXML private Pane mapDisplay;

  @FXML
  private void onReturnSwitch(ActionEvent event) {
    System.out.println("switching to viewer");
    App app = App.getInstance();
    if (app != null) {
      app.switchToViewerScene();
    }
  }

  @FXML
  private void switchToLevelScene() {
    System.out.println("switch to viewer");
    App app = App.getInstance();
    if (app != null) {
      app.switchToLevelScene();
    }
  }

  @FXML
  private void onFinalGuess(ActionEvent event) {
    switchToLevelScene();
  }
}
