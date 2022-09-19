module com.witnip.atm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires mysql.connector.java;
    requires java.sql;

    opens com.witnip.atm to javafx.fxml;
    exports com.witnip.atm;
}