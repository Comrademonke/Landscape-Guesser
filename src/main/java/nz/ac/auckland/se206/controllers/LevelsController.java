package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LevelsController {

  @FXML private Button switchToCounterButton;
  @FXML private Button levelOneButton;
  @FXML private Button levelTwoButton;
  @FXML private Button levelThreeButton;

  @FXML
  private void initialize() {
    System.out.println(
        "************* Initialising Music Controller ************************" + this);
  }

  @FXML
  private void switchToCounter(ActionEvent event) {
    System.out.println("switch scenes");
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.LOADING_PAGE));
  }

  @FXML
  private void onSwitchToLevelOne(ActionEvent event) {
    levelOneButton.setDisable(true);

    switchToLevel("level1", 1, event);
  }

  @FXML
  private void onSwitchToLevelTwo(ActionEvent event) {
    levelTwoButton.setDisable(true);

    switchToLevel("level2", 2, event);
  }

  @FXML
  private void onSwitchToLevelThree(ActionEvent event) {
    levelThreeButton.setDisable(true);

    switchToLevel("level3", 3, event);
  }

  public void switchToLevel(String level, int numberLevel, ActionEvent event) {

    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.LOADING_PAGE));
  }
}
