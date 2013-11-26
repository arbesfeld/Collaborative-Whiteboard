package BoardClient;

public class BoardClient {
  
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PopupGUI.popup();
            }
        });
    }
    
}
