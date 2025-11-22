package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LoadingPageController {

  private int counter = 0;

  @FXML private Button incrementButton;
  @FXML private Button decrementButton;
  @FXML private Button resetButton;
  @FXML private Button switchButton;
  @FXML private Label numberLabel;

  @FXML
  private void initialize() {
    System.out.println(
        "************* Initialising Counter Controller ************************" + this);
  }

  @FXML
  private void increment() {
    System.out.println("incrementing");
    counter++;
    numberLabel.setText(counter + "");
  }

  @FXML
  private void decrement() {
    System.out.println("decrement");
    counter--;
    numberLabel.setText(counter + "");
  }

  @FXML
  private void reset() {
    System.out.println("reset");
    counter = 0;
    numberLabel.setText(counter + "");
  }

  @FXML
  private void switchScene(ActionEvent event) {
    System.out.println("switch scenes");
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.LEVELS));
  }
}
