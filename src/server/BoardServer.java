package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import canvas.DrawableServer;
import canvas.Pixel;
import name.BoardName;
import name.Name;
import name.User;
import packet.Packet;
import packet.PacketBoardState;
import packet.PacketExitBoard;
import packet.PacketDrawPixel;
import packet.PacketGameState;
import packet.PacketJoinBoard;
import packet.PacketNewBoard;
import packet.PacketNewClient;
import models.BoardModel;

public class BoardServer {
    public static final int DEFAULT_PORT = 4444;
    
    // The name used by the server for constructing packets.
    public static final User SERVER_NAME = new User(0, "server");
    
	private final ServerSocket serverSocket;
	
	private class ServerModel extends BoardModel<ClientHandler, DrawableServer> {
        public ServerModel(BoardName boardName, DrawableServer canvas) {
            super(boardName, canvas);
        } 
    };

	private final Map<BoardName, ServerModel> boards;
    private final Set<ClientHandler> clients;
	
	public BoardServer(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.boards = Collections.synchronizedMap(new HashMap<BoardName, ServerModel>());
        this.clients = Collections.synchronizedSet(new HashSet<ClientHandler>());
	}
	
	public void serve() throws IOException {
		while (true) {
			final Socket socket = serverSocket.accept();
			new Thread(new ClientHandler(socket)).start();
		}
	}

    private class ClientHandler extends SocketHandler {
        public ClientHandler(Socket socket) throws IOException {
            super(socket);
        }

        @Override
        protected void beforeConnection() {
            assert !clients.contains(this);
            clients.add(this);
            
            // Update the new client's list of boards.
            sendPacket(constructBoardStatePacket());
        }
        
        @Override
        protected void afterConnection() {
            assert clients.contains(this);
            
            // Remove the instance of "out" from our cache.
            clients.remove(this);
        }
        
        @Override
        protected void receivedNewClientPacket(PacketNewClient packet) {
            User senderName = packet.senderName();

            assert this.user == null;
            this.user = senderName;
        }
        
        @Override
        protected void receivedNewBoardPacket(PacketNewBoard packet) {
            User sender = packet.senderName();
            BoardName boardName = packet.boardName();
            int width = packet.width();
            int height = packet.height();

            assert sender.equals(this.user);
            assert !boards.containsKey(boardName);
            
            // Create a new model under this boardName.
            DrawableServer drawable = new DrawableServer(width, height);
            ServerModel model = 
                    new ServerModel(boardName, drawable);
            boards.put(boardName, model);
            
            // Tell the user to start his board.
            sendPacket(model.constructGameStatePacket());
            model.addUser(this);
            
            // Announce that a new board has been added.
            broadcastPacketToAllClients(constructBoardStatePacket());
        }
        
        @Override
        protected void receivedJoinBoardPacket(PacketJoinBoard packet) {
            User sender = packet.senderName();
            BoardName boardName = packet.boardName();

            assert sender.equals(this.user);
            assert boards.containsKey(boardName);
            
            // A new client has connected to the board
            ServerModel model = boards.get(boardName);

            // Tell the other clients on the board that a new 
            // client has joined.
            broadcastPacketToBoard(model, packet);
            
            // First send a GameState packet, then start sending the client new pixel locations.
            sendPacket(model.constructGameStatePacket());
            model.addUser(this);
        }

        @Override
        protected void receivedExitBoardPacket(PacketExitBoard packet) {
            BoardName boardName = packet.boardName();

            assert packet.senderName().equals(this.user);
            
            // We surely must have already initialized this board.
            assert boards.containsKey(boardName);
            
            ServerModel model = boards.get(boardName);
            
            // Broadcast the packet to all the clients of the board.
            broadcastPacketToBoard(model, packet);
            model.removeUser(this);
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

            assert packet.senderName().equals(this.user);
            assert boards.containsKey(boardName);
            
            ServerModel model = boards.get(boardName);
            model.drawPixel(pixel);
            
            broadcastPacketToBoard(model, packet);
        }
    }
    
    /**
     * Broadcast a packet to all the clients of a specific model.
     * @param model
     * @param packet
     */
    private void broadcastPacketToBoard(ServerModel model, Packet packet) {
        for (ClientHandler handler : model.users()) {
            handler.sendPacket(packet);
        }
    }
    
    /**
     * Broadcast a packet to all clients.
     * @param packet
     */
    private void broadcastPacketToAllClients(Packet packet) {
        synchronized(clients) {
            for (ClientHandler handler : clients) {
                handler.sendPacket(packet);
            }
        }
    }
    
    private PacketBoardState constructBoardStatePacket() {
        return new PacketBoardState(boards.keySet().toArray(new BoardName[boards.keySet().size()]), SERVER_NAME);
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        
        if (args.length > 0)
            port = Integer.parseInt(args[0]);
        try {
            BoardServer server = new BoardServer(port);
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
