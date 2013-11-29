package packet;

import name.ClientIdentifier;

public final class PacketNewClient extends Packet {
    private static final long serialVersionUID = -6603635078887371475L;
    
    private final ClientIdentifier senderName;
    
    public PacketNewClient(ClientIdentifier senderName) {
        super();
        this.senderName = senderName;
    }
    
    public ClientIdentifier senderName() {
    	return senderName;
    }

	@Override
	public void process(PacketHandler handler) {
		handler.receivedNewClientPacket(this);
	}
}
