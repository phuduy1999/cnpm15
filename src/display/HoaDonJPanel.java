/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import Conection.KetNoi;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import model.SanPham;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Admin
 */
public class HoaDonJPanel extends javax.swing.JPanel {

    String username;
    int maHoaDon = 0;//ma hoa don de xuat ra file pdf

    ArrayList<SanPham> ds = new ArrayList<SanPham>(); //danh sach tat ca san pham
    ArrayList<SanPham> gioHang = new ArrayList<SanPham>();//danh sach gio hang tam thoi

    public HoaDonJPanel(String user) throws SQLException {
        initComponents();
        username = user;
        layDanhSachSP();

        jTextFeild_MaSP.setEnabled(false);
        jTextFeild_TenSP.setEnabled(false);
        jTextFeild_DonGia.setEnabled(false);
        jTextFeild_ThanhTien.setEnabled(false);
        jTextFieldRound_TenKH.setEnabled(false);
        loadNgayThang();
        thanhToan();
    }

    private void thanhToan() {
        int tong = 0;
        for (SanPham x : gioHang) {
            tong += x.getDonGia() * x.getSoLuong();
        }
        jTextFeild_ThanhTien.setText(tong + "");
    }

    private void loadNgayThang() {
        Calendar cal = Calendar.getInstance();
        Date ngay = cal.getTime();
        jDateChooser_Now.setDate(ngay);
        jDateChooser_Now.setEnabled(false);
    }

