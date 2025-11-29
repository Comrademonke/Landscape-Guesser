package nz.ac.auckland.se206;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CustomMapLayer extends MapLayer {
  private final Node marker;
  private final Node guessingMarker;

  private MapPoint guessPoint;
  // Temporary stand in point
  private MapPoint targetPoint = new MapPoint(0.0, 0.0);

  public CustomMapLayer() {
    // Target marker
    marker = new Circle(5, Color.RED);
    marker.setVisible(false);
    getChildren().add(marker);

    // Guessing marker
    guessingMarker = new Circle(7, Color.BLUE);
    guessingMarker.setVisible(false);
    getChildren().add(guessingMarker);
  }

  public void updateGuessMarkerVisibility(boolean isGuessingMarkerVisible) {
    guessingMarker.setVisible(isGuessingMarkerVisible);
  }

  public void updateGuessMarker(MapPoint mapPoint) {
    this.guessPoint = mapPoint;
    layoutLayer();
  }

  public MapPoint returnGuessMarker() {
    return guessPoint;
  }

  public void updateTargetMarkerVisibility(boolean isTargetMarkerVisible) {
    marker.setVisible(isTargetMarkerVisible);
  }

  public void updateTargetMarker(MapPoint mapPoint) {
    this.targetPoint = mapPoint;
    layoutLayer();
  }

  @Override
  protected void layoutLayer() {
    Point2D targetScreenPoint = getMapPoint(targetPoint.getLatitude(), targetPoint.getLongitude());
    marker.setTranslateX(targetScreenPoint.getX());
    marker.setTranslateY(targetScreenPoint.getY());

    if (guessPoint != null) {
      Point2D guessScreenPoint = getMapPointLatLong(guessPoint);
      guessingMarker.setTranslateX(guessScreenPoint.getX());
      guessingMarker.setTranslateY(guessScreenPoint.getY());
    }
  }

  private Point2D getMapPointLatLong(MapPoint mapPoint) {
    return getMapPoint(mapPoint.getLatitude(), mapPoint.getLongitude());
  }
}
