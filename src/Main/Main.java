package Main;

import display.DangNhap;
/**
 *
 * @author Admin
 */
public class Main {

    public static void main(String[] args) {
        DangNhap dangNhap = new DangNhap();
        dangNhap.setTitle("Đăng Nhập Hệ Thống");
        dangNhap.setResizable(false);
        dangNhap.setLocationRelativeTo(null);
        dangNhap.setVisible(true);
    }
}
