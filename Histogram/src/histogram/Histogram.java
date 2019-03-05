package histogram;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Cynthia Chang
 */
public class Histogram extends Application {
    Pane pane = new Pane();
    TextField namefield = new TextField();
    VBox vbox = new VBox();
    
    @Override
    public void start(Stage primaryStage) {
        Label fileName = new Label("Filename", namefield);
        fileName.setContentDisplay(ContentDisplay.RIGHT); //sets fileName label to the right
        namefield.setPrefColumnCount(20); // shows 20 cols in the textfield
        Button viewButton = new Button("View");
        HBox hbox = new HBox(fileName, viewButton);
        vbox.getChildren().addAll(pane, hbox);
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Histogram");
        primaryStage.sizeToScene();
        viewButton.setOnAction(e -> {
            updateHistogram();
            vbox.setTranslateY(10);
            primaryStage.sizeToScene();
            primaryStage.setTitle("Histogram");
        });
        primaryStage.show();
    }
    private void updateHistogram() {
        makeHistogram graph = new makeHistogram(namefield.getText());
        pane.getChildren().add(graph);
    }
    
    // creates Histogram that counts the letters in a file
    private static class makeHistogram extends Pane {
        private char[] chars = new char[26];
        private int counter[] = new int[26];
        private Rectangle[] bars = new Rectangle[26];
        private File file;
        GridPane gridPane;
        double width = 350;
        double height = 350;
        public makeHistogram(String filename) {
            this.file = new File(filename.trim());
            setWidth(width);
            setHeight(height);
            readFile();
            drawHistogram();
        }
        // reads filename that is entered by user
        private void readFile() {
            Scanner scanner;
            String s = "";
            try {
                scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    s += scanner.nextLine();
                }
            } catch (IOException e) {}
            s = s.toUpperCase();
            // counts the number of occurances of a letter
            for (int i = 0; i < s.length(); i++) {
                char character = s.charAt(i);
                if (Character.isLetter(character)) {
                    counter[character - 'A']++;
                }
            }
        }
        private double getTotalCount() {
            double total = 0; for (int count : counter) {
                total += count;
            }
            return total;
        }
        // draws the histogram bars
        private void drawHistogram() {
            gridPane = new GridPane();
            double barW = width / chars.length;
            double total = getTotalCount();
            for (int i = 0; i < counter.length; i++) {
                chars[i] = (char)('A' + i);
                double percentage = counter[i] / total;
                double barH = height * percentage;
                bars[i] = new Rectangle(barW, barH);
                Label label = new Label(chars[i] + "", bars[i]);
                label.setContentDisplay(ContentDisplay.TOP);
                gridPane.add(label, i, 0);
                GridPane.setValignment(label, VPos.BASELINE);
            }
            getChildren().addAll(gridPane);
        }
        public int[] getCounter() {
            return counter;
        }
        public void setCounter(int[] counts) {
            this.counter = counts;
            drawHistogram();
        }
    }
}