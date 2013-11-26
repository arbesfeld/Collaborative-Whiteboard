package packet;

import java.util.HashMap;

import name.BoardName;
import name.User;

import com.google.gson.JsonObject;

public final class PacketNewBoard extends Packet {
    private final int width;
    private final int height;
    
    /**
     * 
     * @param data JSON representation of the data.
     * @return the constructed Packet
     */
    protected static Packet createPacketWithDataInternal(JsonObject data) {
        int width = data.get("width").getAsInt();
        int height = data.get("height").getAsInt();
        
        BoardName boardName = getBoardName(data);
        User senderName = getSenderName(data);
        
        return new PacketNewBoard(boardName, senderName, width, height);
    }
    
    public PacketNewBoard(BoardName boardName, User senderName, int width, int height) {
        super(PacketType.PacketTypeNewBoard, boardName, senderName);
        this.width = width;
        this.height = height;
    }
    
    /**
     * See specification in superclass Packet.
     */
    @Override
    protected void addPayloadToData(HashMap<Object, Object> data) {
        data.put("width", width);
        data.put("height", height);
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
