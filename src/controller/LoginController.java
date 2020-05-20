package controller;

import dbconnection.Dbconnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    public AnchorPane root;
    public TextField txt_user_name;
    public PasswordField password;
    public ComboBox<String> role;
    public Button log_in;
    public static String id;
    public static String user_name;
    public static String user_role;
    public HBox user_name_error;
    public HBox password_error;
    public HBox role_error;
    public ImageView loading;

    public void initialize() {
        role.getItems().addAll("Admin","P.S.T.F", "Hospital IT", " Quarantine center IT");
        loading.setVisible(false);
    }

    public void btnLogin_OnAction(ActionEvent actionEvent) {
        try {
            PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("SELECT * FROM user WHERE user_name=? AND  password=? AND role=?");
            preparedStatement.setObject(1,txt_user_name.getText());
            preparedStatement.setObject(2,password.getText());
            preparedStatement.setObject(3,role.getSelectionModel().getSelectedItem());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getString(1);
                user_name = resultSet.getString(5);
                user_role = resultSet.getString(7);

                try {
                    Parent root = FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml"));
                    Scene dashboardScene = new Scene(root);
                    Stage stage = (Stage) (this.root.getScene().getWindow());
                    stage.setScene(dashboardScene);
                    stage.centerOnScreen();
                    stage.sizeToScene();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                user_name_error.setStyle("-fx-background-color: red;-fx-background-radius: 50px;-fx-border-radius: 50px");
                password_error.setStyle("-fx-background-color: red;-fx-background-radius: 50px;-fx-border-radius: 50px");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
