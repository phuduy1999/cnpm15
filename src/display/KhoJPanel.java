package display;

import Conection.KetNoi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.KhachHang;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Admin
 */
public class KhoJPanel extends javax.swing.JPanel {

    String username;
    /**
     * Creates new form KhoJPanel
     */

    DefaultTableModel tbn;

    Connection ketNoi = KetNoi.layKetNoi();

    //private String userName;
    public KhoJPanel(String user) {
        initComponents();
        this.username = user;
        System.out.println("Truy cập StoreHouse với username = " + username);
        loadComboboxLoai();
        loadComboboxSize();
        loadData();
        jButton_XuatBaoCao.setEnabled(true);
        jTextField_SoLuong.setEnabled(false);
    }

    public KhoJPanel(String user, String quyen) {
        initComponents();
        username = user;
        loadComboboxLoai();
        loadComboboxSize();
        if (quyen.equals("boss")) {
            jButton_LamLai.setEnabled(false);
            jButton_SuaSanPham.setEnabled(false);
            jButton_TaoPhieuNhap.setEnabled(false);
            jButton_ThemSanPham.setEnabled(false);
            jButton_XoaSanPham.setEnabled(false);
            jButton_XuatBaoCao.setEnabled(true);
            jButton_RF.setEnabled(false);
            jButton_QLLOAI.setEnabled(false);
        }
        loadData();
        jTextField_SoLuong.setEnabled(false);
    }

    private void loadComboboxSize() {
        try {
            PreparedStatement ps = ketNoi.prepareStatement("Select SIZE from SIZE");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jComboBox_Size.addItem(rs.getString("SIZE"));
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private void loadComboboxLoai() {
        try {
            PreparedStatement ps = ketNoi.prepareStatement("Select LOAI from LoaiGD");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jComboBox_Loai.addItem(rs.getString("Loai"));
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private void loadData() {
        try {
            String sql = "SELECT DISTINCT GIAYDEP.MASP, GIAYDEP.TENSP, GIAYDEP.DONGIA, CHITIETGD.SOLUONGTON, CHITIETGD.SIZE, LOAIGD.LOAI, TRANGTHAI\n"
                    + "                    FROM GIAYDEP\n"
                    + "                    INNER JOIN CHITIETGD ON GIAYDEP.MASP = CHITIETGD.MASP\n"
                    + "                    INNER JOIN SIZE ON GIAYDEP.MASP = CHITIETGD.MASP\n"
                    + "                    INNER JOIN LOAIGD ON GIAYDEP.MALOAI = LOAIGD.MALOAI";
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            //storeHouseList.clear();
            Object[] obj = new Object[]{"STT", "Mã Sản phẩm", "Tên Sản phẩm", "Đơn giá", "Số lượng", "Size", "Loại", "Trạng thái"};
            tbn = new DefaultTableModel(obj, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                }
            };
            jTable_DanhSachSP.setModel(tbn);
            int c = 0;
            try {
                while (rs.next()) {
                    c++;
                    Object[] item = new Object[8];
                    item[0] = c;
                    item[1] = rs.getString("MASP");
                    item[2] = rs.getString("TENSP");
                    item[3] = rs.getString("DONGIA");
                    item[4] = rs.getInt("SOLUONGTON");
                    item[5] = rs.getInt("Size");
                    item[6] = rs.getString("LOAI");
                    item[7] = rs.getBoolean("TRANGTHAI");
                    tbn.addRow(item);
                }
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }

            // Ham tra gia tri tu bang len textField
            jTable_DanhSachSP.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (jTable_DanhSachSP.getSelectedRow() >= 0) {
                        jTextField_MaSanPham.setText(jTable_DanhSachSP.getValueAt(jTable_DanhSachSP.getSelectedRow(), 1) + "");
                        jTextField_TenSanPham.setText(jTable_DanhSachSP.getValueAt(jTable_DanhSachSP.getSelectedRow(), 2) + "");
                        jTextField_DonGia.setText(jTable_DanhSachSP.getValueAt(jTable_DanhSachSP.getSelectedRow(), 3) + "");
                        jTextField_SoLuong.setText(jTable_DanhSachSP.getValueAt(jTable_DanhSachSP.getSelectedRow(), 4) + "");
                        jComboBox_Size.setSelectedItem(jTable_DanhSachSP.getModel().getValueAt(jTable_DanhSachSP.getSelectedRow(), 5) + "");
                        jComboBox_Loai.setSelectedItem(jTable_DanhSachSP.getModel().getValueAt(jTable_DanhSachSP.getSelectedRow(), 6) + "");
                    }
                }
            });
            // ket thuc ham tra gia tri tu bang len textField 
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private void ThemChiTietGD() {
        jTextField_SoLuong.setText("0");
        String sqlChiTietGD = "insert into CHITIETGD values(?,?,?,?)";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sqlChiTietGD);
            ps.setString(1, jTextField_MaSanPham.getText());
            ps.setString(2, (String) jComboBox_Size.getSelectedItem());
            ps.setString(3, jTextField_SoLuong.getText());
            ps.setString(4, "1");
            ps.execute();
        } catch (Exception e) {
        }
    }

