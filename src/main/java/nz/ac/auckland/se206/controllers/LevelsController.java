package nz.ac.auckland.se206.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

public class LevelsController {

  @FXML private Button switchToCounterButton;
  @FXML private Button levelOneButton;
  @FXML private Button levelTwoButton;
  @FXML private Button levelThreeButton;
  @FXML private Button levelFourButton;
  @FXML private Button levelFiveButton;
  @FXML private Button levelSixButton;
  @FXML private Button levelSevenButton;
  @FXML private Button levelEightButton;
  @FXML private Button levelNineButton;
  @FXML private Button levelTenButton;

  @FXML private ImageView rabbitImage;
  @FXML private ImageView penguinImage;
  @FXML private ImageView dogImage;
  @FXML private ImageView pigImage;
  @FXML private ImageView catImage;
  @FXML private ImageView frogImage;
  @FXML private ImageView tigerImage;
  @FXML private ImageView cowImage;
  @FXML private ImageView ratImage;
  @FXML private ImageView chickenImage;

  @FXML private StackPane scoreBoard;
  @FXML private Label scoreLabel;

  private boolean isScoreBoardVisible = false;

  private GUI360JFxController GUI360Controller;
  private GuessingRoomController guessingRoomController;

  @FXML
  private void initialize() {
    scoreBoard.setLayoutX(2000);
  }

  public void setGUI360Controller(GUI360JFxController GUI360Controller) {
    this.GUI360Controller = GUI360Controller;
  }

  public void setGuessingRoomController(GuessingRoomController guessingRoomController) {
    this.guessingRoomController = guessingRoomController;
  }

  @FXML
  private void onSwitchToLevelOne(ActionEvent event) {
    levelOneButton.setText("✔");
    levelOneButton.setDisable(true);
    rabbitImage.setLayoutY(rabbitImage.getLayoutY() - 85.0);

    switchToLevel("level1", 1, event);
  }

  @FXML
  private void onSwitchToLevelTwo(ActionEvent event) {
    levelTwoButton.setText("✔");
    levelTwoButton.setDisable(true);
    penguinImage.setLayoutY(penguinImage.getLayoutY() - 85.0);

    switchToLevel("level2", 2, event);
  }

  @FXML
  private void onSwitchToLevelThree(ActionEvent event) {
    levelThreeButton.setText("✔");
    levelThreeButton.setDisable(true);
    dogImage.setLayoutY(dogImage.getLayoutY() - 85.0);

    switchToLevel("level3", 3, event);
  }

  @FXML
  private void onSwitchToLevelFour(ActionEvent event) {
    levelFourButton.setText("✔");
    levelFourButton.setDisable(true);
    pigImage.setLayoutY(pigImage.getLayoutY() - 85.0);

    switchToLevel("level4", 4, event);
  }

  @FXML
  private void onSwitchToLevelFive(ActionEvent event) {
    levelFiveButton.setText("✔");
    levelFiveButton.setDisable(true);
    catImage.setLayoutY(catImage.getLayoutY() - 85.0);

    switchToLevel("level5", 5, event);
  }

  @FXML
  private void onSwitchToLevelSix(ActionEvent event) {
    levelSixButton.setText("✔");
    levelSixButton.setDisable(true);
    frogImage.setLayoutY(frogImage.getLayoutY() - 85.0);

    switchToLevel("level6", 6, event);
  }

  @FXML
  private void onSwitchToLevelSeven(ActionEvent event) {
    levelSevenButton.setText("✔");
    levelSevenButton.setDisable(true);
    tigerImage.setLayoutY(tigerImage.getLayoutY() - 85.0);

    switchToLevel("level7", 7, event);
  }

  @FXML
  private void onSwitchToLevelEight(ActionEvent event) {
    levelEightButton.setText("✔");
    levelEightButton.setDisable(true);
    cowImage.setLayoutY(cowImage.getLayoutY() - 85.0);

    switchToLevel("level8", 8, event);
  }

  @FXML
  private void onSwitchToLevelNine(ActionEvent event) {
    levelNineButton.setText("✔");
    levelNineButton.setDisable(true);
    ratImage.setLayoutY(ratImage.getLayoutY() - 85.0);

    switchToLevel("level9", 9, event);
  }

