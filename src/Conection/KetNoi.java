/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class KetNoi {

    public static Connection layKetNoi() {
        Connection ketNoi = null;
        String uRL = "jdbc:sqlserver://localhost;databaseName=QLGIAYDEP";
        String userName = "sa";
        String password = "123";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            ketNoi = DriverManager.getConnection(uRL, userName, password);
            if (ketNoi != null) {
                System.out.println("Da ket noi");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Khong ket noi duoc CSDL");
        }
        return ketNoi;
    }
}
