package BoardClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import packet.Packet;

public class BoardClientController implements Runnable {
    
    // Communication.
    private final Socket socket;
    
    // write to the server
    private final PrintWriter out;
    
    // read from the server
    private final BufferedReader in;
    
    public BoardClientController(String hostName, int portNumber) throws IOException {
        socket = new Socket(hostName, portNumber);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    
    /**
     * Start the thread.
     */
    @Override
    public void run() {
        try {
            handleServerPacket();
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
     *  Handles the input from the client.
     *  @throws IOException
     */
    private void handleServerPacket() throws IOException {
        try {
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                Packet packet = Packet.createPacketWithData(line);
            }
        } finally {
            in.close();
            out.close();
        }
    }
    
    public void sendPacket(Packet packet) {
        out.println(packet.data());
    }
}
