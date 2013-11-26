package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import packet.BoardName;
import packet.InvalidPacketTypeException;
import packet.Packet;
import packet.PacketBoardState;
import packet.PacketDisconnectClient;
import packet.PacketDrawPixel;
import packet.PacketGameState;
import packet.PacketNewClient;
import packet.PacketType;
import packet.PacketHandler;

import models.ServerBoardModel;


public class BoardServer extends PacketHandler {
	private final ServerSocket serverSocket;
	private final Map<BoardName, ServerBoardModel> boards;
	
	public BoardServer(int port) throws IOException // TODO add more args here
	{
		serverSocket = new ServerSocket(port);
		this.boards = new HashMap<BoardName, ServerBoardModel>();
	}
	
	public void serve() throws IOException {
		while (true) {
			final Socket socket = serverSocket.accept();
			
			Thread thread = new Thread(new Runnable() {
				public void run() {
					//handle the client
					try {
						handleConnection(socket);
					} catch (IOException e) {
						e.printStackTrace(); // but don't terminate the serve()
					} finally {
						// close the socket at the end
						if (socket !=null) {
							try {
								socket.close();
							} catch (IOException e) {
								e.printStackTrace();
							
							}
						}
					}
					
				}
			});
			thread.start();
		}
	}
	
	private void handleConnection(Socket socket) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		try {
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				Packet packet = Packet.createPacketWithData(line);
				receivedPacket(packet, out);
			}
		} finally {
			out.close();
			in.close();
		}	
	}

    @Override
    protected void receivedNewClientPacket(PacketNewClient packet,
            PrintWriter out) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void receivedDisconnectClientPacket(
            PacketDisconnectClient packet, PrintWriter out) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void receivedGameStatePacket(PacketGameState packet,
            PrintWriter out) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void receivedBoardStatePacket(PacketBoardState packet,
            PrintWriter out) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void receivedDrawPixelPacket(PacketDrawPixel packet,
            PrintWriter out) {
        // TODO Auto-generated method stub
        
    }
	
}