    private String LayMaLoai(String tenLoai) {
        String maLoai = "";
        System.out.println("Ten Loai trong ham lay ma loai la " + tenLoai);
        String sql = "select LOAIGD.MALOAI\n"
                + "from LOAIGD\n"
                + "where LOAIGD.LOAI = N'" + tenLoai + "'";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                maLoai = rs.getString("maLoai");
            }
        } catch (Exception e) {
            e.toString();
        }
        return maLoai;
    }

    private void ThemGiayDep() {
        String tenLoai = jComboBox_Loai.getSelectedItem().toString();
        String maLoai = LayMaLoai(tenLoai);
        System.out.println("maLoai la" + maLoai);
        String sqlGiayDep = "insert into GIAYDEP values(?,?,?,?)";
        try {
            PreparedStatement ps1 = ketNoi.prepareStatement(sqlGiayDep);
            ps1.setString(1, jTextField_MaSanPham.getText());
            ps1.setString(2, jTextField_TenSanPham.getText());
            ps1.setString(3, jTextField_DonGia.getText());
            ps1.setString(4, maLoai);
            ps1.execute();

        } catch (Exception e) {
            System.out.println("Da them 1 size moi cho ma san pham : " + jTextField_MaSanPham.getText());
        }
    }

    private void SuaGiayDep() {
        String maSanPham, tenSanPham, donGiaSP, soLuongSP, sizeSP, loaiSP;
        maSanPham = jTextField_MaSanPham.getText();
        tenSanPham = jTextField_TenSanPham.getText();
        donGiaSP = jTextField_DonGia.getText();
        soLuongSP = jTextField_SoLuong.getText();
        sizeSP = jComboBox_Size.getSelectedItem().toString();

        String tenLoai = jComboBox_Loai.getSelectedItem().toString();
        String maLoai = LayMaLoai(tenLoai);

        String sql = "update GiayDep set TENSP = N'" + tenSanPham + "', DONGIA = '" + donGiaSP + "', MALOAI = N'" + maLoai + "'\n"
                + "where MASP = '" + maSanPham + "'";
        try {
            PreparedStatement ps1 = ketNoi.prepareStatement(sql);
            ps1.execute();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private int Check(String MaSP, String size) {
        String sql = "select CHITIETGD.MASP, CHITIETGD.SIZE from CHITIETGD\n"
                + "where CHITIETGD.MASP = '" + MaSP + "' and CHITIETGD.SIZE = '" + size + "'"
                + " and CHITIETGD.TRANGTHAI = '1'";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MaSP = rs.getString("MASP");
                size = rs.getString("SIZE");
                return 1;
            }

        } catch (Exception e) {
            e.toString();
        }
        return 0;
    }

    private int CheckTrangThai(String MaSP, String size) {
        String sql = "select CHITIETGD.MASP, CHITIETGD.SIZE from CHITIETGD\n"
                + "where CHITIETGD.MASP = '" + MaSP + "' and CHITIETGD.SIZE = '" + size + "'"
                + " and CHITIETGD.TRANGTHAI = '0'";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MaSP = rs.getString("MASP");
                size = rs.getString("SIZE");
                return 1;
            }

        } catch (Exception e) {
            e.toString();
        }
        return 0;
    }

    private void KhoiPhucSanPham() {
        String maSP, size;
        maSP = jTextField_MaSanPham.getText();
        size = jComboBox_Size.getSelectedItem().toString();
        String sql = "Update CHITIETGD set TRANGTHAI = '1' where MASP = '" + maSP + "' and SIZE = '" + size + "'";
        try {
            PreparedStatement ps1 = ketNoi.prepareStatement(sql);
            ps1.execute();
            JOptionPane.showMessageDialog(this, "Bạn vừa khôi phục 1 sản phẩm trước đây từng xóa!!");
        } catch (Exception e) {
            e.toString();
        }
    }

    private boolean checkSP_daDuocBan() {
        String maSP, size;
        maSP = jTextField_MaSanPham.getText();
        size = jComboBox_Size.getSelectedItem().toString();
        boolean c = false;
        String sql = "select * from CHITIETHD where MASP=? and SIZE=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maSP);
            ps.setString(2, size);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c = true;//da duoc ban

            }
        } catch (SQLException ex) {
            Logger.getLogger(KhoJPanel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    private boolean checkSP_CoTrongChiTietGD() {
        boolean c = false;
        String maSP;
        maSP = jTextField_MaSanPham.getText();
        String sql = "select * from CHITIETGD where MASP=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maSP);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c = true;//co trong chitietgd

            }
        } catch (SQLException ex) {
            Logger.getLogger(KhoJPanel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    private void xoaGD(String maSP) {
        String sql = "delete GIAYDEP where MASP=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maSP);
            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(KhoJPanel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void xoaSP_ChuaDuocBan() {
        String maSP, size;
        maSP = jTextField_MaSanPham.getText();
        size = jComboBox_Size.getSelectedItem().toString();
        String sql = "delete CHITIETGD where MASP=? and SIZE=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maSP);
            ps.setString(2, size);
            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(KhoJPanel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void XoaSanPham() {
        String maSP, size;
        maSP = jTextField_MaSanPham.getText();
        size = jComboBox_Size.getSelectedItem().toString();
        String sql = "Update CHITIETGD set TRANGTHAI = '0' where MASP = '" + maSP + "' and SIZE = '" + size + "'";
        try {
            PreparedStatement ps1 = ketNoi.prepareStatement(sql);
            ps1.execute();
        } catch (Exception e) {
            e.toString();
        }
    }

    private void XuatBaoCao() {
        try {
            Hashtable map = new Hashtable();
            JasperReport report = JasperCompileManager.compileReport("src/display/rpBaoCaoSoLuong.jrxml");

            JasperPrint p = JasperFillManager.fillReport(report, map, ketNoi);
            JasperViewer.viewReport(p, false);
        } catch (Exception ex) {
            ex.toString();
        }
    }

    private void boLoc(String timKiem) {
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(tbn);
        jTable_DanhSachSP.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter("(?i)" + timKiem.toLowerCase()));
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
        jLabel_QLKho = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel_MaSP = new javax.swing.JLabel();
        jLabel_TenSP = new javax.swing.JLabel();
        jLabel_DonGia = new javax.swing.JLabel();
        jLabel_SL = new javax.swing.JLabel();
        jLabel_SIZE = new javax.swing.JLabel();
        jTextField_MaSanPham = new javax.swing.JTextField();
        jTextField_TenSanPham = new javax.swing.JTextField();
        jTextField_DonGia = new javax.swing.JTextField();
        jTextField_SoLuong = new javax.swing.JTextField();
        jComboBox_Size = new javax.swing.JComboBox();
        jLabel_Loai = new javax.swing.JLabel();
        jComboBox_Loai = new javax.swing.JComboBox();
        jButton_XoaSanPham = new javax.swing.JButton();
        jButton_SuaSanPham = new javax.swing.JButton();
        jButton_ThemSanPham = new javax.swing.JButton();
        jButton_LamLai = new javax.swing.JButton();
        jLabel_SP = new javax.swing.JLabel();
        jButton_RF = new javax.swing.JButton();
        jPanel_TK = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_DanhSachSP = new javax.swing.JTable();
        jButton_XuatBaoCao = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jTextField_TimSanPham = new jtextfieldround.JTextFieldRound();
        jButton_TaoPhieuNhap = new javax.swing.JButton();
        jButton_QLLOAI = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel_QLKho.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel_QLKho.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_QLKho.setText("QUẢN LÝ KHO");

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        jLabel_MaSP.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_MaSP.setText("Mã Sản Phẩm:");

        jLabel_TenSP.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_TenSP.setText("Tên Sản Phẩm:");

        jLabel_DonGia.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_DonGia.setText("Đơn Giá:");

        jLabel_SL.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_SL.setText("Số Lượng:");

        jLabel_SIZE.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_SIZE.setText("Size:");

        jTextField_MaSanPham.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_MaSanPhamKeyReleased(evt);
            }
        });

        jTextField_DonGia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_DonGiaKeyReleased(evt);
            }
        });

        jTextField_SoLuong.setText("0");

        jComboBox_Size.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-Chọn Size-" }));

        jLabel_Loai.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_Loai.setText("Loại:");

        jComboBox_Loai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-Chọn Loại-" }));

        jButton_XoaSanPham.setBackground(new java.awt.Color(0, 0, 0));
        jButton_XoaSanPham.setForeground(new java.awt.Color(255, 153, 153));
        jButton_XoaSanPham.setText("Xóa Sản Phẩm");
        jButton_XoaSanPham.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_XoaSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_XoaSanPhamActionPerformed(evt);
            }
        });

        jButton_SuaSanPham.setBackground(new java.awt.Color(0, 0, 0));
        jButton_SuaSanPham.setForeground(new java.awt.Color(255, 153, 153));
        jButton_SuaSanPham.setText("Sửa Sản Phẩm");
        jButton_SuaSanPham.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_SuaSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SuaSanPhamActionPerformed(evt);
            }
        });

        jButton_ThemSanPham.setBackground(new java.awt.Color(0, 0, 0));
        jButton_ThemSanPham.setForeground(new java.awt.Color(255, 153, 153));
        jButton_ThemSanPham.setText("Thêm sản phẩm");
        jButton_ThemSanPham.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_ThemSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ThemSanPhamActionPerformed(evt);
            }
        });

        jButton_LamLai.setBackground(new java.awt.Color(0, 0, 0));
        jButton_LamLai.setForeground(new java.awt.Color(255, 153, 153));
        jButton_LamLai.setText("Làm lại");
        jButton_LamLai.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_LamLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_LamLaiActionPerformed(evt);
            }
        });

        jLabel_SP.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel_SP.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_SP.setText("Thêm mới sản phẩm");

        jButton_RF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/refresh (1).png"))); // NOI18N
        jButton_RF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_RF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_RFActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton_ThemSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_XoaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton_LamLai, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_SuaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel_SP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel_Loai, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel_SL, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel_DonGia, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel_TenSP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                                        .addComponent(jLabel_MaSP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField_TenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField_MaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField_DonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField_SoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox_Size, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jComboBox_Loai, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton_RF, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(54, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_SP, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_MaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_TenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_TenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_DonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_DonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_SoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_SL, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_Size, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton_RF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel_Loai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox_Loai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton_SuaSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(jButton_ThemSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_XoaSanPham)
                    .addComponent(jButton_LamLai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel_TK.setBackground(new java.awt.Color(51, 51, 51));
        jPanel_TK.setForeground(new java.awt.Color(255, 153, 153));

        jTable_DanhSachSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable_DanhSachSP);

        jButton_XuatBaoCao.setBackground(new java.awt.Color(0, 0, 0));
        jButton_XuatBaoCao.setForeground(new java.awt.Color(255, 153, 153));
        jButton_XuatBaoCao.setText("Xuất số lượng");
        jButton_XuatBaoCao.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_XuatBaoCao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_XuatBaoCaoActionPerformed(evt);
            }
        });

        jLabel9.setForeground(new java.awt.Color(255, 153, 153));
        jLabel9.setText("Tìm kiếm:");

        jTextField_TimSanPham.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_TimSanPhamKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel_TKLayout = new javax.swing.GroupLayout(jPanel_TK);
        jPanel_TK.setLayout(jPanel_TKLayout);
        jPanel_TKLayout.setHorizontalGroup(
            jPanel_TKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_TKLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_TKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                    .addGroup(jPanel_TKLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(8, 8, 8)
                        .addComponent(jTextField_TimSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_XuatBaoCao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel_TKLayout.setVerticalGroup(
            jPanel_TKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_TKLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_TKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jButton_XuatBaoCao, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_TimSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton_TaoPhieuNhap.setBackground(new java.awt.Color(0, 0, 0));
        jButton_TaoPhieuNhap.setForeground(new java.awt.Color(255, 153, 153));
        jButton_TaoPhieuNhap.setText("Tạo phiếu nhập mới");
        jButton_TaoPhieuNhap.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_TaoPhieuNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_TaoPhieuNhapActionPerformed(evt);
            }
        });

        jButton_QLLOAI.setForeground(new java.awt.Color(255, 153, 153));
        jButton_QLLOAI.setText("Quản lý loại");
        jButton_QLLOAI.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_QLLOAI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_QLLOAIActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel_QLKho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(327, 327, 327)
                        .addComponent(jButton_QLLOAI, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton_TaoPhieuNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel_TK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(17, 17, 17))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel_QLKho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_TaoPhieuNhap, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_QLLOAI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel_TK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(66, Short.MAX_VALUE))
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

    private void jButton_LamLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_LamLaiActionPerformed
        // TODO add your handlingcode  here:
        jTextField_MaSanPham.setText("");
        jTextField_TenSanPham.setText("");
        jTextField_DonGia.setText("");
        jTextField_SoLuong.setText("0");
        jComboBox_Size.setSelectedIndex(0);
        jComboBox_Loai.setSelectedIndex(0);
        loadData();
    }//GEN-LAST:event_jButton_LamLaiActionPerformed

    private void jButton_ThemSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ThemSanPhamActionPerformed
        // TODO add your handling code here:
        String maSanPham, tenSanPham, donGiaSP, soLuongSP, sizeSP, loaiSP;
        maSanPham = jTextField_MaSanPham.getText();
        tenSanPham = jTextField_TenSanPham.getText();
        donGiaSP = jTextField_DonGia.getText();
        soLuongSP = jTextField_SoLuong.getText();
        sizeSP = jComboBox_Size.getSelectedItem().toString();
        loaiSP = jComboBox_Loai.getSelectedItem().toString();

        if (maSanPham.contains(" ") == true) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm không được chứa khoảng trắng");
        } else if (donGiaSP.matches("\\D") == true) {
            JOptionPane.showMessageDialog(this, "Đơn giá chỉ nhận kí tự số");
            //jTextField_DonGia.requestFocus();            
        } else if (maSanPham.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã sản phẩm");
        } else if (tenSanPham.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm");
        } else if (donGiaSP.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đơn giá sản phẩm");
        } else if (soLuongSP.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng sản phẩm");
        } else if (sizeSP.equals("-Chọn Size-")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn size sản phẩm");
        } else if (loaiSP.equals("-Chọn Loại-")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm");
        } else if (Check(maSanPham, sizeSP) == 1) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm và size theo mã đã tồn tại!! Vui lòng nhập mã hoặc size khác!");
        } else {
            if (CheckTrangThai(maSanPham, sizeSP) == 1) {
                if (JOptionPane.showConfirmDialog(this, "Bạn đã xóa sản phẩm này trước kia!! Bạn muốn khôi phục lại chứ?", "confirm",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    KhoiPhucSanPham();
                    loadData();
                }
            } else {
                try {
                    int donGiaTemp = Integer.parseInt(donGiaSP);
                    if (donGiaTemp <= 0) {
                        JOptionPane.showMessageDialog(this, "Đơn giá phải lớn hơn 0");
                    } else {
                        ThemGiayDep();
                        ThemChiTietGD();
                        loadData();
                        JOptionPane.showMessageDialog(this, "Thêm thành công");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Kiểm tra lại đơn giá chỉ chứa kí tự số");
                }

            }
        }
    }//GEN-LAST:event_jButton_ThemSanPhamActionPerformed

    private void jButton_XoaSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_XoaSanPhamActionPerformed
        // TODO add your handling code here:
        String maSanPham, tenSanPham, donGiaSP, soLuongSP, sizeSP, loaiSP;
        maSanPham = jTextField_MaSanPham.getText();
        tenSanPham = jTextField_TenSanPham.getText();
        donGiaSP = jTextField_DonGia.getText();
        soLuongSP = jTextField_SoLuong.getText();
        sizeSP = jComboBox_Size.getSelectedItem().toString();
        loaiSP = jComboBox_Loai.getSelectedItem().toString();
        try {
            if (maSanPham.equals("")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã sản phẩm");
            } else if (tenSanPham.equals("")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm");
            } else if (donGiaSP.equals("")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đơn giá sản phẩm");
            } else if (soLuongSP.equals("")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng sản phẩm");
            } else if (sizeSP.equals("-Chọn Size-")) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn size sản phẩm");
            } else if (loaiSP.equals("-Chọn Loại-")) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm");
            } else {
                if (Check(maSanPham, sizeSP) == 1) {
                    Object[] options = {"Đồng ý", "Hủy"};
                    int chon = JOptionPane.showOptionDialog(this, "Bạn có chắc muốn xóa sản phẩm này?",
                            "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (chon == JOptionPane.YES_OPTION) {
                        if (checkSP_daDuocBan() == false) {
                            xoaSP_ChuaDuocBan();
                            if (checkSP_CoTrongChiTietGD() == false) {
                                xoaGD(maSanPham);
                            }
                        } else {
                            XoaSanPham();
                        }
                        loadData();
                        JOptionPane.showMessageDialog(this, "Xóa thành công");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Không tồn tại sản phẩm trên!");
                }
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }//GEN-LAST:event_jButton_XoaSanPhamActionPerformed

    private void jButton_SuaSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SuaSanPhamActionPerformed
        // TODO add your handling code here:
        String maSanPham, tenSanPham, donGiaSP, soLuongSP, sizeSP, loaiSP;
        maSanPham = jTextField_MaSanPham.getText();
        tenSanPham = jTextField_TenSanPham.getText();
        donGiaSP = jTextField_DonGia.getText();
        soLuongSP = jTextField_SoLuong.getText();
        sizeSP = jComboBox_Size.getSelectedItem().toString();
        loaiSP = jComboBox_Loai.getSelectedItem().toString();

        if (maSanPham.contains(" ") == true) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm không được chứa khoảng trắng");
        } else if (donGiaSP.matches("\\D") == true) {
            JOptionPane.showMessageDialog(this, "Đơn giá chỉ nhận kí tự số");
        } else if (maSanPham.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã sản phẩm");
        } else if (tenSanPham.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm");
        } else if (donGiaSP.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đơn giá sản phẩm");
        } else if (soLuongSP.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng sản phẩm");
        } else if (sizeSP.equals("-Chọn Size-")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn size sản phẩm");
        } else if (loaiSP.equals("-Chọn Loại-")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm");
        } else {
            try {
                int donGiaTemp = Integer.parseInt(donGiaSP);
                if (donGiaTemp <= 0) {
                    JOptionPane.showMessageDialog(this, "Đơn giá phải lớn hơn 0");
                } else {
                    if (jTextField_MaSanPham.getText().toString().equals(jTable_DanhSachSP.getValueAt(jTable_DanhSachSP.getSelectedRow(), 1).toString())
                            & jTextField_TenSanPham.getText().toString().equals(jTable_DanhSachSP.getValueAt(jTable_DanhSachSP.getSelectedRow(), 2).toString())
                            & jTextField_DonGia.getText().toString().equals(jTable_DanhSachSP.getValueAt(jTable_DanhSachSP.getSelectedRow(), 3).toString())
                            & jComboBox_Loai.getSelectedItem().toString().equals(jTable_DanhSachSP.getValueAt(jTable_DanhSachSP.getSelectedRow(), 6).toString())
                            & jComboBox_Size.getSelectedItem().toString().equals(jTable_DanhSachSP.getValueAt(jTable_DanhSachSP.getSelectedRow(), 5).toString())) {
                        JOptionPane.showMessageDialog(this, "Bạn chưa sửa gì cả!!");
                    } else {
                        try {
                            if (!jTextField_MaSanPham.getText().toString().equals(jTable_DanhSachSP.getValueAt(jTable_DanhSachSP.getSelectedRow(), 1).toString())
                                    || !jComboBox_Size.getSelectedItem().toString().equals(jTable_DanhSachSP.getValueAt(jTable_DanhSachSP.getSelectedRow(), 5).toString())) {
                                JOptionPane.showMessageDialog(this, "Mã sản phảm và size không thể sửa");
                            } else {
                                Object[] options = {"Đồng ý", "Hủy"};
                                int chon = JOptionPane.showOptionDialog(this, "Bạn có chắc muốn sửa sản phẩm này?",
                                        "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                                if (chon == JOptionPane.YES_OPTION) {
                                    SuaGiayDep();
                                    loadData();
                                    JOptionPane.showMessageDialog(this, "Sửa thành công");
                                }
                            }
                        } catch (Exception e) {
                            System.err.println(e.toString());
                        }
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa từ danh sách");
            }
        }
    }//GEN-LAST:event_jButton_SuaSanPhamActionPerformed

    private void jButton_TaoPhieuNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_TaoPhieuNhapActionPerformed
        // TODO add your handling code here:
        PhieuNhapJFrame dsPN = new PhieuNhapJFrame(username);
        dsPN.setVisible(true);
        dsPN.setTitle("Quản lý phiếu nhập");
    }//GEN-LAST:event_jButton_TaoPhieuNhapActionPerformed

    private void jButton_XuatBaoCaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_XuatBaoCaoActionPerformed
        // TODO add your handling code here:
        XuatBaoCao();
        System.out.println("clicked button XuatBaoCao");
    }//GEN-LAST:event_jButton_XuatBaoCaoActionPerformed

    private void jTextField_DonGiaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_DonGiaKeyReleased
        // TODO add your handling code here:
        String t = jTextField_DonGia.getText();
        if (t.matches("\\D") == true) {
            JOptionPane.showMessageDialog(this, "Đơn Giá chỉ có thể chưa kí tự số");
            jTextField_DonGia.setText("");
        }
    }//GEN-LAST:event_jTextField_DonGiaKeyReleased

    private void jTextField_MaSanPhamKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_MaSanPhamKeyReleased
        // TODO add your handling code here:
        String t = jTextField_MaSanPham.getText();

        if (t.contains(" ") == true) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm không thể chứa khoảng trắng! Vui lòng nhập lại");
            jTextField_MaSanPham.setText("");
        }
    }//GEN-LAST:event_jTextField_MaSanPhamKeyReleased

    private void jButton_QLLOAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_QLLOAIActionPerformed
        // TODO add your handling code here:
        DanhSachLoaiJFrame dsL = new DanhSachLoaiJFrame(username);
        dsL.setVisible(true);
        dsL.setTitle("Danh Sách Loại");

    }//GEN-LAST:event_jButton_QLLOAIActionPerformed

    private void jTextField_TimSanPhamKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_TimSanPhamKeyReleased
        // TODO add your handling code here:
        String timKiem = jTextField_TimSanPham.getText();
        boLoc(timKiem);
    }//GEN-LAST:event_jTextField_TimSanPhamKeyReleased

    private void jButton_RFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_RFActionPerformed
        // TODO add your handling code here:
        jComboBox_Loai.removeAllItems();
        jComboBox_Loai.addItem("-Chọn Loại-");
        loadComboboxLoai();
        loadData();
    }//GEN-LAST:event_jButton_RFActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_LamLai;
    private javax.swing.JButton jButton_QLLOAI;
    private javax.swing.JButton jButton_RF;
    private javax.swing.JButton jButton_SuaSanPham;
    private javax.swing.JButton jButton_TaoPhieuNhap;
    private javax.swing.JButton jButton_ThemSanPham;
    private javax.swing.JButton jButton_XoaSanPham;
    private javax.swing.JButton jButton_XuatBaoCao;
    private javax.swing.JComboBox jComboBox_Loai;
    private javax.swing.JComboBox jComboBox_Size;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_DonGia;
    private javax.swing.JLabel jLabel_Loai;
    private javax.swing.JLabel jLabel_MaSP;
    private javax.swing.JLabel jLabel_QLKho;
    private javax.swing.JLabel jLabel_SIZE;
    private javax.swing.JLabel jLabel_SL;
    private javax.swing.JLabel jLabel_SP;
    private javax.swing.JLabel jLabel_TenSP;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel_TK;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_DanhSachSP;
    private javax.swing.JTextField jTextField_DonGia;
    private javax.swing.JTextField jTextField_MaSanPham;
    private javax.swing.JTextField jTextField_SoLuong;
    private javax.swing.JTextField jTextField_TenSanPham;
    private jtextfieldround.JTextFieldRound jTextField_TimSanPham;
    // End of variables declaration//GEN-END:variables
}
