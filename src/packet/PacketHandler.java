package packet;


abstract public class PacketHandler {
    
    /*
     * Methods for receiving packets.
     */
    protected void receivedPacket(Packet packet) {
        PacketType packetType = packet.packetType();
        
        switch(packetType) {
        
        case PacketTypeNewClient:
            receivedNewClientPacket((PacketNewClient) packet);
            break;
            
        case PacketTypeNewBoard:
            receivedNewBoardPacket((PacketNewBoard) packet);
            break;
            
        case PacketTypeJoinBoard:
            receivedJoinBoardPacket((PacketJoinBoard) packet);
            break;
            
        case PacketTypeExitBoard:
            receivedExitBoardPacket((PacketExitBoard) packet);
            break;
            
        case PacketTypeBoardModel:
            receivedBoardModelPacket((PacketBoardModel) packet);
            break;
            
        case PacketTypeBoardIdentifierList:
            receivedBoardIdentifierListPacket((PacketBoardIdentifierList) packet);
            break;
            
        case PacketTypeDrawCommand:
            receivedDrawCommandPacket((PacketDrawCommand) packet);
            break;
            
        default:
            throw new InvalidPacketTypeException();
            
        }
    }
    
    protected abstract void receivedNewClientPacket(PacketNewClient packet);
    protected abstract void receivedNewBoardPacket(PacketNewBoard packet);
    protected abstract void receivedJoinBoardPacket(PacketJoinBoard packet);
    protected abstract void receivedExitBoardPacket(PacketExitBoard packet);
    protected abstract void receivedBoardModelPacket(PacketBoardModel packet);
    protected abstract void receivedBoardIdentifierListPacket(PacketBoardIdentifierList packet);
    protected abstract void receivedDrawCommandPacket(PacketDrawCommand packet);
}
