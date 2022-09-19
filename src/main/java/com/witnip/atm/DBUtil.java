package com.witnip.atm;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class DBUtil {
    public static Connection getConnection() {
        Connection mConnection = null;
        String databaseName = "atm";
        String databaseUser = "root";
        String databasePassword = "";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            mConnection = DriverManager.getConnection(url, databaseUser, databasePassword);
            System.out.println("Database connection successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mConnection;
    }

    public static void gotoHome(ActionEvent event, String fxmlFile, String title, User user) {
        Parent root = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DBUtil.class.getResource(fxmlFile));
            root = fxmlLoader.load();
            ATMController homeController = fxmlLoader.getController();
            homeController.setUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        assert root != null;
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void gotoChangePassword(ActionEvent event, String fxmlFile, String title, User user) {
        Parent root = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DBUtil.class.getResource(fxmlFile));
            root = fxmlLoader.load();
            ChangePasswordController changePasswordController = fxmlLoader.getController();
            changePasswordController.setUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        assert root != null;
        stage.setScene(new Scene(root));
        stage.show();
    }


    public static void userLogin(ActionEvent event, String username, String password) {
        Connection mConnection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM `users` WHERE `users`.`Username` = ?";
        try {
            preparedStatement = mConnection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Wrong username and password");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String actualPassword = resultSet.getString("password");
                    if (actualPassword.equals(password)) {
                        User user = new User();
                        user.setUsername(username);
                        user.setPassword(password);
                        int UserID = resultSet.getInt("userID");
                        String accQuery = "SELECT * FROM `account` WHERE `UserID` = ?";
                        PreparedStatement preparedStatement2 = mConnection.prepareStatement(accQuery);
                        preparedStatement2.setInt(1, UserID);
                        ResultSet resultSet2 = preparedStatement2.executeQuery();
                        while (resultSet2.next()) {
                            int AccountNo = resultSet2.getInt("AccountNo");
                            double Amount = resultSet2.getDouble("Amount");
                            user.setAccountNo(AccountNo);
                            user.setAmount(Amount);
                            user.setUserID(UserID);
                            gotoHome(event, "ATMHome.fxml", "ATM Simulator", user);
                        }

                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Wrong username or password");
                        alert.show();
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (mConnection != null) {
                try {
                    mConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void transferAmount(int AccountNo, double Amount, User currentUser) {
        Connection mConnection = getConnection();
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        double currentUserBalance = checkBalance(currentUser.getAccountNo());
        double givenAccBalance = checkBalance(AccountNo);
        double validWithDraw = currentUserBalance-Amount;
        if(currentUserBalance < 100 && validWithDraw < 100){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Insufficient Fund");
            alert.show();
        }else{
            currentUserBalance  = currentUserBalance-Amount;
            givenAccBalance = givenAccBalance+Amount;
        }
        String query1 = "UPDATE `account` SET `Amount`=? WHERE `AccountNo` = ?";
        String query2 = "UPDATE `account` SET `Amount`=? WHERE `AccountNo` = ?";
        try {
            preparedStatement1 = mConnection.prepareStatement(query1);
            preparedStatement1.setDouble(1, currentUserBalance);
            preparedStatement1.setInt(2, currentUser.getAccountNo());

            preparedStatement2 = mConnection.prepareStatement(query2);
            preparedStatement2.setDouble(1, givenAccBalance);
            preparedStatement2.setInt(2, AccountNo);

            preparedStatement1.executeUpdate();
            preparedStatement2.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Fund Transfer Successful!!");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement1 != null) {
                try {
                    preparedStatement1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement2 != null) {
                try {
                    preparedStatement2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (mConnection != null) {
                try {
                    mConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static double checkBalance(int AccountNo) {
        Connection mConnection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        double Amount = 0;
        String query = "SELECT * FROM `account` WHERE `AccountNo` = ?";
        try {
            preparedStatement = mConnection.prepareStatement(query);
            preparedStatement.setInt(1, AccountNo);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No data found");
                alert.show();
            } else {
                while (resultSet.next()) {
                    Amount = resultSet.getDouble("Amount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (mConnection != null) {
                try {
                    mConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return Amount;
    }

    public static void changePassword(ActionEvent event, String currentPassword, String confirmPassword, User user) {
        Connection mConnection = getConnection();
        PreparedStatement preparedStatement1 = null,preparedStatement2 = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM `users` WHERE `users`.`Username` = ?";
        try {
            preparedStatement1 = mConnection.prepareStatement(query);
            preparedStatement1.setString(1,user.getUsername());
            resultSet = preparedStatement1.executeQuery();
            if(!resultSet.isBeforeFirst()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Login issue");
                alert.show();
            }else {
                while (resultSet.next()){
                    String actualPassword = resultSet.getString("password");
                    if(currentPassword.equals(actualPassword)){
                        String query2 = "UPDATE `users` SET `password`=? WHERE `users`.`Username` = ?";
                        preparedStatement2 = mConnection.prepareStatement(query2);
                        preparedStatement2.setString(1,confirmPassword);
                        preparedStatement2.setString(2,user.getUsername());
                        preparedStatement2.executeUpdate();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Password Updated Successfully!!");
                        alert.show();
                        gotoHome(event, "ATMHome.fxml", "ATM Simulator", user);
                    }else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Invalid current password");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement1 != null) {
                try {
                    preparedStatement1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement2 != null) {
                try {
                    preparedStatement2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (mConnection != null) {
                try {
                    mConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
