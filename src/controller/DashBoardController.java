package controller;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class DashBoardController {

    public AnchorPane root;
    public Button GlobalCOVID19;
    public Button ManageHospitals;
    public Button QuarantineCenters;
    public Button ManageUsers;

    public Label lbl_userName;
    public Button Log_Out;

    public void initialize(){
        lbl_userName.setText("Welcome" + LoginController.user_name);
    }

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
        if (LoginController.user_role.equals("Admin") || LoginController.user_role.equals("P.S.T.F")){
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
        }else {
            new Alert(Alert.AlertType.ERROR,"Access denied", ButtonType.OK).showAndWait();
        }
    }
    public void btnQuarantineCenters_OnAction(ActionEvent event) throws IOException{
        if (LoginController.user_role.equals("Admin") || LoginController.user_role.equals("P.S.T.F")){
            Scene QuarantineCentersScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/QuarantineCenters.fxml")));
            Stage primaryStage = (Stage) (this.root.getScene().getWindow());
            primaryStage.setScene(QuarantineCentersScene);
            primaryStage.centerOnScreen();
        }else {
            new Alert(Alert.AlertType.ERROR,"Access denied", ButtonType.OK).showAndWait();
        }
    }

    public void btnManageUsers_OnAction(ActionEvent event) throws IOException {
        if (LoginController.user_role.equals("Admin") || LoginController.user_role.equals("P.S.T.F")){
            Scene ManageUsersScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/ManageUsers.fxml")));
            Stage primaryStage = (Stage) (this.root.getScene().getWindow());
            primaryStage.setScene(ManageUsersScene);
            primaryStage.centerOnScreen();
        }else {
            new Alert(Alert.AlertType.ERROR,"Access denied", ButtonType.OK).showAndWait();
        }
    }

    public void btnLogout_OnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/Login.fxml"));
        Scene loginScene = new Scene(root);
        Stage stage = (Stage) (this.root.getScene().getWindow());
        stage.setScene(loginScene);
        stage.centerOnScreen();
        stage.sizeToScene();
    }
}
