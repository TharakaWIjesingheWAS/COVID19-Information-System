package controller;

import dbconnection.Dbconnection;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.FormValidation;
import util.UserTM;

import javax.imageio.IIOException;
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
    public Label id;
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

    private ArrayList<UserTM> userTMArrayList = new ArrayList<>();


    public void initialize(){
/*        FadeTransition fade = new FadeTransition(Duration.seconds(1),root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setCycleCount(1);

        fade.play();
*/
        Platform.runLater(() ->
        {
            add_new_user.requestFocus();
        }
        );

        cmbUserLocation.setVisible(false);
        txtPasswordShow.setVisible(false);
        eye_show_password.setVisible(false);


        cmbUserRole.getItems().add("Admin");
        cmbUserRole.getItems().add("P.S.T.F");
        cmbUserRole.getItems().add("Hospital IT");
        cmbUserRole.getItems().add("Quarantine Center IT");

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

                } else if (newValue.intValue() ==3) {
                    cmbUserLocation.setVisible(true);
                    cmbUserLocation.setPromptText("select Quarantine Center");
                    cmbUserLocation.getSelectionModel().clearSelection();
                    cmbUserLocation.getSelectionModel().select(null);
                    cmbUserLocation.getItems().clear();
                    loadAllQuarantineCenters();
                }else {
                    cmbUserLocation.setVisible(false);
                }
            }
        });

        tblUserDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("user_id"));
        tblUserDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("user_name"));
        tblUserDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("first_name"));
        tblUserDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("role"));
        tblUserDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("contact_no"));
    //    tblUserDetails.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("email"));
    //    tblUserDetails.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("location"));
    //    tblUserDetails.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("password"));
        loadAllUsers();

        tblUserDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserTM>() {
            @Override
            public void changed(ObservableValue<? extends UserTM> observable, UserTM oldValue, UserTM newValue) {
                UserTM selectedUser = tblUserDetails.getSelectionModel().getSelectedItem();
                if (selectedUser == null){
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
                }
                else {
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
                    if (userTM.getFirst_name().contains(newValue) || userTM.getUser_name().contains(newValue)){
                        tblUserDetails.getItems().add(userTM);
                    }
                }
            }
        });
    }

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

        for (int i=4; i < length; i++){
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return String.valueOf(password);
    }

    private void loadAllHospitals() {
        try {
            PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("SELECT * FROM hospital WHERE status=?");
            preparedStatement.setObject(1,"Not Reserved");
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

    private  void loadAllQuarantineCenters() {
        try {
            PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("SELECT * FROM quarantine_center WHERE status=?");
            preparedStatement.setObject(1,"Not Reserved");
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
                String role  = resultSet.getString(7);
                String location = resultSet.getString(8);

                UserTM userTM = new UserTM(uid,first_name,contact_no,email,user_name,password,role,location);
                tblUserDetails.getItems().add(userTM);
                userTMArrayList.add(userTM);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void btnHome_OnAction(ActionEvent event) throws IOException {
        Scene mainScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml")));
        Stage primaryStage =(Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(mainScene);
        primaryStage.centerOnScreen();
    }

    public void btnNewUser_OnAction(ActionEvent event) throws SQLException {
        PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("SELECT id FROM user ORDER BY id DESC LIMIT 1");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            id.setText("U001");
        }else {
            String last_id = resultSet.getString(1);
            String substring_id = last_id.substring(1,4);
            int new_id = Integer.parseInt(substring_id) + 1;
            if (new_id < 10) {
                id.setText("U00" + new_id);
            }else if (new_id < 100) {
                id.setText("U0" + new_id);
            }else {
                id.setText("U" + new_id);
            }
        }
        first_name.requestFocus();
    }

    public void btnSave_OnAction(ActionEvent event) {

        //boolean userIDValidation = FormValidation.userIDValidation(i,lbl_id,"* Press New User + ");
        boolean nameValidation = FormValidation.nameValidation(first_name,lbl_name,"* Insert valid name");
        boolean contactNumberValidation = FormValidation.contactNumberValidation(contact_no,lbl_contact_no,"* xxx-xxxxxxx");
        boolean emailValidation = FormValidation.emailValidation(email,lbl_email,"* name@example.com");
        boolean userNameValidation = FormValidation.usernameValidation(user_name,lbl_userName,"* Insert valid user name(use a-z , A-Z , 0-9 , @ , .)");
        boolean passwordValidation = FormValidation.passwordValidation(txtPasswordHide,lbl_password,"* password length must be 8");
        boolean roleValidation = FormValidation.comboboxValidation(cmbUserRole,lbl_user_role,"* Select Role");

        if (nameValidation && contactNumberValidation && emailValidation && userNameValidation && passwordValidation && roleValidation){
            if (save.getText().equals("SAVE")) {
                try {
                    PreparedStatement preparedStatement = Dbconnection.getInstance().getConnection().prepareStatement("SELECT user_name FROM user WHERE user_name=?");
                    preparedStatement.setObject(1,user_name.getText());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        new Alert(Alert.AlertType.ERROR,"Sorry!,Username all ready exit,Choose another one",ButtonType.OK).showAndWait();
                        user_name.selectAll();
                        user_name.requestFocus();
                    }else {
                        if (cmbUserRole.getSelectionModel().getSelectedIndex() == 2) {
                            boolean locationValidation = FormValidation.comboboxValidation(cmbUserLocation,lbl_user_location,"* Select Location");

                            if (locationValidation) {
                                try {
                                    String sql = "INSERT INTO user VALUES (?,?,?,?,?,?,?,?)";
                                    PreparedStatement preparedStatement1 = Dbconnection.getInstance().getConnection().prepareStatement(sql);
                                    preparedStatement.setObject(1,id.getText());
                                    preparedStatement.setObject(2,first_name.getText());
                                    preparedStatement.setObject(3,contact_no.getText());
                                    preparedStatement.setObject(4,email.getText());
                                    preparedStatement.setObject(5,user_name.getText());
                                    preparedStatement.setObject(6,txtPasswordHide.getText());
                                    preparedStatement.setObject(7,cmbUserRole.getSelectionModel().getSelectedItem());
                                    preparedStatement.setObject(8,cmbUserLocation.getSelectionModel().getSelectedItem());

                                    String selectedItem = (String) cmbUserLocation.getSelectionModel().getSelectedItem();
                                    String selected_hospital_id = selectedItem.substring(0,4);

                                    int affectedRows = preparedStatement.executeUpdate();
                                    if (affectedRows > 0) {
                                        PreparedStatement preparedStatement2 = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET status=? WHERE hospital_id=?");
                                        preparedStatement2.setObject(1,"Reserved");
                                        preparedStatement2.setObject(2,selected_hospital_id);
                                        preparedStatement2.executeUpdate();
                                        new Alert(Alert.AlertType.INFORMATION,"User added Successful",ButtonType.OK).showAndWait();
                                    }else {
                                        new Alert(Alert.AlertType.ERROR,"Failed,Try Again",ButtonType.OK).showAndWait();
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
                        } else if (cmbUserRole.getSelectionModel().getSelectedIndex() == 3){
                            boolean locationValidation = FormValidation.comboboxValidation(cmbUserLocation,lbl_user_location,"* Select Location");

                            if (locationValidation) {
                                try {
                                    String sql = "INSERT INTO user VALUES (?,?,?,?,?,?,?,?)";
                                    PreparedStatement preparedStatement1 = Dbconnection.getInstance().getConnection().prepareStatement(sql);
                                    preparedStatement.setObject(1,id.getText());
                                    preparedStatement.setObject(2,first_name.getText());
                                    preparedStatement.setObject(3,contact_no.getText());
                                    preparedStatement.setObject(4,email.getText());
                                    preparedStatement.setObject(5,user_name.getText());
                                    preparedStatement.setObject(6,txtPasswordHide.getText());
                                    preparedStatement.setObject(7,cmbUserRole.getSelectionModel().getSelectedItem());
                                    preparedStatement.setObject(8,cmbUserLocation.getSelectionModel().getSelectedItem());

                                    String selectedItem = (String) cmbUserLocation.getSelectionModel().getSelectedItem();
                                    String selected_quarantine_center_id = selectedItem.substring(0,4);

                                    int affectedRows = preparedStatement.executeUpdate();
                                    if (affectedRows > 0) {
                                        PreparedStatement preparedStatement2 = Dbconnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET status=? WHERE id=?");
                                        preparedStatement2.setObject(1,"Reserved");
                                        preparedStatement2.setObject(2,selected_quarantine_center_id);
                                        preparedStatement2.executeUpdate();
                                        new Alert(Alert.AlertType.INFORMATION,"User added Successful",ButtonType.OK).showAndWait();
                                    }else {
                                        new Alert(Alert.AlertType.ERROR,"Failed,Try Again",ButtonType.OK).showAndWait();
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
                                preparedStatement.setObject(1,id.getText());
                                preparedStatement.setObject(2,first_name.getText());
                                preparedStatement.setObject(3,contact_no.getText());
                                preparedStatement.setObject(4,email.getText());
                                preparedStatement.setObject(5,user_name.getText());
                                preparedStatement.setObject(6,txtPasswordHide.getText());
                                preparedStatement.setObject(7,cmbUserRole.getSelectionModel().getSelectedItem());

                                int affectedRows = preparedStatement.executeUpdate();
                                if (affectedRows > 0) {
                                    new Alert(Alert.AlertType.INFORMATION,"User added Successful",ButtonType.OK).showAndWait();
                                }else {
                                    new Alert(Alert.AlertType.ERROR,"Failed,Try Again",ButtonType.OK).showAndWait();
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
            }
            }
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
