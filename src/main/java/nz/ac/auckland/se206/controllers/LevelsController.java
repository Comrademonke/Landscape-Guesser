package nz.ac.auckland.se206.controllers;

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CustomMapDisplay;

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

  @FXML private ImageView levelPreview;
  @FXML private Pane levelPreviewPane;
  @FXML private Pane mapAnswerPane;
  @FXML private MapView mapAnswer;

  private boolean isScoreBoardVisible = false;

  private GUI360JFxController GUI360Controller;
  private GuessingRoomController guessingRoomController;
  private int[] randomNumbers;
  private CustomMapDisplay customMapDisplay;
  private HashMap<Integer, double[]> targetLatitudeLongitudeCoordinates;

  @FXML
  private void initialize() {
    scoreBoard.setLayoutX(2000);

    customMapDisplay = new CustomMapDisplay();
    mapAnswer.addLayer(customMapDisplay);
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

    getLatitudeLongitudeTargetInformation();

    App app = App.getInstance();
    if (app != null) {
      app.switchToViewerScene();
    }
  }

  public void getRandomArray() {
    randomNumbers = GUI360Controller.getRandomArray();
  }

  public void getLatitudeLongitudeTargetInformation() {
    targetLatitudeLongitudeCoordinates = guessingRoomController.returnLatitudeLongitude();
  }

  public void moveAnimals(ImageView image, int yPosition) {
    getRandomArray();
    TranslateTransition transition = new TranslateTransition();
    transition.setNode(image);
    transition.setToY(yPosition);
    transition.setDuration(Duration.seconds(0.5));

    transition.play();
  }

  public void setLatitudeLongitude(int value) {
    if (customMapDisplay == null || targetLatitudeLongitudeCoordinates == null) {
      return;
    }

    double[] coordinates = targetLatitudeLongitudeCoordinates.get(value);

    double latitude = coordinates[0];
    double longitude = coordinates[1];

    customMapDisplay.updateTargetMarker(new MapPoint(latitude, longitude));
  }

  @FXML
  private void moveRabbitUp() {
    moveAnimals(rabbitImage, -85);

    levelPreviewPane.setVisible(true);

    Image image =
        new Image(
            getClass()
                .getResourceAsStream(
                    "/images/panoramasSet" + randomNumbers[0] + "/levelOnePanorama2.jpg"));
    levelPreview.setImage(image);
    levelPreviewPane.setLayoutX(330);
    levelPreviewPane.setLayoutY(170);

    setLatitudeLongitude(1);
  }

  @FXML
  private void moveRabbitDown() {
    moveAnimals(rabbitImage, 0);

    levelPreviewPane.setVisible(false);
  }

  @FXML
  private void movePenguinUp() {
    moveAnimals(penguinImage, -85);

    levelPreviewPane.setVisible(true);

    Image image =
        new Image(
            getClass()
                .getResourceAsStream(
                    "/images/panoramasSet" + randomNumbers[1] + "/levelTwoPanorama2.jpg"));
    levelPreview.setImage(image);
    levelPreviewPane.setLayoutX(600);
    levelPreviewPane.setLayoutY(170);

    setLatitudeLongitude(2);
  }

  @FXML
  private void movePenguinDown() {
    moveAnimals(penguinImage, 0);

    levelPreviewPane.setVisible(false);
  }

  @FXML
  private void moveDogUp() {
    moveAnimals(dogImage, -85);

    levelPreviewPane.setVisible(true);

    Image image =
        new Image(
            getClass()
                .getResourceAsStream(
                    "/images/panoramasSet" + randomNumbers[2] + "/levelThreePanorama2.jpg"));
    levelPreview.setImage(image);
    levelPreviewPane.setLayoutX(330);
    levelPreviewPane.setLayoutY(170);

    setLatitudeLongitude(3);
  }

  @FXML
  private void moveDogDown() {
    moveAnimals(dogImage, 0);

    levelPreviewPane.setVisible(false);
  }

  @FXML
  private void movePigUp() {
    moveAnimals(pigImage, -85);

    levelPreviewPane.setVisible(true);

    Image image =
        new Image(
            getClass()
                .getResourceAsStream(
                    "/images/panoramasSet" + randomNumbers[3] + "/levelFourPanorama2.jpg"));
    levelPreview.setImage(image);
    levelPreviewPane.setLayoutX(600);
    levelPreviewPane.setLayoutY(170);

    setLatitudeLongitude(4);
  }

  @FXML
  private void movePigDown() {
    moveAnimals(pigImage, 0);

    levelPreviewPane.setVisible(false);
  }

  @FXML
  private void moveCatUp() {
    moveAnimals(catImage, -85);

    levelPreviewPane.setVisible(true);

    Image image =
        new Image(
            getClass()
                .getResourceAsStream(
                    "/images/panoramasSet" + randomNumbers[4] + "/levelFivePanorama2.jpg"));
    levelPreview.setImage(image);
    levelPreviewPane.setLayoutX(880);
    levelPreviewPane.setLayoutY(170);

    setLatitudeLongitude(5);
  }

  @FXML
  private void moveCatDown() {
    moveAnimals(catImage, 0);

    levelPreviewPane.setVisible(false);
  }

  @FXML
  private void moveFrogUp() {
    moveAnimals(frogImage, -85);

    levelPreviewPane.setVisible(true);

    Image image =
        new Image(
            getClass()
                .getResourceAsStream(
                    "/images/panoramasSet" + randomNumbers[5] + "/levelSixPanorama2.jpg"));
    levelPreview.setImage(image);
    levelPreviewPane.setLayoutX(330);
    levelPreviewPane.setLayoutY(430);

    setLatitudeLongitude(6);
  }

  @FXML
  private void moveFrogDown() {
    moveAnimals(frogImage, 0);

    levelPreviewPane.setVisible(false);
  }

  @FXML
  private void moveTigerUp() {
    moveAnimals(tigerImage, -85);

    levelPreviewPane.setVisible(true);

    Image image =
        new Image(
            getClass()
                .getResourceAsStream(
                    "/images/panoramasSet" + randomNumbers[6] + "/levelSevenPanorama2.jpg"));
    levelPreview.setImage(image);
    levelPreviewPane.setLayoutX(600);
    levelPreviewPane.setLayoutY(430);

    setLatitudeLongitude(7);
  }

  @FXML
  private void moveTigerDown() {
    moveAnimals(tigerImage, 0);

    levelPreviewPane.setVisible(false);
  }

  @FXML
  private void moveCowUp() {
    moveAnimals(cowImage, -85);

    levelPreviewPane.setVisible(true);

    Image image =
        new Image(
            getClass()
                .getResourceAsStream(
                    "/images/panoramasSet" + randomNumbers[7] + "/levelEightPanorama2.jpg"));
    levelPreview.setImage(image);
    levelPreviewPane.setLayoutX(330);
    levelPreviewPane.setLayoutY(430);

    setLatitudeLongitude(8);
  }

  @FXML
  private void moveCowDown() {
    moveAnimals(cowImage, 0);

    levelPreviewPane.setVisible(false);
  }

  @FXML
  private void moveRatUp() {
    moveAnimals(ratImage, -85);

    levelPreviewPane.setVisible(true);

    Image image =
        new Image(
            getClass()
                .getResourceAsStream(
                    "/images/panoramasSet" + randomNumbers[8] + "/levelNinePanorama2.jpg"));
    levelPreview.setImage(image);
    levelPreviewPane.setLayoutX(600);
    levelPreviewPane.setLayoutY(430);

    setLatitudeLongitude(9);
  }

  @FXML
  private void moveRatDown() {
    moveAnimals(ratImage, 0);

    levelPreviewPane.setVisible(false);
  }

  @FXML
  private void moveChickenUp() {
    moveAnimals(chickenImage, -85);

    levelPreviewPane.setVisible(true);

    Image image =
        new Image(
            getClass()
                .getResourceAsStream(
                    "/images/panoramasSet" + randomNumbers[9] + "/levelTenPanorama2.jpg"));
    levelPreview.setImage(image);
    levelPreviewPane.setLayoutX(880);
    levelPreviewPane.setLayoutY(430);

    setLatitudeLongitude(10);
  }

  @FXML
  private void moveChickenDown() {
    moveAnimals(chickenImage, 0);

    levelPreviewPane.setVisible(false);
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

  public void getGuessingScore() {
    int totalScore = guessingRoomController.getScore();

    scoreLabel.setText("Score:\n" + totalScore);
  }

  @FXML
  private void updateGuessingScore() {
    // When mouse moves update the guessing score
    getGuessingScore();
  }
}
