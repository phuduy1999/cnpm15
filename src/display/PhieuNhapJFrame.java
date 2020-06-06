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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import display.DangNhap;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import javax.swing.ButtonGroup;
import model.SanPham;
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
public class PhieuNhapJFrame extends javax.swing.JFrame {

    /**
     * Creates new form PhieuNhapJFrame
     */
    Connection ketNoi = KetNoi.layKetNoi();
    ArrayList<SanPham> danhSachSP = new ArrayList<SanPham>();
    String username;

    public PhieuNhapJFrame(String user) {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        loadDataPhieuNhap();
        loadComboBoxMaSP();
        loadNgayThang();
        ButtonGroup phieuNhapGroup = new ButtonGroup();
        phieuNhapGroup.add(jRadioButton_TaoPhieuNhap);
        phieuNhapGroup.add(jRadioButton_XemLaiPhieuNhap);
        jTextField_MaPhieuNhap.setEnabled(false);
        this.username = user;
        jTextField_NhanVien.setText(this.username);
        jTextField_NhanVien.setEnabled(false);
        jDateChooser_NgayNhapHang.setEnabled(false);
        jTable_PhieuNhap.setEnabled(false);
        jTable_ChiTietPhieuNhap.setEnabled(false);
        jComboBox_MaSP.setEnabled(false);
        jComboBox_Size.setEnabled(false);
        jTextField_SoLuong.setEnabled(false);
        jButton_LuuCSDL.setEnabled(false);
        jButton_XuatPhieu.setEnabled(false);
        jButton_ThemGiay.setEnabled(false);
        jButton_SuaGiay.setEnabled(false);
        jButton_XoaGiay.setEnabled(false);
        jButton_LamMoiGiay.setEnabled(false);
    }

    private void loadNgayThang() {
        Calendar cal = Calendar.getInstance();
        Date ngay = cal.getTime();
        jDateChooser_NgayNhapHang.setDate(ngay);
    }

    private void loadComboboxSize(String maSP) {
        try {
            PreparedStatement ps = ketNoi.prepareStatement("select CHITIETGD.SIZE"
                    + " from CHITIETGD where CHITIETGD.MASP = '" + maSP + "'"
                    + " and CHITIETGD.TRANGTHAI = 1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jComboBox_Size.addItem(rs.getString("SIZE"));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.err.println("Bi loi khi load ComboBox Size ");
        }
    }

    private void loadComboBoxMaSP() {
        try {
            PreparedStatement ps = ketNoi.prepareStatement("Select DISTINCT Masp from CHITIETGD where TRANGTHAI = '1'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jComboBox_MaSP.addItem(rs.getString("masp"));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.err.println("Bi loi khi load Sanr phaamr");
        }
    }

    private void loadDataPhieuNhap() {
        try {
            DefaultTableModel tbn = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                }
            };
            int number;
            Vector row, column;
            column = new Vector();
            Statement st = ketNoi.createStatement();
            ResultSet rs = st.executeQuery("select DISTINCT CTPN.MAPN, PHIEUNHAP.NGAYNHAP, NHANVIEN.HOTEN\n"
                    + "                    FROM PHIEUNHAP\n"
                    + "                    INNER JOIN NHANVIEN on PHIEUNHAP.MANV = NHANVIEN.MANV\n"
                    + "                    INNER JOIN CTPN on PHIEUNHAP.MAPN = CTPN.MAPN");
            ResultSetMetaData metadata = rs.getMetaData();
            number = metadata.getColumnCount(); //tra ve so cot
            for (int i = 1; i <= number; i++) {
                column.add(metadata.getColumnName(i)); // Lay ra tieu de cua cac cot
            }
            tbn.setColumnIdentifiers(column);

            while (rs.next()) {
                row = new Vector();
                for (int i = 1; i <= number; i++) {
                    row.addElement(rs.getString(i));
                }
                tbn.addRow(row);
                jTable_PhieuNhap.setModel(tbn);
            }
            rs.close();
        } catch (Exception e) {
            System.err.println("Loi khi Load Data Phieu nhap");
        }
        TablePhieuNhap();
    }

