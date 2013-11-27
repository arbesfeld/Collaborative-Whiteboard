package client;

import java.io.IOException;

import javax.swing.SwingUtilities;

public class BoardClient {
    
    public static void startClient(String userName, String hostName, int portNumber) throws IOException {

        final BoardClientController controller = new BoardClientController(userName, hostName, portNumber);
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BoardClientGUI view = new BoardClientGUI();
                view.setController(controller);
                controller.setView(view);
                
                new Thread(controller).start();    
                
                // Display the window.
                view.setSize(450, 260);
                view.setVisible(true);
            }
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new PopupGUI();
                } catch (Exception e) {
                    System.out.println("Connection refused.");
                    run();
                }
            }
        });
    }
    
}
