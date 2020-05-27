package Controller;

import Conection.KetNoi;
import display.KhachHangJPanel;
import display.TrangChuJPanel;
import display.HoaDonJPanel;
import display.BaoCaoJPanel;
import display.KhoJPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import category.DanhMuc;

/**
 *
 * @author Admin
 */
public class ChuyenManHinh {

    String username;

    private JPanel root;
    private String kindSelected = "";

    private List<DanhMuc> listItem = null;

    public ChuyenManHinh(JPanel root) {
        this.root = root;
    }

    public ChuyenManHinh(JPanel root, String user) {
        this.root = root;
        username = user;
    }

    private String layQuyen() {
        Connection ketNoi = KetNoi.layKetNoi();
        String sql = "select AUTHORIZE from ACCOUNT where USERNAME=?";
        String quyen = "";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, username);
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

    public void setView(JPanel jpnItem, JLabel jlbItem) {
        //Chọn màu mặc định cho Nút Trang Chủ
        kindSelected = "TrangChu";
        jpnItem.setBackground(Color.WHITE);
        jlbItem.setBackground(Color.WHITE);

        root.removeAll();
        root.setLayout(new BorderLayout());
        root.add(new TrangChuJPanel()); //Giao Diện Trang chủ sẽ được hiển thị mặc định khi mở chương trình
        root.validate();
        root.repaint();
    }

    public void setEvent(List<DanhMuc> listItem) {
        this.listItem = listItem;
        for (DanhMuc item : listItem) {
            item.getJlb().addMouseListener(new LabelEvent(item.getKind(), item.getJpn(), item.getJlb()));
        }//Cài đặt sự kiện chung cho bên MainJFrame, mỗi DanhMuc sẽ được hiển thị khi click vào Label or Pannel tương ứng

    }

    class LabelEvent implements MouseListener {

        //Cài đặt sự kiện khi Click Chuột vào Label
        private JPanel node;

        private String kind;
        private JPanel jpnItem;
        private JLabel jlbItem;

        public LabelEvent(String kind, JPanel jpnItem, JLabel jlbItem) {
            this.kind = kind;
            this.jpnItem = jpnItem;
            this.jlbItem = jlbItem;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                String q = layQuyen();
                switch (kind) {
                    case "TrangChu":
                        node = new TrangChuJPanel();
                        break;
                    case "SanPham":
                        if (q.equalsIgnoreCase("boss")) {
                            JOptionPane.showMessageDialog(root, "Bạn không đủ quyền truy cập chức năng của ứng dụng");
                            setChangeBackGround(kind);
                            return;
                        } else {
                            node = new HoaDonJPanel(username);
                        }
                        break;
                    case "Kho":
                        if (q.equalsIgnoreCase("boss")) {
                            JOptionPane.showMessageDialog(root, "Với quyền truy cập này bạn chỉ có thể tra cứu số lượng tồn "
                                    + "và lập báo cáo");
                            node = new KhoJPanel(username, q);
                        } else if (q.equalsIgnoreCase("admin")) {
                            node = new KhoJPanel(username, q);
                        } else {
                            node = new KhoJPanel(username);
                        }
                        break;
                    case "KhachHang":
                        if (q.equalsIgnoreCase("boss")) {
                            JOptionPane.showMessageDialog(root, "Bạn không đủ quyền truy cập chức năng của ứng dụng");
                            setChangeBackGround(kind);
                            return;
                        } else {
                            node = new KhachHangJPanel(username);
                        }
                        break;
                    case "BaoCao":
                        if (q.equalsIgnoreCase("Nhân viên")) {
                            JOptionPane.showMessageDialog(root, "Bạn không đủ quyền truy cập chức năng của ứng dụng");
                            setChangeBackGround(kind);
                            return;
                        } else {
                            node = new BaoCaoJPanel(username);
                        }
                        break;
                    default:
                        break;
                }
                root.removeAll();
                root.setLayout(new BorderLayout());
                root.add(node);
                root.validate();
                root.repaint();
                setChangeBackGround(kind);
            } catch (SQLException ex) {
                Logger.getLogger(ChuyenManHinh.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            kindSelected = kind;
            jpnItem.setBackground(Color.WHITE);
            jlbItem.setBackground(Color.WHITE);
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            jpnItem.setBackground(Color.WHITE);
            jlbItem.setBackground(Color.WHITE);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!kindSelected.equalsIgnoreCase(kind)) {
                Color c = new Color(102, 255, 204);
                jpnItem.setBackground(c);
                jlbItem.setBackground(c);
            }
        }

    }

    public void setChangeBackGround(String kind) {
        for (category.DanhMuc item : listItem) {
            if (item.getKind().equalsIgnoreCase(kind)) {
                item.getJpn().setBackground(Color.WHITE);
                item.getJlb().setBackground(Color.WHITE);
            } else {
                Color c = new Color(102, 255, 204);
                item.getJpn().setBackground(c);
                item.getJlb().setBackground(c);
            }
        }
    }
}