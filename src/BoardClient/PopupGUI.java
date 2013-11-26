package BoardClient;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class PopupGUI extends JFrame{
    
    
    public PopupGUI() throws Exception {
        JTextField ip = new JTextField("172.0.0.1");
        JTextField port = new JTextField("4444");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Server Address"));
        panel.add(ip);
        panel.add(new JLabel("Port"));
        panel.add(port);
        int result = JOptionPane.showConfirmDialog(null, panel, "Connect To Server",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String userName = "Matt";
            String portName = ip.getText();
            int portNumber = Integer.parseInt(port.getText());
            
            new BoardClient(userName, portName, portNumber);
        } 
    }

}

