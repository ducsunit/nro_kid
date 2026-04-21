package nro.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import nro.server.io.Message;
import nro.services.Service;
import nro.utils.Log;

public class panel extends JPanel implements ActionListener {

    private JButton baotri, thaydoiexp, thaydoisk, chatserver, kickplayer, doitien, tileroi, tileNcap;

    public panel() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);

        baotri = createButton("Bảo Trì Máy Chủ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(baotri, gbc);

        thaydoiexp = createButton("Đổi Exp Server");
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(thaydoiexp, gbc);

        thaydoisk = createButton("Sự Kiện");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(thaydoisk, gbc);

        chatserver = createButton("Thông Báo Server");
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(chatserver, gbc);

        kickplayer = createButton("Đá All Player");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(kickplayer, gbc);

        doitien = createButton("Khuyến mãi Nạp");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(doitien, gbc);

        tileroi = createButton("Tỉ lệ rơi Toàn Server");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(tileroi, gbc);

        tileNcap = createButton("Tỉ lệ Nâng cấp đồ");
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(tileNcap, gbc);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.BLUE);
        button.setBackground(new Color(255, 255, 0));
        button.setFocusPainted(false);
        return button;
    }
    

    public class ExpInputDialog extends JFrame {

        private final JTextField textField1;
        private final JTextField textField2;

        public ExpInputDialog() {
            setTitle("Tỉ lệ (a/b)");
            setSize(300, 200);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

            JLabel label1 = new JLabel("Nhập a: ");
            textField1 = new JTextField(10);

            JLabel label2 = new JLabel("Nhập b: ");
            textField2 = new JTextField(10);

            JPanel panel1 = new JPanel();
            panel1.add(label1);
            panel1.add(textField1);

            JPanel panel2 = new JPanel();
            panel2.add(label2);
            panel2.add(textField2);

            add(panel1);
            add(panel2);

            int result = JOptionPane.showConfirmDialog(this, this.getContentPane(), "Bảng Exp Server", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    Manager.TILE_ROI_A = Integer.parseInt(textField1.getText());
                    Manager.TILE_ROI_B = Integer.parseInt(textField2.getText());

                    // Thực hiện xử lý với giá trị exp1 và exp2
                    System.out.println("Tỉ lệ: : " + Manager.TILE_ROI_A + "/" + Manager.TILE_ROI_B);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên hợp lệ cho Exp Server.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == baotri) {
            Maintenance.gI().start(10);
            System.out.println("------------- TIEN HANH BAO TRI!-------------\n");
        } else if (e.getSource() == thaydoiexp) {
            String exp = JOptionPane.showInputDialog(this, "Bảng Exp Server\n"
                    + "Exp Server hiện tại: " + Manager.RATE_EXP_SERVER);
            if (exp != null) {
                Manager.RATE_EXP_SERVER = Byte.parseByte(exp);
                System.out.println("------------- TANG EXP HIEN TAI: x" + exp + " LAN -------------\n");
            }
        } else if (e.getSource() == thaydoisk) {
            String sk = JOptionPane.showInputDialog(this, "Bảng Sự Kiện\n"
                    + "Sự Kiện Server: " + Manager.EVENT_SEVER);
            if (sk != null) {
                Manager.EVENT_SEVER = Byte.parseByte(sk);
                System.out.println("------------- SU KIEN HIEN TAI: " + sk + " -------------\n");
            }
        } else if (e.getSource() == chatserver) {
            String chat = JOptionPane.showInputDialog(this, "Thông Báo Server\n");
            if (chat != null) {
                Message msg = new Message(93);
                try {
                    msg.writer().writeUTF(chat);
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
                }
                Service.getInstance().sendMessAllPlayer(msg);
                msg.cleanup();
                System.out.println("------------- THONG BAO: " + chat + " -------------\n");
            }
        } else if (e.getSource() == kickplayer) {
            new Thread(() -> {
                Client.gI().close();
            }).start();
        } else if (e.getSource() == doitien) {
            String naptien = JOptionPane.showInputDialog(this, "Giá trị quy đổi Vàng và Ngọc\n"
                    + "Hiện tại:  x" + Manager.KHUYEN_MAI_NAP + " Đổi tiền");
            if (naptien != null) {
                Manager.KHUYEN_MAI_NAP = Byte.parseByte(naptien);
                System.out.println("------------- KHUYEN MAI QUY DOI DANG: x" + naptien + " LAN -------------\n");
            }
        } else if (e.getSource() == tileroi) {
            new ExpInputDialog();
        } else if (e.getSource() == tileNcap) {
            String tile = JOptionPane.showInputDialog(this, "Tỉ lệ Nang6 cap61 hiện tại: x" + Manager.TILE_NCAP + " lần");
            if (tile != null) {
                Manager.TILE_NCAP = Integer.parseInt(tile);
                System.out.println("------------- TI LE NANG CAP DO TOAN SERVER: " + tile + " LAN -------------\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("NROKID");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                Panel panel = new Panel();
                frame.getContentPane().add(panel);
                frame.setSize(400, 200);
                frame.setVisible(true);
            }
        });
    }
}
