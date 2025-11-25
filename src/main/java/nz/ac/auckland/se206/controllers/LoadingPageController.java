package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.App;

public class LoadingPageController {

  @FXML private Label landscapeLabel;
  @FXML private Label guesserLabel;
  @FXML private ProgressBar loadingBar;

  @FXML private Button switchButton;
  @FXML private Label numberLabel;

  private MediaPlayer mediaPlayer;

  @FXML
  private void initialize() {
    System.out.println(
        "************* Initialising Counter Controller ************************" + this);

    playMusic();
  }

  private void playMusic() {
    String audioFile = getClass().getResource("/sounds/catWalkSong.mp3").toString();

    Media media = new Media(audioFile);
    mediaPlayer = new MediaPlayer(media);

    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    mediaPlayer.setVolume(0.3);

    mediaPlayer.play();
  }

  @FXML
  private void switchScene(ActionEvent event) {
    App app = App.getInstance();
    if (app != null) {
      app.switchToLevelScene();
    }
  }
}
