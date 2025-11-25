package nz.ac.auckland.se206.controllers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import nz.ac.auckland.se206.App;

public class LoadingPageController {

  @FXML private Label landscapeLabel;
  @FXML private Label guesserLabel;
  @FXML private ProgressBar loadingBar;
  @FXML private Button switchButton;
  @FXML private Label numberLabel;
  @FXML private Label loadingLabel;

  private MediaPlayer mediaPlayer;
  private DoubleProperty progressProperty = new SimpleDoubleProperty(0);

  @FXML
  private void initialize() {
    System.out.println(
        "************* Initialising Counter Controller ************************" + this);

    playMusic();

    loadingBar.progressProperty().bind(progressProperty);

    progressProperty.addListener(
        (obs, oldVal, newVal) -> {
          loadingLabel.setText(String.format("%.0f%%", newVal.doubleValue() * 100));
          enableStartButton(newVal.doubleValue() * 100);
        });

    App.getInstance().setLoadingController(this);
  }

  public void enableStartButton(double value) {
    if (value >= 100) {
      switchButton.setDisable(false);
      switchButton.setFont(new Font("futura", 36));
    }
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
}
