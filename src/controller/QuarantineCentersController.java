package controller;

import Model.QuarantineCenterTM;
import dbconnection.Dbconnection;
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
import util.FormValidation;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class QuarantineCentersController {
    public ImageView home;
    public TextField txtSearch;
    public ListView<QuarantineCenterTM> lstCenters;
    public Button add_center;
    public TextField quarantine_center_id;
    public TextField center_name;
    public TextField city;
    public ComboBox district;
    public TextField head_name;
    public TextField head_contact_no;
    public TextField center_contact_no_1;
    public TextField center_contact_no_2;
    public TextField capacity;
    public AnchorPane root;
    public Label lbl_id;
    public Label lbl_center_name;
    public Label lbl_city;
    public Label lbl_district;
    public Label lbl_head_name;
    public Label lbl_head_contact;
    public Label lbl_center_contact_no_1;
    public Label lbl_center_contact_no_2;
    public Label lbl_capacity;
    public Button Save;
    public Button Delete;


    private ArrayList<QuarantineCenterTM> quarantineCenterTMArrayList = new ArrayList<>();
    

    public void initialize(){
        Platform.runLater(() ->
                {
                    add_center.requestFocus();
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

        loadAllQuarantineCenters();
        
        lstCenters.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QuarantineCenterTM>() {
            @Override
            public void changed(ObservableValue<? extends QuarantineCenterTM> observable, QuarantineCenterTM oldValue, QuarantineCenterTM newValue) {
                if (newValue == null) {
                    lstCenters.getSelectionModel().clearSelection();
                }else {
                    quarantine_center_id.setText(newValue.getQuarantine_center_id());
                    center_name.setText(newValue.getCenter_name());
                    city.setText(newValue.getCity());
                    district.getSelectionModel().select(newValue.getDistrict());
                    head_name.setText(newValue.getHead_name());
                    head_contact_no.setText(newValue.getHead_contact_no());
                    center_contact_no_1.setText(newValue.getCenter_contact_no_1());
                    center_contact_no_2.setText(newValue.getCenter_contact_no_2());
                    capacity.setText(newValue.getCapacity());
                    
                    Save.setText("UPDATE");
                }
            }
        });
        
        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                lstCenters.getItems().clear();
                for (QuarantineCenterTM quarantineCenterTM : quarantineCenterTMArrayList) {
                    if (quarantineCenterTM.getCenter_name().contains(newValue)){
                        lstCenters.getItems().add(quarantineCenterTM);
                    }
                }
            }
        });
    }

    public void loadAllQuarantineCenters() {
        try {
            PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("SELECT * FROM quarantine_center");
            ResultSet resultSet = preparedStatement.executeQuery();
            lstCenters.getItems().clear();
            while (resultSet.next()){
                String qc_id = resultSet.getString(1);
                String center_name = resultSet.getString(2);
                String city = resultSet.getString(3);
                String district = resultSet.getString(4);
                String head_name = resultSet.getString(5);
                String head_contact_no = resultSet.getString(6);
                String center_contact_no_1 = resultSet.getString(7);
                String center_contact_no_2 = resultSet.getString(8);
                int capacity = Integer.parseInt(resultSet.getString(9));

                QuarantineCenterTM quarantineCenterDetail= new QuarantineCenterTM(qc_id,center_name,city,district,head_name,head_contact_no,center_contact_no_1,center_contact_no_2,capacity + "");
                lstCenters.getItems().add(quarantineCenterDetail);
                quarantineCenterTMArrayList.add(quarantineCenterDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnHome_OnAction(ActionEvent event) throws IOException {
        Scene mainScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml")));
        Stage primaryStage =(Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(mainScene);
        primaryStage.centerOnScreen();
    }

    public void btnNewCenter_OnAction(ActionEvent event) {
        try {
            Statement statement = Dbconnection.getInstance().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM quarantine_center ORDER BY id DESC LIMIT 1");
            if (!resultSet.next()){
                quarantine_center_id.setText("Q001");
            }else {
                String last_id = resultSet.getString(1);
                String substring = last_id.substring(1,4);
                int qc_id = Integer.parseInt(substring)+ 1;
                if (qc_id < 10 ){
                    quarantine_center_id.setText("Q00" + qc_id);
                }else if (qc_id < 100) {
                    quarantine_center_id.setText("Q0" + qc_id);
                }else {
                    quarantine_center_id.setText("Q" + qc_id);
                }
            }
            center_name.requestFocus();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnSave_OnAction(ActionEvent event) {
        String qc_id = quarantine_center_id.getText();
        String center = center_name.getText();
        String center_city = city.getText();
        String qc_district = (String) district.getSelectionModel().getSelectedItem();
        String qc_head_name = head_name.getText();
        String contact_no = head_contact_no.getText();
        String qc_contact_no_1 = center_contact_no_1.getText();
        String qc_contact_no_2 = center_contact_no_2.getText();
        String qc_capacity = capacity.getText();

        boolean nameValidation = FormValidation.nameValidation(center_name,lbl_center_name,"* Insert valid center name");
        boolean cityValidation = FormValidation.cityValidation(city,lbl_city,"* Insert valid city ");
        boolean districtValidation = FormValidation.comboboxValidation(district,lbl_district,"*Select district");
        boolean directorNameValidation = FormValidation.directorNameValidation(head_name,lbl_head_name,"* insert valid name");
        boolean capacityValidation = FormValidation.capacityValidation(capacity,lbl_capacity,"*Insert valid number");
        boolean contactNumberValidation =FormValidation.contactNumberValidation(head_contact_no,lbl_head_contact,"* xxx-xxxxxxx");
        boolean contactNumber1Validation = FormValidation.contactNumberValidation(center_contact_no_1,lbl_center_contact_no_1,"* xxx-xxxxxxx");
        boolean contactNumber2Validation = FormValidation.contactNumberValidation(center_contact_no_2,lbl_center_contact_no_1,"* xxx-xxxxxxx");
        boolean userIDValidation = FormValidation.userIDValidation(quarantine_center_id,lbl_id,"* Press, '+ New Center'button");

        if (nameValidation && cityValidation && districtValidation && directorNameValidation && capacityValidation && contactNumberValidation && contactNumber1Validation &&
                contactNumber2Validation && userIDValidation ) {
            if (Save.getText().equals("SAVE")) {
                String sql = "INSERT INTO quarantine_center(id,center_name,city,district,head_name,head_contact_no,center_contact_no_1,center_contact_no_2,capacity)" +
                        "VALUES (?,?,?,?,?,?,?,?,?)";
                try {
                    PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement(sql);
                    preparedStatement.setObject(1,qc_id);
                    preparedStatement.setObject(2,center);
                    preparedStatement.setObject(3,center_city);
                    preparedStatement.setObject(4,qc_district);
                    preparedStatement.setObject(5,qc_head_name);
                    preparedStatement.setObject(6,contact_no);
                    preparedStatement.setObject(7,qc_contact_no_1);
                    preparedStatement.setObject(8,qc_contact_no_2);
                    preparedStatement.setObject(9,qc_capacity);

                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        new Alert(Alert.AlertType.INFORMATION,"Center add Successful",ButtonType.OK).showAndWait();
                        loadAllQuarantineCenters();
                    }else {
                        new Alert(Alert.AlertType.ERROR,"Sorry,Try Again",ButtonType.OK).showAndWait();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                clearForm();
                loadAllQuarantineCenters();
            } else {
                String sql = "UPDATE quarantine_center SET id=?,center_name=?,city=?,district=?,head_name=?,head_contact_no=?,center_contact_no_1=?,center_contact_no_2=?,capacity=? WHERE id=?";
                try {
                    PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement(sql);
                    preparedStatement.setObject(1,qc_id);
                    preparedStatement.setObject(2,center);
                    preparedStatement.setObject(3,center_city);
                    preparedStatement.setObject(4,qc_district);
                    preparedStatement.setObject(5,qc_head_name);
                    preparedStatement.setObject(6,contact_no);
                    preparedStatement.setObject(7,qc_contact_no_1);
                    preparedStatement.setObject(8,qc_contact_no_2);
                    preparedStatement.setObject(9,qc_capacity);
                    preparedStatement.setObject(10,lstCenters.getSelectionModel().getSelectedItems());//getid 1ka gnna one();

                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0){
                        new Alert(Alert.AlertType.INFORMATION,"Update Successful",ButtonType.OK).showAndWait();

                        clearForm();
                        Save.setText("SAVE");
                        lstCenters.refresh();
                        loadAllQuarantineCenters();
                    }
                 } catch (SQLException e) {
                    e.printStackTrace();
            }
        }

    }
}

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        if (lstCenters.getSelectionModel().getSelectedItem() == null){
            new Alert(Alert.AlertType.ERROR,"Select center to delete").showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure to delete",ButtonType.YES,ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult()== ButtonType.YES){
            try {
                PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("DELETE FROM quarantine_center WHERE id=?");
                preparedStatement.setObject(1,lstCenters.getSelectionModel().getSelectedItems());//getid 1ka gnna one);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    loadAllQuarantineCenters();
                    new Alert(Alert.AlertType.INFORMATION,"Delete Success",ButtonType.OK).showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            clearForm();
            Save.setText("SAVE");
            loadAllQuarantineCenters();
            lstCenters.refresh();
        }else {
            clearForm();
            Save.setText("SAVE");
        }
    }

    private void clearForm() {
        quarantine_center_id.setText("QXXX");
        center_name.clear();
        city.clear();
        district.getSelectionModel().clearSelection();
        head_name.clear();
        head_contact_no.clear();
        center_contact_no_1.clear();
        center_contact_no_2.clear();
        capacity.clear();
        lstCenters.getSelectionModel().clearSelection();
    }


}
