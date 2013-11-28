package packet;

import name.BoardIdentifier;
import name.ClientIdentifier;


public final class PacketNewBoard extends Packet {
    private static final long serialVersionUID = -3720287069109037134L;
    private final int width;
    private final int height;
    
    public PacketNewBoard(BoardIdentifier boardName, ClientIdentifier senderName, int width, int height) {
        super(PacketType.NEW_BOARD, boardName, senderName);
        this.width = width;
        this.height = height;
    }
    
    public int width() {
        return width;
    }
    
    public int height() {
        return height;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + height;
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PacketNewBoard other = (PacketNewBoard) obj;
		if (height != other.height)
			return false;
		if (width != other.width)
			return false;
		return true;
	}
}
