package Main;

import display.DangNhap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.JMarsDarkTheme;

/**
 *
 * @author Admin
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel(new JMarsDarkTheme()));
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        DangNhap dangNhap = new DangNhap();
        dangNhap.setTitle("Đăng Nhập Hệ Thống");
        dangNhap.setResizable(false);
        dangNhap.setLocationRelativeTo(null);
        dangNhap.setVisible(true);
    }
}
