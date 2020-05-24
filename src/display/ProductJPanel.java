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
import model.ProductInCart;
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
public class ProductJPanel extends javax.swing.JPanel {

    String username;
    int maHoaDon = 0;//ma hoa don de xuat ra file pdf

    ArrayList<ProductInCart> ds = new ArrayList<ProductInCart>();
    ArrayList<ProductInCart> cart = new ArrayList<ProductInCart>();//danh sach gio hang tam thoi

    public ProductJPanel(String user) throws SQLException {
        initComponents();
        username = user;
        layDanhSachSP();

        txtMaSP.setEnabled(false);
        txtTenSP.setEnabled(false);
        txtDonGia.setEnabled(false);
        txtThanhTien.setEnabled(false);
        loadNgayThang();
        thanhToan();
        System.out.println(timNV(username));
    }

    private void thanhToan() {
        int tong = 0;
        for (ProductInCart x : cart) {
            tong += x.getDonGia() * x.getSoLuong();
        }
        txtThanhTien.setText(tong + "");
    }

    private void loadNgayThang() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        jDateChooser_Now.setDate(date);
        jDateChooser_Now.setEnabled(false);
    }

    private void layComboboxSize(String maSP) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select SIZE from CHITIETGD where MASP=?";
        jcbSize.removeAllItems();
        jcbSize.addItem("-Size-");
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maSP);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jcbSize.addItem(rs.getInt("SIZE"));
            }
            ps.close();
            rs.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void layComboboxSoLuong(String maSP, int size) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select SOLUONGTON from CHITIETGD where MASP=? and SIZE=?";
        jcbSoLuong.removeAllItems();
        jcbSoLuong.addItem("-SL-");
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maSP);
            ps.setInt(2, size);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int sl = rs.getInt(1);
                for (int i = 1; i <= sl; i++) {
                    jcbSoLuong.addItem(i);
                }
            }
            ps.close();
            rs.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductJPanel.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ProductJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return loai;
    }

    private void layDanhSachSP() {
        Connection ketNoi = KetNoi.layKetNoi();
        DefaultTableModel dtm = (DefaultTableModel) jtbSanPham.getModel();
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
                ProductInCart p = new ProductInCart(rs.getString(1), rs.getString(2), rs.getInt(3), tenLoai);
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
            jtbSanPham.setModel(dtm);
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadGioHang() {
        DefaultTableModel dtm = (DefaultTableModel) jtbHoaDon.getModel();
        Vector vt = null;
        dtm.setNumRows(0);
        int dem = 1;
        for (ProductInCart x : cart) {
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
        jtbHoaDon.setModel(dtm);
    }

    private int checkGioHang(String maSP, int size, int sl) {
        for (ProductInCart x : cart) {
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
            Logger.getLogger(ProductJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    private int timNV(String acc) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select MANV from NHANVIEN,ACCOUNT where NHANVIEN.USERNAME=ACCOUNT.USERNAME and NHANVIEN.USERNAME=?";
        int maNV = 0;
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, acc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                maNV = rs.getInt("MANV");
            }
            rs.close();
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return maNV;
    }

    private int luuHOADON(int tongHD, String date, String maKH, int maNV) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "insert into HOADON values (?,?,?,?)" + " "
                + "SELECT SCOPE_IDENTITY()"; //cau lenh lay ra id cuoi cung moi them vao
        int maHD = 0;
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setInt(1, tongHD);
            ps.setString(2, date);
            ps.setString(3, maKH);
            ps.setInt(4, maNV);
            ResultSet rs = ps.executeQuery();//them vao bang dong thoi lay ra id cuoi cung moi them vao
            while (rs.next()) {
                maHD = rs.getInt(1);
            }
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return maHD;
    }

    private void luuCHITIETHD(int maHD) {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "insert into CHITIETHD values (?,?,?,?)";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            for (ProductInCart x : cart) {
                ps.setInt(1, maHD);
                ps.setString(2, x.getMaSP());
                ps.setInt(3, x.getSize());
                ps.setInt(4, x.getSoLuong());
                ps.executeUpdate();
            }
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductJPanel.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ProductJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return slTon;
    }

    private void capNhatCTGD() {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "update CHITIETGD set SOLUONGTON=? where MASP=? and SIZE=?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            for (ProductInCart x : cart) {
                int newSLTon = laySoLuongTon(x.getMaSP(), x.getSize()) - x.getSoLuong();
                ps.setInt(1, newSLTon);
                ps.setString(2, x.getMaSP());
                ps.setInt(3, x.getSize());
                ps.executeUpdate();
            }
            ps.close();
            ketNoi.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductJPanel.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ProductJPanel.class.getName()).log(Level.SEVERE, null, ex);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtbHoaDon = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jBtnXuatHoaDon = new javax.swing.JButton();
        jBtnLuuHoaDon = new javax.swing.JButton();
        jDateChooser_Now = new com.toedter.calendar.JDateChooser();
        jCheckBox_maKH = new javax.swing.JCheckBox();
        txtThanhTien = new jtextfieldround.JTextFieldRound();
        jTextField_maKH = new jtextfieldround.JTextFieldRound();
        jButton_XoaGH = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbSanPham = new javax.swing.JTable();
        jBtnThemSanPhamHoaDon = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtMaSP = new javax.swing.JTextField();
        txtTenSP = new javax.swing.JTextField();
        txtDonGia = new javax.swing.JTextField();
        jcbSize = new javax.swing.JComboBox();
        jcbSoLuong = new javax.swing.JComboBox();
        txtTimKiemSanPham = new jtextfieldround.JTextFieldRound();

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setForeground(new java.awt.Color(51, 51, 51));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 153, 153));
        jLabel1.setText("HÓA ĐƠN MUA HÀNG");

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 153, 153));
        jLabel9.setText("Thông tin hóa đơn:");

        jtbHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Size", "Đơn Giá", "Số lượng"
            }
        ));
        jtbHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbHoaDonMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jtbHoaDon);

        jLabel10.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 51, 51));
        jLabel10.setText("Thành tiền:");

        jBtnXuatHoaDon.setBackground(new java.awt.Color(0, 0, 0));
        jBtnXuatHoaDon.setForeground(new java.awt.Color(255, 153, 153));
        jBtnXuatHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/bill (1).png"))); // NOI18N
        jBtnXuatHoaDon.setText("Xuất Hóa Đơn");
        jBtnXuatHoaDon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jBtnXuatHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnXuatHoaDonActionPerformed(evt);
            }
        });

        jBtnLuuHoaDon.setBackground(new java.awt.Color(0, 0, 0));
        jBtnLuuHoaDon.setForeground(new java.awt.Color(255, 153, 153));
        jBtnLuuHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/money (1).png"))); // NOI18N
        jBtnLuuHoaDon.setText("Thanh Toán");
        jBtnLuuHoaDon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jBtnLuuHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnLuuHoaDonActionPerformed(evt);
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

        txtThanhTien.setBackground(new java.awt.Color(255, 102, 0));

        jTextField_maKH.setBackground(new java.awt.Color(51, 51, 255));

        jButton_XoaGH.setBackground(new java.awt.Color(255, 255, 255));
        jButton_XoaGH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/shopping-cart.png"))); // NOI18N
        jButton_XoaGH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_XoaGHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_XoaGH)
                        .addGap(53, 53, 53)
                        .addComponent(jDateChooser_Now, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(txtThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jCheckBox_maKH, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField_maKH, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE)))
                        .addGap(43, 43, 43)
                        .addComponent(jBtnLuuHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnXuatHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addComponent(jDateChooser_Now, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton_XoaGH, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox_maKH)
                    .addComponent(jTextField_maKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnLuuHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnXuatHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 153, 153));
        jLabel15.setText("Danh sách sản phẩm");

        jLabel2.setForeground(new java.awt.Color(255, 153, 153));
        jLabel2.setText("Tìm kiếm");

        jtbSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên Sản Phẩm", "Đơn giá", "Loại sản phẩm"
            }
        ));
        jtbSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbSanPhamMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtbSanPham);

        jBtnThemSanPhamHoaDon.setBackground(new java.awt.Color(0, 0, 0));
        jBtnThemSanPhamHoaDon.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jBtnThemSanPhamHoaDon.setForeground(new java.awt.Color(255, 153, 153));
        jBtnThemSanPhamHoaDon.setText("Thêm sản phẩm trên vào hóa đơn");
        jBtnThemSanPhamHoaDon.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 153, 153), 3, true));
        jBtnThemSanPhamHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnThemSanPhamHoaDonActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 153, 153));
        jLabel3.setText("Mã sản phẩm");

        jLabel4.setForeground(new java.awt.Color(255, 153, 153));
        jLabel4.setText("Tên sản phẩm");

        jLabel5.setForeground(new java.awt.Color(255, 153, 153));
        jLabel5.setText("Đơn giá");

        jLabel6.setForeground(new java.awt.Color(255, 153, 153));
        jLabel6.setText("Size");

        jLabel7.setForeground(new java.awt.Color(255, 153, 153));
        jLabel7.setText("Số lượng");

        jcbSize.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-Size-" }));
        jcbSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbSizeActionPerformed(evt);
            }
        });

        jcbSoLuong.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-SL-" }));

        txtTimKiemSanPham.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemSanPhamKeyReleased(evt);
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
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txtDonGia))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcbSize, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcbSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTimKiemSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBtnThemSanPhamHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtTimKiemSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcbSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jcbSize, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24)
                .addComponent(jBtnThemSanPhamHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

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
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void jBtnXuatHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnXuatHoaDonActionPerformed
        if (maHoaDon == 0) {
            JOptionPane.showMessageDialog(this, "Hệ thống xác nhận chưa có hóa đơn kể từ lúc bạn đăng nhập!");
        } else {
            xuatHoaDon();
        }
    }//GEN-LAST:event_jBtnXuatHoaDonActionPerformed

    private void jtbSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbSanPhamMouseClicked
        int i = jtbSanPham.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) jtbSanPham.getModel();
        txtMaSP.setText(dtm.getValueAt(i, 1).toString());
        txtTenSP.setText(dtm.getValueAt(i, 2).toString());
        txtDonGia.setText(dtm.getValueAt(i, 3).toString());

        String maSP = txtMaSP.getText();
        layComboboxSize(maSP);
        jcbSoLuong.removeAllItems();
        jcbSoLuong.addItem("-SL-");
        jButton_XoaGH.setEnabled(false);
    }//GEN-LAST:event_jtbSanPhamMouseClicked

    private void jBtnThemSanPhamHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnThemSanPhamHoaDonActionPerformed
        String maSP, tenSP;
        int donGia, size, sl;
        maSP = txtMaSP.getText();
        tenSP = txtTenSP.getText();
        String temp = txtDonGia.getText().toString();
        if (!temp.equals("")) {
            donGia = Integer.parseInt(temp);
        } else {
            donGia = -1;
        }
        String temp_size = jcbSize.getSelectedItem().toString();
        if (temp_size.equals("-Size-")) {
            size = -1;
        } else {
            size = Integer.parseInt(temp_size);
        }
        String temp_sl = jcbSoLuong.getSelectedItem().toString();
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
                ProductInCart pc = new ProductInCart(maSP, tenSP, size, donGia, sl);
                cart.add(pc);
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
    }//GEN-LAST:event_jBtnThemSanPhamHoaDonActionPerformed

    private void jCheckBox_maKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_maKHActionPerformed
        if (jCheckBox_maKH.isSelected()) {
            jTextField_maKH.setText("");
            jTextField_maKH.setEnabled(true);
        } else {
            jTextField_maKH.setText("Khách Vãng Lai");
            jTextField_maKH.setEnabled(false);
        }
    }//GEN-LAST:event_jCheckBox_maKHActionPerformed

    private void jtbHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbHoaDonMouseClicked
        int i = jtbHoaDon.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) jtbHoaDon.getModel();
        txtMaSP.setText(dtm.getValueAt(i, 1).toString());
        txtTenSP.setText(dtm.getValueAt(i, 2).toString());
        txtDonGia.setText(dtm.getValueAt(i, 4).toString());
        jcbSize.removeAllItems();
        jcbSize.addItem(dtm.getValueAt(i, 3));
        jcbSize.setSelectedItem(dtm.getValueAt(i, 3));
        layComboboxSoLuong(dtm.getValueAt(i, 1).toString(), Integer.parseInt(dtm.getValueAt(i, 3).toString()));
        jcbSoLuong.setSelectedItem(dtm.getValueAt(i, 5));
        jButton_XoaGH.setEnabled(true);
    }//GEN-LAST:event_jtbHoaDonMouseClicked

    private void jcbSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbSizeActionPerformed
        //load so luong
        if (jcbSize.getSelectedItem() == null || jcbSize.getSelectedItem().toString().equals("-Size-")) {
            return;
        } else {
            String maSP = txtMaSP.getText();
            int size = Integer.parseInt(jcbSize.getSelectedItem().toString());
            layComboboxSoLuong(maSP, size);
        }
    }//GEN-LAST:event_jcbSizeActionPerformed

    private void jBtnLuuHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnLuuHoaDonActionPerformed
        String maKH = null;
        if (cart.isEmpty()) {
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
        String account = username;
        int maNV = timNV(account);
        System.out.println(maNV);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(jDateChooser_Now.getDate()); // Date->String
        int tongHD = Integer.parseInt(txtThanhTien.getText().toString());
        //them hoa don moi vao bang HOADON
        int maHD = luuHOADON(tongHD, date, maKH, maNV);
        //them vao CHITIETHD
        if (maHD != 0) {
            luuCHITIETHD(maHD);
        }
        maHoaDon = maHD;
        //cap nhat SOLUONGTON trong bang CHITIETGD
        capNhatCTGD();
        JOptionPane.showMessageDialog(this, "Cảm ơn quý khách. Hẹn gặp lại!!!");
        cart.clear();
        loadGioHang();
        //lam moi
        txtThanhTien.setText("0");
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtDonGia.setText("");
        jCheckBox_maKH.setSelected(true);
        jTextField_maKH.setText("");
        jcbSize.removeAllItems();
        jcbSize.addItem("-Size-");
        jcbSoLuong.removeAllItems();
        jcbSoLuong.addItem("-SL-");
    }//GEN-LAST:event_jBtnLuuHoaDonActionPerformed

    private void txtTimKiemSanPhamKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemSanPhamKeyReleased
        String f = txtTimKiemSanPham.getText();

        DefaultTableModel dtm = (DefaultTableModel) jtbSanPham.getModel();
        dtm.setNumRows(0);
        int dem = 1;
        for (ProductInCart x : ds) {
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
        jtbSanPham.setModel(dtm);
    }//GEN-LAST:event_txtTimKiemSanPhamKeyReleased

    private void jButton_XoaGHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_XoaGHActionPerformed
        String maSP=txtMaSP.getText();
        if(maSP.equals("")) return;
        int size=Integer.parseInt(jcbSize.getSelectedItem().toString());
        for(ProductInCart x:cart){
            if(x.getMaSP().equals(maSP)&&x.getSize()==size){
                cart.remove(x);
                break;
            }
        }
        loadGioHang();
        thanhToan();
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtDonGia.setText("");
        jCheckBox_maKH.setSelected(true);
        jTextField_maKH.setText("");
        jcbSize.removeAllItems();
        jcbSize.addItem("-Size-");
        jcbSoLuong.removeAllItems();
        jcbSoLuong.addItem("-SL-");
    }//GEN-LAST:event_jButton_XoaGHActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnLuuHoaDon;
    private javax.swing.JButton jBtnThemSanPhamHoaDon;
    private javax.swing.JButton jBtnXuatHoaDon;
    private javax.swing.JButton jButton_XoaGH;
    private javax.swing.JCheckBox jCheckBox_maKH;
    private com.toedter.calendar.JDateChooser jDateChooser_Now;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private jtextfieldround.JTextFieldRound jTextField_maKH;
    private javax.swing.JComboBox jcbSize;
    private javax.swing.JComboBox jcbSoLuong;
    private javax.swing.JTable jtbHoaDon;
    private javax.swing.JTable jtbSanPham;
    private javax.swing.JTextField txtDonGia;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextField txtTenSP;
    private jtextfieldround.JTextFieldRound txtThanhTien;
    private jtextfieldround.JTextFieldRound txtTimKiemSanPham;
    // End of variables declaration//GEN-END:variables
}
