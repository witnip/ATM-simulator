package com.witnip.atm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    Button btnLogin;
    @FXML
    TextField txtUsername;
    @FXML
    PasswordField txtPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogin.setOnAction(event -> {
            if(!txtUsername.getText().isEmpty() && !txtPassword.getText().isEmpty()){
                DBUtil.userLogin(event,txtUsername.getText().trim(), txtPassword.getText().trim());
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Fields can't be empty");
                alert.show();
            }
        });
    }
}
