package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import name.BoardName;
import name.User;

import packet.Packet;
import packet.PacketBoardState;
import packet.PacketExitBoard;
import packet.PacketDrawPixel;
import packet.PacketGameState;
import packet.PacketJoinBoard;
import packet.PacketNewBoard;
import packet.PacketNewClient;
import packet.PacketHandler;
import packet.PacketType;
import pixel.Pixel;

import models.ServerBoardModel;


public class BoardServer extends PacketHandler {
    public static final String DEFAULT_PORT = "4444";
    
    // The name used by the server for constructing packets.
    public static final User SERVER_NAME = new User(0, "server");
    
	private final ServerSocket serverSocket;
	private final Map<BoardName, ServerBoardModel> boards;
    private final Map<User, PrintWriter> users;
	private final int SIZE = 512;
	
	public BoardServer(int port) throws IOException
	{
		this.serverSocket = new ServerSocket(port);
		this.boards = new HashMap<BoardName, ServerBoardModel>();
        this.users = new HashMap<User, PrintWriter>();
	}
	
	public static void main(String[] args) {
	    int port = Integer.parseInt(DEFAULT_PORT);
	    
	    if (args.length > 0)
	        port = Integer.parseInt(args[0]);
	    
	    try {
            BoardServer server = new BoardServer(port);
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
		
		// Update the new client's list of boards.
		sendPacket(constructBoardStatePacket(), out);

		try {
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				Packet packet = Packet.createPacketWithData(line);
				
				receivedPacket(packet);
				
				// Add ourselves to the output stream cache if this is a new client.
				if (packet.packetType() == PacketType.PacketTypeNewClient) {
					User senderName = packet.senderName();
					
					// We shouldn't have cached this user yet.
					assert !users.containsKey(senderName);
					
			        users.put(senderName, out);
				}
			}
		} finally {
			// Remove the instance of "out" from our cache.
			users.values().removeAll(Collections.singleton(out));
			
			out.close();
			in.close();
		}	
	}

    @Override
    protected void receivedNewClientPacket(PacketNewClient packet) {
    	// do nothing
    }
    
    @Override
    protected void receivedNewBoardPacket(PacketNewBoard packet) {
        User sender = packet.senderName();
        BoardName boardName = packet.boardName();
        int width = packet.width();
        int height = packet.height();
    	
        // Create a new model under this boardName.
        ServerBoardModel model = new ServerBoardModel(boardName, width, height);
        boards.put(boardName, model);
        model.addUser(sender);
        
        broadcastPacketToAllUsers(constructBoardStatePacket());
        
        // Tell the user to start his board.
        sendPacket(model.constructGameStatePacket(), users.get(sender));
    }
    
    @Override
    protected void receivedJoinBoardPacket(PacketJoinBoard packet) {
        User sender = packet.senderName();
        BoardName boardName = packet.boardName();
        
        assert boards.containsKey(boardName);
        
        // A new client has connected to the board
        ServerBoardModel model = boards.get(boardName);
        
        // First send a GameState packet, then start sending the client new pixel locations.
        sendPacket(model.constructGameStatePacket(), users.get(sender));
        model.addUser(sender);
        
        // Tell the other users on the board that a new 
        // client has joined.
        broadcastPacket(model, packet);
    }

    @Override
    protected void receivedExitBoardPacket(PacketExitBoard packet) {
        BoardName boardName = packet.boardName();
        
        // We surely must have already initialized this board.
        assert boards.containsKey(boardName);
        
        ServerBoardModel model = boards.get(boardName);
        
        // Broadcast the packet to all the users of the board.
        broadcastPacket(model, packet);
    }

    @Override
    protected void receivedGameStatePacket(PacketGameState packet) {
        // We only send GameStatePackets from the server.
        assert false;
    }

    @Override
    protected void receivedBoardStatePacket(PacketBoardState packet) {
        // We only send BoardStatePackets from the server.
        assert false;
    }

    @Override
    protected void receivedDrawPixelPacket(PacketDrawPixel packet) {
        BoardName boardName = packet.boardName();
        Pixel pixel = packet.pixel();
        
        assert boards.containsKey(boardName);
        
        ServerBoardModel model = boards.get(boardName);
        model.putPixel(pixel);
        
        broadcastPacket(model, packet);
    }
    
	/**
	 * Broadcast a packet to all the users of a specific model.
	 * @param model
	 * @param packet
	 */
	private void broadcastPacket(ServerBoardModel model, Packet packet) {
        for (User user : model.users()) {
            PrintWriter out = users.get(user);
            
            if (out != null) {
                sendPacket(packet, out);
            }
        }
	}
	
	/**
	 * Broadcast a packet to all users.
	 * @param packet
	 */
	private void broadcastPacketToAllUsers(Packet packet) {
	    for (PrintWriter out : users.values()) {
	        sendPacket(packet, out);
	    }
	}
	
	private PacketBoardState constructBoardStatePacket() {
	    return new PacketBoardState(boards.keySet().toArray(new BoardName[boards.keySet().size()]), SERVER_NAME);
	}
	
    private void sendPacket(Packet packet, PrintWriter out) {
        out.println(packet.data());
    }
}
