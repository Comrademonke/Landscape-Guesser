package nz.ac.auckland.se206.controllers;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LevelsController {
  // <Button fx:id="switchToCounterButton" layoutX="458.0" layoutY="31.0" mnemonicParsing="false"
  // onAction="#switchToCounter" text="Switch to Counter" />
  //   <Button fx:id="playFirstSongButton" layoutX="93.0" layoutY="306.0" mnemonicParsing="false"
  // onAction="#playFirstSong" text="First Song" />
  //   <Button fx:id="playSecondSongButton" layoutX="421.0" layoutY="306.0" mnemonicParsing="false"
  // onAction="#playSecondSong" text="Second Song" />

  @FXML private Button switchToCounterButton;
  @FXML private Button playFirstSongButton;
  @FXML private Button playSecondSongButton;

  private MediaPlayer mediaPlayer;

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
    if (mediaPlayer != null) {
      mediaPlayer.stop();
    }
  }

  @FXML
  private void playFirstSong() {
    if (mediaPlayer != null) {
      mediaPlayer.stop();
    }
    System.out.println("Playing first song");
    String audioFilePath = "src/main/resources/sounds/looking-forward-131923.mp3";
    File audioFile = new File(audioFilePath);

    Media media = new Media(audioFile.toURI().toString());
    mediaPlayer = new MediaPlayer(media);

    mediaPlayer.play();
  }

  @FXML
  private void playSecondSong() {
    if (mediaPlayer != null) {
      mediaPlayer.stop();
    }
    System.out.println("Playing second song");
    String audioFilePath = "src/main/resources/sounds/risk-136788.mp3";
    File audioFile = new File(audioFilePath);

    Media media = new Media(audioFile.toURI().toString());
    mediaPlayer = new MediaPlayer(media);

    mediaPlayer.play();
  }
}
