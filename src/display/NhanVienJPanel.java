/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import Conection.KetNoi;
import Controller.ChuyenManHinh;
import java.awt.Window;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import Encrypt.MaHoaPassword;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author User
 */
public class NhanVienJPanel extends javax.swing.JPanel {

    String username;
    String userCanSua;

    /**
     * Creates new form ShowAccount
     */
    public NhanVienJPanel(String user) {
        initComponents();
        username = user;
        this.layDanhSachNV();
    }

    private void layDanhSachNV() {
        DefaultTableModel dtm = (DefaultTableModel) jTable_DSNhanVien.getModel();
        dtm.setNumRows(0);
        Connection ketNoi = KetNoi.layKetNoi();
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
                if (layQuyen(rs.getString("USERNAME")).equals("admin")) {
                    vt.add(rs.getString("PASSWORD")); //ko hien matkhau admin
                } else {
                    vt.add(MaHoaPassword.decodeString(rs.getString("PASSWORD")));
                }
                vt.add(rs.getString("AUTHORIZE"));
                vt.add(rs.getBoolean("TRANGTHAI"));
                dtm.addRow(vt);
            }
            jTable_DSNhanVien.setModel(dtm);
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(NhanVienJPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(NhanVienJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void xoaNhanVien(String user) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql1 = "delete NHANVIEN where USERNAME='" + user + "'";
        String sql2 = "delete ACCOUNT where USERNAME='" + user + "'";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql1);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql2);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int checkForm(String user, String password, String sdt, String email, String hoTen) {
        int check = 0;
        if (user.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Tên đăng nhập không được bỏ trống");
        } else if (user.matches("^[a-zA-Z0-9\\._\\-]{3,}$") == false) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Tên đăng nhập không đúng định dạng");
        } else if (password.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Mật khẩu không được bỏ trống");
        } else if (sdt.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Số điện thoại không được bỏ trống");
        } else if (sdt.matches("^(\\+84|0)\\d{9,10}$") == false) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Số điện thoại không đúng định dạng");
        } else if (email.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Email không được bỏ trống");
        } else if (email.matches("^([a-zA-Z0-9](\\.|_){0,1})+[a-zA-Z0-9]+@[a-zA-Z0-9]+((\\.){0,1}[a-zA-Z0-9]){0,}\\.[a-zA-Z]{2,3}") == false) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Email không đúng định dạng");
        } else if (hoTen.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Họ tên không được bỏ trống");
        }
        return check;
    }

    private int checkFormAdmin(String hoTen, String sdt, String email) {
        int check = 0;
        if (sdt.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Số điện thoại không được bỏ trống");
        } else if (sdt.matches("^(\\+84|0)\\d{9,10}$") == false) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Số điện thoại không đúng định dạng");
        } else if (email.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Email không được bỏ trống");
        } else if (email.matches("^([a-zA-Z0-9](\\.|_){0,1})+[a-zA-Z0-9]+@[a-zA-Z0-9]+((\\.){0,1}[a-zA-Z0-9]){0,}\\.[a-zA-Z]{2,3}") == false) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Email không đúng định dạng");
        } else if (hoTen.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(this, "Họ tên không được bỏ trống");
        }
        return check;
    }

    private int checkThongTinUser(String user, String sdt, String email) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select * from NHANVIEN,ACCOUNT where NHANVIEN.USERNAME=ACCOUNT.USERNAME"
                + " and (NHANVIEN.USERNAME ='" + user + "' OR PHONE= '" + sdt + "' OR EMAIL='" + email + "')";
        int result = 0;
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("USERNAME").equals(user)) {
                    if (rs.getBoolean("TRANGTHAI") == false) {
                        result = 2;
                        break;
                    }
                    JOptionPane.showMessageDialog(this, "Tên đăng nhập đã được sử dụng!");
                } else if (rs.getString("PHONE").equals(sdt)) {
                    JOptionPane.showMessageDialog(this, "Số điện thoại đã được sử dụng!");
                } else if (rs.getString("EMAIL").equals(email)) {
                    JOptionPane.showMessageDialog(this, "Email đã được sử dụng!");
                }
                result = 1;
            }
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private int checkSDTEM(String sdt, String email) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select * from NHANVIEN where (PHONE=? or EMAIL=?)";
        int result = 0;
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, sdt);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (!rs.getString(5).equals(userCanSua)) {
                    if (rs.getString("PHONE").equals(sdt)) {
                        JOptionPane.showMessageDialog(this, "Số điện thoại đã được sử dụng!");
                    } else if (rs.getString("EMAIL").equals(email)) {
                        JOptionPane.showMessageDialog(this, "Email đã được sử dụng!");
                    }
                    result = 1;
                }
            }
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private void themNhanVien(String user, String password, String chucVu, String sdt, String email, String hoTen) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql1 = "insert into NHANVIEN(HOTEN,PHONE,EMAIL,USERNAME) values (?,?,?,?)";
        String sql2 = "insert into ACCOUNT values (?,?,?,?)";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql2);
            ps.setString(1, user);
            ps.setString(2, MaHoaPassword.encodeString(password));
            ps.setString(3, chucVu);
            ps.setInt(4, 1);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoJPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(NhanVienJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql1);
            ps.setString(1, hoTen);
            ps.setString(2, sdt);
            ps.setString(3, email);
            ps.setString(4, user);
            ps.executeUpdate();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int checkUsernameTonTai(String user) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select * from ACCOUNT where USERNAME ='" + user + "'";
        int result = 0;
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập không tồn tại!");
                result = 1;
            }
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private void suaNhanVien(String user, String password, String chucVu, String hoTen, String sdt, String email) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql1 = "update ACCOUNT set USERNAME=?,PASSWORD=?,AUTHORIZE=? where USERNAME=?";
        String sql2 = "update NHANVIEN set HOTEN=?,PHONE=?,EMAIL=? where USERNAME=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql1);
            ps.setString(1, user); //ten user moi
            ps.setString(2, MaHoaPassword.encodeString(password));
            ps.setString(3, chucVu);
            ps.setString(4, userCanSua); //dc luu lai khi click 1 dong table
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoJPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(NhanVienJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql2);
            ps.setString(1, hoTen);
            ps.setString(2, sdt);
            ps.setString(3, email);
            ps.setString(4, user); //username ben bang nhanvien da tu update thanh ten user moi
            ps.executeUpdate();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoJPanel.class.getName()).log(Level.SEVERE, null, ex);
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

    private int checkNV_CoCongViec(String user) {
        int check = 0; // chua thuc hien cong viec nao
        Connection ketNoi = KetNoi.layKetNoi();
        String sql1 = "select * from NHANVIEN,PHIEUNHAP where NHANVIEN.MANV=PHIEUNHAP.MANV and NHANVIEN.USERNAME=?";
        String sql2 = "select * from NHANVIEN,HOADON where NHANVIEN.MANV=HOADON.MANV and NHANVIEN.USERNAME=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql1);
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                check = 1;//da thuc hien cv
            }
        } catch (SQLException ex) {
            Logger.getLogger(NhanVienJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql2);
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                check = 1;//da thuc hien cv
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(NhanVienJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return check;
    }

    private void doiTrangThai(String user, boolean on_off) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "update ACCOUNT set TRANGTHAI=? where USERNAME=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setBoolean(1, on_off);
            ps.setString(2, user);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(NhanVienJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String layQuyen(String user) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select AUTHORIZE from ACCOUNT where USERNAME=?";
        String quyen = "";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                quyen = rs.getString(1);
            }
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(ChuyenManHinh.class.getName()).log(Level.SEVERE, null, ex);
        }
        return quyen;
    }

    private void suaAdmin(String hoTen, String sdt, String email) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "update NHANVIEN set HOTEN=?,PHONE=?,EMAIL=? where USERNAME='admin'";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, hoTen);
            ps.setString(2, sdt);
            ps.setString(3, email);
            ps.executeUpdate();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(NhanVienJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void lamLai() {
        jTextField_TenDN.setText("");
        jTextField_MK.setText("");
        jTextField_TenNV.setText("");
        jTextField_SDT.setText("");
        jTextField_Email.setText("");
        jComboBox_ChucVu.setSelectedItem("Nhân viên");
        jTextField_TenDN.setEnabled(true);
        jTextField_MK.setEnabled(true);
        jComboBox_ChucVu.setEnabled(true);
        jTextField_MK.setEnabled(true);
        userCanSua = "";
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
        jLabel_nv = new javax.swing.JLabel();
        jLabel_TenDN = new javax.swing.JLabel();
        jTextField_TenDN = new javax.swing.JTextField();
        jLabel_MK = new javax.swing.JLabel();
        jTextField_MK = new javax.swing.JTextField();
        jLabel_ChucVu = new javax.swing.JLabel();
        jLabel_hoTen = new javax.swing.JLabel();
        jLabel_sdt = new javax.swing.JLabel();
        jLabel_email = new javax.swing.JLabel();
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
        jLabel_TTNV = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jLabel_nv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel_nv.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_nv.setText("Nhân Viên");

        jLabel_TenDN.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_TenDN.setText("Tên Đăng Nhập");

        jLabel_MK.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_MK.setText("Mật Khẩu");

        jLabel_ChucVu.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_ChucVu.setText("Chức Vụ");

        jLabel_hoTen.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_hoTen.setText("Họ Tên");

        jLabel_sdt.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_sdt.setText("Số Điện Thoại");

        jLabel_email.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_email.setText("Email");

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

        jComboBox_ChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nhân viên", "boss" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel_ChucVu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel_MK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel_TenDN, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField_TenDN)
                                    .addComponent(jTextField_MK)
                                    .addComponent(jComboBox_ChucVu, 0, 216, Short.MAX_VALUE))
                                .addGap(88, 88, 88))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(jButton_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                                .addComponent(jButton_Sua, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(80, 80, 80)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton_Xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel_hoTen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel_sdt, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                .addComponent(jLabel_email, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField_TenNV)
                                .addComponent(jTextField_SDT)
                                .addComponent(jTextField_Email, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_LamLai, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(84, 84, 84))))
                    .addComponent(jLabel_nv, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel_nv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_TenDN, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_TenDN, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_hoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_TenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_MK, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_MK, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_sdt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_SDT, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel_email, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField_Email, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jComboBox_ChucVu)
                    .addComponent(jLabel_ChucVu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                "Mã NV", "Họ Tên", "SDT", "Email", "Tên Đăng Nhập", "Mật khẩu", "Chức vụ", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable_DSNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_DSNhanVienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable_DSNhanVien);
        if (jTable_DSNhanVien.getColumnModel().getColumnCount() > 0) {
            jTable_DSNhanVien.getColumnModel().getColumn(5).setResizable(false);
        }

        jLabel_TTNV.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel_TTNV.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_TTNV.setText("Thông Tin Nhân Viên");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 833, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_TTNV, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_TTNV, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        userCanSua = "";
        String user = jTextField_TenDN.getText();
        String quyen = layQuyen(user);
        if (quyen.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Bạn không thể xóa tài khoản admin!");
            return;
        } else if (user.equals(username)) {
            JOptionPane.showMessageDialog(this, "Bạn không thể xóa tài khoản của chính bạn. Tài khoản đang đăng nhập!");
            return;
        }
        int result = checkUsernameTonTai(user);
        if (result == 0) {
            int check = checkNV_CoCongViec(user);
            if (check == 0) {
                Object[] options = {"Đồng ý", "Hủy"};
                int chon = JOptionPane.showOptionDialog(this, "Bạn có chắc muốn xóa nhân viên này không?",
                        "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (chon == JOptionPane.YES_OPTION) {
                    this.xoaNhanVien(user);
                    JOptionPane.showMessageDialog(this, "Xóa tài khoản và thông tin nhân viên thành công !!");
                    lamLai();
                }
            } else {
                Object[] options = {"Đồng ý", "Hủy"};
                int chon = JOptionPane.showOptionDialog(this, "Nhân viên đã có thực hiện công việc. Bạn có chắc muốn xóa không?",
                        "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (chon == JOptionPane.YES_OPTION) {
                    doiTrangThai(user, false);
                    JOptionPane.showMessageDialog(this, "Khóa tài khoản nhân viên thành công !!");
                    lamLai();
                }
            }
            this.layDanhSachNV();
        }
    }//GEN-LAST:event_jButton_XoaActionPerformed

    private void jButton_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ThemActionPerformed
        userCanSua = "";
        String user = jTextField_TenDN.getText();
        String password = jTextField_MK.getText();
        String sdt = jTextField_SDT.getText();
        String email = jTextField_Email.getText();
        String hoTen = chuanHoaChuoiHoTen(jTextField_TenNV.getText());
        String power = "";
        power = (String) jComboBox_ChucVu.getSelectedItem();
        int checkForm = this.checkForm(user, password, sdt, email, hoTen);
        if (checkForm != 1) {
            int result = checkThongTinUser(user, sdt, email);
            if (result == 0) {
                this.themNhanVien(user, password, power, sdt, email, hoTen);
                JOptionPane.showMessageDialog(this, "Đăng ký tài khoản thành công !!");
                lamLai();
            } else if (result == 2) {
                Object[] options = {"Đồng ý", "Hủy"};
                int chon = JOptionPane.showOptionDialog(this, "Tài khoản và thông tin nhân viên đã bị khóa. "
                        + "Bạn có muốn khôi phục không?",
                        "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (chon == JOptionPane.YES_OPTION) {
                    doiTrangThai(user, true);
                    JOptionPane.showMessageDialog(this, "Khôi phục tài khoản nhân viên thành công !!");
                    lamLai();
                }
            }
            this.layDanhSachNV();
        }
    }//GEN-LAST:event_jButton_ThemActionPerformed


    private void jButton_SuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SuaActionPerformed
        if (jTextField_TenDN.getText().equals("admin")) {// sua danh cho admin tk admin
            if (username.equals("admin")) {
                int check = checkFormAdmin(jTextField_TenNV.getText(), jTextField_SDT.getText(), jTextField_Email.getText());
                if (check != 1) {
                    int check3 = checkSDTEM(jTextField_SDT.getText(), jTextField_Email.getText());
                    if (check3 != 1) {
                        suaAdmin(jTextField_TenNV.getText(), jTextField_SDT.getText(), jTextField_Email.getText());
                        JOptionPane.showMessageDialog(this, "Chỉnh sửa thành công");
                        layDanhSachNV();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Bạn không đủ quyền để sửa tài khoản admin");
            }
        } else {
            String user = jTextField_TenDN.getText();
            String sdt = jTextField_SDT.getText();
            String email = jTextField_Email.getText();
            String tenNV = jTextField_TenNV.getText();
            String password = jTextField_MK.getText();
            String chucVu = jComboBox_ChucVu.getSelectedItem().toString();
            int check = checkForm(user, password, sdt, email, tenNV);
            int check2 = checkUsernameTonTai(userCanSua);
            if (check != 1 && check2 != 1) {
                int check3 = checkSDTEM(sdt, email);
                if (check3 != 1) {
                    if (userCanSua.equals(username) && userCanSua.equals(user) == false) {//sua tk đang đăng nhập va có đổi tên đn
                        Object[] options = {"Đồng ý", "Hủy"};
                        int chon = JOptionPane.showOptionDialog(this, "Bạn cần đăng xuất và đăng nhập lại sau khi sửa tên đăng nhập?",
                                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        if (chon == JOptionPane.YES_OPTION) {
                            suaNhanVien(user, password, chucVu, tenNV, sdt, email);
                            JOptionPane.showMessageDialog(this, "Chỉnh sửa thành công");
                            layDanhSachNV();
                            ((Window) getRootPane().getParent()).dispose(); //đóng cửa số sau khi sửa
                            DangNhap dangNhap = new DangNhap(true);
                            dangNhap.setTitle("Đăng Nhập Hệ Thống");
                            dangNhap.setResizable(false);
                            dangNhap.setLocationRelativeTo(null);
                            dangNhap.setVisible(true);
                        }
                    } else {
                        suaNhanVien(user, password, chucVu, tenNV, sdt, email);
                        JOptionPane.showMessageDialog(this, "Chỉnh sửa thành công");
                        layDanhSachNV();
                    }
                }
            }
        }
    }//GEN-LAST:event_jButton_SuaActionPerformed

    private void jButton_LamLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_LamLaiActionPerformed
        lamLai();
    }//GEN-LAST:event_jButton_LamLaiActionPerformed

    private void jTable_DSNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_DSNhanVienMouseClicked
        DefaultTableModel dtm = (DefaultTableModel) jTable_DSNhanVien.getModel();
        int i = jTable_DSNhanVien.getSelectedRow();

        if (dtm.getValueAt(i, 4).toString().equals("admin")) { //khong duoc sua 1 vai thuoc tinh cua admin khoa textfeild
            jTextField_TenDN.setEnabled(false);
            jTextField_MK.setEnabled(false);
            jComboBox_ChucVu.setEnabled(false);
        } else {
            jTextField_TenDN.setEnabled(true);
            jTextField_MK.setEnabled(true);
            jComboBox_ChucVu.setEnabled(true);
        }

        if (dtm.getValueAt(i, 4).toString().equals(username)) { //khong the sua mk tk cua minh
            jTextField_MK.setEnabled(false);
        } else if (dtm.getValueAt(i, 6).toString().equals("admin") == false) {
            jTextField_MK.setEnabled(true);
        }
        jTextField_TenNV.setText(dtm.getValueAt(i, 1).toString());
        jTextField_SDT.setText(dtm.getValueAt(i, 2).toString());
        jTextField_Email.setText(dtm.getValueAt(i, 3).toString());
        jTextField_TenDN.setText(dtm.getValueAt(i, 4).toString());
        jTextField_MK.setText(dtm.getValueAt(i, 5).toString());
        jComboBox_ChucVu.setSelectedItem(dtm.getValueAt(i, 6));
        userCanSua = dtm.getValueAt(i, 4).toString();
    }//GEN-LAST:event_jTable_DSNhanVienMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_LamLai;
    private javax.swing.JButton jButton_Sua;
    private javax.swing.JButton jButton_Them;
    private javax.swing.JButton jButton_Xoa;
    private javax.swing.JComboBox<String> jComboBox_ChucVu;
    private javax.swing.JLabel jLabel_ChucVu;
    private javax.swing.JLabel jLabel_MK;
    private javax.swing.JLabel jLabel_TTNV;
    private javax.swing.JLabel jLabel_TenDN;
    private javax.swing.JLabel jLabel_email;
    private javax.swing.JLabel jLabel_hoTen;
    private javax.swing.JLabel jLabel_nv;
    private javax.swing.JLabel jLabel_sdt;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_DSNhanVien;
    private javax.swing.JTextField jTextField_Email;
    private javax.swing.JTextField jTextField_MK;
    private javax.swing.JTextField jTextField_SDT;
    private javax.swing.JTextField jTextField_TenDN;
    private javax.swing.JTextField jTextField_TenNV;
    // End of variables declaration//GEN-END:variables
}
