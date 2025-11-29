package nz.ac.auckland.se206;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class CustomMapLayer extends MapLayer {
  private final Node marker;
  private final Node guessingMarker;

  private MapPoint guessPoint;
  // Temporary stand in point
  private MapPoint targetPoint = new MapPoint(0.0, 0.0);

  private Line line;

  public CustomMapLayer() {
    // Line from guess to target
    line = new Line();
    line.setStroke(Color.BLACK);
    line.setStrokeWidth(2);
    line.getStrokeDashArray().addAll(5.0, 5.0);
    line.setVisible(false);
    getChildren().add(line);

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

  public void drawConnectionLine() {
    Point2D targetScreenPoint = getMapPointLatLong(targetPoint);
    Point2D guessScreenPoint = getMapPointLatLong(guessPoint);
    line.setStartX(targetScreenPoint.getX());
    line.setStartY(targetScreenPoint.getY());
    line.setEndX(guessScreenPoint.getX());
    line.setEndY(guessScreenPoint.getY());
  }

  public void updateLineVisibility(boolean isLineVisible) {
    line.setVisible(isLineVisible);
  }

  @Override
  protected void layoutLayer() {
    Point2D targetScreenPoint = getMapPointLatLong(targetPoint);
    marker.setTranslateX(targetScreenPoint.getX());
    marker.setTranslateY(targetScreenPoint.getY());

    if (guessPoint != null) {
      Point2D guessScreenPoint = getMapPointLatLong(guessPoint);
      guessingMarker.setTranslateX(guessScreenPoint.getX());
      guessingMarker.setTranslateY(guessScreenPoint.getY());
      drawConnectionLine();
    }
  }

  private Point2D getMapPointLatLong(MapPoint mapPoint) {
    return getMapPoint(mapPoint.getLatitude(), mapPoint.getLongitude());
  }
}
