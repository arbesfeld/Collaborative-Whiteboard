package BoardClient;

import models.ClientBoardModel;
import packet.User;
import util.Utils;

public class BoardClient {
  
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PopupGUI.popup();
            }
        });
    }
    
}
