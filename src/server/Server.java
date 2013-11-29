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
import name.Identifiable;
import name.Identifier;
import packet.Packet;
import packet.PacketBoardIdentifierList;
import packet.PacketJoinBoard;
import packet.PacketNewBoard;
import canvas.Canvas2d;
import canvas.DrawableBase;

public class Server implements Identifiable {
    public static final int DEFAULT_PORT = 4444;
    
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
            clients.add(handler);
            
            new Thread(handler).start();
            
            // Update the new client's list of boards.
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
        return new PacketBoardIdentifierList(boards.keySet().toArray(new BoardIdentifier[boards.keySet().size()]));
    }

    public void notifyBoardListChanged() {
        broadcastPacketToAllClients(constructBoardIdentifierListPacket());
    }

    public BoardModel newBoard(PacketNewBoard packet) {
        BoardIdentifier boardName = packet.boardName();
        int width = packet.width();
        int height = packet.height();
        
        // Create a new model under this boardName.
        DrawableBase canvas = new Canvas2d(width, height);
        BoardModel model = new BoardModel(boardName, canvas);
        addBoard(boardName, model);
        
        return model;
    }
    
    public BoardModel joinBoard(PacketJoinBoard packet) {
        BoardIdentifier boardName = packet.boardName();
        BoardModel model = boards.get(boardName);
        
        return model;
    }

    @Override
    public Identifier identifier() {
    	throw new UnsupportedOperationException();
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
