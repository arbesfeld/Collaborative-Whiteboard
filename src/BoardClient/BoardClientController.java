package BoardClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import models.ClientBoardModel;
import packet.BoardName;
import packet.Packet;
import packet.PacketDisconnectClient;
import packet.PacketNewClient;

public class BoardClientController implements Runnable {
    
    // Communication.
    private final Socket socket;
    
    // write to the server
    private final PrintWriter out;
    
    // read from the server
    private final BufferedReader in;
    
    private final BoardClient client;
    
    public BoardClientController(BoardClient client, String hostName, int portNumber) throws IOException {
        this.client = client;
        this.socket = new Socket(hostName, portNumber);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
    
    private void sendPacket(Packet packet) {
        out.println(packet.data());
    }
    
    public void connectToBoard(BoardName boardName) {
        PacketNewClient packet = new PacketNewClient(client.name(), boardName);
        sendPacket(packet);
    }
    
    public void disconnectFromBoard(BoardName boardName) {
        PacketDisconnectClient packet = new PacketDisconnectClient(client.name(), boardName);
        sendPacket(packet);
    }
}
