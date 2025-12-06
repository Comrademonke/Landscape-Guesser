package nz.ac.auckland.se206.controllers;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

public class LoadingPageController {

  @FXML private Label landscapeLabel;
  @FXML private Label guesserLabel;
  @FXML private ProgressBar loadingBar;
  @FXML private Button switchButton;
  @FXML private Label numberLabel;
  @FXML private Label loadingLabel;
  @FXML private ImageView imageProgress;
  @FXML private ImageView owlAnimal;
  @FXML private ImageView snakeAnimal;
  @FXML private ImageView monkeyAnimal;
  @FXML private ImageView giraffeAnimal;
  @FXML private ImageView foxAnimal;
  @FXML private Pane settingsPane;
  @FXML private Slider soundBar;
  @FXML private Pane tutorialPane;
  @FXML private Button goLeftButton;
  @FXML private ImageView tutorialView;
  @FXML private Label tutorialLabel;

  private MediaPlayer mediaPlayer;
  private DoubleProperty progressProperty = new SimpleDoubleProperty(0);
  private boolean isSettingsOpen = false;
  private boolean isTutorialOpen = false;
  private int tutorialPage = 0;
  private Image scoreboardGif =
      new Image(getClass().getResource("/images/scoreboardTutorial.gif").toString());
  private Image panningGif =
      new Image(getClass().getResource("/images/panningTutorial.gif").toString());

  @FXML
  private void initialize() {
    owlAnimal.setVisible(false);
    snakeAnimal.setVisible(false);
    monkeyAnimal.setVisible(false);
    giraffeAnimal.setVisible(false);
    foxAnimal.setVisible(false);

    isSettingsOpen = false;
    tutorialPane.setDisable(!isSettingsOpen);
    tutorialPane.setVisible(isSettingsOpen);

    isTutorialOpen = false;
    settingsPane.setDisable(!isTutorialOpen);
    settingsPane.setVisible(isTutorialOpen);

    tutorialPageUpdate();

    soundBar.setValue(30.0);

    soundBar
        .valueProperty()
        .addListener(
            (observableValue, number, newValue) -> {
              mediaPlayer.setVolume(newValue.doubleValue() / 100);
            });

    playMusic();

    loadingBar.progressProperty().bind(progressProperty);

    progressProperty.addListener(
        (obs, oldVal, newVal) -> {
          loadingLabel.setText(String.format("%.0f%%", newVal.doubleValue() * 100));
          imageProgress.setLayoutX(78 + (newVal.doubleValue() * 1300));
          enableStartButton(newVal.doubleValue() * 100);
        });

    App.getInstance().setLoadingController(this);
  }

  public void enableStartButton(double value) {
    if (value >= 100) {
      switchButton.setDisable(false);
      switchButton.setFont(new Font("futura", 36));
      moveAnimals();
    }
  }

  public void moveAnimals() {
    moveMiddleAnimals(owlAnimal, -50, -60, -35);
    moveMiddleAnimals(snakeAnimal, 0, -70, 0);
    moveMiddleAnimals(monkeyAnimal, 0, -70, 0);
    moveMiddleAnimals(giraffeAnimal, 0, -75, 0);
    moveMiddleAnimals(foxAnimal, 50, -65, 35);
  }

  public void moveMiddleAnimals(ImageView image, int xPosition, int yPosition, int rotationAngle) {
    TranslateTransition transition = new TranslateTransition();
    transition.setNode(image);
    transition.setToX(xPosition);
    transition.setToY(yPosition);
    transition.setDuration(Duration.seconds(1));
    image.setVisible(true);

    RotateTransition rotateTransition = new RotateTransition();
    rotateTransition.setNode(image);
    rotateTransition.setByAngle(rotationAngle);
    rotateTransition.setDuration(Duration.seconds(1));

    transition.play();
    rotateTransition.play();
  }

  private void playMusic() {
    String audioFile = getClass().getResource("/sounds/catWalkSong.mp3").toString();

    Media media = new Media(audioFile);
    mediaPlayer = new MediaPlayer(media);

    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    mediaPlayer.setVolume(0.3);

    mediaPlayer.play();
  }

  public DoubleProperty progressProperty() {
    return progressProperty;
  }

  public void setProgress(double progress) {
    progressProperty.set(progress);
  }

  @FXML
  private void switchScene(ActionEvent event) {
    App app = App.getInstance();
    if (app != null) {
      app.switchToLevelScene();
    }
  }

  @FXML
  private void onOpenSettings(ActionEvent event) {
    isSettingsOpen = true;
    settingsPane.setDisable(!isSettingsOpen);
    settingsPane.setVisible(isSettingsOpen);
  }

  @FXML
  private void onExitSettings(ActionEvent event) {
    isSettingsOpen = false;
    settingsPane.setDisable(!isSettingsOpen);
    settingsPane.setVisible(isSettingsOpen);
  }

  @FXML
  private void onOpenTutorial(ActionEvent event) {
    isTutorialOpen = true;
    tutorialPane.setDisable(!isTutorialOpen);
    tutorialPane.setVisible(isTutorialOpen);
  }

  private void onExitTutorial() {
    isTutorialOpen = false;
    tutorialPane.setDisable(!isTutorialOpen);
    tutorialPane.setVisible(isTutorialOpen);
  }

  @FXML
  private void onGoPrevious() {
    tutorialPage--;
    tutorialPageUpdate();
  }

  @FXML
  private void onGoNext() {
    tutorialPage++;
    tutorialPageUpdate();
  }

  private void tutorialPageUpdate() {
    // Update settings
    if (tutorialPage == 0) {
      goLeftButton.setDisable(true);
      goLeftButton.setVisible(false);

      tutorialLabel.setText("Press q to toggle scoreboard");
      tutorialView.setImage(scoreboardGif);

    } else if (tutorialPage == 1) {
      goLeftButton.setDisable(false);
      goLeftButton.setVisible(true);

      tutorialLabel.setText("Use mouse to pan and zoom");
      tutorialView.setImage(panningGif);

    } else {
      tutorialPage--;
      onExitTutorial();
    }
    return;
  }
}
