package com.witnip.atm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ATMController implements Initializable {


    User user = null;
    @FXML
    TextField txtAmount,txtAccountNumber;

    @FXML
    Button btnTransferMoney,btnCheckBalance,btnChangePassword;

    public void setUser(User user){
        this.user = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnTransferMoney.setOnAction(event -> {
            double Amount = Double.parseDouble(txtAmount.getText().trim());
            int AccountNo = Integer.parseInt(txtAccountNumber.getText().trim());
            if(!txtAmount.getText().isEmpty() && !txtAccountNumber.getText().isEmpty()){
                DBUtil.transferAmount(AccountNo,Amount,user);
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Fields can't be empty");
                alert.show();
            }
        });

        btnCheckBalance.setOnAction(event -> {
            double currentAmount = DBUtil.checkBalance(user.getAccountNo());
            txtAmount.setText("%s".formatted(currentAmount));
        });

        btnChangePassword.setOnAction(event -> {
            DBUtil.gotoChangePassword(event,"ChangePassword.fxml","Change Password",user);
        });
    }
}