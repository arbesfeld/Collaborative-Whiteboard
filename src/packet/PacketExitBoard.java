package packet;

import java.util.HashMap;

import name.BoardName;
import name.User;

import com.google.gson.JsonObject;

public final class PacketExitBoard extends Packet {
	
    /**
     * 
     * @param data JSON representation of the data.
     * @return the constructed Packet
     */
    protected static Packet createPacketWithDataInternal(JsonObject data) {
    	BoardName boardName = getBoardName(data);
        User senderName = getSenderName(data);
        
        return new PacketExitBoard(boardName, senderName);
    }
    
    public PacketExitBoard(BoardName boardName, User senderName) {
        super(PacketType.PacketTypeExitBoard, boardName, senderName);
    }
    
    /**
     * See specification in superclass Packet.
     */
    @Override
    protected void addPayloadToData(HashMap<Object, Object> data) {
    	// Nothing to add.
    }
    
}