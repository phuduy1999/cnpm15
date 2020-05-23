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
import model.Customer;
import model.StoreHouse;
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
public class StoreHouseJPanel extends javax.swing.JPanel {

    String username;
    /**
     * Creates new form StoreHouseJPanel
     */
    List<StoreHouse> storeHouseList = new ArrayList<StoreHouse>();

    DefaultTableModel tbn;

    Connection conn = KetNoi.layKetNoi();

    //private String userName;
    
    public StoreHouseJPanel(String user) {
        initComponents();
        this.username = user;
        System.out.println("Truy cập StoreHouse với username = " + username);
        loadComboboxLoai();
        loadComboboxSize();
        loadData();
        jBtnXuatBaoCao.setEnabled(true);
        txtSoLuong.setEnabled(false);
    }

    public StoreHouseJPanel(String user, String quyen) {
        initComponents();
        username = user;
        loadComboboxLoai();
        loadComboboxSize();
        if (quyen.equals("boss")) {
            jBtnLamLai.setEnabled(false);
            jBtnSuaSanPham.setEnabled(false);
            jBtnTaoPhieuNhap.setEnabled(false);
            jBtnThemSanPham.setEnabled(false);
            jBtnXoaSanPham.setEnabled(false);
            jBtnXuatBaoCao.setEnabled(true);
        }
        loadData();
        txtSoLuong.setEnabled(false);
    }

    public void loadComboboxSize() {
        try {
            PreparedStatement ps = conn.prepareStatement("Select SIZE from SIZE");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jcbSize.addItem(rs.getString("SIZE"));
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void loadComboboxLoai() {
        try {
            PreparedStatement ps = conn.prepareStatement("Select LOAI from LoaiGD");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jcbLoai.addItem(rs.getString("Loai"));
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void loadData() {
        try {
            String sql = "SELECT DISTINCT GIAYDEP.MASP, GIAYDEP.TENSP, GIAYDEP.DONGIA, CHITIETGD.SOLUONGTON, CHITIETGD.SIZE, LOAIGD.LOAI\n"
                    + "                    FROM GIAYDEP\n"
                    + "                    INNER JOIN CHITIETGD ON GIAYDEP.MASP = CHITIETGD.MASP\n"
                    + "                    INNER JOIN SIZE ON GIAYDEP.MASP = CHITIETGD.MASP\n"
                    + "                    INNER JOIN LOAIGD ON GIAYDEP.MALOAI = LOAIGD.MALOAI\n"
                    + "					where CHITIETGD.TRANGTHAI = '1'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            //storeHouseList.clear();
            Object[] obj = new Object[]{"STT", "Mã Sản phẩm", "Tên Sản phẩm", "Đơn giá", "Số lượng", "Size", "Loại"};
            tbn = new DefaultTableModel(obj, 0);
            jTbSP.setModel(tbn);
            int c = 0;
            try {
                while (rs.next()) {
                    c++;
                    Object[] item = new Object[7];
                    item[0] = c;
                    item[1] = rs.getString("MASP");
                    item[2] = rs.getString("TENSP");
                    item[3] = rs.getString("DONGIA");
                    item[4] = rs.getInt("SOLUONGTON");
                    item[5] = rs.getInt("Size");
                    item[6] = rs.getString("LOAI");
                    tbn.addRow(item);
                }
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }

            // Ham tra gia tri tu bang len textField
            jTbSP.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (jTbSP.getSelectedRow() >= 0) {
                        txtMaSanPham.setText(jTbSP.getValueAt(jTbSP.getSelectedRow(), 1) + "");
                        txtTenSanPham.setText(jTbSP.getValueAt(jTbSP.getSelectedRow(), 2) + "");
                        txtDonGia.setText(jTbSP.getValueAt(jTbSP.getSelectedRow(), 3) + "");
                        txtSoLuong.setText(jTbSP.getValueAt(jTbSP.getSelectedRow(), 4) + "");
                        jcbSize.setSelectedItem(jTbSP.getModel().getValueAt(jTbSP.getSelectedRow(), 5) + "");
                        jcbLoai.setSelectedItem(jTbSP.getModel().getValueAt(jTbSP.getSelectedRow(), 6) + "");
                    }
                }
            });
            // ket thuc ham tra gia tri tu bang len textField 
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void ThemChiTietGD() {
        txtSoLuong.setText("0");
        String sqlChiTietGD = "insert into CHITIETGD values(?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sqlChiTietGD);
            ps.setString(1, txtMaSanPham.getText());
            ps.setString(2, (String) jcbSize.getSelectedItem());
            ps.setString(3, txtSoLuong.getText());
            ps.setString(4, "1");
            ps.execute();
        } catch (Exception e) {
        }
    }

    public String LayMaLoai(String tenLoai) {
        String maLoai = "";
        System.out.println("Ten Loai trong ham lay ma loai la " + tenLoai);
        String sql = "select LOAIGD.MALOAI\n"
                + "from LOAIGD\n"
                + "where LOAIGD.LOAI = N'" + tenLoai + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                maLoai = rs.getString("maLoai");
            }
        } catch (Exception e) {
            e.toString();
        }
        return maLoai;
    }

