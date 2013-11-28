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
import packet.PacketDrawCommand;
import packet.PacketExitBoard;
import packet.PacketJoinBoard;
import packet.PacketNewBoard;
import canvas.Canvas2d;
import canvas.DrawableBase;
import canvas.command.DrawCommand;

public class Server implements Identifiable {
    public static final int DEFAULT_PORT = 4444;
    
    // The name used by the server for constructing packets.
    public static final ClientIdentifier SERVER_NAME = new ClientIdentifier(0, "server");
    
	private final ServerSocket serverSocket; 

	private final Map<BoardIdentifier, BoardModel> boards;
    private final Set<ServerSocketHandler> clients;
	
	public Server(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.boards = Collections.synchronizedMap(new HashMap<BoardIdentifier, BoardModel>());
        this.clients = Collections.synchronizedSet(new HashSet<ServerSocketHandler>());
	}
	
	public void serve() throws IOException {
		while (true) {
			final Socket socket = serverSocket.accept();
			ServerSocketHandler handler = new ServerSocketHandler(socket, this);
            
            new Thread(handler).start();
            
            // Update the new client's list of boards.
            clients.add(handler);
            handler.sendPacket(constructBoardIdentifierListPacket());
		}
	}

    public void removeClient(ServerSocketHandler handler) {
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
            ((ServerSocketHandler) handler).sendPacket(packet);
        }
    }
    
    /**
     * Broadcast a packet to all clients.
     * @param packet
     */
    private void broadcastPacketToAllClients(Packet packet) {
        synchronized(clients) {
            for (ServerSocketHandler handler : clients) {
                handler.sendPacket(packet);
            }
        }
    }
    
    private Packet constructBoardIdentifierListPacket() {
        return new PacketBoardIdentifierList(boards.keySet().toArray(new BoardIdentifier[boards.keySet().size()]), SERVER_NAME);
    }

    private void notifyBoardListChanged() {
        broadcastPacketToAllClients(constructBoardIdentifierListPacket());
    }

    public void newBoard(ServerSocketHandler handler, PacketNewBoard packet) {
        BoardIdentifier boardName = packet.boardName();
        int width = packet.width();
        int height = packet.height();
        
        // Create a new model under this boardName.
        DrawableBase canvas = new Canvas2d(width, height);
        BoardModel model = new BoardModel(boardName, canvas);
        addBoard(boardName, model);
        
        // Tell the user to start his board.
        handler.sendPacket(new PacketBoardModel(model, SERVER_NAME));
        model.addUser(handler);
        
        // Announce that a new board has been added.
        notifyBoardListChanged();
    }
    
    public void joinBoard(ServerSocketHandler handler, PacketJoinBoard packet) {
        BoardIdentifier boardName = packet.boardName();
        
        BoardModel model = boards.get(boardName);
        handler.sendPacket(new PacketBoardModel(model, SERVER_NAME));

        // Tell the other clients on the board that a new 
        // client has joined.
        broadcastPacketToBoard(boardName, packet);

        model.addUser(handler);
    }

    public void exitBoard(ServerSocketHandler handler, PacketExitBoard packet) {  
        BoardIdentifier boardName = packet.boardName();
        
        // Broadcast the packet to all the clients of the board.
        broadcastPacketToBoard(boardName, packet);
        boards.get(boardName).removeUser(handler);
    }

    public void drawCommand(PacketDrawCommand packet) {
        BoardIdentifier boardName = packet.boardName();
        
        assert boards.containsKey(boardName);
        
        BoardModel model = boards.get(boardName);
        
        assert model != null;
        
        DrawCommand command = packet.drawCommand();
        assert command != null;
        command.drawOn(model);
        
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
            Server server = new Server(port);
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}