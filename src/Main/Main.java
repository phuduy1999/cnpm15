package Main;

import display.Login;
import display.MainJFrame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import Conection.KetNoi;

/**
 *
 * @author Admin
 */

public class Main {

    public static void main(String[] args) {
        Login loginFrame = new Login();
        loginFrame.setTitle("Đăng Nhập Hệ Thống");
        loginFrame.setResizable(false);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }
}
