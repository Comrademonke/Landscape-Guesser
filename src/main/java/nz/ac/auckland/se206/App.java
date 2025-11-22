package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class App extends Application {
  private Scene scene;

  public static void main(String[] args) {
    launch();
  }

  public static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    return fxmlLoader.load();
  }

  @Override
  public void start(Stage stage) throws IOException {
    SceneManager.addUi(AppUi.COUNTER, loadFXML("counter"));
    SceneManager.addUi(AppUi.MUSIC_PLAYER, loadFXML("musicplayer"));
    
    scene = new Scene(SceneManager.getUiRoot(AppUi.MUSIC_PLAYER), 640, 480);
    stage.setScene(scene);
    stage.show();
  }
}
