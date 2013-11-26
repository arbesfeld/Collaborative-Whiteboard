package packet;

import java.util.HashMap;

import name.BoardName;
import name.User;

import com.google.gson.JsonObject;

public final class PacketNewClient extends Packet {
    /**
     * 
     * @param data JSON representation of the data.
     * @return the constructed Packet
     */
    protected static Packet createPacketWithDataInternal(JsonObject data) {
	    BoardName boardName = getBoardName(data);
	    assert boardName.equals(BoardName.NULL_BOARD);
	    
	    User senderName = getSenderName(data);
	    
        return new PacketNewClient(senderName);
    }
    
    public PacketNewClient(User senderName) {
        super(PacketType.PacketTypeNewClient, senderName);
    }
    
    /**
     * See specification in superclass Packet.
     */
    @Override
    protected void addPayloadToData(HashMap<Object, Object> data) {
    	// Do nothing.
    }
    
}
