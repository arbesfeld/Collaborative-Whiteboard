package server;

import java.io.IOException;
import java.net.Socket;

import name.Identifiable;
import packet.Packet;
import packet.PacketBoardIdentifierList;
import packet.PacketBoardModel;
import packet.PacketBoardUsers;
import packet.PacketClientReady;
import packet.PacketDrawCommand;
import packet.PacketExitBoard;
import packet.PacketJoinBoard;
import packet.PacketLayerAdjustment;
import packet.PacketMessage;
import packet.PacketNewBoard;
import packet.PacketNewClient;
import packet.PacketNewLayer;
import canvas.command.DrawCommand;

public class ServerSocketHandler extends SocketHandler {
	private static final long serialVersionUID = 5321337579877366643L;
	
	private final transient Server server;
    private transient ServerSocketState state;
    
    public ServerSocketHandler(Socket socket, Server server) throws IOException {
        super(socket);
        this.server = server;
        this.state = ServerSocketState.INITIALIZING;
    }
    
    @Override 
    protected void connectionClosed() {
        server.removeClient(this);
        if (model != null && model.containsUser(this)) {
        	model.removeUser(this);
        }
    }
    
    @Override
    public void receivedNewClientPacket(PacketNewClient packet) {
        assert this.identifier == null;
        assert state == ServerSocketState.INITIALIZING;
        state = ServerSocketState.IDLE;
        
        this.identifier = packet.senderName();
    }
    
    @Override
    public void receivedNewBoardPacket(PacketNewBoard packet) {
        assert model == null;
        model = server.newBoard(packet);
        
        // Tell the user to start his board.
        sendPacket(new PacketBoardModel(model));
        
        // Announce that a new board has been added.
        server.notifyBoardListChanged();    
    }
    
    @Override
    public void receivedJoinBoardPacket(PacketJoinBoard packet) {
        assert model == null;
        // First send a GameState packet, then start sending the client new pixel locations.
        model = server.joinBoard(packet);

        sendPacket(new PacketBoardModel(model));
    }

    @Override
    public void receivedExitBoardPacket(PacketExitBoard packet) {
        assert state == ServerSocketState.PLAYING;
        state = ServerSocketState.IDLE;
        
        assert model != null;

        model.removeUser(this);
        sendPacket(new PacketExitBoard());
        notifyBoardUsersChanged();
        model = null;
    }

	private void notifyBoardUsersChanged() {
		assert model != null;
		broadcastPacketToBoard(new PacketBoardUsers(model.users()));
	}

	@Override
	public void receivedClientReadyPacket(PacketClientReady packet) {
        assert state == ServerSocketState.IDLE;
        state = ServerSocketState.PLAYING;
        
		model.addUser(this);
        notifyBoardUsersChanged();
	}

    @Override
    public void receivedDrawCommandPacket(PacketDrawCommand packet) {
        assert state == ServerSocketState.PLAYING;
        assert model != null;
        
        DrawCommand command = packet.drawCommand();
        assert command != null;
        command.drawOn(model);
        
        broadcastPacketToBoard(packet);
    }
    
	@Override
	public void receivedMessagePacket(PacketMessage packet) {
		broadcastPacketToBoard(packet);
	}


	@Override
	public void receivedLayerAdjustmentPacket(
			PacketLayerAdjustment packet) {
        assert state == ServerSocketState.PLAYING;
        assert model != null;
		model.adjustLayer(packet.layer(), packet.adjustment());
		broadcastPacketToBoard(packet);
	}

	@Override
	public void receivedNewLayerPacket(PacketNewLayer packet) {
        assert state == ServerSocketState.PLAYING;
        assert model != null;
		model.addLayer(packet.layerName());
		broadcastPacketToBoard(packet);
	}
	
    @Override
    public void receivedBoardModelPacket(PacketBoardModel packet) {
        // We only send GameStatePackets from the server.
        assert false;
    }
    
    @Override
    public void receivedBoardIdentifierListPacket(PacketBoardIdentifierList packet) {
        // We only send BoardStatePackets from the server.
        assert false;
    }
    
	@Override
	public void receivedBoardUsersPacket(PacketBoardUsers packet) {
		// Only server to client.
		assert false;
	}
	
    /**
     * Broadcast a packet to all the clients of a specific model.
     * @param model
     * @param packet
     */
    private void broadcastPacketToBoard(Packet packet) {
        for (Identifiable handler : model.users()) {
            ((ServerSocketHandler) handler).sendPacket(packet);
        }
    }
}