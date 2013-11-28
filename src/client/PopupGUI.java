package client;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import server.BoardServer;


public class PopupGUI extends JFrame {
  
    private static final long serialVersionUID = 1L;

    public PopupGUI(Boolean incorrectServer) throws Exception {
        JTextField username = new JTextField("Matt");
        JTextField ip = new JTextField("localhost");
        JTextField port = new JTextField(Integer.toString(BoardServer.DEFAULT_PORT));
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Username"));
        panel.add(username);
        panel.add(new JLabel("Server Address"));
        panel.add(ip);
        panel.add(new JLabel("Port"));
        panel.add(port);
        if (incorrectServer) {
            JLabel errorLabel = new JLabel("Incorrect Server Address or Port");
            errorLabel.setForeground(Color.red);
            panel.add(errorLabel);
        }
        int result = JOptionPane.showConfirmDialog(null, panel, "Connect To Server",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String userName = username.getText();
            String portName = ip.getText();
            int portNumber = Integer.parseInt(port.getText());
            
            BoardClient.startClient(userName, portName, portNumber);
        } 
    }

}

