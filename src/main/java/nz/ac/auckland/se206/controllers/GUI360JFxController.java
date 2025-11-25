package nz.ac.auckland.se206.controllers;

import java.awt.image.BufferedImage;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Util.EquirectangularToCubic;
import org.fxyz3d.scene.Skybox;

public class GUI360JFxController {

  @FXML private Group root3D;
  @FXML private Group atlas;
  @FXML private AmbientLight light;
  @FXML private PerspectiveCamera camera;
  @FXML private StackPane panoramaContainer;

  private DoubleProperty anglex;
  private DoubleProperty angley;
  private DoubleProperty FOV;
  private Double anchorX;
  private Double anchorY;
  private Double anchorAngleX;
  private Double anchorAngleY;

  private Skybox sky;
  private BufferedImage[] skyboxImages;

  private javafx.scene.image.Image[] skyboxImagesFx;

  public void initialize() {
    root3D = new Group();
    camera = new PerspectiveCamera(true);
    light = new AmbientLight(Color.WHITE);
    atlas = new Group();

    root3D.getChildren().addAll(camera, light, atlas);

    camera.setTranslateX(0);
    camera.setTranslateY(0);
    camera.setTranslateZ(0);
    camera.setNearClip(0.1);
    camera.setFarClip(10000.0);

    setupCamera();
    setupMouseControls();
    setUpPanoramas();
  }

  public Group getRoot3D() {
    return root3D;
  }

  public Group getAtlas() {
    return atlas;
  }

  public PerspectiveCamera getCamera() {
    return camera;
  }

  public AmbientLight getAmbientLight() {
    return light;
  }

  private void setupCamera() {
    FOV = new SimpleDoubleProperty(60.0);
    camera.fieldOfViewProperty().bind(FOV);

    Rotate rx = new Rotate(180.0, Rotate.X_AXIS);
    camera.getTransforms().add(rx);

    Rotate rotx = new Rotate(0.0, Rotate.X_AXIS);
    Rotate roty = new Rotate(0.0, Rotate.Y_AXIS);
    anglex = new SimpleDoubleProperty(0.0);
    angley = new SimpleDoubleProperty(0.0);
    rotx.angleProperty().bind(anglex);
    roty.angleProperty().bind(angley);
    atlas.getTransforms().addAll(rotx, roty);
  }

  private void setupMouseControls() {
    Scene scene = root3D.getScene();
    if (scene != null) {
      setupSceneMouseControls(scene);
    } else {
      root3D
          .sceneProperty()
          .addListener(
              (observable, oldScene, newScene) -> {
                if (newScene != null) {
                  setupSceneMouseControls(newScene);
                }
              });
    }
  }

  private void setupSceneMouseControls(Scene scene) {
    // Set up x and y mouse movement
    scene.setOnMousePressed(
        event -> {
          anchorX = event.getSceneX();
          anchorY = event.getSceneY();
          anchorAngleX = anglex.get();
          anchorAngleY = angley.get();
        });

    scene.setOnMouseDragged(
        event -> {
          if (anchorX != null && anchorY != null) {
            anglex.set(anchorAngleX + (anchorY - event.getSceneY()) * FOV.getValue() / 600.0);
            angley.set(anchorAngleY + (anchorX - event.getSceneX()) * FOV.getValue() / 600.0);
          }
        });

    // Set up mouse zoom
    scene.setOnScroll(
        event -> {
          double newFOV = FOV.getValue() - (event.getDeltaX() + event.getDeltaY()) / 20.0;
          FOV.setValue(returnInsideRange(newFOV, 20.0, 100.0));
        });
  }

  private Double returnInsideRange(Double value, Double min, Double max) {
    return Math.min(Math.max(value, min), max);
  }

  public void setUpPanoramas() {
    LoadingPageController loadingPageController = App.getInstance().getLoadingController();

    // Set up a task to load other panoramas
    Task<Void> panoramas =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            java.net.URL imageUrl = getClass().getResource("/images/Otorohanga.jpg");
            BufferedImage image = ImageIO.read(imageUrl);
            skyboxImages = EquirectangularToCubic.processImage(image);

            openPanoramaImage(skyboxImages);
            updateProgress(100.0, 100.0);
            return null;
          }
        };

    panoramas
        .progressProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (loadingPageController != null) {
                Platform.runLater(
                    () -> {
                      loadingPageController.progressProperty().set(newVal.doubleValue());
                    });
              }
            });

    Thread panoramaLoader = new Thread(panoramas);
    panoramaLoader.start();

    panoramas.setOnSucceeded(
        event -> {
          System.out.println("panorama loading completed");
        });
  }

  public void openPanoramaImage(BufferedImage[] image) {
    skyboxImages = image;

    skyboxImagesFx = new javafx.scene.image.Image[6];

    for (int i = 0; i < 6; i++) {
      skyboxImagesFx[i] = SwingFXUtils.toFXImage(skyboxImages[i], null);
    }

    sky =
        new Skybox(
            skyboxImagesFx[4], // Bottom
            skyboxImagesFx[5], // Top
            skyboxImagesFx[3], // Right
            skyboxImagesFx[1], // Left
            skyboxImagesFx[2], // Front
            skyboxImagesFx[0], // Back
            1000,
            camera);

    sky.setTranslateX(0);
    sky.setTranslateY(0);
    sky.setTranslateZ(0);

    atlas.getChildren().clear();
    atlas.getChildren().add(sky);

    System.out.println("Skybox created successfully - you should be inside the cube now!");
  }
}
