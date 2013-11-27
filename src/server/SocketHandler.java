package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import name.Identifiable;
import name.Name;
import name.User;
import packet.Packet;
import packet.PacketHandler;

public abstract class SocketHandler extends PacketHandler implements Runnable, Identifiable {
    private final Socket socket;

    protected final PrintWriter out;
    protected final BufferedReader in;
    
    protected User user;
    
    protected SocketHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    
    @Override
    public final void run() {
        try {
            handleConnection();
        } catch (IOException e) {
            e.printStackTrace(); // but don't terminate the serve()
        } finally {
            // close the socket at the end
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected final void handleConnection() throws IOException {
        try {
            beforeConnection();
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                Packet packet = Packet.createPacketWithData(line);
                receivedPacket(packet);
            }
        }  
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            afterConnection();
            out.close();
            in.close();
        }   
    }

    protected void sendPacket(Packet packet) {
        out.println(packet.data());
    }
    
    // Optional to implement.
    protected void beforeConnection() { }
    protected void afterConnection() { }
    
    @Override
    public Name identifier() {
        return user;
    }
}
