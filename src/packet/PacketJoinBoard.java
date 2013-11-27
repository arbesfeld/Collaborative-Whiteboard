package packet;

import java.util.HashMap;

import name.BoardIdentifier;
import name.ClientIdentifier;

import com.google.gson.JsonObject;

public final class PacketJoinBoard extends Packet {
    /**
     * 
     * @param data JSON representation of the data.
     * @return the constructed Packet
     */
    protected static Packet createPacketWithDataInternal(JsonObject data) {
	    BoardIdentifier boardName = getBoardName(data);
	    ClientIdentifier senderName = getSenderName(data);
	    
        return new PacketJoinBoard(boardName, senderName);
    }
    
    public PacketJoinBoard(BoardIdentifier boardName, ClientIdentifier senderName) {
        super(PacketType.PacketTypeJoinBoard, boardName, senderName);
    }
    
    /**
     * See specification in superclass Packet.
     */
    @Override
    protected void addPayloadToData(HashMap<Object, Object> data) {
    	// Do nothing.
    }
    
}
