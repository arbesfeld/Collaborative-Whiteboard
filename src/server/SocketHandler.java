package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import packet.PacketHandler;

public abstract class SocketHandler extends PacketHandler implements Runnable {
    private final Socket socket;

    // write to the server
    protected final PrintWriter out;
    
    // read from the server
    protected final BufferedReader in;
    
    protected SocketHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    
    @Override
    public void run() {
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
    
    protected abstract void handleConnection() throws IOException;
}
