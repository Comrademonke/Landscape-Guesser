package nz.ac.auckland.se206.controllers;

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CustomMapLayer;

public class GuessingRoomController {

  @FXML private Button returnButton;
  @FXML private Button finalGuessButton;
  @FXML private Label titleLabel;
  @FXML private Pane mapDisplay;
  @FXML private MapView mapView;
  @FXML private MapView mapViewFiller;
  @FXML private Label distanceLabel;
  @FXML private StackPane scoreBoard;
  @FXML private Label scoreLabel;

  private boolean isScoreBoardVisible = false;
  private double totalScore = 0.0;
  private double dragStartX;
  private double dragStartY;
  private MapPoint dragStartCenter;
  private double currentZoom = 3.0;
  private CustomMapLayer customMapLayer;
  private MapPoint guessMarker;
  private HashMap<Integer, double[]> TargetLatitudeLongitudeCoordinates = new HashMap<>();
  private HashMap<Integer, double[]> latitudeLongitudeCoordinates = new HashMap<>();
  private HashMap<Integer, double[]> latitudeLongitudeCoordinates1 = new HashMap<>();
  private HashMap<Integer, double[]> latitudeLongitudeCoordinates2 = new HashMap<>();
  private HashMap<Integer, double[]> latitudeLongitudeCoordinates3 = new HashMap<>();
  private HashMap<Integer, double[]> latitudeLongitudeCoordinates4 = new HashMap<>();

  @FXML
  public void initialize() {
    setupMap();
    setupMarkers();
    initializeLatitudeLongitudeCoordinates();
    initializeLatitudeLongitudeCoordinates1();
    initializeLatitudeLongitudeCoordinates2();
    initializeLatitudeLongitudeCoordinates3();
    initializeLatitudeLongitudeCoordinates4();

    scoreBoard.setLayoutX(2000);
  }

  private void setupMarkers() {
    customMapLayer = new CustomMapLayer();

    mapView.addLayer(customMapLayer);

    Platform.runLater(
        () -> {
          customMapLayer.updateGuessMarker(new MapPoint(0.0, 0.0));
        });
  }

  private void setupMap() {
    mapView.setZoom(currentZoom);
    mapView.setCenter(new MapPoint(0, 0));

    mapViewFiller.setZoom(currentZoom);
    mapViewFiller.setCenter(new MapPoint(0, 180));

    mapView.prefWidthProperty().bind(mapDisplay.widthProperty());
    mapView.prefHeightProperty().bind(mapDisplay.heightProperty());

    mapViewFiller.prefWidthProperty().bind(mapDisplay.widthProperty());
    mapViewFiller.prefHeightProperty().bind(mapDisplay.heightProperty());

    // needed to create an infinite horizontal globe effect
    mapViewFiller.toBack();

    mapView.setOnScroll(
        event -> {
          double zoomFactor;
          if (event.getDeltaY() > 0) {
            zoomFactor = 1.1;
          } else {
            zoomFactor = 0.9;
          }
          currentZoom *= zoomFactor;

          // Minimum zoom distance
          if (currentZoom < 3) {
            currentZoom = 3.0;
          } else if (currentZoom > 18) {
            currentZoom = 18.0;
          }

          // Apply zoom to both maps
          mapView.setZoom(currentZoom);
          mapViewFiller.setZoom(currentZoom);

          event.consume();
        });

    mapView.setOnMousePressed(
        event -> {
          dragStartX = event.getX();
          dragStartY = event.getY();
          dragStartCenter = mapView.getCenter();
        });

    mapView.setOnMouseReleased(
        event -> {
          if (dragStartX == event.getX() && dragStartY == event.getY()) {
            MapPoint clickedPoint = mapView.getMapPosition(event.getX(), event.getY());

            customMapLayer.updateGuessMarker(clickedPoint);
            customMapLayer.updateGuessMarkerVisibility(true);
          }
        });

    mapView.setOnMouseDragged(
        event -> {
          double deltaX = event.getX() - dragStartX;
          double deltaY = event.getY() - dragStartY;

          // Adjust sensitivity here
          double scale = 3.0 * Math.exp(-0.8 * mapView.getZoom());

          double newLongitude = dragStartCenter.getLongitude() - deltaX * scale;
          double newLatitude = dragStartCenter.getLatitude() + deltaY * scale;

          newLatitude = Math.max(-90, Math.min(90, newLatitude));
          newLongitude = mapWrappingHorizontally(newLongitude);

          mapView.setCenter(new MapPoint(newLatitude, newLongitude));
          updateFillerMap(newLatitude, newLongitude);
        });
  }

