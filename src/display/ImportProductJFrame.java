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
import display.Login;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import javax.swing.ButtonGroup;
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

class Product {

    private String maSP;
    private int soLuong;
    private int size;

    public Product(String maSP, int soLuong, int size) {
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.size = size;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}

public class ImportProductJFrame extends javax.swing.JFrame {

    /**
     * Creates new form ImportProductJFrame
     */
    KetNoi a = new KetNoi();
    Connection conn = a.layKetNoi();
    ArrayList<Product> listProducts = new ArrayList<Product>();
    String userName;
    public ImportProductJFrame(String user) {

        initComponents();
        String maPhieuNhap;
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        //loadComboboxSize();
        loadDataPhieuNhap();
        loadComboBoxMaSP();
        loadNgayThang();
        ButtonGroup phieuNhapGroup = new ButtonGroup();
        phieuNhapGroup.add(jRBTaoPhieuNhap);
        phieuNhapGroup.add(jRBXemLaiPhieuNhap);
        txtMaPhieuNhap1.setEnabled(false);
        this.userName = user;
        txtNhanVien.setText(this.userName);
        txtNhanVien.setEnabled(false);
        jDateNhapHang.setEnabled(false);
        jtbPhieuNhap.setEnabled(false);
        jtbChiTietPhieuNhap.setEnabled(false);
        jcbMaSP.setEnabled(false);
        jcbSize.setEnabled(false);
        txtSoLuong.setEnabled(false);
        jBtnLuuCSDL.setEnabled(false);
        jBtnXuatPhieu.setEnabled(false);
        jBtnThemGiay.setEnabled(false);
        jBtnSuaGiay.setEnabled(false);
        jBtnXoaGiay.setEnabled(false);
        jBtnLamMoiGiay.setEnabled(false);
    }

    public void loadNgayThang() {

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        jDateNhapHang.setDate(date);

    }

    public void loadComboboxSize(String maSP) {
        try {
            PreparedStatement ps = conn.prepareStatement("select CHITIETGD.SIZE"
                    + " from CHITIETGD where CHITIETGD.MASP = '" + maSP + "'"
                    + " and CHITIETGD.TRANGTHAI = 1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jcbSize.addItem(rs.getString("SIZE"));
            }
        } catch (Exception e) {
            System.err.println("Bi loi khi load ComboBox Size ");
        }
    }

    public void loadComboBoxMaSP() {
        try {
            PreparedStatement ps = conn.prepareStatement("Select DISTINCT Masp from CHITIETGD where TRANGTHAI = '1'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jcbMaSP.addItem(rs.getString("masp"));

            }

        } catch (Exception e) {
            System.err.println("Bi loi khi load Sanr phaamr");
        }
    }

    public void loadDataPhieuNhap() {
        try {
            DefaultTableModel tbn = new DefaultTableModel();
            int number;
            Vector row, column;
            column = new Vector();
            Statement st = conn.createStatement();
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
                jtbPhieuNhap.setModel(tbn);
            }
        } catch (Exception e) {
            System.err.println("Loi khi Load Data Phieu nhap");
        }
        TablePhieuNhap();
    }

