package nz.ac.auckland.se206.controllers;

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
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

  private double dragStartX;
  private double dragStartY;
  private MapPoint dragStartCenter;
  private double currentZoom = 3.0;
  private CustomMapLayer customMapLayer;
  private MapPoint guessMarker;

  @FXML
  private void initialize() {
    setupMap();
    setupMarkers();
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
    System.out.println("switching to viewer");
    App app = App.getInstance();
    if (app != null) {
      app.switchToViewerScene();
    }
  }

  @FXML
  private void switchToLevelScene() {
    System.out.println("switch to viewer");
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

      customMapLayer.updateGuessMarkerVisibility(false);
      customMapLayer.updateGuessMarker(new MapPoint(0.0, 0.0));

      switchToLevelScene();
      return;
    }

    finalGuessButton.setText("CONTINUE");
    mapDisplay.setDisable(true);
  }
}
