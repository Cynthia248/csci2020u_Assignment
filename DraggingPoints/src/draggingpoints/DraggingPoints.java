package draggingpoints;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.beans.property.DoubleProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;

/**
 * @author Cynthia Chang
 */
public class DraggingPoints extends Application {
    @Override
    public void start(Stage primaryStage) {
        //sets properties of window pane
        PointPane pane = new PointPane(300, 300);
        BorderPane borderPane = new BorderPane(pane);
        
        Scene scene = new Scene(borderPane);
        primaryStage.setTitle("Dragging Points on a Circle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private class PointPane extends Pane {
        Circle circle = new Circle();
        Vertex[] vertex = new Vertex[3];
        int strokeWidth = 1; // sets the width of the lines
        Color circleStroke = Color.BLACK, legStroke = Color.BLACK;

        PointPane(double w, double h) {
            setPrefSize(w, h);
            setWidth(w);
            setHeight(h);
            
            // set properties for the large circle
            circle.setStroke(circleStroke);
            circle.setFill(Color.TRANSPARENT);
            circle.setStrokeWidth(strokeWidth);
            circle.radiusProperty().bind(heightProperty().multiply(0.3));
            circle.centerXProperty().bind(widthProperty().divide(2));
            circle.centerYProperty().bind(heightProperty().divide(2));
            this.getChildren().add(circle);

            // set properties for each vertex
            for (int i = 0; i < vertex.length; i++) {
                vertex[i] = new Vertex(circle, 2 * Math.PI / vertex.length * (i + Math.random()));
                vertex[i].radiusProperty().bind(circle.radiusProperty().divide(15));
                vertex[i].setPosition();
                vertex[i].setStroke(circleStroke);
                vertex[i].setFill(Color.RED);
                vertex[i].setStrokeWidth(strokeWidth);
                this.getChildren().add(vertex[i]);

                // handles mouse events when dragging the vertices
                vertex[i].setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        int i;
                        for (i = 0; i < vertex.length; i++)
                            if (vertex[i] == event.getSource())
                                break;
                        vertex[i].setAngle(event.getX(), event.getY());
                        moveUpdate((Vertex)event.getSource());
                    }
                });
            }

            for (int i = 0; i < vertex.length; i++) {
                int j = i + 1 < vertex.length ? i + 1 : 0;
                int k = j + 1 < vertex.length ? j + 1 : 0;
                vertex[i].bindLeg(vertex[j], vertex[k]);
                vertex[i].line.setStroke(legStroke);
                vertex[i].line.setStrokeWidth(strokeWidth);
                this.getChildren().add(vertex[i].line);
                this.getChildren().add(vertex[i].text);
            }

            for (DoubleProperty p: new DoubleProperty[] {
                circle.radiusProperty(), circle.centerXProperty(), circle.centerYProperty()
            })
            p.addListener(new ResizeListener()); // adds a tracker to the angle of vertex
            moveUpdate(vertex[0]);
        }

        // tracks the angle of the vertex
        void moveUpdate(Vertex point) {
            point.setPosition();
            double[] lineLength = new double[3];
            for (int i = 0; i < vertex.length; i++)
                lineLength[i] = vertex[i].getLineLength();
            
            for (int i = 0; i < vertex.length; i++) {
                int j = i + 1 < vertex.length ? i + 1 : 0;
                int k = j + 1 < vertex.length ? j + 1 : 0;
                double a = lineLength[i], b = lineLength[j], c = lineLength[k];
                double d = Math.toDegrees(Math.acos((a * a - b * b - c * c) / (-2 * b * c)));
                vertex[i].setText(d);
            }
        }

        private class ResizeListener implements ChangeListener<Number> {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth, Number newWidth) {
                for (Vertex vertex2 : vertex) {
                    vertex2.setPosition();
                }
            }
        }
    }

    private class Vertex extends Circle {
        Circle circle;
        Line line;
        Text text;
        double centerAngle;
        
        // set properties of the angle text
        Vertex(Circle circle, double centerAngle) {
            this.circle = circle;
            this.setAngle(centerAngle);
            line = new Line();
            text = new Text();
            text.setFont(Font.font(18));
            text.setStroke(Color.BLACK);
            text.setTextAlignment(TextAlignment.CENTER);
            text.xProperty().bind(this.centerXProperty().add(25));
            text.yProperty().bind(this.centerYProperty().subtract(10));
        }

        double getCenterAngle() {
            return this.centerAngle;
        }

        void setPosition() {
            this.setCenterX(circle.getCenterX() + circle.getRadius() * Math.cos(this.getCenterAngle()));
            this.setCenterY(circle.getCenterY() + circle.getRadius() * Math.sin(this.getCenterAngle()));
        }

        void setAngle(double centerAngle) {
            this.centerAngle = centerAngle;
        }

        void setAngle(double x, double y) {
            this.setAngle(Math.atan2(y - circle.getCenterY(), x - circle.getCenterX()));
        }

        void bindLeg(Vertex v1, Vertex v2) {
            line.startXProperty().bind(v1.centerXProperty());
            line.startYProperty().bind(v1.centerYProperty());
            line.endXProperty().bind(v2.centerXProperty());
            line.endYProperty().bind(v2.centerYProperty());
        }

        double getLineLength() {
            return Math.sqrt(Math.pow(line.getStartX() - line.getEndX(),2) + Math.pow(line.getStartY() - line.getEndY(),2));
        }

        void setText(double angle) {
            this.text.setText(String.format("%.0f", angle));
        }
    }
}