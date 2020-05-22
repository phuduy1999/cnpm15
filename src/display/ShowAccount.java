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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class ShowAccount extends javax.swing.JPanel {
    String username;
    /**
     * Creates new form ShowAccount
     */
    public ShowAccount() {
        initComponents();
        this.layData();
    }
    
    public ShowAccount(String user) {
        initComponents();
        username=user;
        this.layData();
    }
    
    private void layData(){
        DefaultTableModel dtm = (DefaultTableModel) jTable_DSNhanVien.getModel();
        dtm.setNumRows(0);
        Connection ketNoi =KetNoi.layKetNoi();
        String sql = "select * from NHANVIEN,ACCOUNT where NHANVIEN.USERNAME=ACCOUNT.USERNAME";
        Vector vt;
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                vt = new Vector();
                vt.add(rs.getString("MANV"));
                vt.add(rs.getString("HOTEN"));
                vt.add(rs.getString("PHONE"));
                vt.add(rs.getString("EMAIL"));
                vt.add(rs.getString("USERNAME"));
                vt.add(rs.getString("AUTHORIZE"));
                dtm.addRow(vt);
            }
            jTable_DSNhanVien.setModel(dtm);
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(ShowAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void deleteUser(String username) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql1 = "delete NHANVIEN where USERNAME='" + username + "'";
        String sql2 = "delete ACCOUNT where USERNAME='" + username + "'";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql2);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ReportJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql1);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ReportJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int checkForm(String username, String password, String contact, String address, String fullName) {
        int check = 0;
        if (username.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Tên tài khoản không được bỏ trống");
        } else if (password.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Mật khẩu không được bỏ trống");
        } else if (contact.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Số điện thoại không được bỏ trống");
        } else if (address.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Địa chỉ không được bỏ trống");
        } else if (fullName.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Họ  tên không được bỏ trống");
        }
        return check;
    }

    private int checkUser(String username, String contact, String email) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select * from NHANVIEN where USERNAME ='" + username + "' OR PHONE= '" + contact + "' OR EMAIL='" + email + "'";
        int result = 0;
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("USERNAME").equals(username)) {
                    JOptionPane.showMessageDialog(this, "Tên tài khoản đã được sủ dụng!");
                } else if (rs.getString("PHONE").equals(contact)) {
                    JOptionPane.showMessageDialog(this, "Số điện thoại đã được sủ dụng!");
                }
                result = 1;
            }
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReportJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private void addUser(String username, String password, String power, String contact, String address, String fullName) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql1 = "insert into NHANVIEN(HOTEN,PHONE,EMAIL,USERNAME) values (?,?,?,?)";
        String sql2 = "insert into ACCOUNT values (?,?,?)";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql2);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, power);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ReportJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql1);
            ps.setString(1, fullName);
            ps.setString(2, contact);
            ps.setString(3, address);
            ps.setString(4, username);
            ps.executeUpdate();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReportJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int checkUsername(String username) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select * from ACCOUNT where USERNAME ='" + username + "'";
        int result = 0;
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Tên tài khoản không tồn tại!");
                result = 1;
            }
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReportJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private void editUser(String username, String password, String power, String contact, String address, String fullName) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql1 = "update NHANVIEN set HOTEN=?,PHONE=?,EMAIL=? where USERNAME='" + username + "'";
        String sql2 = "update ACCOUNT set PASSWORD=?,AUTHORIZE=? where USERNAME='" + username + "'";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql2);
            ps.setString(1, password);
            ps.setString(2, power);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ReportJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql1);
            ps.setString(1, fullName);
            ps.setString(2, contact);
            ps.setString(3, address);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ReportJPanel.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField_TenDN = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2_MK = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField_TenNV = new javax.swing.JTextField();
        jTextField_SDT = new javax.swing.JTextField();
        jTextField_Email = new javax.swing.JTextField();
        jButton_Them = new javax.swing.JButton();
        jButton_Sua = new javax.swing.JButton();
        jButton_Xoa = new javax.swing.JButton();
        jButton_LamLai = new javax.swing.JButton();
        jComboBox_ChucVu = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_DSNhanVien = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 153, 153));
        jLabel1.setText("Thêm Nhân Viên Mới");

        jLabel2.setForeground(new java.awt.Color(255, 153, 153));
        jLabel2.setText("Tên Đăng Nhập");

        jLabel3.setForeground(new java.awt.Color(255, 153, 153));
        jLabel3.setText("Mật Khẩu");

        jLabel4.setForeground(new java.awt.Color(255, 153, 153));
        jLabel4.setText("Chức Vụ");

        jLabel5.setForeground(new java.awt.Color(255, 153, 153));
        jLabel5.setText("Họ Tên");

        jLabel6.setForeground(new java.awt.Color(255, 153, 153));
        jLabel6.setText("Số Điện Thoại");

        jLabel7.setForeground(new java.awt.Color(255, 153, 153));
        jLabel7.setText("Email");

        jButton_Them.setBackground(new java.awt.Color(0, 0, 0));
        jButton_Them.setForeground(new java.awt.Color(255, 153, 153));
        jButton_Them.setText("Thêm");
        jButton_Them.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_Them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ThemActionPerformed(evt);
            }
        });

        jButton_Sua.setBackground(new java.awt.Color(0, 0, 0));
        jButton_Sua.setForeground(new java.awt.Color(255, 153, 153));
        jButton_Sua.setText("Sửa");
        jButton_Sua.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_Sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SuaActionPerformed(evt);
            }
        });

        jButton_Xoa.setBackground(new java.awt.Color(0, 0, 0));
        jButton_Xoa.setForeground(new java.awt.Color(255, 153, 153));
        jButton_Xoa.setText("Xóa");
        jButton_Xoa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_Xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_XoaActionPerformed(evt);
            }
        });

        jButton_LamLai.setBackground(new java.awt.Color(0, 0, 0));
        jButton_LamLai.setForeground(new java.awt.Color(255, 153, 153));
        jButton_LamLai.setText("Làm Lại");
        jButton_LamLai.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_LamLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_LamLaiActionPerformed(evt);
            }
        });

        jComboBox_ChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nhân Viên", "boss" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField_TenDN)
                                    .addComponent(jTextField2_MK)
                                    .addComponent(jComboBox_ChucVu, 0, 216, Short.MAX_VALUE))
                                .addGap(88, 88, 88))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addComponent(jButton_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                                .addComponent(jButton_Sua, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(80, 80, 80)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton_Xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField_TenNV)
                                .addComponent(jTextField_SDT)
                                .addComponent(jTextField_Email, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_LamLai, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(84, 84, 84)))))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_TenDN, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_TenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2_MK, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_SDT, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField_Email, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jComboBox_ChucVu))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Them)
                    .addComponent(jButton_Sua)
                    .addComponent(jButton_LamLai)
                    .addComponent(jButton_Xoa))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        jTable_DSNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã NV", "Họ Tên", "SDT", "Email", "Tên Đăng Nhập", "Chức vụ"
            }
        ));
        jTable_DSNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_DSNhanVienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable_DSNhanVien);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 153, 153));
        jLabel8.setText("Thông Tin Nhân Viên");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 833, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void jButton_XoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_XoaActionPerformed
        // TODO add your handling code here:
         String username = jTextField_TenDN.getText();
        int result = checkUsername(username);
        if (result == 0) {
            this.deleteUser(username);
            JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công !!");
            this.layData();
        }
    }//GEN-LAST:event_jButton_XoaActionPerformed
    
    private void jButton_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ThemActionPerformed
        String username = jTextField_TenDN.getText();
        String password = jTextField2_MK.getText();
        String contact = jTextField_SDT.getText();
        String address = jTextField_Email.getText();
        String fullName = jTextField_TenNV.getText();
        String power="";
        power = (String) jComboBox_ChucVu.getSelectedItem();
        int checkForm = this.checkForm(username, password, contact, address, fullName);
        if (checkForm != 1) {
            int result = checkUser(username, contact, address);
            if (result == 0) {
                this.addUser(username, password, power, contact, address, fullName);
                JOptionPane.showMessageDialog(this, "Đăng ký tài khoản thành công !!");
                this.layData();
            }
        }
    }//GEN-LAST:event_jButton_ThemActionPerformed
    
    private void jButton_SuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SuaActionPerformed
        String username = jTextField_TenDN.getText();
        String password = jTextField2_MK.getText();
        String contact = jTextField_SDT.getText();
        String address = jTextField_Email.getText();
        String fullName = jTextField_TenNV.getText();
        String power = (String) jComboBox_ChucVu.getSelectedItem();
        int checkForm = this.checkForm(username, password, contact, address, fullName);
        if (checkForm != 1) {
            int result = checkUsername(username);
            if (result == 0) {
                this.editUser(username, password, power, contact, address, fullName);
                JOptionPane.showMessageDialog(this, "Chỉnh sửa tài khoản thành công !!");
                this.layData();
            }
        }
    }//GEN-LAST:event_jButton_SuaActionPerformed

    private void jButton_LamLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_LamLaiActionPerformed
        jTextField_TenDN.setText("");
        jTextField2_MK.setText("");
        jTextField_TenNV.setText("");
        jTextField_SDT.setText("");
        jTextField_Email.setText("");
    }//GEN-LAST:event_jButton_LamLaiActionPerformed

    private void jTable_DSNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_DSNhanVienMouseClicked
        DefaultTableModel dtm=(DefaultTableModel) jTable_DSNhanVien.getModel();
        int i = jTable_DSNhanVien.getSelectedRow();
        
        jTextField_TenNV.setText(dtm.getValueAt(i, 1).toString());
        jTextField_SDT.setText(dtm.getValueAt(i, 2).toString());
        jTextField_Email.setText(dtm.getValueAt(i, 3).toString());
        jTextField_TenDN.setText(dtm.getValueAt(i, 4).toString());
        jComboBox_ChucVu.setSelectedItem(dtm.getValueAt(i, 5));
    }//GEN-LAST:event_jTable_DSNhanVienMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_LamLai;
    private javax.swing.JButton jButton_Sua;
    private javax.swing.JButton jButton_Them;
    private javax.swing.JButton jButton_Xoa;
    private javax.swing.JComboBox<String> jComboBox_ChucVu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_DSNhanVien;
    private javax.swing.JTextField jTextField2_MK;
    private javax.swing.JTextField jTextField_Email;
    private javax.swing.JTextField jTextField_SDT;
    private javax.swing.JTextField jTextField_TenDN;
    private javax.swing.JTextField jTextField_TenNV;
    // End of variables declaration//GEN-END:variables
}
