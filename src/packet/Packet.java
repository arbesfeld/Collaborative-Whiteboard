package packet;

import java.io.Serializable;

import name.BoardIdentifier;
import name.ClientIdentifier;

public abstract class Packet implements Serializable {
	private static final long serialVersionUID = -2598835099616000879L;
	
	private final PacketType packetType;
    private final BoardIdentifier boardName;
    private final ClientIdentifier senderName;
    
    protected Packet(PacketType packetType, ClientIdentifier senderName) {
        // BoardName is not used
        this(packetType, BoardIdentifier.NULL_BOARD, senderName);
    }
    
    protected Packet(PacketType packetType, BoardIdentifier boardName, ClientIdentifier senderName) {
        if (boardName.id() < 0) {
            throw new IllegalArgumentException("BoardID must be non-negative.");
        }
        
        this.packetType = packetType;
        this.boardName = boardName;
        this.senderName = senderName;
    }
    
    public PacketType packetType() {
        return packetType;
    }
    
    public BoardIdentifier boardName() {
        return boardName;
    }
    
    public ClientIdentifier senderName() {
        return senderName;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((boardName == null) ? 0 : boardName.hashCode());
		result = prime * result
				+ ((packetType == null) ? 0 : packetType.hashCode());
		result = prime * result
				+ ((senderName == null) ? 0 : senderName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Packet other = (Packet) obj;
		if (boardName == null) {
			if (other.boardName != null)
				return false;
		} else if (!boardName.equals(other.boardName))
			return false;
		if (packetType != other.packetType)
			return false;
		if (senderName == null) {
			if (other.senderName != null)
				return false;
		} else if (!senderName.equals(other.senderName))
			return false;
		return true;
	}
    
}
