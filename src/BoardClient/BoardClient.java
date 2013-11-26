package BoardClient;

import java.io.IOException;

import javax.swing.SwingUtilities;

public class BoardClient {
    
    public BoardClient(String userName, String hostName, int portNumber) throws IOException {
        final BoardClientGUI view = new BoardClientGUI();
        BoardClientController controller = new BoardClientController(view, userName, hostName, portNumber);
        new Thread(controller).start();
        view.setController(controller);
        view.display();
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
