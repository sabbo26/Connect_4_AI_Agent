package MVC;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class View_start {

    @FXML
    private Button playbtn;
    @FXML
    private TextField depth_input;
    @FXML
    private CheckBox noBox;
    @FXML
    private CheckBox yesBox;

    @FXML
    private void handleYesBox(){
        if(yesBox.isSelected()) noBox.setSelected(false);
    }

    @FXML
    private void handleNoBox(){
        if(noBox.isSelected()) yesBox.setSelected(false);
    }

    @FXML
    private void handlePlayBtn(){
        System.out.println();
        if(!depth_input.getText().matches("\\d+") ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Depth must be Integer");
            alert.showAndWait();
        }
        if(!yesBox.isSelected() && !noBox.isSelected()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You must choose one of checkbox");
            alert.showAndWait();
        }

    }

}
