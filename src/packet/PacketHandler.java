package packet;


abstract public class PacketHandler {
    
    /*
     * Methods for receiving packets.
     */
    protected void receivedPacket(Packet packet) {
        PacketType packetType = packet.packetType();
        
        switch(packetType) {
        
        case NEW_CLIENT:
            receivedNewClientPacket((PacketNewClient) packet);
            break;
            
        case NEW_BOARD:
            receivedNewBoardPacket((PacketNewBoard) packet);
            break;
            
        case JOIN_BOARD:
            receivedJoinBoardPacket((PacketJoinBoard) packet);
            break;
            
        case EXIT_BOARD:
            receivedExitBoardPacket((PacketExitBoard) packet);
            break;
            
        case BOARD_MODEL:
            receivedBoardModelPacket((PacketBoardModel) packet);
            break;
            
        case BOARD_IDENTIFIER_LIST:
            receivedBoardIdentifierListPacket((PacketBoardIdentifierList) packet);
            break;
            
        case DRAW_COMMAND:
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