  private void updateFillerMap(double latitude, double longitude) {
    double fillerMapLongitude;

    if (longitude >= 0) {
      fillerMapLongitude = longitude - 360;
    } else {
      fillerMapLongitude = longitude + 360;
    }

    mapViewFiller.setCenter(new MapPoint(latitude, fillerMapLongitude));
  }

  private double mapWrappingHorizontally(double longitude) {
    while (longitude > 180) {
      longitude -= 360;
    }
    while (longitude < -180) {
      longitude += 360;
    }
    return longitude;
  }

  @FXML
  private void onReturnSwitch(ActionEvent event) {
    App app = App.getInstance();
    if (app != null) {
      app.switchToViewerScene();
    }
  }

  @FXML
  private void switchToLevelScene() {
    App app = App.getInstance();
    if (app != null) {
      app.switchToLevelScene();
    }
  }

  @FXML
  private void onFinalGuess(ActionEvent event) {
    guessMarker = customMapLayer.returnGuessMarker();
    if (guessMarker.getLatitude() == 0.0 && guessMarker.getLongitude() == 0.0) {
      return;
    }
    if (finalGuessButton.getText().equals("CONTINUE")) {
      finalGuessButton.setText("FINALISE GUESS");

      mapDisplay.setDisable(false);
      returnButton.setDisable(false);
      distanceLabel.setVisible(false);

      customMapLayer.updateGuessMarkerVisibility(false);
      customMapLayer.updateTargetMarkerVisibility(false);
      customMapLayer.updateLineVisibility(false);
      customMapLayer.updateGuessMarker(new MapPoint(0.0, 0.0));

      switchToLevelScene();
      return;
    }

    finalGuessButton.setText("CONTINUE");
    mapDisplay.setDisable(true);
    returnButton.setDisable(true);
    customMapLayer.updateTargetMarkerVisibility(true);
    customMapLayer.updateLineVisibility(true);

    calculateDistance();

    MapPoint finalPoint = customMapLayer.returnTargetMarker();
    double fillerLongitude;
    if (finalPoint.getLongitude() >= 0) {
      fillerLongitude = finalPoint.getLongitude() - 360;
    } else {
      fillerLongitude = finalPoint.getLongitude() + 360;
    }
    MapPoint fillerFinalPoint = new MapPoint(finalPoint.getLatitude(), fillerLongitude);

    mapView.flyTo(1.0, finalPoint, 1.0);
    mapViewFiller.flyTo(1.0, fillerFinalPoint, 1.0);
  }

  private void calculateDistance() {
    MapPoint coordinate1 = customMapLayer.returnGuessMarker();
    MapPoint coordinate2 = customMapLayer.returnTargetMarker();

    double distanceKm =
        calculateHaversineDistance(
            coordinate1.getLatitude(), coordinate1.getLongitude(),
            coordinate2.getLatitude(), coordinate2.getLongitude());

    double distanceMeters = distanceKm * 1000;
    String formattedDistance = String.format("%,d", (int) distanceMeters);

    updateScore(distanceMeters);

    // Display the distance
    distanceLabel.setVisible(true);
    distanceLabel.setText("Your guess was " + formattedDistance + " meters away.");
  }

  private double calculateHaversineDistance(
      double latitude1, double longitude1, double latitude2, double longitude2) {
    // Earth's radius in kilometers
    final int Radius = 6371;

    // Convert degrees to radians
    double latitudeDistance = Math.toRadians(latitude2 - latitude1);
    double longitudeDistance = Math.toRadians(longitude2 - longitude1);

    // const R = 6371e3; // metres
    // const φ1 = lat1 * Math.PI/180; // φ, λ in radians
    // const φ2 = lat2 * Math.PI/180;
    // const Δφ = (lat2-lat1) * Math.PI/180;
    // const Δλ = (lon2-lon1) * Math.PI/180;

    // const a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
    //           Math.cos(φ1) * Math.cos(φ2) *
    //           Math.sin(Δλ/2) * Math.sin(Δλ/2);
    // const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    //
    // const d = R * c;
    // c = angular distance (in radians) a = square of half the chord length between the points
    double a =
        Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2)
            + Math.cos(Math.toRadians(latitude1))
                * Math.cos(Math.toRadians(latitude2))
                * Math.sin(longitudeDistance / 2)
                * Math.sin(longitudeDistance / 2);