    public void ThemGiayDep() {
        String tenLoai = jcbLoai.getSelectedItem().toString();
        String maLoai = LayMaLoai(tenLoai);
        System.out.println("maLoai la" + maLoai);
        String sqlGiayDep = "insert into GIAYDEP values(?,?,?,?)";
        try {
            PreparedStatement ps1 = conn.prepareStatement(sqlGiayDep);
            ps1.setString(1, txtMaSanPham.getText());
            ps1.setString(2, txtTenSanPham.getText());
            ps1.setString(3, txtDonGia.getText());
            ps1.setString(4, maLoai);
            ps1.execute();

        } catch (Exception e) {
            System.out.println("Da them 1 size moi cho ma san pham : " + txtMaSanPham.getText());
        }
    }

    public void SuaGiayDep() {
        String maSanPham, tenSanPham, donGiaSP, soLuongSP, sizeSP, loaiSP;
        maSanPham = txtMaSanPham.getText();
        tenSanPham = txtTenSanPham.getText();
        donGiaSP = txtDonGia.getText();
        soLuongSP = txtSoLuong.getText();
        sizeSP = jcbSize.getSelectedItem().toString();

        String tenLoai = jcbLoai.getSelectedItem().toString();
        String maLoai = LayMaLoai(tenLoai);

        String sql = "update GiayDep set TENSP = N'" + tenSanPham + "', DONGIA = '" + donGiaSP + "', MALOAI = N'" + maLoai + "'\n"
                + "where MASP = '" + maSanPham + "'";
        try {
            PreparedStatement ps1 = conn.prepareStatement(sql);
            ps1.execute();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public int Check(String MaSP, String size) {
        String sql = "select CHITIETGD.MASP, CHITIETGD.SIZE from CHITIETGD\n"
                + "where CHITIETGD.MASP = '" + MaSP + "' and CHITIETGD.SIZE = '" + size + "'"
                + " and CHITIETGD.TRANGTHAI = '1'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
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

    public int CheckTrangThai(String MaSP, String size) {
        String sql = "select CHITIETGD.MASP, CHITIETGD.SIZE from CHITIETGD\n"
                + "where CHITIETGD.MASP = '" + MaSP + "' and CHITIETGD.SIZE = '" + size + "'"
                + " and CHITIETGD.TRANGTHAI = '0'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
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

    public void KhoiPhucSanPham() {
        String maSP, size;
        maSP = txtMaSanPham.getText();
        size = jcbSize.getSelectedItem().toString();
        String sql = "Update CHITIETGD set TRANGTHAI = '1' where MASP = '" + maSP + "' and SIZE = '" + size + "'";
        try {
            PreparedStatement ps1 = conn.prepareStatement(sql);
            ps1.execute();
            JOptionPane.showMessageDialog(this, "Bạn vừa khôi phục 1 sản phẩm trước đây từng xóa!!");
        } catch (Exception e) {
            e.toString();
        }
    }

    public void XoaSanPham() {
        String maSP, size;
        maSP = txtMaSanPham.getText();
        size = jcbSize.getSelectedItem().toString();
        String sql = "Update CHITIETGD set TRANGTHAI = '0' where MASP = '" + maSP + "' and SIZE = '" + size + "'";
        try {
            PreparedStatement ps1 = conn.prepareStatement(sql);
            ps1.execute();
        } catch (Exception e) {
            e.toString();
        }
    }

    public void XuatBaoCao() {

        try {
            Hashtable map = new Hashtable();
            JasperReport report = JasperCompileManager.compileReport("src/display/rpBaoCaoSoLuong.jrxml");

            JasperPrint p = JasperFillManager.fillReport(report, map, conn);
            JasperViewer.viewReport(p, false);
        } catch (Exception ex) {
            ex.toString();
        }
    }

    public void boLoc(String timKiem) {
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(tbn);
        jTbSP.setRowSorter(trs);
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
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtMaSanPham = new javax.swing.JTextField();
        txtTenSanPham = new javax.swing.JTextField();
        txtDonGia = new javax.swing.JTextField();
        txtSoLuong = new javax.swing.JTextField();
        jcbSize = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jcbLoai = new javax.swing.JComboBox();
        jBtnXoaSanPham = new javax.swing.JButton();
        jBtnSuaSanPham = new javax.swing.JButton();
        jBtnThemSanPham = new javax.swing.JButton();
        jBtnLamLai = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        txtTimSanPham = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTbSP = new javax.swing.JTable();
        jBtnXuatBaoCao = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jBtnTaoPhieuNhap = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 153, 153));
        jLabel1.setText("QUẢN LÝ KHO");

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        jLabel2.setForeground(new java.awt.Color(255, 153, 153));
        jLabel2.setText("Mã Sản Phẩm:");

        jLabel3.setForeground(new java.awt.Color(255, 153, 153));
        jLabel3.setText("Tên Sản Phẩm:");

        jLabel4.setForeground(new java.awt.Color(255, 153, 153));
        jLabel4.setText("Đơn Giá:");

        jLabel5.setForeground(new java.awt.Color(255, 153, 153));
        jLabel5.setText("Số Lượng:");

        jLabel6.setForeground(new java.awt.Color(255, 153, 153));
        jLabel6.setText("Size:");

        txtSoLuong.setText("0");

        jcbSize.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-Chọn Size-" }));

        jLabel7.setForeground(new java.awt.Color(255, 153, 153));
        jLabel7.setText("Loại:");

        jcbLoai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-Chọn Loại-" }));

        jBtnXoaSanPham.setBackground(new java.awt.Color(0, 0, 0));
        jBtnXoaSanPham.setForeground(new java.awt.Color(255, 153, 153));
        jBtnXoaSanPham.setText("Xóa Sản Phẩm");
        jBtnXoaSanPham.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jBtnXoaSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnXoaSanPhamActionPerformed(evt);
            }
        });

