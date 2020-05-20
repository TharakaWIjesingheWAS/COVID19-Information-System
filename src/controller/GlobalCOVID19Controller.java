package controller;

import dbconnection.Dbconnection;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.FormValidation;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class GlobalCOVID19Controller {
    public DatePicker txtDate;
    public AnchorPane root;
    public TextField confirmed_cases;
    public TextField recovered_cases;
    public TextField deaths;
    public Label lbl_update_date;
    public Label lbl_confirmed_cases;
    public Label lbl_recovered;
    public Label lbl_deaths;
    public Label lbl_confirmed_error;
    public Label lbl_recovered_error;
    public Label lbl_death_error;
    public Label lbl_date_error;
    public Button update;


    public void initialize(){
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

    public void btnHome_OnAction(ActionEvent event) throws IOException {
        Scene mainScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml")));
        Stage primaryStage =(Stage) (txtDate.getScene().getWindow());
        primaryStage.setScene(mainScene);
        primaryStage.centerOnScreen();
    }

    public void btnUpdate_OnAction(ActionEvent event) throws SQLException {
        if (LoginController.user_role.equals("Admin") || LoginController.user_role.equals("P.S.T.F")) {
            boolean numberValidation = FormValidation.numberValidation(confirmed_cases,lbl_confirmed_error,"* Input a number");
            boolean number2Validation = FormValidation.numberValidation(recovered_cases,lbl_recovered_error,"* Insert a number");
            boolean number3Validation = FormValidation.numberValidation(deaths,lbl_death_error,"* Input a number");
            boolean dateValidation = FormValidation.dateValidation(txtDate,lbl_date_error,"* Select a Date");

            if (numberValidation && number2Validation && number3Validation && dateValidation) {
                if (update.getText().equals("ADD New Record")) {
                    try {
                        String sql = "INSERT INTO globalcovid19(admin_id, updated_date, confirmed_count, recovered_count, death_count) VALUES (?,?,?,?,?)";
                        PreparedStatement preparedStatement =Dbconnection.getInstance().getConnection().prepareStatement(sql);
                        preparedStatement.setObject(1,LoginController.id);
                        preparedStatement.setObject(2,txtDate.getValue());
                        preparedStatement.setObject(3,confirmed_cases.getText());
                        preparedStatement.setObject(4,recovered_cases.getText());
                        preparedStatement.setObject(5,deaths.getText());

                        int affectedRows = preparedStatement.executeUpdate();
                        if (affectedRows > 0) {
                            new Alert(Alert.AlertType.INFORMATION,"Successful").showAndWait();
                        }
                        loadAllEnteredRecord();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else {
                    String sql = "UPDATE globalcovid19 SET admin_id=?,confirmed_count=?,recovered_count=?,death_count=? WHERE updated_date=?";
                    PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement(sql);
                    preparedStatement.setObject(1,LoginController.id);
                    preparedStatement.setObject(2,confirmed_cases.getText());
                    preparedStatement.setObject(3,recovered_cases.getText());
                    preparedStatement.setObject(4,deaths.getText());
                    preparedStatement.setObject(5,txtDate.getValue());

                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        new Alert(Alert.AlertType.INFORMATION,"Update Successful").showAndWait();
                    }
                }
                update.setText("ADD NEW RECORD");
                clearForm();
                txtDate.getEditor().clear();
                txtDate.requestFocus();
                loadAllEnteredRecord();
            }
        }

    }

    public void date_OnAction(ActionEvent actionEvent) throws SQLException {
        try {
            PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("SELECT * FROM globalcovid19 WHERE updated_date=?");
            preparedStatement.setObject(1,txtDate.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                update.setText("UPDATE");
                update.setStyle("-fx-background-color: #336EFF");
                confirmed_cases.setText(resultSet.getString(4));
                recovered_cases.setText(resultSet.getString(5));
                deaths.setText(resultSet.getString(6));
            }else {
                clearForm();
                update.setText("ADD NEW RECORD");
                update.setStyle(" -fx-background-color: #A17E06");
                confirmed_cases.requestFocus();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void loadAllEnteredRecord() {
        try {
            PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("SELECT updated_date FROM globalcovid19 ORDER BY updated_date DESC LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                lbl_update_date.setText(resultSet.getDate(1) + "");

                sumOfCases();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sumOfCases() {
        try {
            PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("SELECT SUM(confirmed_count),SUM(recovered_count),SUM(death_count) FROM globalcovid19");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                lbl_confirmed_cases.setText(resultSet.getInt(1) + "");
                lbl_recovered.setText(resultSet.getInt(2) + "");
                lbl_deaths.setText(resultSet.getInt(3) + "");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        confirmed_cases.clear();
        recovered_cases.clear();
        deaths.clear();
    }

}
