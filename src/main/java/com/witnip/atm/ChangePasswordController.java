package com.witnip.atm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {

    User user;

    @FXML
    PasswordField txtCurrentPassword,txtNewPassword,txtConfirmPassword;
    @FXML
    Button btnChangePassword;
    public void setUser(User user){
        this.user = user;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnChangePassword.setOnAction(event -> {
            if(!txtCurrentPassword.getText().trim().isEmpty()
            && !txtNewPassword.getText().trim().isEmpty()
            && !txtConfirmPassword.getText().trim().isEmpty()){
                String currentPassword = txtCurrentPassword.getText().trim();
                String newPassword = txtNewPassword.getText().trim();
                String confirmPassword = txtConfirmPassword.getText().trim();
                if(newPassword.equals(confirmPassword)){
                    DBUtil.changePassword(event,currentPassword,confirmPassword, user);
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("New Password and confirm password must be same");
                    alert.show();
                }

            }
        });
    }
}
