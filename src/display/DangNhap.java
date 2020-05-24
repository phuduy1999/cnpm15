/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import Conection.KetNoi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 *
 * @author User
 */
public class DangNhap extends javax.swing.JFrame {

    Preferences preferences;
    boolean rememberPreference;

    /**
     * Creates new form demo1
     */
    public DangNhap() {
        initComponents();
        loadHinh();
        nhoTK();
    }

    public DangNhap(boolean dangXuat) {
        initComponents();
        loadHinh();
        nhoTK();
        if (dangXuat == true) {
            jTextFieldRound_TenDangNhap.setText("");
            jPasswordFieldRound_MatKhau.setText("");
        }
    }

    private void loadHinh() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("src/image/nike.jpg"));
            jLabel_HinhDN.setIcon(new ImageIcon(image.getScaledInstance(jLabel_HinhDN.getWidth(), jLabel_HinhDN.getHeight(), Image.SCALE_SMOOTH)));
            jLabel_HinhDN.setBackground(Color.BLACK);
        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }

    private void nhoTK() { //nhớ tài khoản
        preferences = Preferences.userNodeForPackage(this.getClass());
        rememberPreference = preferences.getBoolean("rememberMe", Boolean.valueOf(""));
        if (rememberPreference) {
            jTextFieldRound_TenDangNhap.setText(preferences.get("username", ""));
            jPasswordFieldRound_MatKhau.setText(preferences.get("password", ""));
            jCheckBox_NhoTK.setSelected(rememberPreference);
        }
    }

    private void dangNhap() {
        int check = 0;
        String username = jTextFieldRound_TenDangNhap.getText();
        char[] pass = jPasswordFieldRound_MatKhau.getPassword();
        String password = "";
        for (char x : pass) {
            password += x;  //chuyen char[] sang string vi getText đối với jpassword là không nên dùng
        }
        if (username.equals("")) {
            JOptionPane.showMessageDialog(this, "Tên tài khoản không được để trống!");
            return;
        } else if (password.equals("")) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không được để trống!");
            return;
        } else {
            Connection ketNoi = KetNoi.layKetNoi();
            String sql = "select * from ACCOUNT where USERNAME =? and PASSWORD=? ";
            try {
                PreparedStatement ps = ketNoi.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if(rs.getBoolean("TRANGTHAI")==false){
                        JOptionPane.showMessageDialog(this, "Tài khoản của bạn đã bị khóa!");
                        return;
                    }
                    check = 1;
                    if (jCheckBox_NhoTK.isSelected() && !rememberPreference) {
                        preferences.put("username", username);
                        preferences.put("password", password);
                        preferences.putBoolean("rememberMe", true);
                    } else if (!jCheckBox_NhoTK.isSelected() && rememberPreference) {
                        preferences.put("username", "");
                        preferences.put("password", "");
                        preferences.putBoolean("rememberMe", false);
                    }
                    MainJFrame MJFrame = new MainJFrame(username);
                    MJFrame.setTitle("Quản Lý Shop Giày Dép");
                    MJFrame.setVisible(true);
                    preferences.put("username", username);
                    preferences.put("password", password);
                }
                rs.close();
                ps.close();
                ketNoi.close();
            } catch (SQLException ex) {
                Logger.getLogger(DangNhap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (check == 0) {
            JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không đúng!");
        } else if (check == 1) {
            this.dispose();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel_KhungDN = new javax.swing.JPanel();
        jLabel_TenDN = new javax.swing.JLabel();
        jLabel_MK = new javax.swing.JLabel();
        jLabel_DN = new javax.swing.JLabel();
        jButton_DN = new javax.swing.JButton();
        jCheckBox_NhoTK = new javax.swing.JCheckBox();
        jButton_QMK = new javax.swing.JButton();
        jPasswordFieldRound_MatKhau = new jtextfieldround.JPasswordFieldRound();
        jTextFieldRound_TenDangNhap = new jtextfieldround.JTextFieldRound();
        jPanel2 = new javax.swing.JPanel();
        jLabel_HinhDN = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

        jPanel_KhungDN.setBackground(new java.awt.Color(0, 0, 0));

        jLabel_TenDN.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_TenDN.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_TenDN.setText("TÊN TÀI KHOẢN");

        jLabel_MK.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_MK.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_MK.setText("MẬT KHẨU");

        jLabel_DN.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel_DN.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_DN.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_DN.setText("ĐĂNG NHẬP");

        jButton_DN.setBackground(new java.awt.Color(0, 0, 0));
        jButton_DN.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton_DN.setForeground(new java.awt.Color(255, 153, 153));
        jButton_DN.setText("Đăng Nhập");
        jButton_DN.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_DN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_DNActionPerformed(evt);
            }
        });

        jCheckBox_NhoTK.setBackground(new java.awt.Color(0, 0, 0));
        jCheckBox_NhoTK.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBox_NhoTK.setForeground(new java.awt.Color(255, 153, 153));
        jCheckBox_NhoTK.setText("Nhớ tài khoản");

        jButton_QMK.setBackground(new java.awt.Color(51, 51, 51));
        jButton_QMK.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton_QMK.setForeground(new java.awt.Color(255, 153, 153));
        jButton_QMK.setText("Quên mật khẩu");
        jButton_QMK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_QMKActionPerformed(evt);
            }
        });

        jPasswordFieldRound_MatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldRound_MatKhauActionPerformed(evt);
            }
        });

        jTextFieldRound_TenDangNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRound_TenDangNhapActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_KhungDNLayout = new javax.swing.GroupLayout(jPanel_KhungDN);
        jPanel_KhungDN.setLayout(jPanel_KhungDNLayout);
        jPanel_KhungDNLayout.setHorizontalGroup(
            jPanel_KhungDNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_KhungDNLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel_KhungDNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_KhungDNLayout.createSequentialGroup()
                        .addComponent(jButton_DN, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel_KhungDNLayout.createSequentialGroup()
                        .addGap(159, 159, 159)
                        .addComponent(jButton_QMK, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel_KhungDNLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel_KhungDNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPasswordFieldRound_MatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_MK, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_DN, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldRound_TenDangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox_NhoTK)
                    .addComponent(jLabel_TenDN, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 59, Short.MAX_VALUE))
        );
        jPanel_KhungDNLayout.setVerticalGroup(
            jPanel_KhungDNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_KhungDNLayout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
                .addComponent(jLabel_DN, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel_TenDN, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldRound_TenDangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel_MK, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordFieldRound_MatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel_KhungDNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox_NhoTK)
                    .addComponent(jButton_QMK))
                .addGap(36, 36, 36)
                .addComponent(jButton_DN, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        jLabel_HinhDN.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 409, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel_HinhDN, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel_HinhDN, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_KhungDN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_KhungDN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldRound_TenDangNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldRound_TenDangNhapActionPerformed
        dangNhap();
    }//GEN-LAST:event_jTextFieldRound_TenDangNhapActionPerformed

    private void jButton_QMKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_QMKActionPerformed
        JOptionPane.showMessageDialog(this, "Vui lòng liên hệ admin hoặc chủ cửa hàng để được cấp lại mật khẩu!");
    }//GEN-LAST:event_jButton_QMKActionPerformed

    private void jButton_DNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_DNActionPerformed
        dangNhap();
    }//GEN-LAST:event_jButton_DNActionPerformed

    private void jPasswordFieldRound_MatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldRound_MatKhauActionPerformed
        dangNhap();
    }//GEN-LAST:event_jPasswordFieldRound_MatKhauActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DangNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DangNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DangNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DangNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DangNhap().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_DN;
    private javax.swing.JButton jButton_QMK;
    private javax.swing.JCheckBox jCheckBox_NhoTK;
    private javax.swing.JLabel jLabel_DN;
    private javax.swing.JLabel jLabel_HinhDN;
    private javax.swing.JLabel jLabel_MK;
    private javax.swing.JLabel jLabel_TenDN;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel_KhungDN;
    private jtextfieldround.JPasswordFieldRound jPasswordFieldRound_MatKhau;
    private jtextfieldround.JTextFieldRound jTextFieldRound_TenDangNhap;
    // End of variables declaration//GEN-END:variables
}
