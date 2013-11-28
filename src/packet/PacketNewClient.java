package packet;

import name.ClientIdentifier;

public final class PacketNewClient extends Packet {
    private static final long serialVersionUID = -6603635078887371475L;
    
    public PacketNewClient(ClientIdentifier senderName) {
        super(PacketType.PacketTypeNewClient, senderName);
    }
    
    
}