    double angularDistance = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return Radius * angularDistance;
  }

  public void setLatitudeLongitude(int value) {
    // Assign values to latitude and longitude
    double[] coordinates = TargetLatitudeLongitudeCoordinates.get(value);

    double latitude = coordinates[0];
    double longitude = coordinates[1];

    customMapLayer.updateTargetMarker(new MapPoint(latitude, longitude));
  }

  public void initializeLatitudeLongitudeCoordinates() {
    // Each coordinate for each level

    // Otorohanga
    latitudeLongitudeCoordinates.put(1, new double[] {-38.1888609, 175.209955});
    // Rotorua
    latitudeLongitudeCoordinates.put(2, new double[] {-38.1338429, 176.2515456});
    // Whangarei
    latitudeLongitudeCoordinates.put(3, new double[] {-35.7252682, 174.3244103});
    // Taupo
    latitudeLongitudeCoordinates.put(4, new double[] {-38.6982061, 176.0830989});
    // Paihia
    latitudeLongitudeCoordinates.put(5, new double[] {-35.2813526, 174.091962});
    // Lake Ohia
    latitudeLongitudeCoordinates.put(6, new double[] {-34.9484003, 173.3833872});
    // Hamilton
    latitudeLongitudeCoordinates.put(7, new double[] {-37.7838193, 175.2798247});
    // Cape Reinga
    latitudeLongitudeCoordinates.put(8, new double[] {-34.4266534, 172.6774505});
    // New plymouth
    latitudeLongitudeCoordinates.put(9, new double[] {-39.0561506, 174.0705975});
    // Huka falls
    latitudeLongitudeCoordinates.put(10, new double[] {-38.6496475, 176.0894571});
  }

  public void initializeLatitudeLongitudeCoordinates1() {
    // New York
    latitudeLongitudeCoordinates1.put(1, new double[] {40.7579204, -73.9854278});
    // Madrid
    latitudeLongitudeCoordinates1.put(2, new double[] {40.420241, -3.6882143});
    // Budapest
    latitudeLongitudeCoordinates1.put(3, new double[] {47.513851, 19.0776516});
    // Cape Town
    latitudeLongitudeCoordinates1.put(4, new double[] {-33.9497494, 18.3785152});
    // Campinas
    latitudeLongitudeCoordinates1.put(5, new double[] {-22.9038587, -47.0566533});
    // Helsinki
    latitudeLongitudeCoordinates1.put(6, new double[] {60.1711584, 24.9327061});
    // Dubai
    latitudeLongitudeCoordinates1.put(7, new double[] {25.2428645, 55.3097369});
    // Dushanbe
    latitudeLongitudeCoordinates1.put(8, new double[] {38.5678291, 68.7936325});
    // Edmonton
    latitudeLongitudeCoordinates1.put(9, new double[] {53.5588149, -113.5520091});
    // Reykjavik
    latitudeLongitudeCoordinates1.put(10, new double[] {64.142474, -21.9484259});
  }

  public void initializeLatitudeLongitudeCoordinates2() {
    // Ho Chi Minh City
    latitudeLongitudeCoordinates2.put(1, new double[] {10.7830716, 106.6887789});
    // London
    latitudeLongitudeCoordinates2.put(2, new double[] {51.504771, -0.1236956});
    // Snow Monkeys
    latitudeLongitudeCoordinates2.put(3, new double[] {36.7326326, 138.4621769});
    // Ankara
    latitudeLongitudeCoordinates2.put(4, new double[] {39.9428993, 32.8546204});
    // Samoa
    latitudeLongitudeCoordinates2.put(5, new double[] {-13.8290245, -171.7667481});
    // Antwerp
    latitudeLongitudeCoordinates2.put(6, new double[] {51.2198265, 4.415719});
    // The Gambia
    latitudeLongitudeCoordinates2.put(7, new double[] {13.4361186, -16.6848346});
    // Mexico City
    latitudeLongitudeCoordinates2.put(8, new double[] {19.4359493, -99.1439456});
    // Jakarta
    latitudeLongitudeCoordinates2.put(9, new double[] {-6.2116036, 106.8070186});
    // Nairobi
    latitudeLongitudeCoordinates2.put(10, new double[] {-1.2758919, 36.8244654});
  }

  public void initializeLatitudeLongitudeCoordinates3() {
    // Moscow
    latitudeLongitudeCoordinates3.put(1, new double[] {55.7551975, 37.6188618});
    // Cairo
    latitudeLongitudeCoordinates3.put(2, new double[] {30.0598577, 31.2511909});
    // Santiago
    latitudeLongitudeCoordinates3.put(3, new double[] {-33.4410889, -70.6541727});
    // Papua New Guinea
    latitudeLongitudeCoordinates3.put(4, new double[] {-9.4373722, 147.1846428});
    // Tunis
    latitudeLongitudeCoordinates3.put(5, new double[] {36.8204964, 10.1778165});
    // Oslo
    latitudeLongitudeCoordinates3.put(6, new double[] {59.91265, 10.7458761});
    // Baghdad
    latitudeLongitudeCoordinates3.put(7, new double[] {33.3167989, 44.3549414});
    // Bogota
    latitudeLongitudeCoordinates3.put(8, new double[] {4.6759864, -74.0477919});
    // Seoul
    latitudeLongitudeCoordinates3.put(9, new double[] {37.5591777, 126.9736765});
    // Rome
    latitudeLongitudeCoordinates3.put(10, new double[] {41.8890711, 12.4937244});
  }

  public void initializeLatitudeLongitudeCoordinates4() {
    // San Francisco
    latitudeLongitudeCoordinates4.put(1, new double[] {37.7944706, -122.3931731});
    // Warsaw
    latitudeLongitudeCoordinates4.put(2, new double[] {52.2313538, 21.0104402});
    // Ulaanbaatar
    latitudeLongitudeCoordinates4.put(3, new double[] {47.9172141, 106.9196184});
    // Cameroon
    latitudeLongitudeCoordinates4.put(4, new double[] {3.8662483, 11.5144756});
    // Malaysia
    latitudeLongitudeCoordinates4.put(5, new double[] {3.1490091, 101.6942435});
    // Sao Paulo
    latitudeLongitudeCoordinates4.put(6, new double[] {-23.5557471, -46.6864073});
    // Ottawa
    latitudeLongitudeCoordinates4.put(7, new double[] {45.4243525, -75.6957637});
    // Rabat
    latitudeLongitudeCoordinates4.put(8, new double[] {33.9615334, -6.8658794});
    // Manila
    latitudeLongitudeCoordinates4.put(9, new double[] {14.6392884, 121.0341523});
    // Melbourne
    latitudeLongitudeCoordinates4.put(10, new double[] {-37.8213108, 144.9668387});
  }

  public void createTargetLocations(int[] array) {
    int i = 1;
    for (Integer set : array) {
      if (set == 1) {
        TargetLatitudeLongitudeCoordinates.put(i, latitudeLongitudeCoordinates.get(i));
      } else if (set == 2) {
        TargetLatitudeLongitudeCoordinates.put(i, latitudeLongitudeCoordinates1.get(i));
      } else if (set == 3) {
        TargetLatitudeLongitudeCoordinates.put(i, latitudeLongitudeCoordinates2.get(i));
      } else if (set == 4) {
        TargetLatitudeLongitudeCoordinates.put(i, latitudeLongitudeCoordinates3.get(i));
      } else {
        TargetLatitudeLongitudeCoordinates.put(i, latitudeLongitudeCoordinates4.get(i));
      }
      i++;
    }
  }

  @FXML
  private void moveScoreboard(KeyEvent event) {
    KeyCode code = event.getCode();

    if (code == KeyCode.Q) {
      event.consume();
      toggleScoreBoardPosition();
    }
  }

  private void toggleScoreBoardPosition() {
    if (isScoreBoardVisible) {
      flyOutAnimation();
    } else {
      flyInAnimation();
    }
    isScoreBoardVisible = !isScoreBoardVisible;
  }

  private void flyOutAnimation() {
    double startX = 1068.0;
    double endX = 2000;

    Timeline timeline =
        new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(scoreBoard.layoutXProperty(), startX)),
            new KeyFrame(Duration.millis(400), new KeyValue(scoreBoard.layoutXProperty(), endX)));
    timeline.play();
  }

  private void flyInAnimation() {
    double startX = 2000;
    double endX = 1068.0;

    scoreBoard.setLayoutX(startX);
    scoreBoard.setVisible(true);

    Timeline timeline =
        new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(scoreBoard.layoutXProperty(), startX)),
            new KeyFrame(Duration.millis(400), new KeyValue(scoreBoard.layoutXProperty(), endX)));
    timeline.play();
  }

  public void updateScore(double distance) {
    double currentScore = 5000 * Math.exp(-0.5 * distance / 14916.86);
    totalScore += currentScore;

    scoreLabel.setText("Score:\n" + (int) totalScore);
  }

  public int getScore() {
    return (int) totalScore;
  }
}
