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
  private final MapPoint point = new MapPoint(-40.9006, 174.8860);

  private MapPoint guessPoint;

  public CustomMapLayer() {
    // Target marker
    marker = new Circle(5, Color.RED);
    getChildren().add(marker);

    // Guessing marker
    guessingMarker = new Circle(7, Color.BLUE);
    guessingMarker.setVisible(false);
    getChildren().add(guessingMarker);
  }

  public void updateGuessMarker(MapPoint mapPoint) {
    this.guessPoint = mapPoint;
    guessingMarker.setVisible(true);
    layoutLayer();
  }

  @Override
  protected void layoutLayer() {
    Point2D targetPoint = getMapPoint(point.getLatitude(), point.getLongitude());
    marker.setTranslateX(targetPoint.getX());
    marker.setTranslateY(targetPoint.getY());

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
