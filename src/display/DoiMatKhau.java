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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import Encrypt.MaHoaPassword;
import java.io.UnsupportedEncodingException;
/**
 *
 * @author Admin
 */
public class DoiMatKhau extends javax.swing.JFrame {

    /**
     * Creates new form DoiMatKhau
     */
    private String username;

    public DoiMatKhau(String user) {
        initComponents();
        this.username = user;
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    private int checkPass(String oldPassword) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select * from ACCOUNT where USERNAME ='" + username + "'";
        int result = 0;
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                if(!MaHoaPassword.decodeString(rs.getString(2)).equals(oldPassword)){
                    JOptionPane.showMessageDialog(this, "Mật khẩu cũ không chính xác!");
                    result = 1;
                }
            }
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoJPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DoiMatKhau.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private void doiPass(String newPassword) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "update ACCOUNT set PASSWORD = ? where USERNAME=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            String passEncode=MaHoaPassword.encodeString(newPassword);
            ps.setString(1, passEncode);
            ps.setString(2, username);
            ps.executeUpdate();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoJPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DoiMatKhau.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private int checkForm(String oldPassword, String newPassword, String cfPassword) {
        int check = 0;
        if (oldPassword.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Mật khẩu cũ không được bỏ trống");
        } else if (newPassword.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Mật khẩu mới không được bỏ trống");
        } else if (cfPassword.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu không được bỏ trống");
        }
        return check;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel_DoiMK = new javax.swing.JLabel();
        jLabel_MKcu = new javax.swing.JLabel();
        jPasswordField_MKcu = new javax.swing.JPasswordField();
        jPasswordField_MKmoi = new javax.swing.JPasswordField();
        jLabel_MKmoi = new javax.swing.JLabel();
        jLabel_NhapLaiMK = new javax.swing.JLabel();
        jPasswordField_MKmoiNhapLai = new javax.swing.JPasswordField();
        jButton_XacNhan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 204, 204));

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));

        jLabel_DoiMK.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel_DoiMK.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_DoiMK.setText("Đổi mật khẩu");

        jLabel_MKcu.setText("Mật khẩu cũ");

        jLabel_MKmoi.setText("Mật khẩu mới");

        jLabel_NhapLaiMK.setText("Nhập lại mật khẩu");

        jButton_XacNhan.setBackground(new java.awt.Color(0, 0, 0));
        jButton_XacNhan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton_XacNhan.setForeground(new java.awt.Color(255, 153, 153));
        jButton_XacNhan.setText("Xác nhận");
        jButton_XacNhan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_XacNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_XacNhanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel_DoiMK, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel_MKcu, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPasswordField_MKcu, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel_MKmoi, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPasswordField_MKmoi, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel_NhapLaiMK, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPasswordField_MKmoiNhapLai, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(jButton_XacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel_DoiMK, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_MKcu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordField_MKcu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_MKmoi, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordField_MKmoi, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_NhapLaiMK, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordField_MKmoiNhapLai, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton_XacNhan, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void jButton_XacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_XacNhanActionPerformed
        // TODO add your handling code here: 
        String passwordCu = jPasswordField_MKcu.getText();
        String passwordMoi = jPasswordField_MKmoi.getText();
        String cofirmPassword = jPasswordField_MKmoiNhapLai.getText();
        int checkForm = this.checkForm(passwordCu, passwordMoi, cofirmPassword);
        if (checkForm != 1) {
            if (!passwordMoi.equals(cofirmPassword)) {
                JOptionPane.showMessageDialog(this, "xác nhận mật khẩu không đúng với mật khẩu mới!");
            } else {
                int result = this.checkPass(passwordCu);
                if (result == 0) {
                    this.doiPass(passwordMoi);
                    this.dispose();
                    JOptionPane.showMessageDialog(this, "Đổi mật khẩu tài khoản thành công!");
                }
            }
        }
    }//GEN-LAST:event_jButton_XacNhanActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_XacNhan;
    private javax.swing.JLabel jLabel_DoiMK;
    private javax.swing.JLabel jLabel_MKcu;
    private javax.swing.JLabel jLabel_MKmoi;
    private javax.swing.JLabel jLabel_NhapLaiMK;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField_MKcu;
    private javax.swing.JPasswordField jPasswordField_MKmoi;
    private javax.swing.JPasswordField jPasswordField_MKmoiNhapLai;
    // End of variables declaration//GEN-END:variables
}