        jBtnSuaSanPham.setBackground(new java.awt.Color(0, 0, 0));
        jBtnSuaSanPham.setForeground(new java.awt.Color(255, 153, 153));
        jBtnSuaSanPham.setText("Sửa Sản Phẩm");
        jBtnSuaSanPham.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jBtnSuaSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnSuaSanPhamActionPerformed(evt);
            }
        });

        jBtnThemSanPham.setBackground(new java.awt.Color(0, 0, 0));
        jBtnThemSanPham.setForeground(new java.awt.Color(255, 153, 153));
        jBtnThemSanPham.setText("Thêm sản phẩm");
        jBtnThemSanPham.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jBtnThemSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnThemSanPhamActionPerformed(evt);
            }
        });

        jBtnLamLai.setBackground(new java.awt.Color(0, 0, 0));
        jBtnLamLai.setForeground(new java.awt.Color(255, 153, 153));
        jBtnLamLai.setText("Làm lại");
        jBtnLamLai.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jBtnLamLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnLamLaiActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 153, 153));
        jLabel8.setText("Thêm mới sản phẩm");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jBtnThemSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jBtnLamLai, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jBtnXoaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jBtnSuaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(31, 31, 31))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtMaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jcbSize, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jcbLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addContainerGap()))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbSize, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jBtnSuaSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(jBtnThemSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jBtnXoaSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtnLamLai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));
        jPanel4.setForeground(new java.awt.Color(255, 153, 153));

        txtTimSanPham.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimSanPhamKeyReleased(evt);
            }
        });

        jTbSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTbSP);

        jBtnXuatBaoCao.setBackground(new java.awt.Color(0, 0, 0));
        jBtnXuatBaoCao.setForeground(new java.awt.Color(255, 153, 153));
        jBtnXuatBaoCao.setText("Xuất số lượng");
        jBtnXuatBaoCao.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jBtnXuatBaoCao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnXuatBaoCaoActionPerformed(evt);
            }
        });

        jLabel9.setForeground(new java.awt.Color(255, 153, 153));
        jLabel9.setText("Tìm kiếm:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBtnXuatBaoCao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtTimSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnXuatBaoCao, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jBtnTaoPhieuNhap.setBackground(new java.awt.Color(0, 0, 0));
        jBtnTaoPhieuNhap.setForeground(new java.awt.Color(255, 153, 153));
        jBtnTaoPhieuNhap.setText("Tạo phiếu nhập mới");
        jBtnTaoPhieuNhap.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jBtnTaoPhieuNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnTaoPhieuNhapActionPerformed(evt);
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
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(489, 489, 489)
                        .addComponent(jBtnTaoPhieuNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(17, 17, 17))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtnTaoPhieuNhap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(63, Short.MAX_VALUE))
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

    private void jBtnLamLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnLamLaiActionPerformed
        // TODO add your handlingcode  here:
        txtMaSanPham.setText("");
        txtTenSanPham.setText("");
        txtDonGia.setText("");
        txtSoLuong.setText("0");
        jcbSize.setSelectedIndex(0);
        jcbLoai.setSelectedIndex(0);
        loadData();
    }//GEN-LAST:event_jBtnLamLaiActionPerformed

    private void jBtnThemSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnThemSanPhamActionPerformed
        // TODO add your handling code here:
        String maSanPham, tenSanPham, donGiaSP, soLuongSP, sizeSP, loaiSP;
        maSanPham = txtMaSanPham.getText();
        tenSanPham = txtTenSanPham.getText();
        donGiaSP = txtDonGia.getText();
        soLuongSP = txtSoLuong.getText();
        sizeSP = jcbSize.getSelectedItem().toString();
        loaiSP = jcbLoai.getSelectedItem().toString();
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
        } else if (Check(maSanPham, sizeSP) == 1) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm và size theo mã đã tồn tại!! Vui lòng nhập mã hoặc size khác!");
        } else if (CheckTrangThai(maSanPham, sizeSP) == 1) {
            if (JOptionPane.showConfirmDialog(this, "Bạn đã xóa sản phẩm này trước kia!! Bạn muốn khôi phục lại chứ?", "confirm",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                KhoiPhucSanPham();
                loadData();
            }
        } else {
            try {
                ThemGiayDep();
                ThemChiTietGD();
                loadData();
                JOptionPane.showMessageDialog(this, "Thêm thành công");
            } catch (Exception ex) {

                System.err.println(ex.toString());
            }

        }

    }//GEN-LAST:event_jBtnThemSanPhamActionPerformed

    private void jBtnXoaSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnXoaSanPhamActionPerformed
        // TODO add your handling code here:
        String maSanPham, tenSanPham, donGiaSP, soLuongSP, sizeSP, loaiSP;
        maSanPham = txtMaSanPham.getText();
        tenSanPham = txtTenSanPham.getText();
        donGiaSP = txtDonGia.getText();
        soLuongSP = txtSoLuong.getText();
        sizeSP = jcbSize.getSelectedItem().toString();
        loaiSP = jcbLoai.getSelectedItem().toString();
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
        }else{
            
            XoaSanPham();
            loadData();
            JOptionPane.showMessageDialog(this, "Xóa thành công");
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }//GEN-LAST:event_jBtnXoaSanPhamActionPerformed

    private void jBtnSuaSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSuaSanPhamActionPerformed
        // TODO add your handling code here:
        String maSanPham, tenSanPham, donGiaSP, soLuongSP, sizeSP, loaiSP;
        maSanPham = txtMaSanPham.getText();
        tenSanPham = txtTenSanPham.getText();
        donGiaSP = txtDonGia.getText();
        soLuongSP = txtSoLuong.getText();
        sizeSP = jcbSize.getSelectedItem().toString();
        loaiSP = jcbLoai.getSelectedItem().toString();
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
        } else if (txtMaSanPham.getText().toString().equals(jTbSP.getValueAt(jTbSP.getSelectedRow(), 1).toString())
                & txtTenSanPham.getText().toString().equals(jTbSP.getValueAt(jTbSP.getSelectedRow(), 2).toString())
                & txtDonGia.getText().toString().equals(jTbSP.getValueAt(jTbSP.getSelectedRow(), 3).toString()) 
                & jcbLoai.getSelectedItem().toString().equals(jTbSP.getValueAt(jTbSP.getSelectedRow(), 6).toString()) 
                & jcbSize.getSelectedItem().toString().equals(jTbSP.getValueAt(jTbSP.getSelectedRow(), 5).toString())) 
        {
                JOptionPane.showMessageDialog(this, "Bạn chưa sửa gì cả!!");
        } else {
            try {
                if (!txtMaSanPham.getText().toString().equals(jTbSP.getValueAt(jTbSP.getSelectedRow(), 1).toString())
                    || !jcbSize.getSelectedItem().toString().equals(jTbSP.getValueAt(jTbSP.getSelectedRow(), 5).toString())   ) {
                    JOptionPane.showMessageDialog(this, "Mã sản phảm và size không thể sửa");
                } else {
                    SuaGiayDep();
                    loadData();
                    JOptionPane.showMessageDialog(this, "Sửa thành công");
                }
                
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }//GEN-LAST:event_jBtnSuaSanPhamActionPerformed

    private void jBtnTaoPhieuNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnTaoPhieuNhapActionPerformed
        // TODO add your handling code here:
        ImportProductJFrame viewImportProductJFrame = new ImportProductJFrame(username);
        viewImportProductJFrame.setVisible(true);
    }//GEN-LAST:event_jBtnTaoPhieuNhapActionPerformed

    private void txtTimSanPhamKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimSanPhamKeyReleased
        // TODO add your handling code here:
        String timKiem = txtTimSanPham.getText();
        boLoc(timKiem);

    }//GEN-LAST:event_txtTimSanPhamKeyReleased

    private void jBtnXuatBaoCaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnXuatBaoCaoActionPerformed
        // TODO add your handling code here:
        XuatBaoCao();
        System.out.println("clicked button XuatBaoCao");
    }//GEN-LAST:event_jBtnXuatBaoCaoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnLamLai;
    private javax.swing.JButton jBtnSuaSanPham;
    private javax.swing.JButton jBtnTaoPhieuNhap;
    private javax.swing.JButton jBtnThemSanPham;
    private javax.swing.JButton jBtnXoaSanPham;
    private javax.swing.JButton jBtnXuatBaoCao;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTbSP;
    private javax.swing.JComboBox jcbLoai;
    private javax.swing.JComboBox jcbSize;
    private javax.swing.JTextField txtDonGia;
    private javax.swing.JTextField txtMaSanPham;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenSanPham;
    private javax.swing.JTextField txtTimSanPham;
    // End of variables declaration//GEN-END:variables
}
