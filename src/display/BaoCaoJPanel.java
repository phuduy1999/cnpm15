/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import Conection.KetNoi;
import Main.Main;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.JMarsDarkTheme;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Admin
 */
public class BaoCaoJPanel extends javax.swing.JPanel {

    String username;
    /**
     * Creates new form BaoCaoJPanel
     */
    private JPanel childrenJPanel;
    private JPanel nhanVien;
    private JPanel thongKe;

    public BaoCaoJPanel(String user) {
        initComponents();
        username = user;
        nhanVien = new NhanVienJPanel(username);
        thongKe = new ThongKeJPanel(username);
        this.HienJPanel(nhanVien);
    }

    private void HienJPanel(JPanel panel) {
        childrenJPanel = panel;
        jPanel_DoiKhung.removeAll();
        jPanel_DoiKhung.add(childrenJPanel);
        jPanel_DoiKhung.validate();
        jPanel_DoiKhung.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton_XemNhanVien = new javax.swing.JButton();
        jButton_XemThongKe = new javax.swing.JButton();
        jPanel_DoiKhung = new javax.swing.JPanel();

        setBackground(new java.awt.Color(0, 0, 0));

        jButton_XemNhanVien.setBackground(new java.awt.Color(0, 0, 0));
        jButton_XemNhanVien.setForeground(new java.awt.Color(255, 153, 153));
        jButton_XemNhanVien.setText("Xem Nhân viên");
        jButton_XemNhanVien.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_XemNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_XemNhanVienActionPerformed(evt);
            }
        });

        jButton_XemThongKe.setBackground(new java.awt.Color(0, 0, 0));
        jButton_XemThongKe.setForeground(new java.awt.Color(255, 153, 153));
        jButton_XemThongKe.setText("Xem Thống Kê Theo Ngày");
        jButton_XemThongKe.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_XemThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_XemThongKeActionPerformed(evt);
            }
        });

        jPanel_DoiKhung.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton_XemNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton_XemThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
            .addComponent(jPanel_DoiKhung, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_XemNhanVien)
                    .addComponent(jButton_XemThongKe))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel_DoiKhung, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_XemNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_XemNhanVienActionPerformed
        this.HienJPanel(nhanVien);
    }//GEN-LAST:event_jButton_XemNhanVienActionPerformed

    private void jButton_XemThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_XemThongKeActionPerformed
        this.HienJPanel(thongKe);
    }//GEN-LAST:event_jButton_XemThongKeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_XemNhanVien;
    private javax.swing.JButton jButton_XemThongKe;
    private javax.swing.JPanel jPanel_DoiKhung;
    // End of variables declaration//GEN-END:variables
}
