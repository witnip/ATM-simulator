package com.witnip.atm;

public class User {
    private int UserID;
    private String username;
    private String password;
    private int AccountNo;
    private double Amount;

    public User() {
    }

    public User(int userID, String username, String password, int accountNo, double amount) {
        UserID = userID;
        this.username = username;
        this.password = password;
        AccountNo = accountNo;
        Amount = amount;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountNo() {
        return AccountNo;
    }

    public void setAccountNo(int accountNo) {
        AccountNo = accountNo;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }
}
