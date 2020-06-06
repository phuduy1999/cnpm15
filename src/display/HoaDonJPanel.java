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

    private boolean checkSoLuongTheoSize(String maSP, int size) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select SOLUONGTON from CHITIETGD where MASP=? and SIZE=?";
        boolean c = false;
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maSP);
            ps.setInt(2, size);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int sl = rs.getInt(1);
                if (sl > 0) {
                    c = true;
                }
            }
            ps.close();
            rs.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    private void layComboboxSize(String maSP) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select SIZE from CHITIETGD where MASP=? and TRANGTHAI=1";
        jComboBox_Size.removeAllItems();
        jComboBox_Size.addItem("-Size-");
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maSP);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (checkSoLuongTheoSize(maSP, rs.getInt(1))) {
                    jComboBox_Size.addItem(rs.getInt("SIZE"));
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

    private boolean laySize_TonTaiSP(String maSP) {
        boolean s = false;
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select SIZE from CHITIETGD where MASP=? and TRANGTHAI=1";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maSP);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                s = true;//ton tai size cua sp tren
            }
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
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
                if (laySize_TonTaiSP(rs.getString(1)) == true) {
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
        String sql = "select * from KHACHHANG where MAKH=? and TRANGTHAI=1";
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
        String sql = "select * from KHACHHANG where MAKH=? and TRANGTHAI=1";
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
        jTextFeild_TimKiemSanPham = new jtextfieldround.JTextFieldRound();
        jButton_RF = new javax.swing.JButton();
        jButton_tru = new javax.swing.JButton();
        jTextField_SoLuong = new javax.swing.JTextField();
        jButton_cong = new javax.swing.JButton();
        jButton_LS = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setForeground(new java.awt.Color(51, 51, 51));

        jLabel_HDMH.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel_HDMH.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_HDMH.setText("HÓA ĐƠN MUA HÀNG");

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        jLabel_TTHD.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel_TTHD.setForeground(new java.awt.Color(255, 153, 153));
        jLabel_TTHD.setText("Thông tin hóa đơn:");

        jTable_GioHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Size", "Đơn Giá", "Số lượng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
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

        jTextField_maKH.setBackground(new java.awt.Color(51, 51, 255));
        jTextField_maKH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_maKHKeyReleased(evt);
            }
        });

        jButton_XoaGH.setBackground(new java.awt.Color(255, 255, 255));
        jButton_XoaGH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cart (1).png"))); // NOI18N
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
                .addGap(49, 49, 49)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_ThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFeild_LuuHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFeild_XuatHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFeild_ThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));

        jLabel_DSSP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
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
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
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

        jTextFeild_TimKiemSanPham.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFeild_TimKiemSanPham.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFeild_TimKiemSanPhamKeyReleased(evt);
            }
        });

        jButton_RF.setBackground(new java.awt.Color(102, 102, 255));
        jButton_RF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/refresh (1).png"))); // NOI18N
        jButton_RF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_RFActionPerformed(evt);
            }
        });

        jButton_tru.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/minus.png"))); // NOI18N
        jButton_tru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_truActionPerformed(evt);
            }
        });

        jTextField_SoLuong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_SoLuongKeyReleased(evt);
            }
        });

        jButton_cong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/plus.png"))); // NOI18N
        jButton_cong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_congActionPerformed(evt);
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
                            .addComponent(jLabel_TenSP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                            .addComponent(jLabel_DonGia, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_MaSP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jTextFeild_DonGia))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFeild_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFeild_TenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel_Size, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox_Size, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jButton_tru, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel_SL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField_SoLuong, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_cong, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(56, 56, 56))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel_DSSP)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel_TK)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFeild_TimKiemSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton_RF, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel_DSSP, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel_TK)
                        .addComponent(jTextFeild_TimKiemSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton_RF))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFeild_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Size, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox_Size, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFeild_TenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_TenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_SL, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel_DonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFeild_DonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton_tru)
                    .addComponent(jTextField_SoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_cong))
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_LS)
                    .addComponent(jLabel_HDMH, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        jButton_XoaGH.setEnabled(false);
        jTextField_SoLuong.setText("");
    }//GEN-LAST:event_jTable_SanPhamMouseClicked

    private void jButton_ThemSanPhamHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ThemSanPhamHoaDonActionPerformed
        String maSP, tenSP;
        int donGia, size, sl = 0;
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
        String temp_sl = jTextField_SoLuong.getText();
        if (temp_sl.equals("")) {
            sl = -1;
        } else {
            sl = Integer.parseInt(temp_sl);
        }

        if (maSP.equals("") || tenSP.equals("") || donGia == -1) {
            JOptionPane.showMessageDialog(this, "Bạn cần chọn sản phẩm để thêm vào giỏ hàng!");
        } else if (size == -1) {
            JOptionPane.showMessageDialog(this, "Bạn cần chọn size sản phẩm hoặc sản phẩm đã hết hàng!");
        } else if (sl == -1) {
            JOptionPane.showMessageDialog(this, "Bạn cần chọn số lượng sản phẩm hoặc sản phẩm đã hết hàng!");
        } else if (sl == 0) {
            JOptionPane.showMessageDialog(this, "Bạn cần chọn số lượng sản phẩm lớn hơn 0");
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
        jTextField_SoLuong.setText(dtm.getValueAt(i, 5).toString());
        jButton_XoaGH.setEnabled(true);
    }//GEN-LAST:event_jTable_GioHangMouseClicked

    private void jComboBox_SizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_SizeActionPerformed
        if (jComboBox_Size.getSelectedItem() == null || jComboBox_Size.getSelectedItem().toString().equals("-Size-")) {
            jTextField_SoLuong.setText("");
        } else {
            jTextField_SoLuong.setText("0");
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
                        JOptionPane.showMessageDialog(this, "Mã khách hàng không tồn tại hoặc đã bị khóa!");
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
        jTextField_SoLuong.setText("");
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
        jTextField_SoLuong.setText("");
    }//GEN-LAST:event_jButton_XoaGHActionPerformed

    private void jTextField_maKHKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_maKHKeyReleased
        String t = jTextField_maKH.getText();
        jTextFieldRound_TenKH.setText(layTenKH(t));
    }//GEN-LAST:event_jTextField_maKHKeyReleased

    private void jButton_LSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_LSActionPerformed
        // TODO add your handling code here:
        DanhSachDonHang ls = new DanhSachDonHang();
        ls.setVisible(true);
        ls.setTitle("Danh sách đơn hàng");
    }//GEN-LAST:event_jButton_LSActionPerformed

    private void jButton_RFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_RFActionPerformed
        // TODO add your handling code here:
        layDanhSachSP();
        jTextFeild_MaSP.setText("");
        jTextFeild_TenSP.setText("");
        jTextFeild_DonGia.setText("");
        jComboBox_Size.removeAllItems();
        jComboBox_Size.addItem("-Size-");
        jTextField_SoLuong.setText("");
    }//GEN-LAST:event_jButton_RFActionPerformed

    private void jTextField_SoLuongKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_SoLuongKeyReleased
        if (jComboBox_Size.getSelectedItem() == null || jComboBox_Size.getSelectedItem().toString().equals("-Size-")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn size trước khi nhập số lượng");
            jTextField_SoLuong.setText("");
            return;
        }
        String maSP = jTextFeild_MaSP.getText();
        String soLuong = jTextField_SoLuong.getText();
        int size;
        if(soLuong.equals("")){
            return;
        }
        if (soLuong.matches("^[0-9]*$") == false) {
            JOptionPane.showMessageDialog(this, "Số lượng sản phẩm chỉ chứa ký tự số!");
            jTextField_SoLuong.setText("0");
        } else {
            size = Integer.parseInt(jComboBox_Size.getSelectedItem().toString());
            int sl = Integer.parseInt(soLuong);
            int slt = laySoLuongTon(maSP, size);
            if (sl > slt) {
                JOptionPane.showMessageDialog(this, "Sản phẩm " + maSP + " với size " + size + " chỉ còn " + slt + " sản phẩm!");
                jTextField_SoLuong.setText(Integer.toString(slt));
            }
        }
    }//GEN-LAST:event_jTextField_SoLuongKeyReleased

    private void jButton_truActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_truActionPerformed
        String soluong = jTextField_SoLuong.getText();
        if (!soluong.equals("")) {
            int sl = Integer.parseInt(jTextField_SoLuong.getText());
            if (sl > 0) {
                sl = sl - 1;
            }
            jTextField_SoLuong.setText(Integer.toString(sl));
        }
    }//GEN-LAST:event_jButton_truActionPerformed

    private void jButton_congActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_congActionPerformed
        String soluong = jTextField_SoLuong.getText();
        if (!soluong.equals("")) {
            String maSP = jTextFeild_MaSP.getText();
            int size = Integer.parseInt(jComboBox_Size.getSelectedItem().toString());
            int sl = Integer.parseInt(soluong);
            int slt = laySoLuongTon(maSP, size);
            if (sl < slt) {
                sl++;
            } else {
                JOptionPane.showMessageDialog(this, "Sản phẩm " + maSP + " với size " + size + " chỉ còn " + slt + " sản phẩm!");
            }
            jTextField_SoLuong.setText(Integer.toString(sl));
        }
    }//GEN-LAST:event_jButton_congActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_LS;
    private javax.swing.JButton jButton_RF;
    private javax.swing.JButton jButton_ThemSanPhamHoaDon;
    private javax.swing.JButton jButton_XoaGH;
    private javax.swing.JButton jButton_cong;
    private javax.swing.JButton jButton_tru;
    private javax.swing.JCheckBox jCheckBox_maKH;
    private javax.swing.JComboBox jComboBox_Size;
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
    private javax.swing.JTextField jTextField_SoLuong;
    private jtextfieldround.JTextFieldRound jTextField_maKH;
    // End of variables declaration//GEN-END:variables
}
