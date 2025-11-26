package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import nz.ac.auckland.se206.controllers.GUI360JFxController;
import nz.ac.auckland.se206.controllers.LevelsController;
import nz.ac.auckland.se206.controllers.LoadingPageController;

public class App extends Application {
  private static App instance;
  private Scene loadingPageScene;
  private Scene levelScene;
  private Scene viewerScene;
  private LoadingPageController loadingPageController;

  private Stage buttonStage;
  private Button switchButton;
  private Pane buttonPane;

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

    FXMLLoader levelsLoader = new FXMLLoader(App.class.getResource("/fxml/levels.fxml"));
    Parent levelsRoot = levelsLoader.load();
    LevelsController levelsController = levelsLoader.getController();
    levelScene = new Scene(levelsRoot, 1537, 800);

    // Inject controller
    levelsController.setGUI360Controller(GUI360JFxController);

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
      stage.setScene(levelScene);
    }
    if (buttonPane != null) {
      buttonPane.setVisible(false);
    }
  }

  public void switchToViewerScene() {
    Stage stage = getCurrentStage();
    if (stage != null && viewerScene != null) {
      stage.setScene(viewerScene);
      createOverlayButton(stage, "Make a guess!", this::switchToLevelScene);
    }
  }

  private Stage getCurrentStage() {
    // get the current scene
    if (loadingPageScene != null && loadingPageScene.getWindow() != null) {
      return (Stage) loadingPageScene.getWindow();
    } else if (levelScene != null && levelScene.getWindow() != null) {
      return (Stage) levelScene.getWindow();
    } else if (viewerScene != null && viewerScene.getWindow() != null) {
      return (Stage) viewerScene.getWindow();
    }
    return null;
  }

  public static App getInstance() {
    return instance;
  }

  public void setLoadingController(LoadingPageController controller) {
    this.loadingPageController = controller;
  }

  public LoadingPageController getLoadingController() {
    return loadingPageController;
  }

  private void createOverlayButton(Stage mainStage, String buttonText, Runnable switchAction) {
    // Check for button stage
    if (buttonStage != null) {
      buttonStage.close();
    }

    // Create a transparent stage for the button
    buttonStage = new Stage();
    buttonStage.initOwner(mainStage);
    buttonStage.initStyle(StageStyle.TRANSPARENT);

    // Button with same styling format as css buttons
    switchButton = new Button(buttonText);
    switchButton.setStyle(
        "-fx-background-color: linear-gradient(to bottom, #44c767 0%, #5cbf2a 100%);"
            + " -fx-background-radius: 15px;-fx-font-size: 25px; -fx-border-color:"
            + " #03fa1c;-fx-border-radius: 15px; -fx-border-width: 1px;-fx-text-fill:"
            + " #000000;-fx-cursor: hand;-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0,"
            + " 1); -fx-font-family: futura; -fx-font-weight: bold;");

    switchButton.setPrefWidth(300);
    switchButton.setPrefHeight(50);
    switchButton.setLayoutX(0);
    switchButton.setLayoutY(0);

    switchButton.setOnAction(e -> switchAction.run());

    // Create transparent pane for button
    buttonPane = new Pane(switchButton);
    buttonPane.setStyle("-fx-background-color: transparent;");

    buttonPane.setVisible(true);

    Scene buttonScene = new Scene(buttonPane, 300, 53);
    buttonScene.setFill(null);

    buttonStage.setScene(buttonScene);
    buttonStage.setX(mainStage.getX() + 1200);
    buttonStage.setY(mainStage.getY() + 750);
    buttonStage.show();

    // Move button relative to the main screen
    mainStage
        .xProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              buttonStage.setX(mainStage.getX() + 1000);
            });
    mainStage
        .yProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              buttonStage.setY(mainStage.getY() + 750);
            });
  }
}