    public void TablePhieuNhap() {

        // Ham tra gia tri tu bang len textField
        jtbPhieuNhap.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (jtbPhieuNhap.getSelectedRow() >= 0) {
                    //txtMaPhieuNhap.setText(jtbPhieuNhap.getValueAt(jtbPhieuNhap.getSelectedRow(), 0) + "");

                    //jDateNhapHang.getDate(jtbPhieuNhap.getModel().getValueAt(jtbPhieuNhap.getSelectedRow(), 1 + "");
                    //txtNhanVien.setText(jtbPhieuNhap.getModel().getValueAt(jtbPhieuNhap.getSelectedRow(), 2) + "");
                    loadDataChiTietPhieuNhap(txtMaPhieuNhap1.getText());
                    txtMaPhieuNhap1.setText(jtbPhieuNhap.getValueAt(jtbPhieuNhap.getSelectedRow(), 0) + "");
                }
            }
        }
        );
        // ket thuc ham tra gia tri tu bang len textField
    }

    public void loadDataChiTietPhieuNhap(String maPhieuNhap) {
        try {
            DefaultTableModel tbn = new DefaultTableModel(); 
            String sql = "";
            sql = "select DISTINCT PHIEUNHAP.MAPN, GIAYDEP.MASP, CHITIETGD.SIZE, CTPN.SOLUONG\n"
                    + "FRom PHIEUNHAP, GIAYDEP, CHITIETGD, CTPN \n"
                    + "where PHIEUNHAP.MAPN = CTPN.MAPN and GIAYDEP.MASP = CTPN.MASP and CHITIETGD.SIZE = CTPN.SIZE and CTPN.MAPN ='" + maPhieuNhap + "'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            Object[] obj = new Object[]{"STT", "Mã Phiếu nhập", "Mã Sản phẩm", "Size", "Số Lượng",};
            DefaultTableModel tableModel = new DefaultTableModel(obj, 0);
            jtbChiTietPhieuNhap.setModel(tableModel);
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

    public void TableChiTietPhieuNhap() {

        // Ham tra gia tri tu bang len textField
        jtbChiTietPhieuNhap.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (jtbChiTietPhieuNhap.getSelectedRow() >= 0) {
                    txtSoLuong.setText(jtbChiTietPhieuNhap.getValueAt(jtbChiTietPhieuNhap.getSelectedRow(), 2) + "");
                    jcbMaSP.setSelectedItem(jtbChiTietPhieuNhap.getValueAt(jtbChiTietPhieuNhap.getSelectedRow(), 1) + "");
                    jcbSize.setSelectedItem(jtbChiTietPhieuNhap.getModel().getValueAt(jtbChiTietPhieuNhap.getSelectedRow(), 3) + "");

                }
            }

        });
        // ket thuc ham tra gia tri tu bang len textField

    }

    public String LayMaNhanVien() {
        String maNV = "";

        try {
           
            String sql = "Select NHANVIEN.MANV \n"
                    + "from NHANVIEN\n"
                    + "where NHANVIEN.USERNAME = '" + userName + "'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                maNV = rs.getString("MANV");

            }
            System.out.println("Ma Nhan Vien : " + maNV);

        } catch (Exception e) {
            System.err.println("Bi loi khi lay ma nhan vien trong tao phieu nhap");
        }
        return maNV;
    }

    public String LayMaPhieuNhap() {
        String maPN = "";

        try {
            String sql = "SELECT MAX(MAPN)\n"
                    + "  FROM PHIEUNHAP";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                maPN = rs.getString(1);

            }
            System.out.println("Ma PHIeuNhap : " + maPN);

        } catch (Exception e) {
            e.toString();
        }
        return maPN;
    }

    public int LaySoLuongPhieuNhap() {
        String soMaPN = "";
        int soPN = 0;
        try {
            String sql = "SELECT COUNT(MAPN)\n"
                    + "FROM PHIEUNHAP";
            PreparedStatement ps = conn.prepareStatement(sql);
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

    public void ThemChiTietPhieuNhap() {
        String sqlChiTietPN = "insert into CTPN values(?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sqlChiTietPN);
            ps.setString(1, txtMaPhieuNhap1.getText());
            ps.setString(2, (String) jcbMaSP.getSelectedItem());
            ps.setString(4, txtSoLuong.getText());
            ps.setString(3, (String) jcbSize.getSelectedItem());
            ps.execute();
            loadDataChiTietPhieuNhap(txtMaPhieuNhap1.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Sản phẩm vừa nhập chưa được tạo, vui lòng tạo sản phẩm trước khi thêm số lượng!");
        }
    }

    public void SuaChiTietPhieuNhap() {
        String maPN, maSP, size, soLuong;
        maPN = txtMaPhieuNhap1.getText();
        maSP = jcbMaSP.getSelectedItem().toString();
        soLuong = txtSoLuong.getText();
        size = jcbSize.getSelectedItem().toString();
        String sql = "update CTPN set SOLUONG = '" + soLuong + "' \n"
                + "where MASP = '" + maSP + "' and SIZE = '" + size + "' and MAPN = '" + maPN + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            loadDataChiTietPhieuNhap(txtMaPhieuNhap1.getText());
        } catch (Exception e) {
            System.err.println("Lỗi tại hàm SuaChiTietPhieuNhap");
        }

    }

    public void XoaSanPhamOfChiTietPN() {
        String maPN, maSP, size, soLuong;
        maPN = txtMaPhieuNhap1.getText();
        maSP = jcbMaSP.getSelectedItem().toString();
        //soLuong = txtSoLuong.getText();
        size = jcbSize.getSelectedItem().toString();

        String sql = "delete CTPN Where MAPN = '" + maPN + "' and MASP = '" + maSP + "' and SIZE ='" + size + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            //ps.setString(1, jtbChiTietPhieuNhap.getValueAt(jtbChiTietPhieuNhap.getSelectedRow(), 0).toString());
            ps.execute();
            loadDataChiTietPhieuNhap(txtMaPhieuNhap1.getText());
        } catch (Exception e) {
            System.err.println("Loi tại hàm xóa sản phẩm của ChiTietPhieuNhap");
        }
    }

    public String GetSoLuongTrongKho(String maSP, String size) {
        String soLuongTrongKho = "";
        String sql = "select CHITIETGD.SOLUONGTON from CHITIETGD\n"
                + "where CHITIETGD.MASP = '" + maSP + "' and CHITIETGD.SIZE = '" + size + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                soLuongTrongKho = rs.getString("SOLUONGTON");

            }

        } catch (Exception e) {
            e.toString();
        }
        return soLuongTrongKho;
    }

    public void XuatPhieuNhap(String maPN) {
        try {
            Hashtable map = new Hashtable();
            JasperReport report = JasperCompileManager.compileReport("src/display/rpPhieuNhap.jrxml");

            map.put("MAPN", maPN);
            System.out.println("Truy cập hàm xuất phiếu nhập với tham số mã phiếu nhập : " + maPN);
            JasperPrint p = JasperFillManager.fillReport(report, map, conn);
            JasperViewer.viewReport(p, false);
            JasperExportManager.exportReportToPdfFile(p, "PhieuNhap.pdf");
        } catch (Exception ex) {
            ex.toString();
        }
    }

    private void TaoMoiPN() {
        DefaultTableModel tbModel = (DefaultTableModel) jtbChiTietPhieuNhap.getModel();
        tbModel.setRowCount(0);
    }

    private void loadChiTietPhieuNhapKhiThem() {
        DefaultTableModel dtm = (DefaultTableModel) jtbChiTietPhieuNhap.getModel();

        Object[] obj = new Object[]{"STT", "Mã Sản phẩm", "Số lượng", "Size"};
        dtm = new DefaultTableModel(obj, 0);
        jtbChiTietPhieuNhap.setModel(dtm);

        Vector vt;
        dtm.setNumRows(0);
        int dem = 1;
        for (Product x : listProducts) {
            vt = new Vector();
            vt.add(dem);
            vt.add(x.getMaSP());
            vt.add(x.getSoLuong());
            vt.add(x.getSize());
            dem++;
            dtm.addRow(vt);
        }
        jtbChiTietPhieuNhap.setModel(dtm);
    }

    private int checkChiTietPhieuNhap(String maSP, int size) {
        for (Product x : listProducts) {
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

        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        jcbSize = new javax.swing.JComboBox();
        jDateNhapHang = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jBtnThemGiay = new javax.swing.JButton();
        jBtnXoaGiay = new javax.swing.JButton();
        jBtnLamMoiGiay = new javax.swing.JButton();
        jBtnSuaGiay = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jcbMaSP = new javax.swing.JComboBox();
        txtNhanVien = new javax.swing.JTextField();
        txtMaPhieuNhap1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbPhieuNhap = new javax.swing.JTable();
        jBtnLuuCSDL = new javax.swing.JButton();
        jBtnXuatPhieu = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtbChiTietPhieuNhap = new javax.swing.JTable();
        jRBTaoPhieuNhap = new javax.swing.JRadioButton();
        jRBXemLaiPhieuNhap = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("NHẬP HÀNG MỚI");

        jLabel2.setText("Mã Sản Phẩm:");

        jLabel4.setText("Nhân Viên Nhập");

        jLabel5.setText("Số Lượng:");

        jLabel6.setText("Size:");

        jcbSize.setToolTipText("");
        jcbSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbSizeActionPerformed(evt);
            }
        });

        jDateNhapHang.setDateFormatString("yyyy-MM-dd");

        jLabel8.setText("Ngày nhập hàng");

        jBtnThemGiay.setText("Thêm Sản Phẩm");
        jBtnThemGiay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnThemGiayActionPerformed(evt);
            }
        });

        jBtnXoaGiay.setText("Xóa Sản Phẩm");
        jBtnXoaGiay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnXoaGiayActionPerformed(evt);
            }
        });

        jBtnLamMoiGiay.setText("Làm Mới");
        jBtnLamMoiGiay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnLamMoiGiayActionPerformed(evt);
            }
        });

        jBtnSuaGiay.setText("Sửa Sản Phẩm");
        jBtnSuaGiay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnSuaGiayActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Thêm vào chi tiết phiếu nhập");

        jLabel7.setText("Mã Phiếu Nhập");

        jcbMaSP.setToolTipText("-Chọn Mã Sản Phẩm-");
        jcbMaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbMaSPActionPerformed(evt);
            }
        });
        jcbMaSP.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jcbMaSPPropertyChange(evt);
            }
        });

        txtNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNhanVienActionPerformed(evt);
            }
        });

        txtMaPhieuNhap1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaPhieuNhap1ActionPerformed(evt);
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
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNhanVien))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jcbSize, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jcbMaSP, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jBtnXoaGiay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jBtnThemGiay))
                                .addGap(37, 37, 37)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jBtnSuaGiay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jBtnLamMoiGiay, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtMaPhieuNhap1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 9, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jDateNhapHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateNhapHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaPhieuNhap1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbSize, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnSuaGiay)
                    .addComponent(jBtnThemGiay))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnXoaGiay)
                    .addComponent(jBtnLamMoiGiay))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setText("DANH SÁCH PHIẾU NHẬP");

        jtbPhieuNhap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Số lượng", "Size", "Loại", "Ngày nhập hàng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jtbPhieuNhap);

        jBtnLuuCSDL.setText("Lưu vào CSDL");
        jBtnLuuCSDL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnLuuCSDLActionPerformed(evt);
            }
        });

        jBtnXuatPhieu.setText("Xuất phiếu nhập");
        jBtnXuatPhieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnXuatPhieuActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel12.setText("CHI TIẾT PHIẾU NHẬP");

        jtbChiTietPhieuNhap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jtbChiTietPhieuNhap);

        jRBTaoPhieuNhap.setText("Tạo phiếu nhập mới");
        jRBTaoPhieuNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBTaoPhieuNhapActionPerformed(evt);
            }
        });

        jRBXemLaiPhieuNhap.setText("Xem lại phiếu nhập cũ");
        jRBXemLaiPhieuNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBXemLaiPhieuNhapActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addGap(39, 39, 39)
                .addComponent(jRBTaoPhieuNhap)
                .addGap(18, 18, 18)
                .addComponent(jRBXemLaiPhieuNhap)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 532, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 532, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jBtnLuuCSDL, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnXuatPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(102, 102, 102))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jRBTaoPhieuNhap)
                    .addComponent(jRBXemLaiPhieuNhap))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnLuuCSDL, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnXuatPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
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
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnTaoPhieuNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnTaoPhieuNhapActionPerformed

    }//GEN-LAST:event_jBtnTaoPhieuNhapActionPerformed

    private void jBtnThemGiayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnThemGiayActionPerformed
        // TODO add your handling code here:
       String maPN, maSP, size, soLuong;
        maPN = txtMaPhieuNhap1.getText();
        maSP = jcbMaSP.getSelectedItem().toString();
        soLuong = txtSoLuong.getText();
        size = jcbSize.getSelectedItem().toString();

        
        int sizeTemp = Integer.parseInt(size);

        if (maSP.equals("-Chọn Mã Sản Phẩm-")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn mã sản phẩm");
        } else if (soLuong.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Số lượng");
        } else if (size.equals("-Chọn Size-")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn size sản phẩm");
        } else if (checkChiTietPhieuNhap(maSP, sizeTemp) == 0) {
            try {
                int soLuongTemp = Integer.parseInt(soLuong);
                Product pd = new Product(maSP, soLuongTemp, sizeTemp);
                listProducts.add(pd);
                loadChiTietPhieuNhapKhiThem();

            } catch (Exception e) {
                System.err.println("Có lỗi tại nút thêm chi tiết phiếu nhập");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm và size bạn vừa nhập hiện đã tồn tại!"
                    + " Vui lòng trọng mã sản phẩm và size khác hoặc click nút Sửa để cập nhật số lượng sản phẩm");
        }
    }//GEN-LAST:event_jBtnThemGiayActionPerformed

    private void jBtnLamMoiGiayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnLamMoiGiayActionPerformed
        // TODO add your handling code here:
        jcbMaSP.setSelectedIndex(0);
        jcbSize.setSelectedIndex(0);
        txtSoLuong.setText("");

    }//GEN-LAST:event_jBtnLamMoiGiayActionPerformed

    private void jBtnXoaGiayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnXoaGiayActionPerformed
        // TODO add your handling code here:
        try {
            String maSP, size;
            maSP = jcbMaSP.getSelectedItem().toString();
            size = jcbSize.getSelectedItem().toString();
            int sizeTemp = Integer.parseInt(size);

            for (Product x : listProducts) {
                if (x.getMaSP().equals(maSP) && x.getSize() == sizeTemp) {
                    listProducts.remove(x);
                    JOptionPane.showMessageDialog(this, "Xóa thành công");
                    loadChiTietPhieuNhapKhiThem();
                }
            }

        } catch (Exception e) {

        }
    }//GEN-LAST:event_jBtnXoaGiayActionPerformed

    private void jBtnLuuCSDLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnLuuCSDLActionPerformed
        // TODO add your handling code here:
        if(jtbChiTietPhieuNhap.getRowCount()>0){
///////////////////////Tạo mới phiếu nhập////////////////////////
        //Lay ra maPN cuối cùng trong SQL
        ImportProductJFrame layPN = new ImportProductJFrame(userName);
        String Chuoi = layPN.LayMaPhieuNhap();
        int count = layPN.LaySoLuongPhieuNhap();

        System.out.println("MAPN = " + Chuoi + "Count = " + count);

        String chuoiTemp1;
        int chuoiSTT1;
        chuoiTemp1 = Chuoi.replace("PN", "");
        chuoiSTT1 = Integer.parseInt(chuoiTemp1);
        if (chuoiSTT1 < 9) {
            txtMaPhieuNhap1.setText("PN0" + String.valueOf(count + 1));
        } else if (chuoiSTT1 >= 9 & chuoiSTT1 < 1000) {
            txtMaPhieuNhap1.setText("PN" + String.valueOf(count + 1));
        }

        String date = "";
        if (jDateNhapHang.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format(jDateNhapHang.getDate()); // Date->String
        };

        ImportProductJFrame layNV = new ImportProductJFrame(userName);
        String maNV = layNV.LayMaNhanVien();

        try {
            String sql = "insert into PHIEUNHAP values (?,?,?)";
            PreparedStatement ps1 = conn.prepareStatement(sql);
            ps1.setString(1, txtMaPhieuNhap1.getText());
            ps1.setString(2, date);
            ps1.setString(3, maNV);
            ps1.execute();
            JOptionPane.showMessageDialog(this, "Đã tạo mới phiếu nhập!");
        } catch (Exception e) {
            e.toString();
        }
//////////////////////////////////////////////////////////////

        String maSP = "", size = "", soluong = "";
        for (int i = 0; i < jtbChiTietPhieuNhap.getRowCount(); i++) {
            maSP = (String) jtbChiTietPhieuNhap.getValueAt(i, 1);
            size = (String.valueOf(jtbChiTietPhieuNhap.getValueAt(i, 3)));
            soluong = String.valueOf(jtbChiTietPhieuNhap.getValueAt(i, 2));
            System.out.println(maSP);
            System.out.println(size);
            System.out.println("So luong nhap vao la : " + soluong);

////////////////////them du lieu vao chi tiet phieu nhap////////////////
            try {
                String sql = "insert into CTPN values (?,?,?,?)";
                PreparedStatement ps1 = conn.prepareStatement(sql);
                for (Product x : listProducts) {
                ps1.setString(1, txtMaPhieuNhap1.getText());
                ps1.setString(2, maSP);
                ps1.setString(3, size);
                ps1.setString(4, soluong);
                ps1.execute();
            }            
                JOptionPane.showMessageDialog(this, "Đã thêm sản phẩm vào phiếu nhập mới!");
            } catch (Exception e) {
                e.toString();
            }
///////////////////////////////////////////////////////////////////////            

/////////////////////update lai so luong cho chi tiet giay dep////////////  
            ImportProductJFrame laySoLuongTon = new ImportProductJFrame(userName);
            String soLuongTon = laySoLuongTon.GetSoLuongTrongKho(maSP, size);
            System.out.println("So luong ton kho la : " + soLuongTon);

            int soLuongTemp = Integer.parseInt(soluong);
            int soLuongTonTemp = Integer.parseInt(soLuongTon);
            
            int soLuongSauKhiNhap = soLuongTemp + soLuongTonTemp;
            System.out.println("So luong sau khi nhap hang la : " + soLuongSauKhiNhap);
            try {
                String sql = "update CHITIETGD set SOLUONGTON = '" + soLuongSauKhiNhap + "' \n"
                        + "where MASP = '" + maSP + "' and SIZE = '" + size + "'";
                PreparedStatement ps1 = conn.prepareStatement(sql);
                ps1.execute();
                JOptionPane.showMessageDialog(this, "Đã cập nhật số lượng trong CSDL thành công!!");
            } catch (Exception e1) {
                e1.toString();
            }
        }
        /////////////////////////////////////////////////////////////////////////
        ////////////////////Làm mới ArrayList///////////////////////////////////
        listProducts.clear();
        loadChiTietPhieuNhapKhiThem();
        /////////////////////////////////////////////////////////////////////////
        loadDataPhieuNhap();
        }else{
            JOptionPane.showMessageDialog(this, "Phiếu nhập hiện đang rỗng, xin vui lòng nhập sản phẩm mới cho phiếu nhập");
        }
    }//GEN-LAST:event_jBtnLuuCSDLActionPerformed

    private void jBtnSuaGiayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSuaGiayActionPerformed
        // TODO add your handling code here:
        String maSP, size, soLuong;
        maSP = jcbMaSP.getSelectedItem().toString();
        soLuong = txtSoLuong.getText();
        size = jcbSize.getSelectedItem().toString();
        int soLuongTemp = Integer.parseInt(soLuong);
        int sizeTemp = Integer.parseInt(size);

        for (Product x : listProducts) {
            if (x.getMaSP().equals(maSP) && x.getSize() == sizeTemp) {
                x.setSoLuong(soLuongTemp);
                JOptionPane.showMessageDialog(this, "Sửa thành công");
                loadChiTietPhieuNhapKhiThem();
            }
            else {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm và size bạn muốn sửa hiện không tồn tại!");
            }
        }


    }//GEN-LAST:event_jBtnSuaGiayActionPerformed

    private void txtNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNhanVienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNhanVienActionPerformed

    private void jcbMaSPPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jcbMaSPPropertyChange
        // TODO add your handling code here:


    }//GEN-LAST:event_jcbMaSPPropertyChange

    private void jcbMaSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbMaSPActionPerformed
        // TODO add your handling code here:
        try {
            jcbSize.removeAllItems();
            String maSP = jcbMaSP.getSelectedItem().toString();          
            loadComboboxSize(maSP);
        } catch (Exception e) {
            e.toString();
        }
    }//GEN-LAST:event_jcbMaSPActionPerformed

    private void jcbSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbSizeActionPerformed

    }//GEN-LAST:event_jcbSizeActionPerformed

    private void jBtnXuatPhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnXuatPhieuActionPerformed
        // TODO add your handling code here:
        String maPN = txtMaPhieuNhap1.getText();
        XuatPhieuNhap(maPN);
        System.out.println("maPN = " + maPN);
    }//GEN-LAST:event_jBtnXuatPhieuActionPerformed

    private void jRBTaoPhieuNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBTaoPhieuNhapActionPerformed
        // TODO add your handling code here:
        loadChiTietPhieuNhapKhiThem();
        jtbPhieuNhap.setEnabled(false);
        jtbChiTietPhieuNhap.setEnabled(true);
        jBtnLuuCSDL.setEnabled(true);
        jcbMaSP.setEnabled(true);
        jcbSize.setEnabled(true);
        txtSoLuong.setEnabled(true);
        jBtnThemGiay.setEnabled(true);
        jBtnSuaGiay.setEnabled(true);
        jBtnXoaGiay.setEnabled(true);
        jBtnLamMoiGiay.setEnabled(true);
        jBtnXuatPhieu.setEnabled(true);
    }//GEN-LAST:event_jRBTaoPhieuNhapActionPerformed

    private void jRBXemLaiPhieuNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBXemLaiPhieuNhapActionPerformed
        // TODO add your handling code here:
        jtbPhieuNhap.setEnabled(true);
        jtbChiTietPhieuNhap.setEnabled(false);
        jBtnLuuCSDL.setEnabled(false);
        jcbMaSP.setEnabled(false);
        jcbSize.setEnabled(false);
        txtSoLuong.setEnabled(false);
        jBtnThemGiay.setEnabled(false);
        jBtnSuaGiay.setEnabled(false);
        jBtnXoaGiay.setEnabled(false);
        jBtnLamMoiGiay.setEnabled(false);
        jBtnXuatPhieu.setEnabled(true);
    }//GEN-LAST:event_jRBXemLaiPhieuNhapActionPerformed

    private void txtMaPhieuNhap1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaPhieuNhap1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaPhieuNhap1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnLamMoiGiay;
    private javax.swing.JButton jBtnLuuCSDL;
    private javax.swing.JButton jBtnSuaGiay;
    private javax.swing.JButton jBtnThemGiay;
    private javax.swing.JButton jBtnXoaGiay;
    private javax.swing.JButton jBtnXuatPhieu;
    private com.toedter.calendar.JDateChooser jDateNhapHang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRBTaoPhieuNhap;
    private javax.swing.JRadioButton jRBXemLaiPhieuNhap;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox jcbMaSP;
    private javax.swing.JComboBox jcbSize;
    private javax.swing.JTable jtbChiTietPhieuNhap;
    private javax.swing.JTable jtbPhieuNhap;
    private javax.swing.JTextField txtMaPhieuNhap1;
    private javax.swing.JTextField txtNhanVien;
    private javax.swing.JTextField txtSoLuong;
    // End of variables declaration//GEN-END:variables
}
