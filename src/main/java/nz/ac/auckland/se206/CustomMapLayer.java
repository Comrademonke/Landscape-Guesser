package nz.ac.auckland.se206;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CustomMapLayer extends MapLayer {
  private final Node marker;
  private final MapPoint point = new MapPoint(-40.9006, 174.8860);

  public CustomMapLayer() {
    // Target marker
    marker = new Circle(5, Color.RED);
    getChildren().add(marker);
  }

  @Override
  protected void layoutLayer() {
    Point2D targetPoint = getMapPoint(point.getLatitude(), point.getLongitude());
    marker.setTranslateX(targetPoint.getX());
    marker.setTranslateY(targetPoint.getY());
  }
}