    private void TablePhieuNhap() {
        // Ham tra gia tri tu bang len textField
        jTable_PhieuNhap.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (jTable_PhieuNhap.getSelectedRow() >= 0) {
                    loadDataChiTietPhieuNhap(jTextField_MaPhieuNhap.getText());
                    jTextField_MaPhieuNhap.setText(jTable_PhieuNhap.getValueAt(jTable_PhieuNhap.getSelectedRow(), 0) + "");
                }
            }
        }
        );
        // ket thuc ham tra gia tri tu bang len textField
    }

    private void loadDataChiTietPhieuNhap(String maPhieuNhap) {
        try {
            String sql = "";
            sql = "select DISTINCT PHIEUNHAP.MAPN, GIAYDEP.MASP, CHITIETGD.SIZE, CTPN.SOLUONG\n"
                    + "FRom PHIEUNHAP, GIAYDEP, CHITIETGD, CTPN \n"
                    + "where PHIEUNHAP.MAPN = CTPN.MAPN and GIAYDEP.MASP = CTPN.MASP and CHITIETGD.SIZE = CTPN.SIZE and CTPN.MAPN ='" + maPhieuNhap + "'";

            Statement st = ketNoi.createStatement();
            ResultSet rs = st.executeQuery(sql);

            Object[] obj = new Object[]{"STT", "Mã Phiếu nhập", "Mã Sản phẩm", "Size", "Số Lượng",};
            DefaultTableModel tableModel = new DefaultTableModel(obj, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                }
            };
            jTable_ChiTietPhieuNhap.setModel(tableModel);
            int c = 0;
            try {
                while (rs.next()) {
                    Object[] item = new Object[7];
                    c++;
                    item[0] = c;
                    item[1] = rs.getString("MAPN");
                    item[2] = rs.getString("MASP");
                    item[3] = rs.getInt("SIZE");
                    item[4] = rs.getInt("Soluong");
                    tableModel.addRow(item);
                }
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }

        } catch (Exception e) {
            System.err.println("Loi khi Load Data Chi tiet phieu nhap");
        }

        TableChiTietPhieuNhap();
    }

    private void TableChiTietPhieuNhap() {
        // Ham tra gia tri tu bang len textField
        jTable_ChiTietPhieuNhap.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (jTable_ChiTietPhieuNhap.getSelectedRow() >= 0) {
                    jTextField_SoLuong.setText(jTable_ChiTietPhieuNhap.getValueAt(jTable_ChiTietPhieuNhap.getSelectedRow(), 2) + "");
                    jComboBox_MaSP.setSelectedItem(jTable_ChiTietPhieuNhap.getValueAt(jTable_ChiTietPhieuNhap.getSelectedRow(), 1) + "");
                    jComboBox_Size.setSelectedItem(jTable_ChiTietPhieuNhap.getModel().getValueAt(jTable_ChiTietPhieuNhap.getSelectedRow(), 3) + "");
                }
            }
        });
        // ket thuc ham tra gia tri tu bang len textField

    }

    private String LayMaNhanVien() {
        String maNV = "";
        try {
            String sql = "Select NHANVIEN.MANV \n"
                    + "from NHANVIEN\n"
                    + "where NHANVIEN.USERNAME = '" + username + "'";
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                maNV = rs.getString("MANV");
            }
            System.out.println("Ma Nhan Vien : " + maNV);
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.err.println("Bi loi khi lay ma nhan vien trong tao phieu nhap");
        }
        return maNV;
    }

    private String LayMaPhieuNhap() {
        String maPN = "";
        try {
            String sql = "SELECT MAX(MAPN)\n"
                    + "  FROM PHIEUNHAP";
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                maPN = rs.getString(1);
            }
            System.out.println("Ma PHIeuNhap : " + maPN);
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.toString();
        }
        return maPN;
    }

    private int LaySoLuongPhieuNhap() {
        String soMaPN = "";
        int soPN = 0;
        try {
            String sql = "SELECT COUNT(MAPN)\n"
                    + "FROM PHIEUNHAP";
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                soMaPN = rs.getString(1);
            }
            System.out.println("Số lượng PN : " + soMaPN);
            soPN = Integer.parseInt(soMaPN);
        } catch (Exception e) {
            e.toString();
        }
        return soPN;
    }

    private String GetSoLuongTrongKho(String maSP, String size) {
        String soLuongTrongKho = "";
        String sql = "select CHITIETGD.SOLUONGTON from CHITIETGD\n"
                + "where CHITIETGD.MASP = '" + maSP + "' and CHITIETGD.SIZE = '" + size + "'";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                soLuongTrongKho = rs.getString("SOLUONGTON");
            }
        } catch (Exception e) {
            e.toString();
        }
        return soLuongTrongKho;
    }

    private void XuatPhieuNhap(String maPN) {
        try {
            Hashtable map = new Hashtable();
            JasperReport report = JasperCompileManager.compileReport("src/display/rpPhieuNhap.jrxml");
            map.put("MAPN", maPN);
            System.out.println("Truy cập hàm xuất phiếu nhập với tham số mã phiếu nhập : " + maPN);
            JasperPrint p = JasperFillManager.fillReport(report, map, ketNoi);
            JasperViewer.viewReport(p, false);
        } catch (Exception ex) {
            ex.toString();
        }
    }

    private void loadChiTietPhieuNhapKhiThem() {
        DefaultTableModel dtm = (DefaultTableModel) jTable_ChiTietPhieuNhap.getModel();

        Object[] obj = new Object[]{"STT", "Mã Sản phẩm", "Số lượng", "Size"};
        dtm = new DefaultTableModel(obj, 0);
        jTable_ChiTietPhieuNhap.setModel(dtm);

        Vector vt;
        dtm.setNumRows(0);
        int dem = 1;
        for (SanPham x : danhSachSP) {
            vt = new Vector();
            vt.add(dem);
            vt.add(x.getMaSP());
            vt.add(x.getSoLuong());
            vt.add(x.getSize());
            dem++;
            dtm.addRow(vt);
        }
        jTable_ChiTietPhieuNhap.setModel(dtm);
    }

    private int checkChiTietPhieuNhap(String maSP, int size) {
        for (SanPham x : danhSachSP) {
            if (x.getMaSP().equals(maSP) && x.getSize() == size) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel_NhapHang = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel_MaSP = new javax.swing.JLabel();
        jLabel_NVNhap = new javax.swing.JLabel();
        jLabel_SL = new javax.swing.JLabel();
        jLabel_SIZE = new javax.swing.JLabel();
        jTextField_SoLuong = new javax.swing.JTextField();
        jComboBox_Size = new javax.swing.JComboBox();
        jDateChooser_NgayNhapHang = new com.toedter.calendar.JDateChooser();
        jLabel_NgayNhap = new javax.swing.JLabel();
        jButton_ThemGiay = new javax.swing.JButton();
        jButton_XoaGiay = new javax.swing.JButton();
        jButton_LamMoiGiay = new javax.swing.JButton();
        jButton_SuaGiay = new javax.swing.JButton();
        jLabel_ThemCTPN = new javax.swing.JLabel();
        jLabel_MaPN = new javax.swing.JLabel();
        jComboBox_MaSP = new javax.swing.JComboBox();
        jTextField_NhanVien = new javax.swing.JTextField();
        jTextField_MaPhieuNhap = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel_PN = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_PhieuNhap = new javax.swing.JTable();
        jButton_LuuCSDL = new javax.swing.JButton();
        jButton_XuatPhieu = new javax.swing.JButton();
        jLabel_CTPN = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_ChiTietPhieuNhap = new javax.swing.JTable();
        jRadioButton_TaoPhieuNhap = new javax.swing.JRadioButton();
        jRadioButton_XemLaiPhieuNhap = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel_NhapHang.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel_NhapHang.setText("NHẬP HÀNG MỚI");

        jLabel_MaSP.setText("Mã Sản Phẩm:");

        jLabel_NVNhap.setText("Nhân Viên Nhập");

        jLabel_SL.setText("Số Lượng:");

        jLabel_SIZE.setText("Size:");

        jTextField_SoLuong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_SoLuongKeyReleased(evt);
            }
        });

        jComboBox_Size.setToolTipText("");

        jDateChooser_NgayNhapHang.setDateFormatString("yyyy-MM-dd");

        jLabel_NgayNhap.setText("Ngày nhập hàng");

        jButton_ThemGiay.setText("Thêm Sản Phẩm");
        jButton_ThemGiay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ThemGiayActionPerformed(evt);
            }
        });

        jButton_XoaGiay.setText("Xóa Sản Phẩm");
        jButton_XoaGiay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_XoaGiayActionPerformed(evt);
            }
        });

        jButton_LamMoiGiay.setText("Làm Mới");
        jButton_LamMoiGiay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_LamMoiGiayActionPerformed(evt);
            }
        });

        jButton_SuaGiay.setText("Sửa Sản Phẩm");
        jButton_SuaGiay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SuaGiayActionPerformed(evt);
            }
        });

        jLabel_ThemCTPN.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel_ThemCTPN.setText("Thêm vào chi tiết phiếu nhập");

        jLabel_MaPN.setText("Mã Phiếu Nhập");

        jComboBox_MaSP.setToolTipText("-Chọn Mã Sản Phẩm-");
        jComboBox_MaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_MaSPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_SIZE, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_SL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_MaSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_MaPN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField_MaPhieuNhap)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField_SoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox_Size, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 32, Short.MAX_VALUE))
                            .addComponent(jComboBox_MaSP, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_ThemCTPN)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton_XoaGiay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton_ThemGiay))
                                .addGap(37, 37, 37)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton_SuaGiay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton_LamMoiGiay, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 39, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_NgayNhap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_NVNhap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField_NhanVien)
                            .addComponent(jDateChooser_NgayNhapHang, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser_NgayNhapHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel_NgayNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_NVNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_ThemCTPN, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel_MaPN, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField_MaPhieuNhap)
                        .addGap(6, 6, 6)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel_MaSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_SL, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_SoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox_Size, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_SuaGiay)
                    .addComponent(jButton_ThemGiay))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_XoaGiay)
                    .addComponent(jButton_LamMoiGiay))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel_PN.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel_PN.setText("DANH SÁCH PHIẾU NHẬP");

        jTable_PhieuNhap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Số lượng", "Size", "Loại", "Ngày nhập hàng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable_PhieuNhap);

        jButton_LuuCSDL.setText("Lưu vào CSDL");
        jButton_LuuCSDL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_LuuCSDLActionPerformed(evt);
            }
        });

        jButton_XuatPhieu.setText("Xuất phiếu nhập");
        jButton_XuatPhieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_XuatPhieuActionPerformed(evt);
            }
        });

        jLabel_CTPN.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel_CTPN.setText("CHI TIẾT PHIẾU NHẬP");

        jTable_ChiTietPhieuNhap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jTable_ChiTietPhieuNhap);

        jRadioButton_TaoPhieuNhap.setText("Tạo phiếu nhập mới");
        jRadioButton_TaoPhieuNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_TaoPhieuNhapActionPerformed(evt);
            }
        });

        jRadioButton_XemLaiPhieuNhap.setText("Xem lại phiếu nhập cũ");
        jRadioButton_XemLaiPhieuNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_XemLaiPhieuNhapActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel_PN)
                .addGap(39, 39, 39)
                .addComponent(jRadioButton_TaoPhieuNhap)
                .addGap(18, 18, 18)
                .addComponent(jRadioButton_XemLaiPhieuNhap)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 532, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_CTPN)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 532, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton_LuuCSDL, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_XuatPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(102, 102, 102))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_PN)
                    .addComponent(jRadioButton_TaoPhieuNhap)
                    .addComponent(jRadioButton_XemLaiPhieuNhap))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_CTPN, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_LuuCSDL, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_XuatPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_NhapHang, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_NhapHang)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_ThemGiayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ThemGiayActionPerformed
        // TODO add your handling code here:
        String maPN, maSP, size, soLuong;
        maPN = jTextField_MaPhieuNhap.getText();
        maSP = jComboBox_MaSP.getSelectedItem().toString();
        soLuong = jTextField_SoLuong.getText();
        size = jComboBox_Size.getSelectedItem().toString();

        int sizeTemp = Integer.parseInt(size);

        if (soLuong.matches("\\D") == true) {
            JOptionPane.showMessageDialog(this, "Số lượng chỉ có thể chưa kí tự số");
        } else if (maSP.equals("-Chọn Mã Sản Phẩm-")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn mã sản phẩm");
        } else if (soLuong.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Số lượng");
        } else if (size.equals("-Chọn Size-")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn size sản phẩm");
        } else if (checkChiTietPhieuNhap(maSP, sizeTemp) == 0) {
            try {
                int soLuongTemp = Integer.parseInt(soLuong);
                if (soLuongTemp <= 0) {
                    JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0");
                } else {
                    SanPham pd = new SanPham(maSP, soLuongTemp, sizeTemp);
                    danhSachSP.add(pd);
                    loadChiTietPhieuNhapKhiThem();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Kiểm tra lại số lượng nhập vào chỉ là số");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm và size bạn vừa nhập hiện đã tồn tại!"
                    + " Vui lòng trọng mã sản phẩm và size khác hoặc click nút Sửa để cập nhật số lượng sản phẩm");
        }
    }//GEN-LAST:event_jButton_ThemGiayActionPerformed

    private void jButton_LamMoiGiayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_LamMoiGiayActionPerformed
        // TODO add your handling code here:
        jComboBox_MaSP.setSelectedIndex(0);
        jComboBox_Size.setSelectedIndex(0);
        jTextField_SoLuong.setText("");
    }//GEN-LAST:event_jButton_LamMoiGiayActionPerformed

    private void jButton_XoaGiayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_XoaGiayActionPerformed
        // TODO add your handling code here:
        try {
            String maSP, size;
            maSP = jComboBox_MaSP.getSelectedItem().toString();
            size = jComboBox_Size.getSelectedItem().toString();
            int sizeTemp = Integer.parseInt(size);

            for (SanPham x : danhSachSP) {
                if (x.getMaSP().equals(maSP) && x.getSize() == sizeTemp) {
                    Object[] options = {"Đồng ý", "Hủy"};
                    int chon = JOptionPane.showOptionDialog(this, "Bạn có chắc muốn xóa sản phẩm này không?",
                            "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (chon == JOptionPane.YES_OPTION) {
                        danhSachSP.remove(x);
                        JOptionPane.showMessageDialog(this, "Xóa thành công");
                        loadChiTietPhieuNhapKhiThem();
                    }
                }
            }
        } catch (Exception e) {

        }
    }//GEN-LAST:event_jButton_XoaGiayActionPerformed

    private void jButton_LuuCSDLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_LuuCSDLActionPerformed
        // TODO add your handling code here:
        if (jTable_ChiTietPhieuNhap.getRowCount() > 0) {
///////////////////////Tạo mới phiếu nhập////////////////////////
            //Lay ra maPN cuối cùng trong SQL
            String Chuoi = LayMaPhieuNhap();
            int count = LaySoLuongPhieuNhap();

            System.out.println("MAPN = " + Chuoi + "Count = " + count);

            String chuoiTemp1;
            int chuoiSTT1;
            chuoiTemp1 = Chuoi.replace("PN", "");
            chuoiSTT1 = Integer.parseInt(chuoiTemp1);
            if (chuoiSTT1 < 9) {
                jTextField_MaPhieuNhap.setText("PN0" + String.valueOf(count + 1));
            } else if (chuoiSTT1 >= 9 & chuoiSTT1 < 1000) {
                jTextField_MaPhieuNhap.setText("PN" + String.valueOf(count + 1));
            }

            String date = "";
            if (jDateChooser_NgayNhapHang.getDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.format(jDateChooser_NgayNhapHang.getDate()); // Date->String
            };

            String maNV = LayMaNhanVien();

            try {
                String sql = "insert into PHIEUNHAP values (?,?,?)";
                PreparedStatement ps1 = ketNoi.prepareStatement(sql);
                ps1.setString(1, jTextField_MaPhieuNhap.getText());
                ps1.setString(2, date);
                ps1.setString(3, maNV);
                ps1.executeUpdate();
                JOptionPane.showMessageDialog(this, "Đã tạo mới phiếu nhập!");
            } catch (Exception e) {
                e.toString();
            }
//////////////////////////////////////////////////////////////

            String maSP = "", size = "", soluong = "";
            for (int i = 0; i < jTable_ChiTietPhieuNhap.getRowCount(); i++) {
                maSP = (String) jTable_ChiTietPhieuNhap.getValueAt(i, 1);
                size = (String.valueOf(jTable_ChiTietPhieuNhap.getValueAt(i, 3)));
                soluong = String.valueOf(jTable_ChiTietPhieuNhap.getValueAt(i, 2));
                System.out.println(maSP);
                System.out.println(size);
                System.out.println("So luong nhap vao la : " + soluong);

////////////////////them du lieu vao chi tiet phieu nhap////////////////
                try {
                    String sql = "insert into CTPN values (?,?,?,?)";
                    PreparedStatement ps1 = ketNoi.prepareStatement(sql);
                    for (SanPham x : danhSachSP) {
                        ps1.setString(1, jTextField_MaPhieuNhap.getText());
                        ps1.setString(2, maSP);
                        ps1.setString(3, size);
                        ps1.setString(4, soluong);
                        ps1.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(this, "Đã thêm sản phẩm vào phiếu nhập mới!");
                } catch (Exception e) {
                    e.toString();
                }
///////////////////////////////////////////////////////////////////////            

/////////////////////update lai so luong cho chi tiet giay dep////////////  
                String soLuongTon = GetSoLuongTrongKho(maSP, size);
                System.out.println("So luong ton kho la : " + soLuongTon);

                int soLuongTemp = Integer.parseInt(soluong);
                int soLuongTonTemp = Integer.parseInt(soLuongTon);

                int soLuongSauKhiNhap = soLuongTemp + soLuongTonTemp;
                System.out.println("So luong sau khi nhap hang la : " + soLuongSauKhiNhap);
                try {
                    String sql = "update CHITIETGD set SOLUONGTON = '" + soLuongSauKhiNhap + "' \n"
                            + "where MASP = '" + maSP + "' and SIZE = '" + size + "'";
                    PreparedStatement ps1 = ketNoi.prepareStatement(sql);
                    ps1.executeUpdate();
                    ps1.close();
                    JOptionPane.showMessageDialog(this, "Đã cập nhật số lượng trong CSDL thành công!!");
                } catch (Exception e1) {
                    e1.toString();
                }
            }
            /////////////////////////////////////////////////////////////////////////
            ////////////////////Làm mới ArrayList///////////////////////////////////
            danhSachSP.clear();
            loadChiTietPhieuNhapKhiThem();
            /////////////////////////////////////////////////////////////////////////
            loadDataPhieuNhap();
        } else {
            JOptionPane.showMessageDialog(this, "Phiếu nhập hiện đang rỗng, xin vui lòng nhập sản phẩm mới cho phiếu nhập");
        }
    }//GEN-LAST:event_jButton_LuuCSDLActionPerformed

    private void jButton_SuaGiayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SuaGiayActionPerformed
        // TODO add your handling code here:
        String maSP, size, soLuong;
        maSP = jComboBox_MaSP.getSelectedItem().toString();
        soLuong = jTextField_SoLuong.getText();
        size = jComboBox_Size.getSelectedItem().toString();
        int sizeTemp = Integer.parseInt(size);
        if (jTable_ChiTietPhieuNhap.getRowCount() > 0) {
            for (SanPham x : danhSachSP) {
                if (soLuong.matches("\\D") == true) {
                    JOptionPane.showMessageDialog(this, "Số lượng chỉ có thể chưa kí tự số");
                } else if (x.getMaSP().equals(maSP) && x.getSize() == sizeTemp) {
                    try {
                        int soLuongTemp = Integer.parseInt(soLuong);
                        if (soLuongTemp <= 0) {
                            JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0");
                        } else {
                            x.setSoLuong(soLuongTemp);
                            JOptionPane.showMessageDialog(this, "Sửa thành công");
                            loadChiTietPhieuNhapKhiThem();
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Kiểm tra lại số lượng chỉ nhận kí tự số");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Mã sản phẩm và size bạn muốn sửa hiện không tồn tại!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không có sản phẩm nào hiện có để sửa");
        }
    }//GEN-LAST:event_jButton_SuaGiayActionPerformed

    private void jComboBox_MaSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_MaSPActionPerformed
        // TODO add your handling code here:
        try {
            jComboBox_Size.removeAllItems();
            String maSP = jComboBox_MaSP.getSelectedItem().toString();
            loadComboboxSize(maSP);
        } catch (Exception e) {
            e.toString();
        }
    }//GEN-LAST:event_jComboBox_MaSPActionPerformed

    private void jButton_XuatPhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_XuatPhieuActionPerformed
        // TODO add your handling code here:
        String maPN = jTextField_MaPhieuNhap.getText();
        XuatPhieuNhap(maPN);
        System.out.println("maPN = " + maPN);
    }//GEN-LAST:event_jButton_XuatPhieuActionPerformed

    private void jRadioButton_TaoPhieuNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_TaoPhieuNhapActionPerformed
        // TODO add your handling code here:
        loadChiTietPhieuNhapKhiThem();
        jTable_PhieuNhap.setEnabled(false);
        jTable_ChiTietPhieuNhap.setEnabled(true);
        jButton_LuuCSDL.setEnabled(true);
        jComboBox_MaSP.setEnabled(true);
        jComboBox_Size.setEnabled(true);
        jTextField_SoLuong.setEnabled(true);
        jButton_ThemGiay.setEnabled(true);
        jButton_SuaGiay.setEnabled(true);
        jButton_XoaGiay.setEnabled(true);
        jButton_LamMoiGiay.setEnabled(true);
        jButton_XuatPhieu.setEnabled(true);
    }//GEN-LAST:event_jRadioButton_TaoPhieuNhapActionPerformed

    private void jRadioButton_XemLaiPhieuNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_XemLaiPhieuNhapActionPerformed
        // TODO add your handling code here:
        jTable_PhieuNhap.setEnabled(true);
        jTable_ChiTietPhieuNhap.setEnabled(false);
        jButton_LuuCSDL.setEnabled(false);
        jComboBox_MaSP.setEnabled(false);
        jComboBox_Size.setEnabled(false);
        jTextField_SoLuong.setEnabled(false);
        jButton_ThemGiay.setEnabled(false);
        jButton_SuaGiay.setEnabled(false);
        jButton_XoaGiay.setEnabled(false);
        jButton_LamMoiGiay.setEnabled(false);
        jButton_XuatPhieu.setEnabled(true);
    }//GEN-LAST:event_jRadioButton_XemLaiPhieuNhapActionPerformed

    private void jTextField_SoLuongKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_SoLuongKeyReleased
        // TODO add your handling code here:
        String t = jTextField_SoLuong.getText();
        if (t.matches("\\D") == true) {
            JOptionPane.showMessageDialog(this, "Số lượng chỉ có thể chưa kí tự số");
            jTextField_SoLuong.setText("");
        }
    }//GEN-LAST:event_jTextField_SoLuongKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_LamMoiGiay;
    private javax.swing.JButton jButton_LuuCSDL;
    private javax.swing.JButton jButton_SuaGiay;
    private javax.swing.JButton jButton_ThemGiay;
    private javax.swing.JButton jButton_XoaGiay;
    private javax.swing.JButton jButton_XuatPhieu;
    private javax.swing.JComboBox jComboBox_MaSP;
    private javax.swing.JComboBox jComboBox_Size;
    private com.toedter.calendar.JDateChooser jDateChooser_NgayNhapHang;
    private javax.swing.JLabel jLabel_CTPN;
    private javax.swing.JLabel jLabel_MaPN;
    private javax.swing.JLabel jLabel_MaSP;
    private javax.swing.JLabel jLabel_NVNhap;
    private javax.swing.JLabel jLabel_NgayNhap;
    private javax.swing.JLabel jLabel_NhapHang;
    private javax.swing.JLabel jLabel_PN;
    private javax.swing.JLabel jLabel_SIZE;
    private javax.swing.JLabel jLabel_SL;
    private javax.swing.JLabel jLabel_ThemCTPN;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton_TaoPhieuNhap;
    private javax.swing.JRadioButton jRadioButton_XemLaiPhieuNhap;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable_ChiTietPhieuNhap;
    private javax.swing.JTable jTable_PhieuNhap;
    private javax.swing.JTextField jTextField_MaPhieuNhap;
    private javax.swing.JTextField jTextField_NhanVien;
    private javax.swing.JTextField jTextField_SoLuong;
    // End of variables declaration//GEN-END:variables
}
