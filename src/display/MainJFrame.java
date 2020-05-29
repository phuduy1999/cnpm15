package display;

import Controller.ChuyenManHinh;
import category.DanhMuc;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Admin
 */
public class MainJFrame extends javax.swing.JFrame {

    private String username;

    public MainJFrame(String user) throws SQLException {
        initComponents();
        this.username = user;
        jTextFieldRound_user.setText(username);
        setTitle("Phần mềm quản lý shop giày dép");
        ChuyenManHinh dieuKhien = new ChuyenManHinh(jpnView, username);
        dieuKhien.setView(jpnTrangChu, jlbTrangChu);

        List<category.DanhMuc> listItem = new ArrayList<>();
        listItem.add(new DanhMuc("TrangChu", jpnTrangChu, jlbTrangChu));
        listItem.add(new DanhMuc("SanPham", jpnSanPham, jlbSanPham));
        listItem.add(new DanhMuc("Kho", jpnKho, jlbKho));
        listItem.add(new DanhMuc("KhachHang", jpnKhachHang, jlbKhachHang));
        listItem.add(new DanhMuc("BaoCao", jpnBaoCao, jlbBaoCao));

        dieuKhien.setEvent(listItem);
    }

    //changePassword viewChangePassword = new DoiMatKhau();
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpnRoot = new javax.swing.JPanel();
        jpnMenu = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel_QLGD = new javax.swing.JLabel();
        jpnTrangChu = new javax.swing.JPanel();
        jlbTrangChu = new javax.swing.JLabel();
        jpnSanPham = new javax.swing.JPanel();
        jlbSanPham = new javax.swing.JLabel();
        jpnKho = new javax.swing.JPanel();
        jlbKho = new javax.swing.JLabel();
        jpnKhachHang = new javax.swing.JPanel();
        jlbKhachHang = new javax.swing.JLabel();
        jpnBaoCao = new javax.swing.JPanel();
        jlbBaoCao = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jpnView = new javax.swing.JPanel();
        jButton_DoiMK = new javax.swing.JButton();
        jButton_DX = new javax.swing.JButton();
        jButton_Refresh = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldRound_user = new jtextfieldround.JTextFieldRound();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jpnRoot.setBackground(new java.awt.Color(0, 0, 0));

        jpnMenu.setBackground(new java.awt.Color(255, 153, 153));
        jpnMenu.setToolTipText("");

        jPanel5.setBackground(new java.awt.Color(255, 153, 153));

