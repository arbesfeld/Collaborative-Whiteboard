package packet;

import java.util.HashMap;

import name.BoardIdentifier;
import name.ClientIdentifier;

import com.google.gson.JsonObject;

public final class PacketNewClient extends Packet {
    /**
     * 
     * @param data JSON representation of the data.
     * @return the constructed Packet
     */
    protected static Packet createPacketWithDataInternal(JsonObject data) {
	    BoardIdentifier boardName = getBoardName(data);
	    assert boardName.equals(BoardIdentifier.NULL_BOARD);
	    
	    ClientIdentifier senderName = getSenderName(data);
	    
        return new PacketNewClient(senderName);
    }
    
    public PacketNewClient(ClientIdentifier senderName) {
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
