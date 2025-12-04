package nz.ac.auckland.se206.controllers;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
  private BufferedImage[] skyBoxImages;
  private BufferedImage[] skyBoxImageLevel1 = new BufferedImage[6];
  private BufferedImage[] skyBoxImageLevel2 = new BufferedImage[6];
  private BufferedImage[] skyBoxImageLevel3 = new BufferedImage[6];
  private BufferedImage[] skyBoxImageLevel4 = new BufferedImage[6];
  private BufferedImage[] skyBoxImageLevel5 = new BufferedImage[6];
  private BufferedImage[] skyBoxImageLevel6 = new BufferedImage[6];
  private BufferedImage[] skyBoxImageLevel7 = new BufferedImage[6];
  private BufferedImage[] skyBoxImageLevel8 = new BufferedImage[6];
  private BufferedImage[] skyBoxImageLevel9 = new BufferedImage[6];
  private BufferedImage[] skyBoxImageLevel10 = new BufferedImage[6];

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
            // Storing images here
            // java.net.URL imageUrl =
            //     getClass().getResource("/images/unprocessedSet2Panoramas/Reykjavik.jpg");
            // BufferedImage image = ImageIO.read(imageUrl);
            // skyBoxImages = EquirectangularToCubic.processImage(image);

            // int j = 0;
            // for (BufferedImage imagee : skyBoxImages) {
            //   saveBufferedImage(
            //       imagee,
            //       "jpg",
            //       "src/main/resources/images/panoramasSet2/levelTenPanorama" + j + ".jpg");
            //   j++;
            // }

            int currentStep = 0;

            for (int i = 0; i < 6; i++) {
              BufferedImage originalImage =
                  ImageIO.read(
                      getClass()
                          .getResource("/images/panoramasSet1/levelOnePanorama" + i + ".jpg"));
              BufferedImage compressedImage = scaleImage(originalImage, 0.5);
              skyBoxImageLevel1[i] = compressedImage;

              updateProgress(returnProgress(currentStep++) * 100, 100.0);
            }

            for (int i = 0; i < 6; i++) {
              BufferedImage originalImage =
                  ImageIO.read(
                      getClass()
                          .getResource("/images/panoramasSet1/levelTwoPanorama" + i + ".jpg"));
              BufferedImage compressedImage = scaleImage(originalImage, 0.5);
              skyBoxImageLevel2[i] = compressedImage;

              updateProgress(returnProgress(currentStep++) * 100, 100.0);
            }

            for (int i = 0; i < 6; i++) {
              BufferedImage originalImage =
                  ImageIO.read(
                      getClass()
                          .getResource("/images/panoramasSet1/levelThreePanorama" + i + ".jpg"));
              BufferedImage compressedImage = scaleImage(originalImage, 0.5);
              skyBoxImageLevel3[i] = compressedImage;

              updateProgress(returnProgress(currentStep++) * 100, 100.0);
            }

            for (int i = 0; i < 6; i++) {
              BufferedImage originalImage =
                  ImageIO.read(
                      getClass()
                          .getResource("/images/panoramasSet1/levelFourPanorama" + i + ".jpg"));
              BufferedImage compressedImage = scaleImage(originalImage, 0.5);
              skyBoxImageLevel4[i] = compressedImage;

              updateProgress(returnProgress(currentStep++) * 100, 100.0);
            }

            for (int i = 0; i < 6; i++) {
              BufferedImage originalImage =
                  ImageIO.read(
                      getClass()
                          .getResource("/images/panoramasSet1/levelFivePanorama" + i + ".jpg"));
              BufferedImage compressedImage = scaleImage(originalImage, 0.5);
              skyBoxImageLevel5[i] = compressedImage;

              updateProgress(returnProgress(currentStep++) * 100, 100.0);
            }

            for (int i = 0; i < 6; i++) {
              BufferedImage originalImage =
                  ImageIO.read(
                      getClass()
                          .getResource("/images/panoramasSet1/levelSixPanorama" + i + ".jpg"));
              BufferedImage compressedImage = scaleImage(originalImage, 0.5);
              skyBoxImageLevel6[i] = compressedImage;

              updateProgress(returnProgress(currentStep++) * 100, 100.0);
            }

            for (int i = 0; i < 6; i++) {
              BufferedImage originalImage =
                  ImageIO.read(
                      getClass()
                          .getResource("/images/panoramasSet1/levelSevenPanorama" + i + ".jpg"));
              BufferedImage compressedImage = scaleImage(originalImage, 0.5);
              skyBoxImageLevel7[i] = compressedImage;

              updateProgress(returnProgress(currentStep++) * 100, 100.0);
            }

            for (int i = 0; i < 6; i++) {
              BufferedImage originalImage =
                  ImageIO.read(
                      getClass()
                          .getResource("/images/panoramasSet1/levelEightPanorama" + i + ".jpg"));
              BufferedImage compressedImage = scaleImage(originalImage, 0.5);
              skyBoxImageLevel8[i] = compressedImage;

              updateProgress(returnProgress(currentStep++) * 100, 100.0);
            }

            for (int i = 0; i < 6; i++) {
              BufferedImage originalImage =
                  ImageIO.read(
                      getClass()
                          .getResource("/images/panoramasSet1/levelNinePanorama" + i + ".jpg"));
              BufferedImage compressedImage = scaleImage(originalImage, 0.5);
              skyBoxImageLevel9[i] = compressedImage;

              updateProgress(returnProgress(currentStep++) * 100, 100.0);
            }

            for (int i = 0; i < 6; i++) {
              BufferedImage originalImage =
                  ImageIO.read(
                      getClass()
                          .getResource("/images/panoramasSet1/levelTenPanorama" + i + ".jpg"));
              BufferedImage compressedImage = scaleImage(originalImage, 0.5);
              skyBoxImageLevel10[i] = compressedImage;

              updateProgress(returnProgress(currentStep++) * 100, 100.0);
            }

            updateProgress(returnProgress(currentStep++) * 100, 100.0);

            skyBoxImages = skyBoxImageLevel1;
            openPanoramaImage();
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

  private double returnProgress(int step) {
    return step / (double) 60.0;
  }

  private BufferedImage scaleImage(BufferedImage original, double scale) {
    int newWidth = (int) (original.getWidth() * scale);
    int newHeight = (int) (original.getHeight() * scale);
    BufferedImage scaled = new BufferedImage(newWidth, newHeight, original.getType());

    Graphics2D smoothScaled = scaled.createGraphics();

    // Bilinear to smooth out image
    smoothScaled.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    // Higher quality over scaling speed
    // Remove if images are taking too long
    smoothScaled.setRenderingHint(
        RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

    // Antialiasing to produce less jagged edges
    smoothScaled.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Draw the scaled image
    smoothScaled.drawImage(original, 0, 0, newWidth, newHeight, null);
    smoothScaled.dispose();

    return scaled;
  }

  public static void saveBufferedImage(BufferedImage image, String formatName, String filePath) {
    try {
      File outputFile = new File(filePath);
      ImageIO.write(image, formatName, outputFile);
      System.out.println("Image successfully saved to: " + filePath);
    } catch (IOException e) {
      System.err.println("Error saving image: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void openPanoramaImage() {

    Task<Void> openPanoramas =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            Platform.runLater(
                () -> {
                  try {
                    skyboxImagesFx = new javafx.scene.image.Image[6];

                    for (int i = 0; i < 6; i++) {
                      skyboxImagesFx[i] = SwingFXUtils.toFXImage(skyBoxImages[i], null);
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
                  } catch (Exception e) {
                    System.out.println("Error creating skybox: " + e.getMessage());
                    e.printStackTrace();
                  }
                });
            return null;
          }
        };

    Thread panoramaLoader = new Thread(openPanoramas);
    panoramaLoader.start();

    openPanoramas.setOnSucceeded(
        event -> {
          Platform.runLater(
              () -> {
                atlas.getChildren().clear();
                atlas.getChildren().add(sky);
              });
        });
  }

  public void panoramaPicker(String panorama) {
    // Set up the next panorama to be loaded
    switch (panorama) {
      case "level1":
        skyBoxImages = skyBoxImageLevel1;
        break;
      case "level2":
        skyBoxImages = skyBoxImageLevel2;
        break;
      case "level3":
        skyBoxImages = skyBoxImageLevel3;
        break;
      case "level4":
        skyBoxImages = skyBoxImageLevel4;
        break;
      case "level5":
        skyBoxImages = skyBoxImageLevel5;
        break;
      case "level6":
        skyBoxImages = skyBoxImageLevel6;
        break;
      case "level7":
        skyBoxImages = skyBoxImageLevel7;
        break;
      case "level8":
        skyBoxImages = skyBoxImageLevel8;
        break;
      case "level9":
        skyBoxImages = skyBoxImageLevel9;
        break;
      case "level10":
        skyBoxImages = skyBoxImageLevel10;
        break;
    }
    openPanoramaImage();
  }
}
