package cn.mikylin.myths.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtils {

    public static Connection conn(String driverName,String url,String userName,String password) {
        try {
            Class.forName(driverName);
            return DriverManager.getConnection(url,userName,password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("class driver exception.");
        }
    }
}
