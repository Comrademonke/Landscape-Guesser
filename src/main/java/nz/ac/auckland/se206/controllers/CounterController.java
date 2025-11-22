package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class CounterController {

    //<Button fx:id="incrementButton" layoutX="58.0" layoutY="386.0" mnemonicParsing="false" onAction="#increment" text="Increment" />
    // <Button fx:id="decrementButton" layoutX="191.0" layoutY="386.0" mnemonicParsing="false" onAction="#decrement" text="Decrement" />
    // <Button fx:id="resetButton" layoutX="337.0" layoutY="386.0" mnemonicParsing="false" onAction="#reset" text="Reset" />
    //<Label fx:id="numberLabel" alignment="CENTER" layoutX="61.0" layoutY="57.0" prefHeight="233.0" prefWidth="335.0" text="0" textAlignment="CENTER">
    //<Button fx:id="switchButton" layoutX="307.0" layoutY="14.0" mnemonicParsing="false" onAction="#switchScene" text="Switch to Music player" />
     
    private int counter = 0;

    @FXML private Button incrementButton;
    @FXML private Button decrementButton;
    @FXML private Button resetButton;
    @FXML private Button switchButton;
    @FXML private Label numberLabel;

    @FXML
    private void initialize(){
        System.out.println("************* Initialising Counter Controller ************************" + this);
    }

    @FXML
    private void increment(){
        System.out.println("incrementing");
        counter++;
        numberLabel.setText(counter + "");
    }

    @FXML
    private void decrement(){
        System.out.println("decrement");
        counter--;
        numberLabel.setText(counter + "");
    }

    @FXML
    private void reset(){
        System.out.println("reset");
        counter = 0;
        numberLabel.setText(counter + "");
    }

    @FXML
    private void switchScene(ActionEvent event){
        System.out.println("switch scenes");
        Button button = (Button) event.getSource();
        Scene sceneButtonIsIn = button.getScene();
        sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.MUSIC_PLAYER));
        
    }


}