    private void layComboboxSize(String maSP) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select SIZE from CHITIETGD where MASP=?";
        jComboBox_Size.removeAllItems();
        jComboBox_Size.addItem("-Size-");
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maSP);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jComboBox_Size.addItem(rs.getInt("SIZE"));
            }
            ps.close();
            rs.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void layComboboxSoLuong(String maSP, int size) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select SOLUONGTON from CHITIETGD where MASP=? and SIZE=?";
        jComboBox_SoLuong.removeAllItems();
        jComboBox_SoLuong.addItem("-SL-");
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maSP);
            ps.setInt(2, size);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int sl = rs.getInt(1);
                for (int i = 1; i <= sl; i++) {
                    jComboBox_SoLuong.addItem(i);
                }
            }
            ps.close();
            rs.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String layTenLoaiSP(String maLoai) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select LOAI from LOAIGD where MALOAI=?";
        String loai = "";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maLoai);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                loai = rs.getString(1);
            }
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return loai;
    }

    private void layDanhSachSP() {
        Connection ketNoi = KetNoi.layKetNoi();
        DefaultTableModel dtm = (DefaultTableModel) jTable_SanPham.getModel();
        dtm.setNumRows(0);
        String sql = "select * from GIAYDEP";
        Vector vt;
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ds.clear();
            int stt = 1;
            while (rs.next()) {
                String tenLoai = layTenLoaiSP(rs.getString(4));
                SanPham p = new SanPham(rs.getString(1), rs.getString(2), rs.getInt(3), tenLoai);
                ds.add(p);
                vt = new Vector();
                vt.add(stt);
                vt.add(rs.getString(1));
                vt.add(rs.getString(2));
                vt.add(rs.getInt(3));
                vt.add(tenLoai);
                dtm.addRow(vt);
                stt++;
            }
            jTable_SanPham.setModel(dtm);
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadGioHang() {
        DefaultTableModel dtm = (DefaultTableModel) jTable_GioHang.getModel();
        Vector vt = null;
        dtm.setNumRows(0);
        int dem = 1;
        for (SanPham x : gioHang) {
            vt = new Vector();
            vt.add(dem);
            vt.add(x.getMaSP());
            vt.add(x.getTenSP());
            vt.add(x.getSize());
            vt.add(x.getDonGia());
            vt.add(x.getSoLuong());
            dem++;
            dtm.addRow(vt);
        }
        jTable_GioHang.setModel(dtm);
    }

    private int checkGioHang(String maSP, int size, int sl) {
        for (SanPham x : gioHang) {
            if (x.getMaSP().equals(maSP) && x.getSize() == size) {
                if (x.getSoLuong() == sl) {
                    return 1;//trung gio hang
                } else {
                    x.setSoLuong(sl); // cap nhat so luong moi cho san pham trong gio hang
                    return 2;//thay doi so luong san pham
                }
            }
        }
        return 0;
    }

    private int checkMaKH(String maKH) {
        Connection ketNoi = KetNoi.layKetNoi();
        int c = 0;
        String sql = "select * from KHACHHANG where MAKH=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maKH);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                c = 1;//ton tai makh
            }
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    private String layTenKH(String maKH) {
        Connection ketNoi = KetNoi.layKetNoi();
        String c = "";
        String sql = "select * from KHACHHANG where MAKH=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maKH);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                c = rs.getString("HOTEN");
            }
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    private int timNV() {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select MANV from NHANVIEN,ACCOUNT where NHANVIEN.USERNAME=ACCOUNT.USERNAME and NHANVIEN.USERNAME=?";
        int maNV = 0;
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                maNV = rs.getInt("MANV");
            }
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return maNV;
    }

    private int luuHOADON(int tongHD, String ngayBan, String maKH, int maNV) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "insert into HOADON values (?,?,?,?)" + " "
                + "SELECT SCOPE_IDENTITY()"; //cau lenh lay ra id cuoi cung moi them vao
        int maHD = 0;
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setInt(1, tongHD);
            ps.setString(2, ngayBan);
            ps.setString(3, maKH);
            ps.setInt(4, maNV);
            ResultSet rs = ps.executeQuery();//them vao bang dong thoi lay ra id cuoi cung moi them vao
            while (rs.next()) {
                maHD = rs.getInt(1);
            }
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return maHD;
    }

    private void luuCHITIETHD(int maHD) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "insert into CHITIETHD values (?,?,?,?)";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            for (SanPham x : gioHang) {
                ps.setInt(1, maHD);
                ps.setString(2, x.getMaSP());
                ps.setInt(3, x.getSize());
                ps.setInt(4, x.getSoLuong());
                ps.executeUpdate();
            }
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int laySoLuongTon(String maSP, int size) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select SOLUONGTON from CHITIETGD where MASP=? and SIZE=?";
        int slTon = -1;
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maSP);
            ps.setInt(2, size);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                slTon = rs.getInt(1); //lay so luong ton
            }
            ps.close();
            rs.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return slTon;
    }

    private void capNhatCTGD() {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "update CHITIETGD set SOLUONGTON=? where MASP=? and SIZE=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            for (SanPham x : gioHang) {
                int newSLTon = laySoLuongTon(x.getMaSP(), x.getSize()) - x.getSoLuong();
                ps.setInt(1, newSLTon);
                ps.setString(2, x.getMaSP());
                ps.setInt(3, x.getSize());
                ps.executeUpdate();
            }
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void xuatHoaDon() {
        Connection ketNoi = KetNoi.layKetNoi();
        Hashtable map = new Hashtable();
        try {
            JasperReport report = JasperCompileManager.compileReport("src/display/rpHoaDon.jrxml");
            map.put("MAHD", maHoaDon);
            JasperPrint p = JasperFillManager.fillReport(report, map, ketNoi);
            JasperViewer.viewReport(p, false);
        } catch (JRException ex) {
            Logger.getLogger(HoaDonJPanel.class.getName()).log(Level.SEVERE, null, ex);
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
        jLabel_HDMH = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel_TTHD = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_GioHang = new javax.swing.JTable();
        jLabel_ThanhTien = new javax.swing.JLabel();
        jTextFeild_XuatHoaDon = new javax.swing.JButton();
        jTextFeild_LuuHoaDon = new javax.swing.JButton();
        jDateChooser_Now = new com.toedter.calendar.JDateChooser();
        jCheckBox_maKH = new javax.swing.JCheckBox();
        jTextFeild_ThanhTien = new jtextfieldround.JTextFieldRound();
        jTextField_maKH = new jtextfieldround.JTextFieldRound();
        jButton_XoaGH = new javax.swing.JButton();
        jTextFieldRound_TenKH = new jtextfieldround.JTextFieldRound();
        jPanel4 = new javax.swing.JPanel();
        jLabel_DSSP = new javax.swing.JLabel();
        jLabel_TK = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_SanPham = new javax.swing.JTable();
        jButton_ThemSanPhamHoaDon = new javax.swing.JButton();
        jLabel_MaSP = new javax.swing.JLabel();
        jLabel_TenSP = new javax.swing.JLabel();
        jLabel_DonGia = new javax.swing.JLabel();
        jLabel_Size = new javax.swing.JLabel();
        jLabel_SL = new javax.swing.JLabel();
        jTextFeild_MaSP = new javax.swing.JTextField();
        jTextFeild_TenSP = new javax.swing.JTextField();
        jTextFeild_DonGia = new javax.swing.JTextField();
        jComboBox_Size = new javax.swing.JComboBox();
        jComboBox_SoLuong = new javax.swing.JComboBox();
        jTextFeild_TimKiemSanPham = new jtextfieldround.JTextFieldRound();
        jButton_LS = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setForeground(new java.awt.Color(51, 51, 51));

        jLabel_HDMH.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel_HDMH.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_HDMH.setText("HÓA ĐƠN MUA HÀNG");

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        jLabel_TTHD.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel_TTHD.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_TTHD.setText("Thông tin hóa đơn:");

        jTable_GioHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Size", "Đơn Giá", "Số lượng"
            }
        ));
        jTable_GioHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_GioHangMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable_GioHang);

        jLabel_ThanhTien.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel_ThanhTien.setForeground(new java.awt.Color(255, 51, 51));
        jLabel_ThanhTien.setText("Thành tiền:");

        jTextFeild_XuatHoaDon.setBackground(new java.awt.Color(0, 0, 0));
        jTextFeild_XuatHoaDon.setForeground(new java.awt.Color(255, 153, 153));
        jTextFeild_XuatHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/bill (1).png"))); // NOI18N
        jTextFeild_XuatHoaDon.setText("Xuất Hóa Đơn");
        jTextFeild_XuatHoaDon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jTextFeild_XuatHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFeild_XuatHoaDonActionPerformed(evt);
            }
        });

        jTextFeild_LuuHoaDon.setBackground(new java.awt.Color(0, 0, 0));
        jTextFeild_LuuHoaDon.setForeground(new java.awt.Color(255, 153, 153));
        jTextFeild_LuuHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/money (1).png"))); // NOI18N
        jTextFeild_LuuHoaDon.setText("Thanh Toán");
        jTextFeild_LuuHoaDon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jTextFeild_LuuHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFeild_LuuHoaDonActionPerformed(evt);
            }
        });

        jDateChooser_Now.setBackground(new java.awt.Color(255, 153, 153));
        jDateChooser_Now.setForeground(new java.awt.Color(255, 153, 153));

        jCheckBox_maKH.setBackground(new java.awt.Color(0, 0, 0));
        jCheckBox_maKH.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jCheckBox_maKH.setForeground(new java.awt.Color(255, 153, 153));
        jCheckBox_maKH.setSelected(true);
        jCheckBox_maKH.setText("Có mã KH");
        jCheckBox_maKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_maKHActionPerformed(evt);
            }
        });

        jTextFeild_ThanhTien.setBackground(new java.awt.Color(255, 102, 0));

        jTextField_maKH.setBackground(new java.awt.Color(51, 51, 255));
        jTextField_maKH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_maKHKeyReleased(evt);
            }
        });

        jButton_XoaGH.setBackground(new java.awt.Color(255, 255, 255));
        jButton_XoaGH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/shopping-cart.png"))); // NOI18N
        jButton_XoaGH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_XoaGHActionPerformed(evt);
            }
        });

        jTextFieldRound_TenKH.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel_TTHD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_XoaGH)
                        .addGap(53, 53, 53)
                        .addComponent(jDateChooser_Now, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel_ThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(jTextFeild_ThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jCheckBox_maKH, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField_maKH, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jTextFeild_LuuHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFeild_XuatHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextFieldRound_TenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel_TTHD, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addComponent(jDateChooser_Now, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton_XoaGH, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox_maKH)
                    .addComponent(jTextField_maKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldRound_TenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_ThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFeild_LuuHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFeild_XuatHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFeild_ThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));

        jLabel_DSSP.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel_DSSP.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_DSSP.setText("Danh sách sản phẩm");

        jLabel_TK.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_TK.setText("Tìm kiếm");

        jTable_SanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên Sản Phẩm", "Đơn giá", "Loại sản phẩm"
            }
        ));
        jTable_SanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_SanPhamMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable_SanPham);

        jButton_ThemSanPhamHoaDon.setBackground(new java.awt.Color(0, 0, 0));
        jButton_ThemSanPhamHoaDon.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton_ThemSanPhamHoaDon.setForeground(new java.awt.Color(255, 153, 153));
        jButton_ThemSanPhamHoaDon.setText("Thêm sản phẩm trên vào hóa đơn");
        jButton_ThemSanPhamHoaDon.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 153, 153), 3, true));
        jButton_ThemSanPhamHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ThemSanPhamHoaDonActionPerformed(evt);
            }
        });

        jLabel_MaSP.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_MaSP.setText("Mã sản phẩm");

        jLabel_TenSP.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_TenSP.setText("Tên sản phẩm");

        jLabel_DonGia.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_DonGia.setText("Đơn giá");

        jLabel_Size.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_Size.setText("Size");

        jLabel_SL.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_SL.setText("Số lượng");

        jComboBox_Size.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-Size-" }));
        jComboBox_Size.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_SizeActionPerformed(evt);
            }
        });

        jComboBox_SoLuong.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-SL-" }));

        jTextFeild_TimKiemSanPham.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFeild_TimKiemSanPham.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFeild_TimKiemSanPhamKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel_MaSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_TenSP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                            .addComponent(jLabel_DonGia, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jTextFeild_DonGia))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFeild_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFeild_TenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel_Size, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_SL, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox_Size, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox_SoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel_DSSP)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel_TK)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFeild_TimKiemSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton_ThemSanPhamHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_DSSP, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_TK)
                    .addComponent(jTextFeild_TimKiemSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFeild_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFeild_TenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_TenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_DonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFeild_DonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_SL, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_Size, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox_Size, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_SoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24)
                .addComponent(jButton_ThemSanPhamHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton_LS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/health-report.png"))); // NOI18N
        jButton_LS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_LSActionPerformed(evt);
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
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(16, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel_HDMH)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_LS, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_HDMH, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_LS))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFeild_XuatHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFeild_XuatHoaDonActionPerformed
        if (maHoaDon == 0) {
            JOptionPane.showMessageDialog(this, "Hệ thống xác nhận chưa có hóa đơn kể từ lúc bạn đăng nhập!");
        } else {
            xuatHoaDon();
        }
    }//GEN-LAST:event_jTextFeild_XuatHoaDonActionPerformed

    private void jTable_SanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_SanPhamMouseClicked
        int i = jTable_SanPham.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) jTable_SanPham.getModel();
        jTextFeild_MaSP.setText(dtm.getValueAt(i, 1).toString());
        jTextFeild_TenSP.setText(dtm.getValueAt(i, 2).toString());
        jTextFeild_DonGia.setText(dtm.getValueAt(i, 3).toString());

        String maSP = jTextFeild_MaSP.getText();
        layComboboxSize(maSP);
        jComboBox_SoLuong.removeAllItems();
        jComboBox_SoLuong.addItem("-SL-");
        jButton_XoaGH.setEnabled(false);
    }//GEN-LAST:event_jTable_SanPhamMouseClicked

    private void jButton_ThemSanPhamHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ThemSanPhamHoaDonActionPerformed
        String maSP, tenSP;
        int donGia, size, sl;
        maSP = jTextFeild_MaSP.getText();
        tenSP = jTextFeild_TenSP.getText();
        String temp = jTextFeild_DonGia.getText().toString();
        if (!temp.equals("")) {
            donGia = Integer.parseInt(temp);
        } else {
            donGia = -1;
        }
        String temp_size = jComboBox_Size.getSelectedItem().toString();
        if (temp_size.equals("-Size-")) {
            size = -1;
        } else {
            size = Integer.parseInt(temp_size);
        }
        String temp_sl = jComboBox_SoLuong.getSelectedItem().toString();
        if (temp_sl.equals("-SL-")) {
            sl = -1;
        } else {
            sl = Integer.parseInt(temp_sl);
        }

        if (maSP.equals("") || tenSP.equals("") || donGia == -1) {
            JOptionPane.showMessageDialog(this, "Bạn cần chọn sản phẩm để thêm vào giỏ hàng!");
            return;
        } else if (size == -1) {
            JOptionPane.showMessageDialog(this, "Bạn cần chọn size sản phẩm hoặc sản phẩm đã hết hàng!");
            return;
        } else if (sl == -1) {
            JOptionPane.showMessageDialog(this, "Bạn cần chọn số lượng sản phẩm hoặc sản phẩm đã hết hàng!");
            return;
        } else {
            int c = checkGioHang(maSP, size, sl);
            if (c == 0) {
                SanPham pc = new SanPham(maSP, tenSP, size, donGia, sl);
                gioHang.add(pc);
                loadGioHang();
                thanhToan();
            } else if (c == 2) {//update soluong
                JOptionPane.showMessageDialog(this, "Bạn vừa cập nhật số lượng của sản phẩm '" + tenSP + "'!");
                loadGioHang(); //da setSL trong ham checkGioHang
                thanhToan();
            } else {
                JOptionPane.showMessageDialog(this, "Sản phẩm đã tồn tại trong giỏ hàng, bạn có thể cập nhật lại số lượng mới!");
                return;
            }
        }
    }//GEN-LAST:event_jButton_ThemSanPhamHoaDonActionPerformed

    private void jCheckBox_maKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_maKHActionPerformed
        if (jCheckBox_maKH.isSelected()) {
            jTextField_maKH.setText("");
            jTextField_maKH.setEnabled(true);
        } else {
            jTextField_maKH.setText("Khách Vãng Lai");
            jTextField_maKH.setEnabled(false);
            jTextFieldRound_TenKH.setText("");
        }
    }//GEN-LAST:event_jCheckBox_maKHActionPerformed

    private void jTable_GioHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_GioHangMouseClicked
        int i = jTable_GioHang.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) jTable_GioHang.getModel();
        jTextFeild_MaSP.setText(dtm.getValueAt(i, 1).toString());
        jTextFeild_TenSP.setText(dtm.getValueAt(i, 2).toString());
        jTextFeild_DonGia.setText(dtm.getValueAt(i, 4).toString());
        jComboBox_Size.removeAllItems();
        jComboBox_Size.addItem(dtm.getValueAt(i, 3));
        jComboBox_Size.setSelectedItem(dtm.getValueAt(i, 3));
        layComboboxSoLuong(dtm.getValueAt(i, 1).toString(), Integer.parseInt(dtm.getValueAt(i, 3).toString()));
        jComboBox_SoLuong.setSelectedItem(dtm.getValueAt(i, 5));
        jButton_XoaGH.setEnabled(true);
    }//GEN-LAST:event_jTable_GioHangMouseClicked

    private void jComboBox_SizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_SizeActionPerformed
        //load so luong
        if (jComboBox_Size.getSelectedItem() == null || jComboBox_Size.getSelectedItem().toString().equals("-Size-")) {
            return;
        } else {
            String maSP = jTextFeild_MaSP.getText();
            int size = Integer.parseInt(jComboBox_Size.getSelectedItem().toString());
            layComboboxSoLuong(maSP, size);
        }
    }//GEN-LAST:event_jComboBox_SizeActionPerformed

    private void jTextFeild_LuuHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFeild_LuuHoaDonActionPerformed
        String maKH = null;
        if (gioHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng rỗng, vui lòng chọn sản phẩm!");
            return;
        } else {
            if (jCheckBox_maKH.isSelected()) {
                String tmp = jTextField_maKH.getText();
                if (tmp.equals("")) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập mã khách hàng!");
                    return;
                } else {
                    maKH = jTextField_maKH.getText();
                    if (checkMaKH(maKH) == 0) {
                        JOptionPane.showMessageDialog(this, "Mã khách hàng không tồn tại!");
                        return;
                    }
                }
            } else {
                maKH = "KVL";
            }
        }
        int maNV = timNV();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ngayBan = sdf.format(jDateChooser_Now.getDate()); // Date->String
        int tongHD = Integer.parseInt(jTextFeild_ThanhTien.getText().toString());
        //them hoa don moi vao bang HOADON
        int maHD = luuHOADON(tongHD, ngayBan, maKH, maNV);
        //them vao CHITIETHD
        if (maHD != 0) {
            luuCHITIETHD(maHD);
        }
        maHoaDon = maHD; //de luu lai hoa don vua moi thanh toan de xuat ra hoa don cho khach
        //cap nhat SOLUONGTON trong bang CHITIETGD
        capNhatCTGD();
        JOptionPane.showMessageDialog(this, "Cảm ơn quý khách. Hẹn gặp lại!!!");
        gioHang.clear();
        loadGioHang();
        //lam moi
        jTextFeild_ThanhTien.setText("0");
        jTextFeild_MaSP.setText("");
        jTextFeild_TenSP.setText("");
        jTextFeild_DonGia.setText("");
        jCheckBox_maKH.setSelected(true);
        jTextField_maKH.setText("");
        jComboBox_Size.removeAllItems();
        jComboBox_Size.addItem("-Size-");
        jComboBox_SoLuong.removeAllItems();
        jComboBox_SoLuong.addItem("-SL-");
        xuatHoaDon();
    }//GEN-LAST:event_jTextFeild_LuuHoaDonActionPerformed

    private void jTextFeild_TimKiemSanPhamKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFeild_TimKiemSanPhamKeyReleased
        String f = jTextFeild_TimKiemSanPham.getText();

        DefaultTableModel dtm = (DefaultTableModel) jTable_SanPham.getModel();
        dtm.setNumRows(0);
        int dem = 1;
        for (SanPham x : ds) {
            String ten = x.getTenSP().toLowerCase();
            String ma = x.getMaSP().toLowerCase();
            String loai = x.getLoaiSP().toLowerCase();
            if (ten.contains(f.toLowerCase()) || ma.contains(f.toLowerCase()) || loai.contains(f.toLowerCase())) {
                Vector vt = new Vector();
                vt.add(dem);
                vt.add(x.getMaSP());
                vt.add(x.getTenSP());
                vt.add(x.getDonGia());
                vt.add(x.getLoaiSP());
                dtm.addRow(vt);
                dem++;
            }
        }
        jTable_SanPham.setModel(dtm);
    }//GEN-LAST:event_jTextFeild_TimKiemSanPhamKeyReleased

    private void jButton_XoaGHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_XoaGHActionPerformed
        String maSP = jTextFeild_MaSP.getText();
        if (maSP.equals("")) {
            return;
        }
        int size = Integer.parseInt(jComboBox_Size.getSelectedItem().toString());
        for (SanPham x : gioHang) {
            if (x.getMaSP().equals(maSP) && x.getSize() == size) {
                gioHang.remove(x);
                break;
            }
        }
        loadGioHang();
        thanhToan();
        jTextFeild_MaSP.setText("");
        jTextFeild_TenSP.setText("");
        jTextFeild_DonGia.setText("");
        jCheckBox_maKH.setSelected(true);
        jTextField_maKH.setText("");
        jComboBox_Size.removeAllItems();
        jComboBox_Size.addItem("-Size-");
        jComboBox_SoLuong.removeAllItems();
        jComboBox_SoLuong.addItem("-SL-");
    }//GEN-LAST:event_jButton_XoaGHActionPerformed

    private void jTextField_maKHKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_maKHKeyReleased
        String t = jTextField_maKH.getText();
        jTextFieldRound_TenKH.setText(layTenKH(t));
    }//GEN-LAST:event_jTextField_maKHKeyReleased

    private void jButton_LSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_LSActionPerformed
        // TODO add your handling code here:
        LichSuHD ls=new LichSuHD();
        ls.setVisible(true);
        ls.setTitle("Lịch sử đơn hàng");
    }//GEN-LAST:event_jButton_LSActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_LS;
    private javax.swing.JButton jButton_ThemSanPhamHoaDon;
    private javax.swing.JButton jButton_XoaGH;
    private javax.swing.JCheckBox jCheckBox_maKH;
    private javax.swing.JComboBox jComboBox_Size;
    private javax.swing.JComboBox jComboBox_SoLuong;
    private com.toedter.calendar.JDateChooser jDateChooser_Now;
    private javax.swing.JLabel jLabel_DSSP;
    private javax.swing.JLabel jLabel_DonGia;
    private javax.swing.JLabel jLabel_HDMH;
    private javax.swing.JLabel jLabel_MaSP;
    private javax.swing.JLabel jLabel_SL;
    private javax.swing.JLabel jLabel_Size;
    private javax.swing.JLabel jLabel_TK;
    private javax.swing.JLabel jLabel_TTHD;
    private javax.swing.JLabel jLabel_TenSP;
    private javax.swing.JLabel jLabel_ThanhTien;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable_GioHang;
    private javax.swing.JTable jTable_SanPham;
    private javax.swing.JTextField jTextFeild_DonGia;
    private javax.swing.JButton jTextFeild_LuuHoaDon;
    private javax.swing.JTextField jTextFeild_MaSP;
    private javax.swing.JTextField jTextFeild_TenSP;
    private jtextfieldround.JTextFieldRound jTextFeild_ThanhTien;
    private jtextfieldround.JTextFieldRound jTextFeild_TimKiemSanPham;
    private javax.swing.JButton jTextFeild_XuatHoaDon;
    private jtextfieldround.JTextFieldRound jTextFieldRound_TenKH;
    private jtextfieldround.JTextFieldRound jTextField_maKH;
    // End of variables declaration//GEN-END:variables
}
