package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;
import nz.ac.auckland.se206.controllers.GUI360JFxController;

public class App extends Application {
  private static App instance;
  private Scene loadingPageScene;
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

    Parent loadingPageRoot = loadFXML("loadingPage");
    loadingPageScene = new Scene(loadingPageRoot);

    GUI360JFxController GUI360JFxController = new GUI360JFxController();
    Parent levelsRoot = loadFXML("levels");
    levelScene = new Scene(levelsRoot, 1537, 800);

    GUI360JFxController.initialize();

    // Create the 3D scene
    Group root3D = GUI360JFxController.getRoot3D();
    viewerScene = new Scene(root3D, 1537, 800, true, SceneAntialiasing.BALANCED);
    viewerScene.setCamera(GUI360JFxController.getCamera());

    stage.setScene(loadingPageScene);
    stage.show();
  }

  public void switchToLevelScene() {
    Stage stage = getCurrentStage();
    if (stage != null && levelScene != null) {
      System.out.println("switchin to level scenelekafjiljf;");
      stage.setScene(levelScene);
    }
  }

  public void switchToViewerScene() {
    Stage stage = getCurrentStage();
    if (stage != null && viewerScene != null) {
      stage.setScene(viewerScene);
    }
  }

  private Stage getCurrentStage() {
    // get the current scene
    if (loadingPageScene != null && loadingPageScene.getWindow() != null) {
      return (Stage) loadingPageScene.getWindow();
    } else if (levelScene != null && levelScene.getWindow() != null) {
      System.out.println("level scene is confirmed");
      return (Stage) levelScene.getWindow();
    }
    return null;
  }

  public static App getInstance() {
    return instance;
  }
}
