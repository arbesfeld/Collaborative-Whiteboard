package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import models.BoardModel;
import name.BoardIdentifier;
import name.ClientIdentifier;
import name.Identifiable;
import name.Identifier;
import packet.Packet;
import packet.PacketBoardIdentifierList;
import packet.PacketBoardModel;
import packet.PacketDrawPixel;
import packet.PacketExitBoard;
import packet.PacketJoinBoard;
import packet.PacketNewBoard;
import canvas.DrawableServer;

public class BoardServer implements Identifiable {
    public static final int DEFAULT_PORT = 4444;
    
    // The name used by the server for constructing packets.
    public static final ClientIdentifier SERVER_NAME = new ClientIdentifier(0, "server");
    
	private final ServerSocket serverSocket; 

	private final Map<BoardIdentifier, BoardModel> boards;
    private final Set<ClientHandler> clients;
	
	public BoardServer(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.boards = Collections.synchronizedMap(new HashMap<BoardIdentifier, BoardModel>());
        this.clients = Collections.synchronizedSet(new HashSet<ClientHandler>());
	}
	
	public void serve() throws IOException {
		while (true) {
			final Socket socket = serverSocket.accept();
			ClientHandler handler = new ClientHandler(socket, this);
            
            new Thread(handler).start();
            
            // Update the new client's list of boards.
            clients.add(handler);
            handler.sendPacket(constructBoardIdentifierListPacket());
	      
		}
	}

    public void removeClient(ClientHandler handler) {
        clients.remove(handler);
    }

    private void addBoard(BoardIdentifier boardName, BoardModel model) {
        assert !boards.containsKey(boardName);
        boards.put(boardName, model);
    }
    
    /**
     * Broadcast a packet to all the clients of a specific model.
     * @param model
     * @param packet
     */
    private void broadcastPacketToBoard(BoardIdentifier boardName, Packet packet) {
        for (Identifiable handler : boards.get(boardName).users()) {
            assert clients.contains(handler);
            ((ClientHandler) handler).sendPacket(packet);
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
    
    private PacketBoardIdentifierList constructBoardIdentifierListPacket() {
        return new PacketBoardIdentifierList(boards.keySet().toArray(new BoardIdentifier[boards.keySet().size()]), SERVER_NAME);
    }

    private void notifyBoardListChanged() {
        broadcastPacketToAllClients(constructBoardIdentifierListPacket());
    }

    public void newBoard(ClientHandler handler, PacketNewBoard packet) {
        BoardIdentifier boardName = packet.boardName();
        int width = packet.width();
        int height = packet.height();
        
        // Create a new model under this boardName.
        DrawableServer drawable = new DrawableServer(width, height);
        BoardModel model = new BoardModel(boardName, drawable);
        addBoard(boardName, model);
        
        // Tell the user to start his board.
        handler.sendPacket(new PacketBoardModel(model, SERVER_NAME));
        model.addUser(handler);
        
        // Announce that a new board has been added.
        notifyBoardListChanged();
    }
    
    public void joinBoard(ClientHandler handler, PacketJoinBoard packet) {
        BoardIdentifier boardName = packet.boardName();
        
        BoardModel model = boards.get(boardName);
        handler.sendPacket(new PacketBoardModel(model, SERVER_NAME));

        // Tell the other clients on the board that a new 
        // client has joined.
        broadcastPacketToBoard(boardName, packet);

        model.addUser(handler);
    }

    public void exitBoard(ClientHandler handler, PacketExitBoard packet) {  
        BoardIdentifier boardName = packet.boardName();
        
        // Broadcast the packet to all the clients of the board.
        broadcastPacketToBoard(boardName, packet);
        boards.get(boardName).removeUser(handler);
    }

    public void drawPixel(PacketDrawPixel packet) {
        BoardIdentifier boardName = packet.boardName();
        
        assert boards.containsKey(boardName);
        
        BoardModel model = boards.get(boardName);
        model.drawPixel(packet.pixel());
        
        broadcastPacketToBoard(boardName, packet);
    }

    @Override
    public Identifier identifier() {
        return SERVER_NAME;
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
