package controller;

import dbconnection.Dbconnection;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class GlobalCOVID19Controller {
    public DatePicker txtDate;
    public AnchorPane root;
    public ImageView home;
    public TextField confirmed_cases;
    public TextField recovered_cases;
    public TextField deaths;


    public void initialize(){
        /*FadeTransition fade = new FadeTransition(Duration.seconds(1),root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setCycleCount(1);

        fade.play();
*/
        LocalDate minDate = LocalDate.of(2019,11,01);
        LocalDate maxDate = LocalDate.now();
        txtDate.setDayCellFactory(d ->
                new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
                    }
                });
        loadAllEnteredRecord();
    }

    private void loadAllEnteredRecord() {
    }


    public void btnHome_OnAction(ActionEvent event) throws IOException {
        Scene mainScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml")));
        Stage primaryStage =(Stage) (txtDate.getScene().getWindow());
        primaryStage.setScene(mainScene);
        primaryStage.centerOnScreen();
    }

    public void btnUpdate_OnAction(ActionEvent event) {
    }

}
