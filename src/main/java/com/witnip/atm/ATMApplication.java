package com.witnip.atm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

import static com.witnip.atm.DBUtil.getConnection;

public class ATMApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ATMApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("ATM Simulator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
//        Connection mConnection = getConnection();
        launch();
    }
}