  @FXML
  private void onSwitchToLevelTen(ActionEvent event) {
    levelTenButton.setText("✔");
    levelTenButton.setDisable(true);
    chickenImage.setLayoutY(chickenImage.getLayoutY() - 85.0);

    switchToLevel("level10", 10, event);
  }

  public void switchToLevel(String level, int numberLevel, ActionEvent event) {

    GUI360Controller.panoramaPicker(level);
    guessingRoomController.setLatitudeLongitude(numberLevel);

    App app = App.getInstance();
    if (app != null) {
      app.switchToViewerScene();
    }
  }

  public void moveAnimals(ImageView image, int yPosition) {
    TranslateTransition transition = new TranslateTransition();
    transition.setNode(image);
    transition.setToY(yPosition);
    transition.setDuration(Duration.seconds(0.5));

    transition.play();
  }

  @FXML
  private void moveRabbitUp() {
    moveAnimals(rabbitImage, -85);
  }

  @FXML
  private void moveRabbitDown() {
    moveAnimals(rabbitImage, 0);
  }

  @FXML
  private void movePenguinUp() {
    moveAnimals(penguinImage, -85);
  }

  @FXML
  private void movePenguinDown() {
    moveAnimals(penguinImage, 0);
  }

  @FXML
  private void moveDogUp() {
    moveAnimals(dogImage, -85);
  }

  @FXML
  private void moveDogDown() {
    moveAnimals(dogImage, 0);
  }

  @FXML
  private void movePigUp() {
    moveAnimals(pigImage, -85);
  }

  @FXML
  private void movePigDown() {
    moveAnimals(pigImage, 0);
  }

  @FXML
  private void moveCatUp() {
    moveAnimals(catImage, -85);
  }

  @FXML
  private void moveCatDown() {
    moveAnimals(catImage, 0);
  }

  @FXML
  private void moveFrogUp() {
    moveAnimals(frogImage, -85);
  }

  @FXML
  private void moveFrogDown() {
    moveAnimals(frogImage, 0);
  }

  @FXML
  private void moveTigerUp() {
    moveAnimals(tigerImage, -85);
  }

  @FXML
  private void moveTigerDown() {
    moveAnimals(tigerImage, 0);
  }

  @FXML
  private void moveCowUp() {
    moveAnimals(cowImage, -85);
  }

  @FXML
  private void moveCowDown() {
    moveAnimals(cowImage, 0);
  }

  @FXML
  private void moveRatUp() {
    moveAnimals(ratImage, -85);
  }

  @FXML
  private void moveRatDown() {
    moveAnimals(ratImage, 0);
  }

  @FXML
  private void moveChickenUp() {
    moveAnimals(chickenImage, -85);
  }

  @FXML
  private void moveChickenDown() {
    moveAnimals(chickenImage, 0);
  }

  @FXML
  private void moveScoreboard(KeyEvent event) {
    KeyCode code = event.getCode();

    if (code == KeyCode.Q) {
      event.consume();
      toggleScoreBoardPosition();
    }
  }

  private void toggleScoreBoardPosition() {
    if (isScoreBoardVisible) {
      flyOutAnimation();
    } else {
      flyInAnimation();
    }
    isScoreBoardVisible = !isScoreBoardVisible;
  }

  private void flyOutAnimation() {
    double startX = 1068.0;
    double endX = 2000;

    Timeline timeline =
        new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(scoreBoard.layoutXProperty(), startX)),
            new KeyFrame(Duration.millis(400), new KeyValue(scoreBoard.layoutXProperty(), endX)));
    timeline.play();
  }

  private void flyInAnimation() {
    double startX = 2000;
    double endX = 1068.0;

    scoreBoard.setLayoutX(startX);
    scoreBoard.setVisible(true);

    Timeline timeline =
        new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(scoreBoard.layoutXProperty(), startX)),
            new KeyFrame(Duration.millis(400), new KeyValue(scoreBoard.layoutXProperty(), endX)));
    timeline.play();
  }
}
