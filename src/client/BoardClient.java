package client;

import java.io.IOException;

import javax.swing.SwingUtilities;

public class BoardClient {
    
    public BoardClient(String userName, String hostName, int portNumber) throws IOException {
        final BoardClientController controller = new BoardClientController(userName, hostName, portNumber);
        new Thread(controller).start();        
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final BoardClientGUI view = new BoardClientGUI(controller);
                controller.setView(view);
                
                //Display the window.
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
                    e.printStackTrace();
                    run();
                }
            }
        });
    }
    
}
