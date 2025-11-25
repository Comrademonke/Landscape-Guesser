package nz.ac.auckland.se206.controllers;

import java.awt.image.BufferedImage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javax.imageio.ImageIO;
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

  public void setUpPanoramas() {

    // Set up a task to load other panoramas
    Task<Void> panoramas =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {

            java.net.URL imageUrl = getClass().getResource("/images/Otorohanga.jpg");
            BufferedImage image = ImageIO.read(imageUrl);
            skyboxImages = EquirectangularToCubic.processImage(image);

            openPanoramaImage(skyboxImages);

            return null;
          }
        };

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
            skyboxImagesFx[4], // Up
            skyboxImagesFx[5], // Down
            skyboxImagesFx[3], // Front
            skyboxImagesFx[1], // Back
            skyboxImagesFx[0], // Left
            skyboxImagesFx[2], // Right
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
