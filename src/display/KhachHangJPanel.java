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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.JMarsDarkTheme;
import model.KhachHang;

/**
 *
 * @author Admin
 */
public class KhachHangJPanel extends javax.swing.JPanel {

    String username;

    List<KhachHang> DSKhachHang = new ArrayList<KhachHang>();

    public KhachHangJPanel(String user) {
        initComponents();
        username = user;
        jLabel_LOISDT.setVisible(false);
        layKhachHang();
    }

    private void layKhachHang() {
        Connection ketNoi = KetNoi.layKetNoi();
        DefaultTableModel dtm = (DefaultTableModel) JTable_KhachHang.getModel();
        dtm.setNumRows(0);
        String sql = "select * from KHACHHANG";
        Vector vt;
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            DSKhachHang.clear(); //moi lan lay lai kh phai reset lai list moi (loi bi trung du lieu)
            while (rs.next()) {
                KhachHang cs = new KhachHang(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(5));
                DSKhachHang.add(cs);
                if (rs.getString(1).equals("KVL")) {
                    continue; //khach vang lai ko can xuat ra table
                }
                vt = new Vector();
                vt.add(rs.getString(1));
                vt.add(rs.getString(2));
                vt.add(rs.getString(3));
                vt.add(rs.getString(4));
                vt.add(rs.getBoolean(5));
                dtm.addRow(vt);
            }
            JTable_KhachHang.setModel(dtm);
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void themKhachHang(KhachHang cs) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "insert into KHACHHANG values (?,?,?,?,?)";
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
            ps.setBoolean(5, cs.getTrangThai());
            ps.executeUpdate();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangJPanel.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void suaKhachHang(KhachHang cs) {
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
            Logger.getLogger(KhachHangJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void doiTrangThaiKH(String maKH, boolean trangThai) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "update KHACHHANG set TRANGTHAI=? where MAKH=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setBoolean(1, trangThai);
            ps.setString(2, maKH);
            ps.executeUpdate();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangJPanel.class.getName()).log(Level.SEVERE, null, ex);
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
        for (KhachHang cs : DSKhachHang) {
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
            Logger.getLogger(KhachHangJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    private void lamMoi() {
        jTextField_TenKhachHang.setText("");
        jTextArea_DiaChiKhachHang.setText("");
        jTextField_SDTKhachHang.setText("");
        jDateChooser_KhachHang.setCalendar(null);
        jLabel_LOISDT.setVisible(false);
    }

    private boolean soSanhNgay(Date hienTai, Date ngaySinh) {
        if (hienTai.before(ngaySinh)) {
            return false;
        } else {
            return true;
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
        jLabel_QLKH = new javax.swing.JLabel();
        jPanel_TTKH2 = new javax.swing.JPanel();
        jLabel_maKH = new javax.swing.JLabel();
        jLabel_TenKH = new javax.swing.JLabel();
        jLabel_NS = new javax.swing.JLabel();
        jLabel_DC = new javax.swing.JLabel();
        jTextField_SDTKhachHang = new javax.swing.JTextField();
        jTextField_TenKhachHang = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_DiaChiKhachHang = new javax.swing.JTextArea();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
        } catch (ClassNotFoundException ex) {
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        }
        jDateChooser_KhachHang = new com.toedter.calendar.JDateChooser();
        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel(new JMarsDarkTheme()));
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        jLabel_LOISDT = new javax.swing.JLabel();
        jPanel_TTKH = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTable_KhachHang = new javax.swing.JTable();
        jLabel_TTKH = new javax.swing.JLabel();
        jButton_TimKiemKhachHang = new javax.swing.JButton();
        jTextField_TimKiemKhachHang = new jtextfieldround.JTextFieldRound();
        jPanel_ButtonXuLy = new javax.swing.JPanel();
        jButton_ThemKhachHang = new javax.swing.JButton();
        jButton_SuaKhachHang = new javax.swing.JButton();
        jButton_XoaKhachHang = new javax.swing.JButton();
        jButton_LamMoiKhachHang = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel_QLKH.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel_QLKH.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_QLKH.setText("QUẢN LÝ KHÁCH HÀNG");

        jPanel_TTKH2.setBackground(new java.awt.Color(51, 51, 51));

        jLabel_maKH.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_maKH.setText("Mã khách hàng/SDT");

        jLabel_TenKH.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_TenKH.setText("Tên khách hàng");

        jLabel_NS.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_NS.setText("Ngày sinh:");

        jLabel_DC.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_DC.setText("Địa chỉ:");

        jTextField_SDTKhachHang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_SDTKhachHangKeyReleased(evt);
            }
        });

        jTextArea_DiaChiKhachHang.setColumns(20);
        jTextArea_DiaChiKhachHang.setRows(5);
        jScrollPane2.setViewportView(jTextArea_DiaChiKhachHang);

        jDateChooser_KhachHang.setBackground(new java.awt.Color(255, 255, 255));

        jLabel_LOISDT.setForeground(new java.awt.Color(255, 0, 0));
        jLabel_LOISDT.setText("Sai định dạng sđt. Hãy thử lại");

        javax.swing.GroupLayout jPanel_TTKH2Layout = new javax.swing.GroupLayout(jPanel_TTKH2);
        jPanel_TTKH2.setLayout(jPanel_TTKH2Layout);
        jPanel_TTKH2Layout.setHorizontalGroup(
            jPanel_TTKH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_TTKH2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_TTKH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_NS, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_DC, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_TenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_maKH, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_TTKH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField_SDTKhachHang)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                    .addComponent(jTextField_TenKhachHang, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooser_KhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_LOISDT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_TTKH2Layout.setVerticalGroup(
            jPanel_TTKH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_TTKH2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_TTKH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_maKH, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_SDTKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_LOISDT, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(jPanel_TTKH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_TenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_TenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel_TTKH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel_NS, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(jDateChooser_KhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_TTKH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel_DC, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                .addGap(46, 46, 46))
        );

        jPanel_TTKH.setBackground(new java.awt.Color(51, 51, 51));

        JTable_KhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SĐT", "Họ và tên", "Ngày Sinh", "Địa chỉ", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        JTable_KhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTable_KhachHangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(JTable_KhachHang);

        jLabel_TTKH.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel_TTKH.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_TTKH.setText("Thông tin khách hàng:");

        jButton_TimKiemKhachHang.setBackground(new java.awt.Color(0, 0, 0));
        jButton_TimKiemKhachHang.setForeground(new java.awt.Color(255, 153, 153));
        jButton_TimKiemKhachHang.setText("Tìm Kiếm");
        jButton_TimKiemKhachHang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153), 2));
        jButton_TimKiemKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_TimKiemKhachHangActionPerformed(evt);
            }
        });

        jTextField_TimKiemKhachHang.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextField_TimKiemKhachHang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_TimKiemKhachHangKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel_TTKHLayout = new javax.swing.GroupLayout(jPanel_TTKH);
        jPanel_TTKH.setLayout(jPanel_TTKHLayout);
        jPanel_TTKHLayout.setHorizontalGroup(
            jPanel_TTKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_TTKHLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_TTKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                    .addGroup(jPanel_TTKHLayout.createSequentialGroup()
                        .addGroup(jPanel_TTKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_TTKH)
                            .addGroup(jPanel_TTKHLayout.createSequentialGroup()
                                .addComponent(jTextField_TimKiemKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton_TimKiemKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
        );
        jPanel_TTKHLayout.setVerticalGroup(
            jPanel_TTKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_TTKHLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_TTKH, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_TTKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_TimKiemKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_TimKiemKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel_ButtonXuLy.setBackground(new java.awt.Color(51, 51, 51));

        jButton_ThemKhachHang.setBackground(new java.awt.Color(0, 0, 0));
        jButton_ThemKhachHang.setForeground(new java.awt.Color(255, 153, 153));
        jButton_ThemKhachHang.setText("Thêm khách hàng");
        jButton_ThemKhachHang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_ThemKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ThemKhachHangActionPerformed(evt);
            }
        });

        jButton_SuaKhachHang.setBackground(new java.awt.Color(0, 0, 0));
        jButton_SuaKhachHang.setForeground(new java.awt.Color(255, 153, 153));
        jButton_SuaKhachHang.setText("Sửa thông tin");
        jButton_SuaKhachHang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_SuaKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SuaKhachHangActionPerformed(evt);
            }
        });

        jButton_XoaKhachHang.setBackground(new java.awt.Color(0, 0, 0));
        jButton_XoaKhachHang.setForeground(new java.awt.Color(255, 153, 153));
        jButton_XoaKhachHang.setText("Xóa thông tin");
        jButton_XoaKhachHang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_XoaKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_XoaKhachHangActionPerformed(evt);
            }
        });

        jButton_LamMoiKhachHang.setBackground(new java.awt.Color(0, 0, 0));
        jButton_LamMoiKhachHang.setForeground(new java.awt.Color(255, 153, 153));
        jButton_LamMoiKhachHang.setText("Làm mới");
        jButton_LamMoiKhachHang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_LamMoiKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_LamMoiKhachHangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_ButtonXuLyLayout = new javax.swing.GroupLayout(jPanel_ButtonXuLy);
        jPanel_ButtonXuLy.setLayout(jPanel_ButtonXuLyLayout);
        jPanel_ButtonXuLyLayout.setHorizontalGroup(
            jPanel_ButtonXuLyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ButtonXuLyLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanel_ButtonXuLyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton_ThemKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(jButton_XoaKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_ButtonXuLyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_LamMoiKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_SuaKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(105, Short.MAX_VALUE))
        );
        jPanel_ButtonXuLyLayout.setVerticalGroup(
            jPanel_ButtonXuLyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ButtonXuLyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_ButtonXuLyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_ThemKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_SuaKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_ButtonXuLyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_XoaKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_LamMoiKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addComponent(jLabel_QLKH, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                        .addGap(596, 596, 596))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel_TTKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel_ButtonXuLy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel_TTKH2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_QLKH)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel_TTKH2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel_ButtonXuLy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel_TTKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void jButton_ThemKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ThemKhachHangActionPerformed
        String maKH = jTextField_SDTKhachHang.getText();
        String tenKH = chuanHoaChuoiHoTen(jTextField_TenKhachHang.getText());
        String diaChi = jTextArea_DiaChiKhachHang.getText();

        String ngaySinh = "";
        if (jDateChooser_KhachHang.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            ngaySinh = sdf.format(jDateChooser_KhachHang.getDate()); // Date->String
        }
        Calendar cal = Calendar.getInstance();
        Date hienTai = cal.getTime();

        String regexTen = "^[a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳýỵỷỹ\\s]+$";
        boolean cohieu = true;
        if (maKH.equals("")) {
            JOptionPane.showMessageDialog(this, "Bạn cần nhập mã khách hàng là sđt của khách!");
            cohieu = false;
        } else if (maKH.matches("^(\\+84|0)\\d{9,10}$") == false) {
            jLabel_LOISDT.setVisible(true);
            jTextField_SDTKhachHang.requestFocus();
            cohieu = false;
        } else if (tenKH.equals("")) {
            JOptionPane.showMessageDialog(this, "Bạn cần nhập tên khách hàng!");
            cohieu = false;
        } else if (tenKH.matches(regexTen) == false) {
            JOptionPane.showMessageDialog(this, "Tên khách hàng không đúng định dạng(Không chứa số,...)");
            cohieu = false;
        } else if (soSanhNgay(hienTai, jDateChooser_KhachHang.getDate()) == false) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không thể trước ngày hiện tại!");
            cohieu = false;
        }
        if (cohieu == true) {
            int c = checkMaKH_SDT(maKH);
            if (c == 1) {
                boolean k = false;
                for (KhachHang x : DSKhachHang) {
                    if (maKH.equals(x.getMaKH())) {
                        if (x.getTrangThai() == false) {
                            Object[] options = {"Đồng ý", "Hủy"};
                            int chon = JOptionPane.showOptionDialog(this, "Khách hàng đã bị khóa bạn có muốn khôi phục lại?",
                                    "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                            if (chon == JOptionPane.YES_OPTION) {
                                doiTrangThaiKH(maKH, true);
                                JOptionPane.showMessageDialog(this, "Khôi phục khách hàng thành công!");
                                k = true;
                            }
                        }
                    }
                }
                if (k == false) {
                    JOptionPane.showMessageDialog(this, "Mã khách hàng với sđt trên đã bị trùng. Vui lòng kiểm tra lại!");
                }
            } else {
                KhachHang cs = new KhachHang(maKH, tenKH, ngaySinh, diaChi, true);
                themKhachHang(cs);
                JOptionPane.showMessageDialog(this, "Đã thêm thành công khách hàng mới!");
            }
            this.layKhachHang();
        }
    }//GEN-LAST:event_jButton_ThemKhachHangActionPerformed

    private void jButton_SuaKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SuaKhachHangActionPerformed
        String maKH = jTextField_SDTKhachHang.getText();
        String tenKH = chuanHoaChuoiHoTen(jTextField_TenKhachHang.getText());
        String regexTen = "^[a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳýỵỷỹ\\s]+$";
        Calendar cal = Calendar.getInstance();
        Date hienTai = cal.getTime();
        if (maKH.equals("")) {
            JOptionPane.showMessageDialog(this, "Bạn cần nhập mã khách hàng!");
            return;
        }
        int c = checkMaKH_SDT(maKH);
        if (c == 0) {
            JOptionPane.showMessageDialog(this, "Mã khách hàng hiện chưa có. Vui lòng thêm mới!");
        } else {
            if (tenKH.equals("")) {
                JOptionPane.showMessageDialog(this, "Bạn cần nhập tên khách hàng!");
                return;
            } else if (tenKH.matches(regexTen) == false) {
                JOptionPane.showMessageDialog(this, "Tên khách hàng không đúng định dạng(Không chứa số,...)");
                return;
            }
            String diaChi = jTextArea_DiaChiKhachHang.getText();
            String ngaySinh = "";
            if (jDateChooser_KhachHang.getDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                ngaySinh = sdf.format(jDateChooser_KhachHang.getDate());
            }
            if (soSanhNgay(hienTai, jDateChooser_KhachHang.getDate()) == false) {
                JOptionPane.showMessageDialog(this, "Ngày sinh không thể trước ngày hiện tại!");
                return;
            }
            for (KhachHang cs : DSKhachHang) {
                if (cs.getMaKH().equals(maKH)) {
                    String tmp;
                    if (cs.getNgaySinh() == null) {
                        tmp = "";
                    } else {
                        tmp = cs.getNgaySinh();
                    }
                    if (cs.getHoTen().equalsIgnoreCase(tenKH) && tmp.equals(ngaySinh) && cs.getDiaChi().equals(diaChi)) {
                        JOptionPane.showMessageDialog(this, "Không có sự thay đổi thông tin cần sửa!");
                        return;
                    }
                    cs.setHoTen(tenKH);
                    cs.setNgaySinh(ngaySinh);
                    cs.setDiaChi(diaChi);
                    suaKhachHang(cs);
                    JOptionPane.showMessageDialog(this, "Sửa thông tin khách hàng thành công!");
                    this.layKhachHang();
                    break;
                }
            }
        }
    }//GEN-LAST:event_jButton_SuaKhachHangActionPerformed

    private void jButton_XoaKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_XoaKhachHangActionPerformed
        String maKH = jTextField_SDTKhachHang.getText();
        if (maKH.equals("")) {
            JOptionPane.showMessageDialog(this, "Mã khách hàng cần xóa không được để trống!");
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
                        int chon1 = JOptionPane.showOptionDialog(this, "Khách hàng đã từng mua hàng, khi xóa sẽ khóa thông tin khách hàng ?",
                                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        if (chon1 == JOptionPane.YES_OPTION) {
                            doiTrangThaiKH(maKH, false);
                            JOptionPane.showMessageDialog(this, "Khóa khách hàng thành công!");
                        }
                    } else {
                        xoaKhachHang(maKH);
                        JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
                    }
                    this.layKhachHang();
                } else {
                    lamMoi();
                }
            }
        }
    }//GEN-LAST:event_jButton_XoaKhachHangActionPerformed

    private void jButton_LamMoiKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_LamMoiKhachHangActionPerformed
        lamMoi();
    }//GEN-LAST:event_jButton_LamMoiKhachHangActionPerformed

    private void JTable_KhachHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTable_KhachHangMouseClicked
        int i = JTable_KhachHang.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) JTable_KhachHang.getModel();
        jTextField_SDTKhachHang.setText(dtm.getValueAt(i, 0).toString());
        jTextField_TenKhachHang.setText(dtm.getValueAt(i, 1).toString());
        if (dtm.getValueAt(i, 2) != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date ngaySinh = sdf.parse((String) dtm.getValueAt(i, 2)); //String->Date
                jDateChooser_KhachHang.setDate(ngaySinh);
            } catch (ParseException ex) {
                Logger.getLogger(KhachHangJPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            jDateChooser_KhachHang.setCalendar(null);
        }
        if (dtm.getValueAt(i, 3) != null) {
            jTextArea_DiaChiKhachHang.setText(dtm.getValueAt(i, 3).toString());
        } else {
            jTextArea_DiaChiKhachHang.setText("");
        }
    }//GEN-LAST:event_JTable_KhachHangMouseClicked

    private void jTextField_SDTKhachHangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_SDTKhachHangKeyReleased
        String t = jTextField_SDTKhachHang.getText();
        if (t.matches("^(\\+84|0)\\d{9,10}$") == false) {
            jLabel_LOISDT.setVisible(true);
        } else {
            jLabel_LOISDT.setVisible(false);
        }
    }//GEN-LAST:event_jTextField_SDTKhachHangKeyReleased

    private void jButton_TimKiemKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_TimKiemKhachHangActionPerformed
        String f = jTextField_TimKiemKhachHang.getText();
        boolean c = false;
        for (KhachHang cs : DSKhachHang) {
            String ten = cs.getHoTen().toLowerCase();
            if (cs.getMaKH().contains(f) || ten.contains(f.toLowerCase())) {
                c = true;
            }
        }
        if (c == false) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng này!");
        }
    }//GEN-LAST:event_jButton_TimKiemKhachHangActionPerformed

    private void jTextField_TimKiemKhachHangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_TimKiemKhachHangKeyReleased
        String f = jTextField_TimKiemKhachHang.getText();

        DefaultTableModel dtm = (DefaultTableModel) JTable_KhachHang.getModel();
        dtm.setNumRows(0);
        for (KhachHang x : DSKhachHang) {
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
                vt.add(x.getTrangThai());
                dtm.addRow(vt);
            }
        }
        JTable_KhachHang.setModel(dtm);
    }//GEN-LAST:event_jTextField_TimKiemKhachHangKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable JTable_KhachHang;
    private javax.swing.JButton jButton_LamMoiKhachHang;
    private javax.swing.JButton jButton_SuaKhachHang;
    private javax.swing.JButton jButton_ThemKhachHang;
    private javax.swing.JButton jButton_TimKiemKhachHang;
    private javax.swing.JButton jButton_XoaKhachHang;
    private com.toedter.calendar.JDateChooser jDateChooser_KhachHang;
    private javax.swing.JLabel jLabel_DC;
    private javax.swing.JLabel jLabel_LOISDT;
    private javax.swing.JLabel jLabel_NS;
    private javax.swing.JLabel jLabel_QLKH;
    private javax.swing.JLabel jLabel_TTKH;
    private javax.swing.JLabel jLabel_TenKH;
    private javax.swing.JLabel jLabel_maKH;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel_ButtonXuLy;
    private javax.swing.JPanel jPanel_TTKH;
    private javax.swing.JPanel jPanel_TTKH2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea_DiaChiKhachHang;
    private javax.swing.JTextField jTextField_SDTKhachHang;
    private javax.swing.JTextField jTextField_TenKhachHang;
    private jtextfieldround.JTextFieldRound jTextField_TimKiemKhachHang;
    // End of variables declaration//GEN-END:variables
}
