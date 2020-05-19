package controller;

import Model.HospitalTM;
import dbconnection.Dbconnection;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.FormValidation;

import javax.swing.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ManageHospitalsController {
    public TextField txtSearch;
    public ImageView home;
    public ListView lstHospitals;
    public TextField hospital_id;
    public TextField hospital_name;
    public TextField hospital_city;
    public ComboBox district;
    public TextField capacity;
    public TextField director_name;
    public TextField director_contact_no;
    public TextField hospital_contact_no_1;
    public TextField hospital_contact_no_2;
    public TextField hospital_fax_no;
    public TextField hospital_email;
    public Button add_new_hospital;
    public AnchorPane root;
    public Button save;
    public Button delete;
    public Label lbl_id;
    public Label lbl_name;
    public Label lbl_hospital_mail;
    public Label lbl_hospital_no_2;
    public Label lbl_hospital_fax;
    public Label lbl_hospital_no_1;
    public Label lbl_director_contact_no;
    public Label lbl_director_name;
    public Label lbl_capacity;
    public Label lbl_district;
    public Label lbl_city;

    private ArrayList<HospitalTM> hospitalTMArrayList = new ArrayList<>();


    public  void initialize(){
/*        FadeTransition fade = new FadeTransition(Duration.seconds(1),root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setCycleCount(1);

        fade.play();
*/
        Platform.runLater(() ->
            {
                add_new_hospital.requestFocus();
             }
        );

        String districtText = "Ampara\n" +
                "Anuradhapura\n" +
                "Badulla\n" +
                "Batticaloa\n" +
                "Colombo\n" +
                "Galle\n" +
                "Gampaha\n" +
                "Hambantota\n" +
                "Jaffna\n" +
                "Kalutara\n" +
                "Kandy\n" +
                "Kegalle\n" +
                "Kilinochchi\n" +
                "Kurunegala\n" +
                "Mannar\n" +
                "Matale\n" +
                "Matara\n" +
                "Monaragala\n" +
                "Mullaitivu\n" +
                "Nuwara Eliya\n" +
                "Polonnaruwa\n" +
                "Puttalam\n" +
                "Ratnapura\n" +
                "Trincomalee\n" +
                "Vavuniya";

        String[] districts = districtText.split("\n");
        ObservableList<String> observableListDistricts = FXCollections.observableArrayList(Arrays.asList(districts));
        district.setItems(observableListDistricts);


        loadAllHospitals();

        lstHospitals.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HospitalTM>() {
            @Override
            public void changed(ObservableValue<? extends HospitalTM> observable, HospitalTM oldValue, HospitalTM newValue) {
                if (newValue == null) {
                    lstHospitals.getSelectionModel().clearSelection();
                }else {
                    hospital_id.setText(newValue.getHospital_id());
                    hospital_name.setText(newValue.getHospital_name());
                    hospital_city.setText(newValue.getHospital_city());
                    district.getSelectionModel().select(newValue.getDistrict());
                    capacity.setText(newValue.getCapacity());
                    director_name.setText(newValue.getDirector_name());
                    director_contact_no.setText(newValue.getDirector_contact_no());
                    hospital_contact_no_1.setText(newValue.getHospital_contact_no_1());
                    hospital_contact_no_2.setText(newValue.getHospital_contact_no_2());
                    hospital_fax_no.setText(newValue.getHospital_fax_no());
                    hospital_email.setText(newValue.getHospital_email());

                    save.setText("UPDATE");
                }
            }
        });

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<HospitalTM> hospitalTMObservableList = lstHospitals.getItems();
                lstHospitals.getItems().clear();
               for (HospitalTM hospitalTM : hospitalTMArrayList){
                    if (hospitalTM.getHospital_name().contains(newValue)){
                        hospitalTMObservableList.add(hospitalTM);
                    }
                }
           }
        });
    }

    public void btnHome_OnAction(ActionEvent event) throws IOException {
        Scene mainScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml")));
        Stage primaryStage =(Stage) (txtSearch.getScene().getWindow());
        primaryStage.setScene(mainScene);
        primaryStage.centerOnScreen();
    }

    public void btnNewHospital_OnAction(ActionEvent event) {
        try {
            PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("SELECT hospital_id FROM hospital ORDER BY hospital_id DESC  LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                hospital_id.setText("H001");
            }else {
                String last_id = resultSet.getString(1);
                String substring = last_id.substring(1,4);
                int id = Integer.parseInt(substring) + 1;
                if (id < 10) {
                    hospital_id.setText("H00" + id);
                }else if (id < 100){
                    hospital_id.setText("H0" + id);
                }else {
                    hospital_id.setText("H" + id);
                }
            }
            hospital_name.requestFocus();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void btnSave_OnAction(ActionEvent event) {
        HospitalTM selectedHospital = (HospitalTM) lstHospitals.getSelectionModel().getSelectedItem();

        String id = hospital_id.getText();
        String name = hospital_name.getText();
        String city = hospital_city.getText();
        String selectedDistrict = (String) district.getSelectionModel().getSelectedItem();
        String hospital_capacity = capacity.getText();
        String hospital_director_name = director_name.getText();
        String hospital_director_contact_no = director_contact_no.getText();
        String hospital_no_01 = hospital_contact_no_1.getText();
        String hospital_no_02 = hospital_contact_no_2.getText();
        String hospital_fax = hospital_fax_no.getText();
        String hospital_mail = hospital_email.getText();

        boolean userIDValidation = FormValidation.userIDValidation(hospital_id,lbl_id,"* Press + New Hospital button");
        boolean nameValidation = FormValidation.nameValidation(hospital_name,lbl_name,"*Insert valid hospital name");
        boolean cityValidation = FormValidation.cityValidation(hospital_city,lbl_city,"*Insert valid city");
        boolean districtValidation = FormValidation.comboboxValidation(district,lbl_district,"Select district");
        boolean capacityValidation = FormValidation.capacityValidation(capacity,lbl_capacity,"*Insert valid capacity number");
        boolean directorNameValidation = FormValidation.directorNameValidation(director_name,lbl_director_name,"*Insert valid director name");
        boolean contactNumberValidation = FormValidation.contactNumberValidation(director_contact_no, lbl_director_contact_no,"* xxx-xxxxxxx");
        boolean contactNumber1Validation = FormValidation.contactNumberValidation(hospital_contact_no_1,lbl_hospital_no_1,"* xxx-xxxxxxx");
        boolean contactNumber2Validation = FormValidation.contactNumberValidation(hospital_contact_no_2,lbl_hospital_no_2,"*xxx-xxxxxxx");
        boolean faxNumberValidation = FormValidation.contactNumberValidation(hospital_fax_no,lbl_hospital_fax,"*xxx-xxxxxxx");
        boolean emailValidation = FormValidation.emailValidation(hospital_email,lbl_hospital_mail,"*name@example.com");

        if (userIDValidation && nameValidation && cityValidation && districtValidation && capacityValidation && directorNameValidation && contactNumberValidation && contactNumber1Validation &&
                contactNumber2Validation && faxNumberValidation && emailValidation) {
            if (save.getText().equals("Save")) {
                String sql = "INSERT INTO hospital(hospital_id,hospital_name,hospital_city,district,capacity,director_name,director_contact_no,hospital_contact_no_1,hospital_contact_no_2," +
                        "hospital_fax_no,hospital_email) " +
                        "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
                try {
                    PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement(sql);
                    preparedStatement.setObject(1,id);
                    preparedStatement.setObject(2,name);
                    preparedStatement.setObject(3,city);
                    preparedStatement.setObject(4,selectedDistrict);
                    preparedStatement.setObject(5,hospital_capacity);
                    preparedStatement.setObject(6,hospital_director_name);
                    preparedStatement.setObject(7,hospital_director_contact_no);
                    preparedStatement.setObject(8,hospital_no_01);
                    preparedStatement.setObject(9,hospital_no_02);
                    preparedStatement.setObject(10,hospital_fax);
                    preparedStatement.setObject(11,hospital_mail);
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0){
                        new Alert(Alert.AlertType.INFORMATION,"Successfull",ButtonType.OK).showAndWait();
                        loadAllHospitals();
                    }else {
                        new Alert(Alert.AlertType.ERROR,"Not successfull,Try again",ButtonType.OK).showAndWait();
                    }
                }  catch (SQLException e){
                    e.printStackTrace();
                }
            }else {
                String sql = "UPDATE hospital SET hospital_id=?,hospital_name=?,hospital_city=?,district=?,capacity=?,director_name=?,director_contact_no=?,hospital_contact_no_1=?," +
                        "hospital_contact_no_2=?,hospital_fax_no=?,hospital_email=? WHERE hospital_id=?";
                try {
                    PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement(sql);
                    preparedStatement.setObject(1,id);
                    preparedStatement.setObject(2,name);
                    preparedStatement.setObject(3,city);
                    preparedStatement.setObject(4,selectedDistrict);
                    preparedStatement.setObject(5,hospital_capacity);
                    preparedStatement.setObject(6,hospital_director_name);
                    preparedStatement.setObject(7,hospital_director_contact_no);
                    preparedStatement.setObject(8,hospital_no_01);
                    preparedStatement.setObject(9,hospital_no_02);
                    preparedStatement.setObject(10,hospital_fax);
                    preparedStatement.setObject(11,hospital_mail);
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        new Alert(Alert.AlertType.INFORMATION,"UPDATE Successful",ButtonType.OK).showAndWait();
                        lstHospitals.getSelectionModel().clearSelection();
                        save.setText("Save");
                        lstHospitals.refresh();
                        loadAllHospitals();
                    } else {
                        new Alert(Alert.AlertType.ERROR,"Not successfull,Try again",ButtonType.OK).showAndWait();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            clearForm();
        }

    }

    public void btnDelete_OnAction(ActionEvent event) {
        if (lstHospitals.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR,"Select a Hospital to detele", ButtonType.OK).showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure to delete",ButtonType.YES,ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult()==ButtonType.YES){
            HospitalTM selectedHospital = (HospitalTM) lstHospitals.getSelectionModel().getSelectedItem();
            try {
                PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("DELETE FROM hospital where hospital_id=?");
                preparedStatement.setObject(1,selectedHospital.getHospital_id());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0){
                    new Alert(Alert.AlertType.INFORMATION,"Deleted",ButtonType.OK).showAndWait();

                    lstHospitals.getSelectionModel().clearSelection();
                    lstHospitals.refresh();
                    loadAllHospitals();
                    save.setText("Save");
                    clearForm();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }else {
            clearForm();
            lstHospitals.getSelectionModel().clearSelection();
            save.setText("Save");
            Platform.runLater(() ->
            {
                add_new_hospital.requestFocus();
            }
            );
        }
    }

    private  void loadAllHospitals(){
        try {
            PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("SELECT * FROM hospital");
            ResultSet resultSet = preparedStatement.executeQuery();
            lstHospitals.getItems().clear();
            while (resultSet.next()){
                String hospital_id = resultSet.getString(1);
                String hospital_name = resultSet.getString(2);
                String hospital_city = resultSet.getString(3);
                String district = resultSet.getString(4);
                String hospital_capacity = resultSet.getString(5);
                String director_name = resultSet.getString(6);
                String director_contact_no = resultSet.getString(7);
                String hospital_contact_no_01 = resultSet.getString(8);
                String hospital_contact_no_02 = resultSet.getString(9);
                String hospital_fax_no = resultSet.getString(10);
                String hospital_email = resultSet.getString(11);

                HospitalTM hospitalDetail = new HospitalTM(hospital_id,hospital_name,hospital_city,district,hospital_capacity + "", director_name,director_contact_no,hospital_contact_no_01,
                        hospital_contact_no_02,hospital_fax_no,hospital_email);
                lstHospitals.getItems().add(hospitalDetail);
                hospitalTMArrayList.add(hospitalDetail);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void clearForm(){
        hospital_id.setText("HXXX");
        hospital_name.clear();
        hospital_city.clear();
        district.getSelectionModel().clearSelection();
        capacity.clear();
        director_name.clear();
        director_contact_no.clear();
        hospital_contact_no_1.clear();
        hospital_contact_no_2.clear();
        hospital_fax_no.clear();
        hospital_email.clear();
    }
}
