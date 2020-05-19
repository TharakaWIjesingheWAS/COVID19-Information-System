package dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Dbconnection {
    private static Dbconnection dbconnection;
    private Connection connection;

    private  Dbconnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Covid19", "root", "mysql");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Dbconnection getInstance() {
        return (dbconnection == null) ? dbconnection = new Dbconnection() : dbconnection;
    }

    public Connection getConnection() {return connection;}
}
