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
import packet.User;
import pixel.Pixel;

import models.ServerBoardModel;


public class BoardServer extends PacketHandler {
	private final ServerSocket serverSocket;
	private final Map<BoardName, ServerBoardModel> boards;
    private final Map<User, PrintWriter> users;
	private final int SIZE = 256;
	
	public BoardServer(int port) throws IOException // TODO add more args here
	{
		serverSocket = new ServerSocket(port);
		this.boards = new HashMap<BoardName, ServerBoardModel>();
        this.users = new HashMap<User, PrintWriter>();
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
	
	private void broadcastPacket(ServerBoardModel model, Packet packet) {
        for (User user : model.users()) {
            PrintWriter out = users.get(user);
            sendPacket(packet, out);
        }
	}
	
    @Override
    protected void receivedNewClientPacket(PacketNewClient packet,
            PrintWriter out) {
        User user = packet.user();
        
        // If this is a new user, cache his output stream.
        if (!users.containsKey(user)) {
            users.put(user, out);
        }
        
        BoardName boardName = packet.boardName();
        
        ServerBoardModel model;
        
        // Check if we have created a board with this board name.
        if (boards.containsKey(boardName)) {
            // A new client has connected.
            
            model = boards.get(boardName);
            
            // Tell the other users on the board that a new 
            // client has joined.
            broadcastPacket(model, packet);
        } else {
            // Create a new model under this boardName.
            model = new ServerBoardModel(boardName, SIZE, SIZE);
            boards.put(boardName, model);
        }
        
        sendPacket(model.constructGameStatePacket(), out);
    }

    @Override
    protected void receivedDisconnectClientPacket(
            PacketDisconnectClient packet, PrintWriter out) {
        BoardName boardName = packet.boardName();
        
        // We surely must have already initialized this board.
        assert boards.containsKey(boardName);
        
        ServerBoardModel model = boards.get(boardName);
        
        // Broadcast the packet to all the users of the board.
        broadcastPacket(model, packet);
    }

    @Override
    protected void receivedGameStatePacket(PacketGameState packet,
            PrintWriter out) {
        // We only send GameStatePackets from the server.
        assert false;
    }

    @Override
    protected void receivedBoardStatePacket(PacketBoardState packet,
            PrintWriter out) {
        // We only send BoardStatePackets from the server.
        assert false;
    }

    @Override
    protected void receivedDrawPixelPacket(PacketDrawPixel packet,
            PrintWriter out) {
        BoardName boardName = packet.boardName();
        Pixel pixel = packet.pixel();
        
        assert boards.containsKey(boardName);
        
        ServerBoardModel model = boards.get(boardName);
        model.putPixel(pixel);
        
        broadcastPacket(model, packet);
    }
	
    private void sendPacket(Packet packet, PrintWriter out) {
        out.println(packet.data());
    }
}
