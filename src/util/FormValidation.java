package util;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class FormValidation {
    public static boolean textFieldIsNull(TextField t1, Label label1, String validationText) {
        boolean b = true;
        String s = null;

        if (t1.getText().trim().isEmpty()) {
            b = false;
            s =validationText;
        }
        label1.setText(s);
        return b;
    }

    public static boolean nameValidation(TextField textField,Label label,String validationText) {
        boolean b = true;
        String s = null;

        if (textField.getText().trim().isEmpty() || !textField.getText().matches("^[a-zA-Z] + ([\\sa-zA-Z])*")) {
            b = false;
            s = validationText;
            textField.requestFocus();
            textField.getSelection().getEnd();
        }
        label.setText(s);
        return b;
    }

    public static boolean cityValidation(TextField textField,Label label,String validationText) {
        boolean b = true;
        String s = null;

        if (textField.getText().trim().isEmpty() || !textField.getText().matches("^[a-zA-Z] + ([\\sa-zA-Z0-9])*")) {
            b = false;
            s = validationText;

        }
        label.setText(s);
        return b;
    }

    public static boolean comboboxValidation(ComboBox comboBox,Label label,String validationText) {
        boolean b = true;
        String s = null;

        if (comboBox.getSelectionModel().isEmpty()) {
            b = false;
            s = validationText;
        }
        label.setText(s);
        return b;
    }

    public static boolean directorNameValidation(TextField textField,Label label,String validationText) {
        boolean b = true;
        String s = null;

        if (textField.getText().trim().isEmpty() || !textField.getText().matches("^[a-zA-Z] + ([\\s.a-zA-Z])*")) {
            b = false;
            s = validationText;
        }
        label.setText(s);
        return  b;
    }

    public static boolean capacityValidation(TextField textField,Label label,String validationText) {
        boolean b = true;
        String s = null;

        if (textField.getText().trim().isEmpty() || !textField.getText().matches("^[0-9] +")) {
            b = false;
            s = validationText;
        }
        label.setText(s);
        return b;
    }

    public static boolean contactNumberValidation(TextField textField,Label label,String validationText) {
        boolean b = true;
        String s = null;

        if (textField.getText().trim().isEmpty() || !textField.getText().matches("^[0-9]{3}[-][0-9]{7}")) {
            b = false;
            s = validationText;
        }
        label.setText(s);
        return b;
    }

    public static boolean emailValidation(TextField textField,Label label,String validationText) {
        boolean b = true;
        String s = null;

        if (textField.getText().trim().isEmpty() || !textField.getText().matches("^[a-z]+([.0-9a-z])*[@]([a-z])+[.]([a-z])*")) {
            b = false;
            s = validationText;
        }
        label.setText(s);
        return b;
    }

    public static boolean usernameValidation(TextField textField,Label label,String validationText) {
        boolean b = true;
        String s = null;

        if (textField.getText().trim().isEmpty() || !textField.getText().matches("^[a-zA-Z]+([@.a-zA-Z0-9])*")) {
            b = false;
            s = validationText;
        }
        label.setText(s);
        return b;
    }

    public static boolean passwordValidation(TextField textField,Label label,String validationText) {
        boolean b = true;
        String s = null;

        if (textField.getText().trim().isEmpty() || !textField.getText().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()+=]).{8,}")){
            b = false;
            s = validationText;
        }
        label.setText(s);
        return b;
    }

    public static boolean numberValidation(TextField textField,Label label,String validationText) {
        boolean b = true;
        String s = null;

        if (textField.getText().trim().isEmpty() || textField.getText().matches("^[0-9] + ")) {
            b = false;
            s = validationText;
        }
        label.setText(s);
        return b;
    }

    public static boolean dateValidation(DatePicker datePicker, Label label, String validationText) {
        boolean b = true;
        String s = null;

        if (datePicker.getValue()==null){
            b = false;
            s = validationText;
        }
        label.setText(s);
        return b;
    }

    public static boolean userIDValidation(TextField label, Label l1, String validationText) {
        boolean b = true;
        String s = null;

        if (label.getText().equals("UXXX") || l1.getText().equals("HXXX") || label.getText().equals("QXXX")) {
            b = false;
            s = validationText;
        }
        l1.setText(s);
        return b;
    }
}
