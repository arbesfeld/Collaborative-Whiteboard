package networking;

import game.Board;
import game.GameState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import packet.Packet;

public class BoardClient implements Runnable {
    
    // Communication.
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private BlockingQueue<Packet> packetQueue;
    
    // Board handling.
    private Board board;
    private GameState gameState;
    
    BoardClient(Socket socket) throws IOException {
        // TODO: add ui stuff which sends packets to the server
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
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
}
