package nz.ac.auckland.se206;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CustomMapDisplay extends MapLayer {

  private final Node marker;

  // Temporary stand in point
  private MapPoint targetPoint = new MapPoint(-37.8213108, 144.9668387);

  public CustomMapDisplay() {

    // Target marker
    marker = new Circle(5, Color.RED);
    getChildren().add(marker);
  }

  public void updateTargetMarker(MapPoint mapPoint) {
    this.targetPoint = mapPoint;
    layoutLayer();
  }

  public MapPoint returnTargetMarker() {
    return targetPoint;
  }

  @Override
  protected void layoutLayer() {
    Point2D targetScreenPoint = getMapPointLatLong(targetPoint);
    marker.setTranslateX(targetScreenPoint.getX());
    marker.setTranslateY(targetScreenPoint.getY());
  }

  private Point2D getMapPointLatLong(MapPoint mapPoint) {
    return getMapPoint(mapPoint.getLatitude(), mapPoint.getLongitude());
  }
}