        jLabel_QLGD.setBackground(new java.awt.Color(255, 153, 153));
        jLabel_QLGD.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel_QLGD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_QLGD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/run.png"))); // NOI18N
        jLabel_QLGD.setText("QUẢN LÝ SHOP GIÀY DÉP");
        jLabel_QLGD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel_QLGD, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 30, Short.MAX_VALUE)
                .addComponent(jLabel_QLGD, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jpnTrangChu.setBackground(new java.awt.Color(102, 255, 204));
        jpnTrangChu.setVerifyInputWhenFocusTarget(false);

        jlbTrangChu.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jlbTrangChu.setForeground(new java.awt.Color(51, 51, 51));
        jlbTrangChu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbTrangChu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/social-media.png"))); // NOI18N
        jlbTrangChu.setText("Trang chủ");
        jlbTrangChu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpnTrangChuLayout = new javax.swing.GroupLayout(jpnTrangChu);
        jpnTrangChu.setLayout(jpnTrangChuLayout);
        jpnTrangChuLayout.setHorizontalGroup(
            jpnTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnTrangChuLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jlbTrangChu, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );
        jpnTrangChuLayout.setVerticalGroup(
            jpnTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnTrangChuLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jlbTrangChu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jpnSanPham.setBackground(new java.awt.Color(102, 255, 204));
        jpnSanPham.setVerifyInputWhenFocusTarget(false);

        jlbSanPham.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jlbSanPham.setForeground(new java.awt.Color(51, 51, 51));
        jlbSanPham.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/online.png"))); // NOI18N
        jlbSanPham.setText("Tạo Hóa Đơn");
        jlbSanPham.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpnSanPhamLayout = new javax.swing.GroupLayout(jpnSanPham);
        jpnSanPham.setLayout(jpnSanPhamLayout);
        jpnSanPhamLayout.setHorizontalGroup(
            jpnSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnSanPhamLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnSanPhamLayout.setVerticalGroup(
            jpnSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnSanPhamLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jlbSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jpnKho.setBackground(new java.awt.Color(102, 255, 204));
        jpnKho.setVerifyInputWhenFocusTarget(false);

        jlbKho.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jlbKho.setForeground(new java.awt.Color(51, 51, 51));
        jlbKho.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbKho.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/buildings.png"))); // NOI18N
        jlbKho.setText("Quản lý kho");
        jlbKho.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpnKhoLayout = new javax.swing.GroupLayout(jpnKho);
        jpnKho.setLayout(jpnKhoLayout);
        jpnKhoLayout.setHorizontalGroup(
            jpnKhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnKhoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbKho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnKhoLayout.setVerticalGroup(
            jpnKhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnKhoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jlbKho, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        jpnKhachHang.setBackground(new java.awt.Color(102, 255, 204));
        jpnKhachHang.setVerifyInputWhenFocusTarget(false);

        jlbKhachHang.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jlbKhachHang.setForeground(new java.awt.Color(51, 51, 51));
        jlbKhachHang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbKhachHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/seo-and-web.png"))); // NOI18N
        jlbKhachHang.setText("Khách hàng");
        jlbKhachHang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpnKhachHangLayout = new javax.swing.GroupLayout(jpnKhachHang);
        jpnKhachHang.setLayout(jpnKhachHangLayout);
        jpnKhachHangLayout.setHorizontalGroup(
            jpnKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnKhachHangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnKhachHangLayout.setVerticalGroup(
            jpnKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnKhachHangLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jlbKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jpnBaoCao.setBackground(new java.awt.Color(102, 255, 204));
        jpnBaoCao.setVerifyInputWhenFocusTarget(false);

        jlbBaoCao.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jlbBaoCao.setForeground(new java.awt.Color(51, 51, 51));
        jlbBaoCao.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbBaoCao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/monitor.png"))); // NOI18N
        jlbBaoCao.setText("Quản lý nhân viên");
        jlbBaoCao.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpnBaoCaoLayout = new javax.swing.GroupLayout(jpnBaoCao);
        jpnBaoCao.setLayout(jpnBaoCaoLayout);
        jpnBaoCaoLayout.setHorizontalGroup(
            jpnBaoCaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnBaoCaoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbBaoCao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnBaoCaoLayout.setVerticalGroup(
            jpnBaoCaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnBaoCaoLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jlbBaoCao, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/baseline_copyright_black_18dp.png"))); // NOI18N
        jLabel7.setText("Nhập Môn Công Nghệ Phần Mềm - Nhóm 15");

        javax.swing.GroupLayout jpnMenuLayout = new javax.swing.GroupLayout(jpnMenu);
        jpnMenu.setLayout(jpnMenuLayout);
        jpnMenuLayout.setHorizontalGroup(
            jpnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnMenuLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jpnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpnKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpnKho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpnSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpnTrangChu, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpnBaoCao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jpnMenuLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );
        jpnMenuLayout.setVerticalGroup(
            jpnMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnMenuLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jpnTrangChu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jpnSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jpnKho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jpnKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jpnBaoCao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jpnView.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jpnViewLayout = new javax.swing.GroupLayout(jpnView);
        jpnView.setLayout(jpnViewLayout);
        jpnViewLayout.setHorizontalGroup(
            jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1107, Short.MAX_VALUE)
        );
        jpnViewLayout.setVerticalGroup(
            jpnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButton_DoiMK.setBackground(new java.awt.Color(0, 0, 0));
        jButton_DoiMK.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jButton_DoiMK.setForeground(new java.awt.Color(255, 153, 153));
        jButton_DoiMK.setText("Đổi mật khẩu");
        jButton_DoiMK.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_DoiMK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_DoiMKActionPerformed(evt);
            }
        });

        jButton_DX.setBackground(new java.awt.Color(0, 0, 0));
        jButton_DX.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jButton_DX.setForeground(new java.awt.Color(255, 153, 153));
        jButton_DX.setText("Đăng xuất");
        jButton_DX.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        jButton_DX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_DXActionPerformed(evt);
            }
        });

        jButton_Refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update.png"))); // NOI18N
        jButton_Refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_RefreshActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 153, 153));
        jLabel1.setText("Xin chào");

        jTextFieldRound_user.setEditable(false);
        jTextFieldRound_user.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N

        javax.swing.GroupLayout jpnRootLayout = new javax.swing.GroupLayout(jpnRoot);
        jpnRoot.setLayout(jpnRootLayout);
        jpnRootLayout.setHorizontalGroup(
            jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnRootLayout.createSequentialGroup()
                .addComponent(jpnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpnRootLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jpnView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpnRootLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldRound_user, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_DoiMK, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_DX, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton_Refresh)
                        .addGap(48, 48, 48))))
        );
        jpnRootLayout.setVerticalGroup(
            jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnRootLayout.createSequentialGroup()
                .addGroup(jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnRootLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton_Refresh)
                            .addGroup(jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButton_DX, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton_DoiMK, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))))
                    .addGroup(jpnRootLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextFieldRound_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jpnMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnRoot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnRoot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_DoiMKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_DoiMKActionPerformed
        // TODO add your handling code here:
        new DoiMatKhau(this.username).setVisible(true);
    }//GEN-LAST:event_jButton_DoiMKActionPerformed

    private void jButton_DXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_DXActionPerformed
        Object[] options = {"Đồng ý", "Hủy"};
        int i = JOptionPane.showOptionDialog(this, "Bạn chắc chắn muốn đăng xuất?",
                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (i == JOptionPane.YES_OPTION) {
            this.dispose();
            DangNhap dangNhap = new DangNhap(true);
            dangNhap.setTitle("Đăng Nhập Hệ Thống");
            dangNhap.setResizable(false);
            dangNhap.setLocationRelativeTo(null);
            dangNhap.setVisible(true);
        }
    }//GEN-LAST:event_jButton_DXActionPerformed

    private void jButton_RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_RefreshActionPerformed
        jTextFieldRound_user.setText(username);
        Object[] options = {"Đồng ý", "Hủy"};
        int i = JOptionPane.showOptionDialog(this, "Khi refresh các tiến trình đang thực hiện sẽ bị mất?",
                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (i == JOptionPane.YES_OPTION) {
            ChuyenManHinh dieuKhien = null;
            try {
                dieuKhien = new ChuyenManHinh(jpnView, username);
            } catch (SQLException ex) {
                Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            dieuKhien.setView(jpnTrangChu, jlbTrangChu);

            List<category.DanhMuc> listItem = new ArrayList<>();
            listItem.add(new DanhMuc("TrangChu", jpnTrangChu, jlbTrangChu));
            listItem.add(new DanhMuc("SanPham", jpnSanPham, jlbSanPham));
            listItem.add(new DanhMuc("Kho", jpnKho, jlbKho));
            listItem.add(new DanhMuc("KhachHang", jpnKhachHang, jlbKhachHang));
            listItem.add(new DanhMuc("BaoCao", jpnBaoCao, jlbBaoCao));

            dieuKhien.setEvent(listItem);
        }
    }//GEN-LAST:event_jButton_RefreshActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_DX;
    private javax.swing.JButton jButton_DoiMK;
    private javax.swing.JButton jButton_Refresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel_QLGD;
    private javax.swing.JPanel jPanel5;
    private jtextfieldround.JTextFieldRound jTextFieldRound_user;
    private javax.swing.JLabel jlbBaoCao;
    private javax.swing.JLabel jlbKhachHang;
    private javax.swing.JLabel jlbKho;
    private javax.swing.JLabel jlbSanPham;
    private javax.swing.JLabel jlbTrangChu;
    private javax.swing.JPanel jpnBaoCao;
    private javax.swing.JPanel jpnKhachHang;
    private javax.swing.JPanel jpnKho;
    private javax.swing.JPanel jpnMenu;
    private javax.swing.JPanel jpnRoot;
    private javax.swing.JPanel jpnSanPham;
    private javax.swing.JPanel jpnTrangChu;
    private javax.swing.JPanel jpnView;
    // End of variables declaration//GEN-END:variables
}
