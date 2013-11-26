package BoardClient;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class PopupGUI extends JFrame{
    
    static void popup() {
        JTextField ip = new JTextField("172.0.0.1");
        JTextField port = new JTextField("4444");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Server Adress"));
        panel.add(ip);
        panel.add(new JLabel("Port"));
        panel.add(port);
        int result = JOptionPane.showConfirmDialog(null, panel, "Connect To Server",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            //TODO Connect to server
            System.out.println(ip.getText());
            System.out.println(port.getText());
        } 
    }

}

