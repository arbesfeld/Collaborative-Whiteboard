package server;

import java.io.IOException;
import java.net.Socket;

import packet.Packet;
import packet.PacketBoardIdentifierList;
import packet.PacketBoardModel;
import packet.PacketDrawCommand;
import packet.PacketExitBoard;
import packet.PacketJoinBoard;
import packet.PacketNewBoard;
import packet.PacketNewClient;

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
    }
    
    @Override
    protected void receivedPacket(Packet packet) {
        super.receivedPacket(packet);
        assert identifier().equals(packet.senderName());
    }
    
    @Override
    protected void receivedNewClientPacket(PacketNewClient packet) {
        assert this.identifier == null;
        assert state == ServerSocketState.INITIALIZING;
        state = ServerSocketState.IDLE;
        
        this.identifier = packet.senderName();
    }
    
    @Override
    protected void receivedNewBoardPacket(PacketNewBoard packet) {
        assert state == ServerSocketState.IDLE;
        state = ServerSocketState.PLAYING;
        
        server.newBoard(this, packet);
    }
    
    @Override
    protected void receivedJoinBoardPacket(PacketJoinBoard packet) {
        assert state == ServerSocketState.IDLE;
        state = ServerSocketState.PLAYING;
        
        // First send a GameState packet, then start sending the client new pixel locations.
        server.joinBoard(this, packet);
    }

    @Override
    protected void receivedExitBoardPacket(PacketExitBoard packet) {
        assert state == ServerSocketState.PLAYING;
        state = ServerSocketState.IDLE;
        
        server.exitBoard(this, packet);
    }

    @Override
    protected void receivedBoardModelPacket(PacketBoardModel packet) {
        // We only send GameStatePackets from the server.
        assert false;
    }

    @Override
    protected void receivedBoardIdentifierListPacket(PacketBoardIdentifierList packet) {
        // We only send BoardStatePackets from the server.
        assert false;
    }

    @Override
    protected void receivedDrawCommandPacket(PacketDrawCommand packet) {
        assert state == ServerSocketState.PLAYING;
        
        server.drawCommand(packet);
    }
}