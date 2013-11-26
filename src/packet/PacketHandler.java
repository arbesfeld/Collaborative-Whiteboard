package packet;

import java.io.PrintWriter;

abstract public class PacketHandler {
    
    /*
     * Methods for receiving packets.
     */
    protected void receivedPacket(Packet packet, PrintWriter out) {
        PacketType packetType = packet.packetType();
        
        switch(packetType) {
        
        case PacketTypeNewClient:
            receivedNewClientPacket((PacketNewClient) packet, out);
            break;
            
        case PacketTypeDisconnectClient:
            receivedDisconnectClientPacket((PacketDisconnectClient) packet, out);
            break;
            
        case PacketTypeGameState:
            receivedGameStatePacket((PacketGameState) packet, out);
            break;
            
        case PacketTypeBoardState:
            receivedBoardStatePacket((PacketBoardState) packet, out);
            break;
            
        case PacketTypeDrawPixel:
            receivedDrawPixelPacket((PacketDrawPixel) packet, out);
            break;
            
        default:
            throw new InvalidPacketTypeException();
            
        }
    }
    
    protected abstract void receivedNewClientPacket(PacketNewClient packet, PrintWriter out);
    protected abstract void receivedDisconnectClientPacket(PacketDisconnectClient packet, PrintWriter out);
    protected abstract void receivedGameStatePacket(PacketGameState packet, PrintWriter out);
    protected abstract void receivedBoardStatePacket(PacketBoardState packet, PrintWriter out);
    protected abstract void receivedDrawPixelPacket(PacketDrawPixel packet, PrintWriter out);
}
