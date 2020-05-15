package controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class DashBoardController {

    public AnchorPane root;
    public Button GlobalCOVID19;
    public Button ManageHospitals;
    public Button QuarantineCenters;
    public Button ManageUsers;

    public static void main(String[] args) {

    }

    public void btnGlobalCOVID19_OnAction(ActionEvent event) throws IOException {
        Scene GlobalCOVID19Scene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/GlobalCOVID19.fxml")));
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(GlobalCOVID19Scene);
        primaryStage.centerOnScreen();
    }

    public void btnMangeHospitals_ONAction(ActionEvent event) throws IOException {
        Scene ManageHospitalsScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/ManageHospitals.fxml")));
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(ManageHospitalsScene);
        primaryStage.centerOnScreen();
    }

    public void btnQuarantineCenters_OnAction(ActionEvent event) throws IOException{
        Scene QuarantineCentersScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/QuarantineCenters.fxml")));
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(QuarantineCentersScene);
        primaryStage.centerOnScreen();
    }

    public void btnManageUsers_OnAction(ActionEvent event) throws IOException{
        Scene ManageUsersScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/ManageUsers.fxml")));
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(ManageUsersScene);
        primaryStage.centerOnScreen();
    }
}
