package server;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import name.Identifiable;
import name.Identifier;
import packet.Packet;
import packet.PacketHandler;

public abstract class SocketHandler extends PacketHandler implements Runnable, Identifiable {
    private final Socket socket;

    protected final ObjectOutputStream out;
    protected final ObjectInputStream in;
    
    protected Identifier identifier;
    
    protected SocketHandler(Socket socket) throws IOException {
        this.socket = socket;
        
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
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
            for (Object obj = in.readObject(); obj != null; obj = in.readObject()) {
                receivedPacket((Packet)obj);
            }
        }  
        catch (EOFException e) {
            // do nothing.
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            connectionClosed();
            out.close();
            in.close();
        }   
    }

    protected void connectionClosed() { }

    public void sendPacket(Packet packet) {
        try {
            out.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Identifier identifier() {
        return identifier;
    }
}
