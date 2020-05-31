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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class DanhSachLoaiJFrame extends javax.swing.JFrame {

    DefaultTableModel tbn;
    private String username;
    Connection ketNoi = KetNoi.layKetNoi();

    /**
     * Creates new form DanhSachLoaiJFrame
     */
    public DanhSachLoaiJFrame(String user) {
        initComponents();
        loadData();
        this.username = user;
    }

    private void loadData() {
        try {
            String sql = "Select * from LOAIGD";
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            //storeHouseList.clear();
            Object[] obj = new Object[]{"STT", "Mã Loại", "Tên Loại"};
            tbn = new DefaultTableModel(obj, 0);
            jTable_DSLoai.setModel(tbn);
            int c = 0;
            try {
                while (rs.next()) {
                    if (!rs.getString("MALOAI").equals("KHAC")) {
                        c++;
                        Object[] item = new Object[7];
                        item[0] = c;
                        item[1] = rs.getString("MALOAI");
                        item[2] = rs.getString("LOAI");
                        tbn.addRow(item);
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }

            // Ham tra gia tri tu bang len textField
            jTable_DSLoai.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (jTable_DSLoai.getSelectedRow() >= 0) {
                        textField_MaLoai.setText(jTable_DSLoai.getValueAt(jTable_DSLoai.getSelectedRow(), 1) + "");
                        textField_TenLoai.setText(jTable_DSLoai.getValueAt(jTable_DSLoai.getSelectedRow(), 2) + "");
                        textField_MaLoai.setEnabled(false);
                    }
                }
            });
            // ket thuc ham tra gia tri tu bang len textField 
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private int checkMaLoai(String maLoai) {
        String sql = "select GIAYDEP.MALOAI from GIAYDEP where GIAYDEP.MALOAI = '" + maLoai + "'";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return 1;
            }
        } catch (Exception e) {
            e.toString();
        }
        return 0;
    }

    private int checkMaLoaiThem(String maLoai) {
        String sql = "select MALOAI from LOAIGD where LOAIGD.MALOAI = '" + maLoai + "'";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return 1;
            }

        } catch (Exception e) {
            e.toString();
        }
        return 0;
    }

    private void ThemLoai() {
        String sqlGiayDep = "insert into LOAIGD values(?,?)";
        try {
            PreparedStatement ps1 = ketNoi.prepareStatement(sqlGiayDep);
            ps1.setString(1, textField_MaLoai.getText());
            ps1.setString(2, textField_TenLoai.getText());
            ps1.executeUpdate();
        } catch (Exception e) {
            e.toString();
        }
    }

    private void SuaLoai(String maLoai, String tenLoai) {
        String sql = "update LOAIGD set LOAI = N'" + tenLoai + "'"
                + "where MALOAI = '" + maLoai + "'";
        try {
            PreparedStatement ps1 = ketNoi.prepareStatement(sql);
            ps1.executeUpdate();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private void suaLoaiTrongGiayDep(String maLoai) {
        String sql = "update GIAYDEP set MALOAI=? where MALOAI=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, "KHAC");
            ps.setString(2, maLoai);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DanhSachLoaiJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void XoaLoaiChuaTonTaiMaSanPham(String maLoai) {
        String sql = "delete LOAIGD where MALOAI = N'" + maLoai + "'";
        try {
            PreparedStatement ps1 = ketNoi.prepareStatement(sql);
            ps1.executeUpdate();
        } catch (Exception e) {
            System.err.println(e.toString());
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

        jScrollPane13 = new javax.swing.JScrollPane();
        jTable_DSLoai = new javax.swing.JTable();
        textField_MaLoai = new javax.swing.JTextField();
        textField_TenLoai = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton_them = new javax.swing.JButton();
        jButton_sua = new javax.swing.JButton();
        jButton_xoa = new javax.swing.JButton();
        jButton_lamlai = new javax.swing.JButton();

        jTable_DSLoai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã loại", "Tên loại"
            }
        ));
        jScrollPane13.setViewportView(jTable_DSLoai);

        textField_MaLoai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textField_MaLoaiKeyReleased(evt);
            }
        });

        jLabel1.setText("Mã loại:");

        jLabel2.setText("Tên loại");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Danh sách loại sản phẩm:");

        jButton_them.setText("Thêm");
        jButton_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_themActionPerformed(evt);
            }
        });

        jButton_sua.setText("Sửa");
        jButton_sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_suaActionPerformed(evt);
            }
        });

        jButton_xoa.setText("Xóa");
        jButton_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_xoaActionPerformed(evt);
            }
        });

        jButton_lamlai.setText("Làm lại");
        jButton_lamlai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_lamlaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(textField_MaLoai, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(textField_TenLoai)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton_them, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(19, 19, 19)
                                        .addComponent(jButton_sua, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton_xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(jButton_lamlai, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(textField_MaLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textField_TenLoai, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_them)
                    .addComponent(jButton_sua)
                    .addComponent(jButton_xoa)
                    .addComponent(jButton_lamlai))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textField_MaLoaiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textField_MaLoaiKeyReleased
        // TODO add your handling code here:
        String t = textField_MaLoai.getText();
        if (t.contains(" ") == true) {
            JOptionPane.showMessageDialog(this, "Mã loại không thể chứa khoảng trắng! Vui lòng nhập lại");
            textField_MaLoai.setText("");
        }
    }//GEN-LAST:event_textField_MaLoaiKeyReleased

    private void jButton_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_themActionPerformed
        // TODO add your handling code here:
        String maLoai = textField_MaLoai.getText();
        String tenLoai = textField_TenLoai.getText();
        String regexTen = "^[a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳýỵỷỹ\\s]+$";
        if (maLoai.contains(" ") == true) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm không được chứa khoảng trắng");
        } else if (maLoai.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã loại");
        } else if (tenLoai.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên loại");
        } else if (tenLoai.matches(regexTen) == false) {
            JOptionPane.showMessageDialog(this, "Tên loại không chứa ký tự số!");
        } else {
            if (checkMaLoaiThem(maLoai) == 1) {
                JOptionPane.showMessageDialog(this, "Mã loại " + maLoai + " đã tồn tại!");
            } else {
                ThemLoai();
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                loadData();
                textField_MaLoai.setText("");
                textField_TenLoai.setText("");
            }
        }
    }//GEN-LAST:event_jButton_themActionPerformed

    private void jButton_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_suaActionPerformed
        // TODO add your handling code here:
        String maLoai = textField_MaLoai.getText();
        String tenLoai = textField_TenLoai.getText();
        String regexTen = "^[a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳýỵỷỹ\\s]+$";
        try {
            if (maLoai.contains(" ") == true) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm không được chứa khoảng trắng");
            } else if (maLoai.equals("")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã loại");
            } else if (tenLoai.equals("")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên loại");
            } else if (tenLoai.matches(regexTen) == false) {
                JOptionPane.showMessageDialog(this, "Tên loại không chứa ký tự số!");
            } else {
                if (textField_MaLoai.getText().equals(jTable_DSLoai.getValueAt(jTable_DSLoai.getSelectedRow(), 1).toString())
                        && textField_TenLoai.getText().equals(jTable_DSLoai.getValueAt(jTable_DSLoai.getSelectedRow(), 2).toString())) {
                    JOptionPane.showMessageDialog(this, "Bạn chưa sửa gì cả!!");
                } else if (!textField_MaLoai.getText().equals(jTable_DSLoai.getValueAt(jTable_DSLoai.getSelectedRow(), 1).toString())) {
                    JOptionPane.showMessageDialog(this, "Mã sản phẩm không thể sửa");
                } else {
                    SuaLoai(maLoai, tenLoai);
                    JOptionPane.showMessageDialog(this, "Sửa thành công");
                    loadData();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại cần sửa từ danh sách");
        }
    }//GEN-LAST:event_jButton_suaActionPerformed

    private void jButton_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_xoaActionPerformed
        // TODO add your handling code here:
        String maLoai = textField_MaLoai.getText();
        String tenLoai = textField_TenLoai.getText();

        if (maLoai.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã loại");
        } else if (tenLoai.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên loại");
        } else if (checkMaLoai(maLoai) == 1) {
            Object[] options = {"Đồng ý", "Hủy"};
            int chon = JOptionPane.showOptionDialog(this, "Mã loại bạn đang chọn hiện đang chứa sản phẩm. Sản phẩm sẽ sửa thành loại 'Khác'. Bạn đồng ý chứ?",
                    "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (chon == JOptionPane.YES_OPTION) {
                suaLoaiTrongGiayDep(maLoai);
                JOptionPane.showMessageDialog(this, "Xóa thành công loại sản phẩm!");
                XoaLoaiChuaTonTaiMaSanPham(maLoai);
                loadData();
            }
        } else {
            Object[] options = {"Đồng ý", "Hủy"};
            int chon = JOptionPane.showOptionDialog(this, "Bạn có chắc muốn xóa loại sản phẩm này không?",
                    "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (chon == JOptionPane.YES_OPTION) {
                XoaLoaiChuaTonTaiMaSanPham(maLoai);
                JOptionPane.showMessageDialog(this, "Xóa thành công loại sản phẩm!");
                loadData();
            }
        }
    }//GEN-LAST:event_jButton_xoaActionPerformed

    private void jButton_lamlaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_lamlaiActionPerformed
        // TODO add your handling code here:
        textField_MaLoai.setText("");
        textField_TenLoai.setText("");
        textField_MaLoai.setEnabled(true);
    }//GEN-LAST:event_jButton_lamlaiActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_lamlai;
    private javax.swing.JButton jButton_sua;
    private javax.swing.JButton jButton_them;
    private javax.swing.JButton jButton_xoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JTable jTable_DSLoai;
    private javax.swing.JTextField textField_MaLoai;
    private javax.swing.JTextField textField_TenLoai;
    // End of variables declaration//GEN-END:variables
}
