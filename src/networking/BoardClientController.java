package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BoardClientController implements Runnable {

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private BoardClientView view;
    
    BoardClientController(Socket socket, BoardClientView view) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
        this.view = view;
    }
    
    /**
     *  Handles the input and output to the client.
     *  @throws IOException
     */
    private void handleConnection() throws IOException {
        try {
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                
            }
        } finally {
            in.close();
            out.close();
        }
    }
    

    /**
     * Start the thread.
     */
    @Override
    public void run() {
        try {
            handleConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
