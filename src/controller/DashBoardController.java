package controller;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;

public class DashBoardController {

    public AnchorPane root;
    public Button GlobalCOVID19;
    public Button ManageHospitals;
    public Button QuarantineCenters;
    public Button ManageUsers;

    /*public void initialize(){
        FadeTransition fade = new FadeTransition(Duration.seconds(2),root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setCycleCount(1);

        fade.play();

    }*/

    public void btnGlobalCOVID19_OnAction(ActionEvent event) {
        try {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/GlobalCOVID19.fxml"));
        Scene GlobalCOVID19Scene = new Scene(root);
        Stage stage = (Stage) (this.root.getScene().getWindow());
        stage.setScene(GlobalCOVID19Scene);
        stage.centerOnScreen();
        stage.sizeToScene();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void btnMangeHospitals_ONAction(ActionEvent event)  {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/ManageHospitals.fxml"));
            Scene ManageHospitalsScene = new Scene(root);
            Stage stage = (Stage) (this.root.getScene().getWindow());
            stage.setScene(ManageHospitalsScene);
            stage.centerOnScreen();
            stage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void btnQuarantineCenters_OnAction(ActionEvent event) throws IOException{
        Scene QuarantineCentersScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/QuarantineCenters.fxml")));
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(QuarantineCentersScene);
        primaryStage.centerOnScreen();
    }

    public void btnManageUsers_OnAction(ActionEvent event) throws IOException {
        Scene ManageUsersScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/ManageUsers.fxml")));
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(ManageUsersScene);
        primaryStage.centerOnScreen();
    }
}
