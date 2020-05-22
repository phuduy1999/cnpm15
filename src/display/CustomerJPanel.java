/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import Conection.KetNoi;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import model.Customer;

/**
 *
 * @author Admin
 */
public class CustomerJPanel extends javax.swing.JPanel {

    String username;

    List<Customer> customerList = new ArrayList<Customer>();

    public CustomerJPanel(String user) {
        initComponents();
        username = user;
        jLabel_LOISDT.setVisible(false);
        layKhachHang();
    }

    private void layKhachHang() {
        Connection ketNoi = KetNoi.layKetNoi();
        DefaultTableModel dtm = (DefaultTableModel) jtbKhachHang.getModel();
        dtm.setNumRows(0);
        String sql = "select * from KHACHHANG";
        Vector vt;
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            customerList.clear(); //moi lan lay lai kh phai reset lai list moi (loi bi trung du lieu)
            while (rs.next()) {
                Customer cs = new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
                customerList.add(cs);
                if (rs.getString(1).equals("KVL")) {
                    continue; //khach vang lai ko can xuat ra table
                }
                vt = new Vector();
                vt.add(rs.getString(1));
                vt.add(rs.getString(2));
                vt.add(rs.getString(3));
                vt.add(rs.getString(4));
                dtm.addRow(vt);
            }
            jtbKhachHang.setModel(dtm);
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void themKhachHang(Customer cs) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "insert into KHACHHANG values (?,?,?,?)";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, cs.getMaKH());
            ps.setString(2, cs.getHoTen());
            if (cs.getNgaySinh().equals("")) {
                ps.setString(3, null);
            } else {
                ps.setString(3, cs.getNgaySinh());
            }
            ps.setString(4, cs.getDiaChi());
            ps.executeUpdate();
            ps.close();
            ketNoi.close();
            customerList.add(cs);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void xoaKhachHang(String maKH) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "delete from KHACHHANG where MAKH=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maKH);
            ps.executeUpdate();
            ps.close();
            ketNoi.close();
            for (Customer cs : customerList) {
                if (cs.getMaKH().equals(maKH)) {
                    customerList.remove(cs);
                    break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void suaKhachHang(Customer cs) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "update KHACHHANG set HOTEN=?,NGAYSINH=?,DIACHI=? where MAKH=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, cs.getHoTen());
            if (cs.getNgaySinh().equals("")) {
                ps.setString(2, null);
            } else {
                ps.setString(2, cs.getNgaySinh());
            }
            ps.setString(3, cs.getDiaChi());
            ps.setString(4, cs.getMaKH());
            ps.executeUpdate();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void suaHoaDon(String maKH) { //khi xoa 1 khach hang da tung mua hang du lieu se doi lai la KVL
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "update HOADON set MAKH=? where MAKH=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, "KVL");
            ps.setString(2, maKH);
            ps.executeUpdate();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String chuanHoaChuoiHoTen(String hoTen) {
        String ten = hoTen.trim().replaceAll("\\s+", " ");
        if (ten.equals("")) {
            return ten;
        }
        String[] arr = ten.split(" ");
        String tmp = "";
        for (String t : arr) {
            t = t.substring(0, 1).toUpperCase() + t.substring(1, t.length());
            tmp = tmp + t + " ";
        }
        ten = tmp.trim();
        return ten;
    }

    private int checkMaKH_SDT(String maKH) {
        for (Customer cs : customerList) {
            if (cs.getMaKH().equals(maKH)) {
                return 1;//trung sdt
            }
        }
        return 0;
    }

    private int checkMaKH_MuaHang(String maKH) {
        Connection ketNoi = KetNoi.layKetNoi();
        int c = 0;
        String sql = "select MAKH from HOADON where MAKH=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maKH);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c = 1;
            }
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    private void lamMoi() {
        txtTenKhachHang.setText("");
        txtDiaChiKhachHang.setText("");
        txtSDTKhachHang.setText("");
        jDateKhachHang.setCalendar(null);
        jLabel_LOISDT.setVisible(false);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtSDTKhachHang = new javax.swing.JTextField();
        txtTenKhachHang = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDiaChiKhachHang = new javax.swing.JTextArea();
        jDateKhachHang = new com.toedter.calendar.JDateChooser();
        jLabel_LOISDT = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbKhachHang = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jBtnTimKiemKhachHang = new javax.swing.JButton();
        txtTimKiemKhachHang = new jtextfieldround.JTextFieldRound();
        jPanel4 = new javax.swing.JPanel();
        jBtnThemKhachHang = new javax.swing.JButton();
        jBtnSuaKhachHang = new javax.swing.JButton();
        jBtnXoaKhachHang = new javax.swing.JButton();
        jBtnLamMoiKhachHang = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 153, 153));
        jLabel1.setText("DANH MỤC: KHÁCH HÀNG");

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        jLabel3.setForeground(new java.awt.Color(255, 153, 153));
        jLabel3.setText("Mã khách hàng/SDT");

        jLabel4.setForeground(new java.awt.Color(255, 153, 153));
        jLabel4.setText("Tên khách hàng");

        jLabel5.setForeground(new java.awt.Color(255, 153, 153));
        jLabel5.setText("Ngày sinh:");

        jLabel6.setForeground(new java.awt.Color(255, 153, 153));
        jLabel6.setText("Địa chỉ:");

        txtSDTKhachHang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSDTKhachHangKeyReleased(evt);
            }
        });

        txtDiaChiKhachHang.setColumns(20);
        txtDiaChiKhachHang.setRows(5);
        jScrollPane2.setViewportView(txtDiaChiKhachHang);

        jLabel_LOISDT.setForeground(new java.awt.Color(255, 0, 0));
        jLabel_LOISDT.setText("Sai định dạng sđt. Hãy thử lại");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSDTKhachHang)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                    .addComponent(txtTenKhachHang, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_LOISDT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSDTKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_LOISDT, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(jDateKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                .addGap(46, 46, 46))
        );

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));

        jtbKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SĐT", "Họ và tên", "Ngày Sinh", "Địa chỉ"
            }
        ));
        jtbKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbKhachHangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtbKhachHang);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 153, 153));
        jLabel2.setText("Thông tin khách hàng:");

        jBtnTimKiemKhachHang.setBackground(new java.awt.Color(0, 0, 0));
        jBtnTimKiemKhachHang.setForeground(new java.awt.Color(255, 153, 153));
        jBtnTimKiemKhachHang.setText("Tìm Kiếm");
        jBtnTimKiemKhachHang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153), 2));
        jBtnTimKiemKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnTimKiemKhachHangActionPerformed(evt);
            }
        });

        txtTimKiemKhachHang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKhachHangKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtTimKiemKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jBtnTimKiemKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnTimKiemKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiemKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));

        jBtnThemKhachHang.setBackground(new java.awt.Color(0, 0, 0));
        jBtnThemKhachHang.setForeground(new java.awt.Color(255, 153, 153));
        jBtnThemKhachHang.setText("Thêm khách hàng");
        jBtnThemKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnThemKhachHangActionPerformed(evt);
            }
        });

        jBtnSuaKhachHang.setBackground(new java.awt.Color(0, 0, 0));
        jBtnSuaKhachHang.setForeground(new java.awt.Color(255, 153, 153));
        jBtnSuaKhachHang.setText("Sửa thông tin");
        jBtnSuaKhachHang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jBtnSuaKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnSuaKhachHangActionPerformed(evt);
            }
        });

        jBtnXoaKhachHang.setBackground(new java.awt.Color(0, 0, 0));
        jBtnXoaKhachHang.setForeground(new java.awt.Color(255, 153, 153));
        jBtnXoaKhachHang.setText("Xóa thông tin");
        jBtnXoaKhachHang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jBtnXoaKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnXoaKhachHangActionPerformed(evt);
            }
        });

        jBtnLamMoiKhachHang.setBackground(new java.awt.Color(0, 0, 0));
        jBtnLamMoiKhachHang.setForeground(new java.awt.Color(255, 153, 153));
        jBtnLamMoiKhachHang.setText("Làm mới");
        jBtnLamMoiKhachHang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jBtnLamMoiKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnLamMoiKhachHangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jBtnXoaKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtnThemKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBtnSuaKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnLamMoiKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnThemKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnSuaKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnXoaKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnLamMoiKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                        .addGap(596, 596, 596))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnThemKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnThemKhachHangActionPerformed
        String maKH = txtSDTKhachHang.getText();
        String tenKH = chuanHoaChuoiHoTen(txtTenKhachHang.getText());
        String diaChi = txtDiaChiKhachHang.getText();

        String date = "";
        if (jDateKhachHang.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format(jDateKhachHang.getDate()); // Date->String
        }

        boolean cohieu = true;
        if (maKH.equals("")) {
            JOptionPane.showMessageDialog(this, "Bạn cần nhập mã khách hàng là sđt của khách!");
            return;
        }
        if (maKH.matches("^(\\+84|0)\\d{9,10}$") == false) {
            jLabel_LOISDT.setVisible(true);
            txtSDTKhachHang.requestFocus();
            cohieu = false;
        }
        if (tenKH.equals("")) {
            JOptionPane.showMessageDialog(this, "Bạn cần nhập tên khách hàng!");
            return;
        }
        if (cohieu == true) {
            int c = checkMaKH_SDT(maKH);
            if (c == 1) {
                JOptionPane.showMessageDialog(this, "Mã khách hàng với sđt trên đã bị trùng. Vui lòng kiểm tra lại!");
            } else {
                Customer cs = new Customer(maKH, tenKH, date, diaChi);
                themKhachHang(cs);
                JOptionPane.showMessageDialog(this, "Đã thêm thành công khách hàng mới!");
            }
        }
        this.layKhachHang();
    }//GEN-LAST:event_jBtnThemKhachHangActionPerformed

    private void jBtnSuaKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSuaKhachHangActionPerformed
        String maKH = txtSDTKhachHang.getText();
        int c = checkMaKH_SDT(maKH);
        if (c == 0) {
            JOptionPane.showMessageDialog(this, "Mã khách hàng hiện chưa có. Vui lòng thêm mới!");
        } else {
            String tenKH = txtTenKhachHang.getText();
            String diaChi = txtDiaChiKhachHang.getText();
            String date = "";
            if (jDateKhachHang.getDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.format(jDateKhachHang.getDate());
            }
            for (Customer cs : customerList) {
                if (cs.getMaKH().equals(maKH)) {
                    String tmp;
                    if (cs.getNgaySinh() == null) {
                        tmp = "";
                    } else {
                        tmp = cs.getNgaySinh();
                    }
                    if (cs.getHoTen().equalsIgnoreCase(tenKH) && tmp.equals(date) && cs.getDiaChi().equals(diaChi)) {
                        JOptionPane.showMessageDialog(this, "Không có sự thay đổi thông tin cần sửa!");
                        return;
                    }
                    cs.setHoTen(tenKH);
                    cs.setNgaySinh(date);
                    cs.setDiaChi(diaChi);
                    suaKhachHang(cs);
                    JOptionPane.showMessageDialog(this, "Sửa thông tin khách hàng thành công!");
                    this.layKhachHang();
                    break;
                }
            }
        }
    }//GEN-LAST:event_jBtnSuaKhachHangActionPerformed

    private void jBtnXoaKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnXoaKhachHangActionPerformed
        String maKH = txtSDTKhachHang.getText();
        if (maKH.equals("")) {
            JOptionPane.showMessageDialog(this, "Mã khách hàng cần xóa không được dể trống!");
        } else {
            int c = checkMaKH_SDT(maKH);
            if (c == 0) {
                JOptionPane.showMessageDialog(this, "Mã khách hàng này không tồn tại!");
            } else {
                Object[] options = {"Đồng ý", "Hủy"};
                int chon = JOptionPane.showOptionDialog(this, "Bạn có chắc muốn xóa khách hàng này không?",
                        "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (chon == JOptionPane.YES_OPTION) {
                    if (checkMaKH_MuaHang(maKH) == 1) {
                        int chon1 = JOptionPane.showOptionDialog(this, "Khách hàng đã từng mua hàng, sau khi xóa sẽ "
                                + "cập nhật đơn hàng thành khách vãng lai?",
                                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        if (chon1 == JOptionPane.YES_OPTION) {
                            suaHoaDon(maKH);
                        } else {
                            return;
                        }
                    }
                    xoaKhachHang(maKH);
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
                    this.layKhachHang();
                } else {
                    lamMoi();
                }
            }
        }
    }//GEN-LAST:event_jBtnXoaKhachHangActionPerformed

    private void jBtnLamMoiKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnLamMoiKhachHangActionPerformed
        lamMoi();
    }//GEN-LAST:event_jBtnLamMoiKhachHangActionPerformed

    private void jtbKhachHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbKhachHangMouseClicked
        int i = jtbKhachHang.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) jtbKhachHang.getModel();
        txtSDTKhachHang.setText(dtm.getValueAt(i, 0).toString());
        txtTenKhachHang.setText(dtm.getValueAt(i, 1).toString());
        if (dtm.getValueAt(i, 2) != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse((String) dtm.getValueAt(i, 2)); //String->Date
                jDateKhachHang.setDate(date);
            } catch (ParseException ex) {
                Logger.getLogger(CustomerJPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            jDateKhachHang.setCalendar(null);
        }
        if (dtm.getValueAt(i, 3) != null) {
            txtDiaChiKhachHang.setText(dtm.getValueAt(i, 3).toString());
        } else {
            txtDiaChiKhachHang.setText("");
        }
    }//GEN-LAST:event_jtbKhachHangMouseClicked

    private void txtSDTKhachHangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSDTKhachHangKeyReleased
        String t = txtSDTKhachHang.getText();
        if (t.matches("^(\\+84|0)\\d{9,10}$") == false) {
            jLabel_LOISDT.setVisible(true);
        } else {
            jLabel_LOISDT.setVisible(false);
        }
    }//GEN-LAST:event_txtSDTKhachHangKeyReleased

    private void jBtnTimKiemKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnTimKiemKhachHangActionPerformed
        String f = txtTimKiemKhachHang.getText();
        boolean c = false;
        for (Customer cs : customerList) {
            String ten = cs.getHoTen().toLowerCase();
            if (cs.getMaKH().contains(f) || ten.contains(f.toLowerCase())) {
                c = true;
            }
        }
        if (c == false) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng này!");
        }
    }//GEN-LAST:event_jBtnTimKiemKhachHangActionPerformed

    private void txtTimKiemKhachHangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKhachHangKeyReleased
        String f = txtTimKiemKhachHang.getText();

        DefaultTableModel dtm = (DefaultTableModel) jtbKhachHang.getModel();
        dtm.setNumRows(0);
        for (Customer x : customerList) {
            if (x.getMaKH().equals("KVL")) {
                continue;
            }
            String ten = x.getHoTen().toLowerCase();
            if (x.getMaKH().contains(f) || ten.contains(f.toLowerCase())) {
                Vector vt = new Vector();
                vt.add(x.getMaKH());
                vt.add(x.getHoTen());
                vt.add(x.getNgaySinh());
                vt.add(x.getDiaChi());
                dtm.addRow(vt);
            }
        }
        jtbKhachHang.setModel(dtm);
    }//GEN-LAST:event_txtTimKiemKhachHangKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnLamMoiKhachHang;
    private javax.swing.JButton jBtnSuaKhachHang;
    private javax.swing.JButton jBtnThemKhachHang;
    private javax.swing.JButton jBtnTimKiemKhachHang;
    private javax.swing.JButton jBtnXoaKhachHang;
    private com.toedter.calendar.JDateChooser jDateKhachHang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel_LOISDT;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jtbKhachHang;
    private javax.swing.JTextArea txtDiaChiKhachHang;
    private javax.swing.JTextField txtSDTKhachHang;
    private javax.swing.JTextField txtTenKhachHang;
    private jtextfieldround.JTextFieldRound txtTimKiemKhachHang;
    // End of variables declaration//GEN-END:variables
}
