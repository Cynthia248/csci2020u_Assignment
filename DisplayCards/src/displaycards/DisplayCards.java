package displaycards;

import java.util.ArrayList;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Cynthia Chang
 */
public class DisplayCards extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        ArrayList<String> cardsList = new ArrayList<>();

        for (int i = 0; i < 54; i++) {
            cardsList.add(String.valueOf(i + 1)); // adds a number into the list from 1 to 54
        }

        java.util.Collections.shuffle(cardsList); // method that randomly permutes the list 

        ImageView card1 = new ImageView(new Image("Cards/" + cardsList.get(0) + ".png")); //gets image of card1 from Cards folder
        ImageView card2 = new ImageView(new Image("Cards/" + cardsList.get(1) + ".png")); //gets image of card2 from Cards folder
        ImageView card3 = new ImageView(new Image("Cards/" + cardsList.get(2) + ".png")); //gets image of card3 from Cards folder

        HBox root = new HBox(); //creates a horizontal box
        
        // all images are placed horizontally
        root.getChildren().add(card1);
        root.getChildren().add(card2);
        root.getChildren().add(card3);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Display Three Cards");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}