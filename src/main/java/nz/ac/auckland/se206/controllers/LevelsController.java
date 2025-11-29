package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;

public class LevelsController {

  @FXML private Button switchToCounterButton;
  @FXML private Button levelOneButton;
  @FXML private Button levelTwoButton;
  @FXML private Button levelThreeButton;

  private GUI360JFxController GUI360Controller;
  private GuessingRoomController guessingRoomController;

  @FXML
  private void initialize() {
    System.out.println(
        "************* Initialising Music Controller ************************" + this);
  }

  public void setGUI360Controller(GUI360JFxController GUI360Controller) {
    this.GUI360Controller = GUI360Controller;
  }

  public void setGuessingRoomController(GuessingRoomController guessingRoomController) {
    this.guessingRoomController = guessingRoomController;
  }

  @FXML
  private void onSwitchToLevelOne(ActionEvent event) {
    // levelOneButton.setDisable(true);

    switchToLevel("level1", 1, event);
  }

  @FXML
  private void onSwitchToLevelTwo(ActionEvent event) {
    // levelTwoButton.setDisable(true);

    switchToLevel("level2", 2, event);
  }

  @FXML
  private void onSwitchToLevelThree(ActionEvent event) {
    // levelThreeButton.setDisable(true);

    switchToLevel("level3", 3, event);
  }

  @FXML
  private void onSwitchToLevelFour(ActionEvent event) {
    // levelFourButton.setDisable(true);

    switchToLevel("level4", 4, event);
  }

  @FXML
  private void onSwitchToLevelFive(ActionEvent event) {
    // levelFiveButton.setDisable(true);

    switchToLevel("level5", 5, event);
  }

  @FXML
  private void onSwitchToLevelSix(ActionEvent event) {
    // levelSixButton.setDisable(true);

    switchToLevel("level6", 6, event);
  }

  public void switchToLevel(String level, int numberLevel, ActionEvent event) {

    GUI360Controller.panoramaPicker(level);
    guessingRoomController.setLatitudeLongitude(numberLevel);

    App app = App.getInstance();
    if (app != null) {
      app.switchToViewerScene();
    }
  }
}
