package nz.ac.auckland.se206.controllers;

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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

  private double dragStartX;
  private double dragStartY;
  private MapPoint dragStartCenter;
  private double currentZoom = 3.0;
  private CustomMapLayer customMapLayer;
  private MapPoint guessMarker;
  private HashMap<Integer, double[]> latitudeLongitudeCoordinates = new HashMap<>();

  @FXML
  private void initialize() {
    setupMap();
    setupMarkers();
    initializeLatitudeLongitudeCoordinates();
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
          double scale = 0.5 / mapView.getZoom();
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
    double[] coordinates = latitudeLongitudeCoordinates.get(value);

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
  }
}
