package controller;

import dbconnection.Dbconnection;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.FormValidation;
import util.UserTM;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class ManageUsersController {
    public ImageView home;
    public Button add_new_user;
    public TextField first_name;
    public TextField contact_no;
    public TextField email;
    public TextField user_name;
    public PasswordField txtPasswordHide;
    public ComboBox cmbUserRole;
    public ComboBox cmbUserLocation;
    public Button save;
    public TextField search_username;
    public TableView<UserTM> tblUserDetails;
    public TableColumn username;
    public TableColumn name;
    public TableColumn role;
    public TableColumn user_id;
    public AnchorPane root;
    public TextField id;
    public Label lbl_name;
    public Label lbl_contact_no;
    public Label lbl_email;
    public Label lbl_password;
    public Label lbl_userName;
    public Label lbl_id;
    public Label lbl_user_role;
    public Label lbl_user_location;
    public ImageView eye_hide_password;
    public PasswordField txtPasswordShow;
    public ImageView eye_show_password;
    public Button delete;

    private ArrayList<UserTM> userTMArrayList = new ArrayList<>();


    public void initialize() {
        Platform.runLater(() ->
                {
                    add_new_user.requestFocus();
                }
        );

        cmbUserLocation.setVisible(false);
        txtPasswordShow.setVisible(false);
        eye_show_password.setVisible(false);

        // values add to combobox

        cmbUserRole.getItems().add("Admin");
        cmbUserRole.getItems().add("P.S.T.F");
        cmbUserRole.getItems().add("Hospital IT");
        cmbUserRole.getItems().add("Quarantine Center IT");

        //combox select...
        cmbUserRole.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue == null) {
                    return;
                }
                if (newValue.intValue() == 2) {
                    cmbUserLocation.setVisible(true);
                    cmbUserLocation.setPromptText("select Hospital");
                    cmbUserLocation.getSelectionModel().clearSelection();
                    cmbUserLocation.getSelectionModel().select(null);
                    cmbUserLocation.getItems().clear();
                    loadAllHospitals();

                } else if (newValue.intValue() == 3) {
                    cmbUserLocation.setVisible(true);
                    cmbUserLocation.setPromptText("select Quarantine Center");
                    cmbUserLocation.getSelectionModel().clearSelection();
                    cmbUserLocation.getSelectionModel().select(null);
                    cmbUserLocation.getItems().clear();
                    loadAllQuarantineCenters();
                } else {
                    cmbUserLocation.setVisible(false);
                }
            }
        });

        // load database data to table...
        tblUserDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("user_id"));
        tblUserDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("user_name"));
        tblUserDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("first_name"));
        tblUserDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("role"));
        tblUserDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("contact_no"));
        //    tblUserDetails.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("email"));
        //    tblUserDetails.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("location"));
        //    tblUserDetails.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("password"));
        loadAllUsers();

        // user table selection...
        tblUserDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserTM>() {
            @Override
            public void changed(ObservableValue<? extends UserTM> observable, UserTM oldValue, UserTM newValue) {
                UserTM selectedUser = tblUserDetails.getSelectionModel().getSelectedItem();
                if (selectedUser == null) {
                    return;
                }
                if (newValue.getUser_role().equals("Hospital IT") || newValue.getUser_role().equals("Quarantine Center IT")) {
                    id.setText(selectedUser.getUser_id());
                    first_name.setText(selectedUser.getFirst_name());
                    contact_no.setText(selectedUser.getContact_no());
                    email.setText(selectedUser.getEmail());
                    user_name.setText(selectedUser.getUser_name());
                    txtPasswordHide.setText(selectedUser.getPassword());
                    cmbUserRole.getSelectionModel().select(newValue.getUser_role());
                    cmbUserLocation.getSelectionModel().select(newValue.getLocation());

                    cmbUserLocation.setVisible(true);
                } else {
                    id.setText(String.valueOf(selectedUser));
                    first_name.setText(selectedUser.getFirst_name());
                    contact_no.setText(selectedUser.getContact_no());
                    email.setText(selectedUser.getEmail());
                    user_name.setText(selectedUser.getUser_name());
                    txtPasswordHide.setText(selectedUser.getPassword());
                    cmbUserRole.getSelectionModel().select(newValue.getUser_role());
                }
                save.setText("UPDATE");
            }
        });

        txtPasswordHide.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txtPasswordShow.setText(newValue);
            }
        });

        txtPasswordShow.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txtPasswordHide.setText(newValue);
            }
        });

        user_name.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                txtPasswordHide.setText(generatePassword(8));
            }
        });
        search_username.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                UserTM selectedUser = tblUserDetails.getSelectionModel().getSelectedItem();
                tblUserDetails.getItems().clear();
                for (UserTM userTM : userTMArrayList) {
                    if (userTM.getFirst_name().contains(newValue) || userTM.getUser_name().contains(newValue)) {
                        tblUserDetails.getItems().add(userTM);
                    }
                }
            }
        });
    }

    public void btnHome_OnAction(ActionEvent event) throws IOException {
        Scene mainScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml")));
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(mainScene);
        primaryStage.centerOnScreen();
    }

    public void btnNewUser_OnAction(ActionEvent event) throws SQLException {
        PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("SELECT id FROM user ORDER BY id DESC LIMIT 1");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            id.setText("U001");
        } else {
            String last_id = resultSet.getString(1);
            String substring_id = last_id.substring(1, 4);
            int new_id = Integer.parseInt(substring_id) + 1;
            if (new_id < 10) {
                id.setText("U00" + new_id);
            } else if (new_id < 100) {
                id.setText("U0" + new_id);
            } else {
                id.setText("U" + new_id);
            }
        }
        first_name.requestFocus();
    }

    public void btnSave_OnAction(ActionEvent event) {
        // form validation...
        boolean userIDValidation = FormValidation.userIDValidation(id, lbl_id, "* Press New User + ");
        boolean nameValidation = FormValidation.nameValidation(first_name, lbl_name, "* Insert valid name");
        boolean contactNumberValidation = FormValidation.contactNumberValidation(contact_no, lbl_contact_no, "* xxx-xxxxxxx");
        boolean emailValidation = FormValidation.emailValidation(email, lbl_email, "* name@example.com");
        boolean userNameValidation = FormValidation.usernameValidation(user_name, lbl_userName, "* Insert valid user name(use a-z , A-Z , 0-9 , @ , .)");
        boolean passwordValidation = FormValidation.passwordValidation(txtPasswordHide, lbl_password, "* password length must be 8");
        boolean roleValidation = FormValidation.comboboxValidation(cmbUserRole, lbl_user_role, "* Select Role");

        if (userIDValidation && nameValidation && contactNumberValidation && emailValidation && userNameValidation && passwordValidation && roleValidation) {
            if (save.getText().equals("SAVE")) {
                // check user name is ok....
                try {
                    PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("SELECT user_name FROM user WHERE user_name=?");
                    preparedStatement.setObject(1, user_name.getText());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        new Alert(Alert.AlertType.ERROR, "Sorry!,Username all ready exit,Choose another one", ButtonType.OK).showAndWait();
                        user_name.selectAll();
                        user_name.requestFocus();
                    } else {
                        // send input to database...
                        if (cmbUserRole.getSelectionModel().getSelectedIndex() == 2) {
                            boolean locationValidation = FormValidation.comboboxValidation(cmbUserLocation, lbl_user_location, "* Select Location");

                            if (locationValidation) {
                                try {
                                    String sql = "INSERT INTO user VALUES (?,?,?,?,?,?,?,?)";
                                    PreparedStatement preparedStatement1 = Dbconnection.getInstance().getConnection().prepareStatement(sql);
                                    preparedStatement.setObject(1, id.getText());
                                    preparedStatement.setObject(2, first_name.getText());
                                    preparedStatement.setObject(3, contact_no.getText());
                                    preparedStatement.setObject(4, email.getText());
                                    preparedStatement.setObject(5, user_name.getText());
                                    preparedStatement.setObject(6, txtPasswordHide.getText());
                                    preparedStatement.setObject(7, cmbUserRole.getSelectionModel().getSelectedItem());
                                    preparedStatement.setObject(8, cmbUserLocation.getSelectionModel().getSelectedItem());

                                    String selectedItem = (String) cmbUserLocation.getSelectionModel().getSelectedItem();
                                    String selected_hospital_id = selectedItem.substring(0, 4);

                                    int affectedRows = preparedStatement.executeUpdate();
                                    if (affectedRows > 0) {
                                        // update hospital database table ....
                                        PreparedStatement preparedStatement2 = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET status=? WHERE hospital_id=?");
                                        preparedStatement2.setObject(1, "Reserved");
                                        preparedStatement2.setObject(2, selected_hospital_id);
                                        preparedStatement2.executeUpdate();
                                        new Alert(Alert.AlertType.INFORMATION, "User added Successful", ButtonType.OK).showAndWait();
                                    } else {
                                        new Alert(Alert.AlertType.ERROR, "Failed,Try Again", ButtonType.OK).showAndWait();
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                tblUserDetails.getItems().clear();
                                loadAllUsers();
                                tblUserDetails.refresh();
                                clearForm();
                                add_new_user.requestFocus();
                            }
                        } else if (cmbUserRole.getSelectionModel().getSelectedIndex() == 3) {
                            boolean locationValidation = FormValidation.comboboxValidation(cmbUserLocation, lbl_user_location, "* Select Location");

                            if (locationValidation) {
                                try {
                                    String sql = "INSERT INTO user VALUES (?,?,?,?,?,?,?,?)";
                                    PreparedStatement preparedStatement1 = Dbconnection.getInstance().getConnection().prepareStatement(sql);
                                    preparedStatement.setObject(1, id.getText());
                                    preparedStatement.setObject(2, first_name.getText());
                                    preparedStatement.setObject(3, contact_no.getText());
                                    preparedStatement.setObject(4, email.getText());
                                    preparedStatement.setObject(5, user_name.getText());
                                    preparedStatement.setObject(6, txtPasswordHide.getText());
                                    preparedStatement.setObject(7, cmbUserRole.getSelectionModel().getSelectedItem());
                                    preparedStatement.setObject(8, cmbUserLocation.getSelectionModel().getSelectedItem());

                                    String selectedItem = (String) cmbUserLocation.getSelectionModel().getSelectedItem();
                                    String selected_quarantine_center_id = selectedItem.substring(0, 4);

                                    int affectedRows = preparedStatement.executeUpdate();
                                    if (affectedRows > 0) {
                                        // update quarantine database....
                                        PreparedStatement preparedStatement2 = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET status=? WHERE id=?");
                                        preparedStatement2.setObject(1, "Reserved");
                                        preparedStatement2.setObject(2, selected_quarantine_center_id);
                                        preparedStatement2.executeUpdate();
                                        new Alert(Alert.AlertType.INFORMATION, "User added Successful", ButtonType.OK).showAndWait();
                                    } else {
                                        new Alert(Alert.AlertType.ERROR, "Failed,Try Again", ButtonType.OK).showAndWait();
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                tblUserDetails.getItems().clear();
                                loadAllUsers();
                                tblUserDetails.refresh();
                                clearForm();
                                add_new_user.requestFocus();
                            }
                        } else {
                            try {
                                String sql = "INSERT INTO user (id,first_name,contact_no,email,user_name,password,role) VALUES (?,?,?,?,?,?,?)";
                                PreparedStatement preparedStatement1 = Dbconnection.getInstance().getConnection().prepareStatement(sql);
                                preparedStatement.setObject(1, id.getText());
                                preparedStatement.setObject(2, first_name.getText());
                                preparedStatement.setObject(3, contact_no.getText());
                                preparedStatement.setObject(4, email.getText());
                                preparedStatement.setObject(5, user_name.getText());
                                preparedStatement.setObject(6, txtPasswordHide.getText());
                                preparedStatement.setObject(7, cmbUserRole.getSelectionModel().getSelectedItem());

                                int affectedRows = preparedStatement.executeUpdate();
                                if (affectedRows > 0) {
                                    new Alert(Alert.AlertType.INFORMATION, "User added Successful", ButtonType.OK).showAndWait();
                                } else {
                                    new Alert(Alert.AlertType.ERROR, "Failed,Try Again", ButtonType.OK).showAndWait();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            tblUserDetails.getItems().clear();
                            loadAllUsers();
                            tblUserDetails.refresh();
                            clearForm();
                            add_new_user.requestFocus();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                if (user_name.getText().equals(tblUserDetails.getSelectionModel().getSelectedItem().getUser_name())) {

                    if (tblUserDetails.getSelectionModel().getSelectedItem().getUser_role().equals("Hospital IT")) {
                        hospitalITUpdate();
                    } else if (tblUserDetails.getSelectionModel().getSelectedItem().getUser_role().equals("QuarantineCenter IT")) {
                        quarantineCenterITUpdate();
                    } else if (tblUserDetails.getSelectionModel().getSelectedItem().getUser_role().equals("Admin") || tblUserDetails.getSelectionModel().getSelectedItem().getUser_role().equals("P.S.T.F")) {
                        adminAndPSTFUpdate();
                    }
                } else {
                    try {
                        PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("SELECT user_name FROM user WHERE user_name=?");
                        preparedStatement.setObject(1, user_name.getText());
                        ResultSet resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            new Alert(Alert.AlertType.ERROR, "Sorry,user name all ready exit.", ButtonType.OK).showAndWait();
                            user_name.selectAll();
                            user_name.requestFocus();
                        } else {
                            if (tblUserDetails.getSelectionModel().getSelectedItem().getUser_role().equals("Hospital IT")) {
                                hospitalITUpdate();
                            } else if (tblUserDetails.getSelectionModel().getSelectedItem().getUser_role().equals("Quarantine Center IT")) {
                                quarantineCenterITUpdate();
                            } else if (tblUserDetails.getSelectionModel().getSelectedItem().getUser_role().equals("Admin") || tblUserDetails.getSelectionModel().getSelectedItem().getUser_role().equals("P.S.T.F")) {
                                adminAndPSTFUpdate();
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        UserTM selectedUser = tblUserDetails.getSelectionModel().getSelectedItem();
        if ( selectedUser== null) {
            new Alert(Alert.AlertType.WARNING,"Select user want to delete",ButtonType.OK).showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure to delete this user?",ButtonType.YES,ButtonType.NO);
        alert.showAndWait();
        // If hospital It delete...
        if (alert.getResult() == ButtonType.YES){
            if (cmbUserRole.getSelectionModel().getSelectedIndex() ==2){
                try {
                    String selected_location = tblUserDetails.getSelectionModel().getSelectedItem().getLocation();
                    String substring_location_id = selected_location.substring(0,4);

                    PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET status=? WHERE hospital_id=?");
                    preparedStatement.setObject(1,"Not Reserved");
                    preparedStatement.setObject(2,substring_location_id);
                    int hos = preparedStatement.executeUpdate();

                    PreparedStatement preparedStatement1 =Dbconnection.getInstance().getConnection().prepareStatement("DELETE  FROM user WHERE id=?");
                    preparedStatement1.setObject(1,selectedUser.getUser_id());
                    int affectedRows = preparedStatement1.executeUpdate();
                    if (affectedRows > 0){
                        new Alert(Alert.AlertType.INFORMATION,"Deleted",ButtonType.OK).getResult();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //if Qurantine center IT delete ....
            } else if (cmbUserRole.getSelectionModel().getSelectedIndex() ==3) {
                try {
                    String selected_location = tblUserDetails.getSelectionModel().getSelectedItem().getLocation();
                    String substring_location_id = selected_location.substring(0,4);

                    PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET status=? WHERE id=?");
                    preparedStatement.setObject(1,"Not Reserved");
                    preparedStatement.setObject(2,substring_location_id);
                    int hos = preparedStatement.executeUpdate();

                    PreparedStatement preparedStatement1 =Dbconnection.getInstance().getConnection().prepareStatement("DELETE  FROM user WHERE id=?");
                    preparedStatement1.setObject(1,selectedUser.getUser_id());
                    int affectedRows = preparedStatement1.executeUpdate();
                    if (affectedRows > 0){
                        new Alert(Alert.AlertType.INFORMATION,"Deleted",ButtonType.OK).getResult();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // if Admin or PSTF delete...
            } else {
                try {
                    PreparedStatement preparedStatement =Dbconnection.getInstance().getConnection().prepareStatement("DELETE  FROM user WHERE id=?");
                    preparedStatement.setObject(1,selectedUser.getUser_id());
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0){
                        new Alert(Alert.AlertType.INFORMATION,"Deleted",ButtonType.OK).getResult();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            tblUserDetails.getItems().clear();
            tblUserDetails.refresh();
            loadAllUsers();
            clearForm();
            add_new_user.requestFocus();
        } else {
            tblUserDetails.refresh();
            clearForm();
            add_new_user.requestFocus();
        }
    }

    // password hide...
    public void hide_password_OnMouseClicked(MouseEvent mouseEvent) {
        eye_hide_password.setVisible(false);
        txtPasswordHide.setVisible(false);
        eye_show_password.setVisible(true);
        txtPasswordShow.setVisible(true);

        txtPasswordShow.requestFocus();
        txtPasswordShow.deselect();
        txtPasswordShow.positionCaret(txtPasswordShow.getLength());
    }

    // password show..
    public void show_password_OnMouseClicked(MouseEvent mouseEvent) {
        eye_show_password.setVisible(false);
        txtPasswordShow.setVisible(false);
        eye_hide_password.setVisible(true);
        txtPasswordHide.setVisible(true);

        txtPasswordHide.requestFocus();
        txtPasswordHide.deselect();
        txtPasswordHide.positionCaret(txtPasswordHide.getLength());
    }

    //hospital update method...
    private void hospitalITUpdate() {
        if (cmbUserRole.getSelectionModel().getSelectedIndex() == 0 || cmbUserRole.getSelectionModel().getSelectedIndex() == 1) {
            String selected_location = tblUserDetails.getSelectionModel().getSelectedItem().getLocation();
            String substring_location_id = selected_location.substring(0,4);

            try {
                PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET status=? WHERE hospital_id=?");
                preparedStatement.setObject(1,"Not Reserved");
                preparedStatement.setObject(2,substring_location_id);
                int affectedRows =preparedStatement.executeUpdate();
                if (affectedRows > 0){
                    System.out.println("wade goda");
                }
                updateSQLofAdminAndPSTF();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            tblRefresh();
        } else if (cmbUserRole.getSelectionModel().getSelectedIndex() == 2) {

            if (!cmbUserLocation.getSelectionModel().getSelectedItem().equals(tblUserDetails.getSelectionModel().getSelectedItem().getLocation())) {
                String selected_item = (String) cmbUserLocation.getSelectionModel().getSelectedItem();
                String selected_hospital_id = selected_item.substring(0, 4);

                try {
                    PreparedStatement preparedStatement1 = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET status=? WHERE hospital_id=?");
                    preparedStatement1.setObject(1, "Reserved");
                    preparedStatement1.setObject(2, selected_hospital_id);
                    int i = preparedStatement1.executeUpdate();
                    if (i > 0) {
                        System.out.println("niyamai");
                    }

                    String selected_location = tblUserDetails.getSelectionModel().getSelectedItem().getLocation();
                    String substring_location_id = selected_location.substring(0, 4);

                    PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET status=? WHERE hospital_id=?");
                    preparedStatement.setObject(1, "Not Reserved");
                    preparedStatement.setObject(2,substring_location_id);
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("wade goda");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            updateSQLofLocation();
            tblRefresh();
        } else {
            boolean locationValidation = FormValidation.comboboxValidation(cmbUserLocation,lbl_user_location,"* select location");
            if (locationValidation){
                String selected_hospital_location = tblUserDetails.getSelectionModel().getSelectedItem().getLocation();
                String selected_hospital_location_id = selected_hospital_location.substring(0,4);

                try {
                    PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET status=? where hospital_id=?");
                    preparedStatement.setObject(1,"Not Reserved");
                    preparedStatement.setObject(2,selected_hospital_location_id);
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0){
                        System.out.println("wade goda");
                    }
                    String selected_QuarantineCenter = (String) cmbUserLocation.getSelectionModel().getSelectedItem();
                    String selected_QuarantineCenter_id = selected_QuarantineCenter.substring(0,4);

                    PreparedStatement preparedStatement2 = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET status=? WHERE id=?");
                    preparedStatement2.setObject(1,"Reserved");
                    preparedStatement2.setObject(2,selected_QuarantineCenter_id);
                    int i = preparedStatement2.executeUpdate();
                    if (i > 0){
                        System.out.println("niyamai");
                    }
                } catch (SQLException e) {
                        e.printStackTrace();
                 }

                updateSQLofLocation();
                tblRefresh();
            }
        }
    }

    // quarantine center update method...
    private void quarantineCenterITUpdate() {
        if (cmbUserRole.getSelectionModel().getSelectedIndex() == 0 || cmbUserRole.getSelectionModel().getSelectedIndex() == 1) {
            String selected_location = tblUserDetails.getSelectionModel().getSelectedItem().getLocation();
            String substring_location_id = selected_location.substring(0,4);

            try {
                PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET  status=? WHERE id=?");
                preparedStatement.setObject(1,"Not Reserved");
                preparedStatement.setObject(2,substring_location_id);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("wadegoda");
                }
                updateSQLofAdminAndPSTF();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            tblRefresh();
        }else  if (cmbUserRole.getSelectionModel().getSelectedIndex() ==2){
            boolean locationValidation = FormValidation.comboboxValidation(cmbUserLocation,lbl_user_location,"* select location");

            if (locationValidation){
                String selected_qc_location = tblUserDetails.getSelectionModel().getSelectedItem().getLocation();
                String selected_qc_location_id = selected_qc_location.substring(0,4);

                try {
                    PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET status=? WHERE id=?");
                    preparedStatement.setObject(1,"Not Reserved");
                    preparedStatement.setObject(2,selected_qc_location_id);
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("niyamai");
                    }
                    String selected_hospital = (String) cmbUserLocation.getSelectionModel().getSelectedItem();
                    String selected_hospital_id = selected_hospital.substring(0,4);

                    PreparedStatement preparedStatement1 = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET status=? WHERE  hospital_id=?");
                    preparedStatement1.setObject(1,"Reserved");
                    preparedStatement1.setObject(2,selected_hospital_id);
                    int i = preparedStatement1.executeUpdate();
                    if (i > 0) {
                        System.out.println("wadegoda");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                updateSQLofLocation();
                tblRefresh();
            }
        } else {
            if (!cmbUserLocation.getSelectionModel().getSelectedItem().equals(tblUserDetails.getSelectionModel().getSelectedItem().getLocation())) {
                String selectedItem = (String) cmbUserLocation.getSelectionModel().getSelectedItem();
                String selected_quarantine_id = selectedItem.substring(0,4);

                try {
                    PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET status=? WHERE id=?");
                    preparedStatement.setObject(1,"Reserved");
                    preparedStatement.setObject(2,selected_quarantine_id);
                    int i = preparedStatement.executeUpdate();
                    if (i > 0) {
                        System.out.println("wadegoda");
                    }
                    String selected_location = tblUserDetails.getSelectionModel().getSelectedItem().getLocation();
                    String substring_location_id = selected_location.substring(0,4);

                    PreparedStatement preparedStatement1 = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET status=? WHERE id=?");
                    preparedStatement1.setObject(1,"Not Reserved");
                    preparedStatement1.setObject(2,substring_location_id);
                    int affectedRows = preparedStatement1.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("wadegoda");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            updateSQLofLocation();
            tblRefresh();
        }
    }

    //Admin and PSTF update method...
    private void adminAndPSTFUpdate() {
        if (cmbUserRole.getSelectionModel().getSelectedIndex() == 0 || cmbUserRole.getSelectionModel().getSelectedIndex() == 1){
            try {
                String sql ="UPDATE user SET id=?,first_name=?,contact_no=?,email=?,user_name=?,password=?,role=? WHERE id=?";
                PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement(sql);
                preparedStatement.setObject(1,lbl_id.getText());
                preparedStatement.setObject(2,first_name.getText());
                preparedStatement.setObject(3,contact_no.getText());
                preparedStatement.setObject(4,email.getText());
                preparedStatement.setObject(5,user_name.getText());
                preparedStatement.setObject(6,txtPasswordHide.getText());
                preparedStatement.setObject(7,cmbUserRole.getSelectionModel().getSelectedItem());
                preparedStatement.setObject(8,tblUserDetails.getSelectionModel().getSelectedItem().getUser_id());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0){
                    System.out.println("Update Successful");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tblRefresh();
        } else if (cmbUserRole.getSelectionModel().getSelectedIndex() == 2) {
            boolean locationValidation = FormValidation.comboboxValidation(cmbUserLocation,lbl_user_location,"* select location");
            if (locationValidation) {
                String selectedItem = (String) cmbUserLocation.getSelectionModel().getSelectedItem();
                String selected_hospital_id = selectedItem.substring(0,4);

                try {
                    PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE  hospital SET status=? WHERE hospital_id=?");
                    preparedStatement.setObject(1,"Reserved");
                    preparedStatement.setObject(2,selected_hospital_id);
                    int i = preparedStatement.executeUpdate();
                    if (i > 0){
                        System.out.println("goda");
                    }
                    updateSQLofLocation();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                tblRefresh();
            }
        }else {
            boolean locationValidation = FormValidation.comboboxValidation(cmbUserLocation,lbl_user_location,"* select location");

            if (locationValidation){
                String selectedItem = (String) cmbUserLocation.getSelectionModel().getSelectedItem();
                String selected_quarantineCenter_id = selectedItem.substring(0,4);

                try {
                    PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET status=? WHERE id=?");
                    preparedStatement.setObject(1,"Reserved");
                    preparedStatement.setObject(2,selected_quarantineCenter_id);
                    int i = preparedStatement.executeUpdate();
                    if (i > 0) {
                        System.out.println("goda");
                    }
                    updateSQLofLocation();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                tblRefresh();
            }
        }
    }

    // password generate..
    private String generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$%^&*()+=";
        String numbers = "0123456789";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return String.valueOf(password);
    }

    // load all hospital to combobox...
    private void loadAllHospitals() {
        try {
            PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("SELECT * FROM hospital WHERE status=?");
            preparedStatement.setObject(1, "Not Reserved");
            ResultSet resultSet = preparedStatement.executeQuery();
            cmbUserLocation.getItems().clear();
            while (resultSet.next()) {
                String hospital_id = resultSet.getString(1);
                String hospital_name = resultSet.getString(2);

                cmbUserLocation.getItems().add(hospital_id + "-" + hospital_name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //load all quarantine centers to combobox...
    private void loadAllQuarantineCenters() {
        try {
            PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("SELECT * FROM quarantine_center WHERE status=?");
            preparedStatement.setObject(1, "Not Reserved");
            ResultSet resultSet = preparedStatement.executeQuery();
            cmbUserLocation.getItems().clear();
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String center_name = resultSet.getString(2);

                cmbUserLocation.getItems().add(id + "-" + center_name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //load all users to tabale...
    private void loadAllUsers() {
        try {
            Statement statement = Dbconnection.getInstance().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
            while (resultSet.next()) {
                String uid = resultSet.getString(1);
                String first_name = resultSet.getString(2);
                String contact_no = resultSet.getString(3);
                String email = resultSet.getString(4);
                String user_name = resultSet.getString(5);
                String password = resultSet.getString(6);
                String role = resultSet.getString(7);
                String location = resultSet.getString(8);

                UserTM userTM = new UserTM(uid, first_name, contact_no, email, user_name, password, role, location);
                tblUserDetails.getItems().add(userTM);
                userTMArrayList.add(userTM);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateSQLofAdminAndPSTF() {
        String sql = "UPDATE user SET id=?,first_name=?,contact_no=?,email=?,user_name=?,password=?,role=? WHERE id=?";
        try {
            PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setObject(1,lbl_id.getText());
            preparedStatement.setObject(2,first_name.getText());
            preparedStatement.setObject(3,contact_no.getText());
            preparedStatement.setObject(4,email.getText());
            preparedStatement.setObject(5,user_name.getText());
            preparedStatement.setObject(6,txtPasswordHide.getText());
            preparedStatement.setObject(7,cmbUserRole.getSelectionModel().getSelectedItem());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Update Successful");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateSQLofLocation() {

        String sql = "UPDATE user SET id=?,first_name=?,contact_no=?,email=?,user_name=?,password=?,role=?,location=? WHERE id=?";
        try {
            PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setObject(1,lbl_id.getText());
            preparedStatement.setObject(2,first_name.getText());
            preparedStatement.setObject(3,contact_no.getText());
            preparedStatement.setObject(4,email.getText());
            preparedStatement.setObject(5,user_name.getText());
            preparedStatement.setObject(6,txtPasswordHide.getText());
            preparedStatement.setObject(7,cmbUserRole.getSelectionModel().getSelectedItem());
            preparedStatement.setObject(8,cmbUserLocation.getSelectionModel().getSelectedItem());
            preparedStatement.setObject(9,tblUserDetails.getSelectionModel().getSelectedItem().getUser_id());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Update Successful");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void tblRefresh() {
        tblUserDetails.getItems().clear();
        loadAllUsers();
        tblUserDetails.refresh();
        clearForm();
        save.setText("Save");
        add_new_user.requestFocus();
    }

    private void clearForm() {
        id.setText("UXXX");
        first_name.clear();
        contact_no.clear();
        email.clear();
        user_name.clear();
        txtPasswordHide.clear();
        search_username.clear();
        cmbUserRole.getSelectionModel().clearSelection();
        cmbUserLocation.getSelectionModel().clearSelection();
        tblUserDetails.getSelectionModel().clearSelection();
    }

}