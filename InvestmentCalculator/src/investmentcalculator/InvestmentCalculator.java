package investmentcalculator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.stage.Stage;

/**
 * @author Cynthia Chang
 */
public class InvestmentCalculator extends Application {
    private TextField investAmount = new TextField();
    private TextField numOfYears = new TextField();
    private TextField interestRate = new TextField();
    private TextField futureValue = new TextField();
    private Button calculate = new Button("Calculate");
    
    @Override
    public void start(Stage primaryStage) {
        // creates template of interface
        GridPane pane = new GridPane();
        pane.setVgap(2); // sets vertical spacing
        pane.setHgap(5); // sets horizontal spacing
        pane.add(new Label("Investment Amount"), 0, 0);
	pane.add(investAmount, 1, 0);
	pane.add(new Label("Years"), 0, 1);
	pane.add(numOfYears, 1, 1);
	pane.add(new Label("Annual Interest Rate"), 0, 2);
	pane.add(interestRate, 1, 2);
	pane.add(new Label("Future Value"), 0, 3);
	pane.add(futureValue, 1, 3);
        pane.add(calculate, 1, 4);
        
        // sets the interface properties
        pane.setAlignment(Pos.CENTER);
	investAmount.setAlignment(Pos.BOTTOM_RIGHT);
	numOfYears.setAlignment(Pos.BOTTOM_RIGHT);
	interestRate.setAlignment(Pos.BOTTOM_RIGHT);
	futureValue.setAlignment(Pos.BOTTOM_RIGHT);
	futureValue.setEditable(false); // makes the textBox of futureValue uneditable
	GridPane.setHalignment(calculate, HPos.RIGHT);
        pane.setPadding(new Insets(10, 10, 10, 10));
        
        // sets the calculated value onto the textfield of futureValue
        calculate.setOnAction((ActionEvent e) -> {
            calcFutureValue();
        });
        
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Investment-Value Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void calcFutureValue() {
	double investmentAmount = Double.parseDouble(investAmount.getText());
	int years = Integer.parseInt(numOfYears.getText());
	double monthlyInterestRate = Double.parseDouble(interestRate.getText()) / 1200;
	futureValue.setText(String.format("$%.2f", (investmentAmount * Math.pow(1 + monthlyInterestRate, years * 12))));
    }
}