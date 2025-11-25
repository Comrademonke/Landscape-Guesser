package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.GUI360JFxController;

public class App extends Application {
  private static App instance;
  private Scene levelScene;
  private Scene viewerScene;

  public static void main(String[] args) {
    launch();
  }

  public static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    return fxmlLoader.load();
  }

  @Override
  public void start(Stage stage) throws IOException {
    instance = this;

    SceneManager.addUi(AppUi.LOADING_PAGE, loadFXML("loadingPage"));

    Parent levelsRoot = loadFXML("levels");

    GUI360JFxController GUI360JFxController = new GUI360JFxController();

    levelScene = new Scene(levelsRoot, 1537, 800);

    GUI360JFxController.initialize();

    Group root3D = GUI360JFxController.getRoot3D();

    // Create the 3D scene
    viewerScene = new Scene(root3D, 1537, 800, true, SceneAntialiasing.BALANCED);
    viewerScene.setCamera(GUI360JFxController.getCamera());

    // scene = new Scene(SceneManager.getUiRoot(AppUi.LEVELS), 1537, 800);

    stage.setScene(levelScene);
    stage.show();
  }

  public void switchToViewerScene() {
    Stage stage = getCurrentStage();
    if (stage != null && viewerScene != null) {
      stage.setScene(viewerScene);
    }
  }

  private Stage getCurrentStage() {
    // get the current scene
    if (levelScene != null && levelScene.getWindow() != null) {
      return (Stage) levelScene.getWindow();
    }
    return null;
  }

  public static App getInstance() {
    return instance;
  }
}
