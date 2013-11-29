package packet;

import name.BoardIdentifier;
import name.Identifiable;

public class PacketBoardUsers extends Packet {
	private static final long serialVersionUID = 6138270598524186918L;
	
	private final Identifiable[] users;
    
    public PacketBoardUsers(Identifiable[] users) {
        this.users = users;
    }
    
    public Identifiable[] boardUsers() {
        return users;
    }

	@Override
	public void process(PacketHandler handler) {
		handler.receivedBoardUsersPacket(this);
	}

}
