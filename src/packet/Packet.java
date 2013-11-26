package packet;

import java.util.HashMap;

import name.BoardName;
import name.User;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class Packet {
    
    private final PacketType packetType;
    private final BoardName boardName;
    private final User senderName;
    
    public static Packet createPacketWithData(String data) {
        // Format of all packets is { "packetType": <Integer>, ... }
        JsonElement jelement = new JsonParser().parse(data);
        JsonObject jobject = jelement.getAsJsonObject();
        
        // Get the packet type from JSON object.
        int packetTypeNumber = jobject.get("packetType").getAsInt();
       
        PacketType packetType = null;
        try {
            packetType = PacketType.values()[packetTypeNumber];
        } catch (IndexOutOfBoundsException err) {
            // We received a packet type that we do not recognize.
            throw new InvalidPacketTypeException();
        }
        assert packetType != null;
        
        Packet packet;
        switch (packetType) {
        
        case PacketTypeNewClient:
            packet = PacketNewClient.createPacketWithDataInternal(jobject);
            break;
            
        case PacketTypeJoinBoard:
            packet = PacketJoinBoard.createPacketWithDataInternal(jobject);
            break;
            
        case PacketTypeExitBoard:
            packet = PacketExitBoard.createPacketWithDataInternal(jobject);
            break;
            
        case PacketTypeGameState:
            packet = PacketGameState.createPacketWithDataInternal(jobject);
            break;
            
        case PacketTypeBoardState:
            packet = PacketBoardState.createPacketWithDataInternal(jobject);
            break;
            
        case PacketTypeDrawPixel:
            packet = PacketDrawPixel.createPacketWithDataInternal(jobject);
            break;
            
        default:
            throw new InvalidPacketTypeException();
            
        }
        
        return packet;
    }
    
    protected Packet(PacketType packetType, User senderName) {
        // BoardName is not used
        this(packetType, BoardName.NULL_BOARD, senderName);
    }
    
    protected Packet(PacketType packetType, BoardName boardName, User senderName) {
        if (boardName.id() < 0) {
            throw new IllegalArgumentException("BoardID must be non-negative.");
        }
        
        this.packetType = packetType;
        this.boardName = boardName;
        this.senderName = senderName;
    }
    
    protected abstract void addPayloadToData(HashMap<Object, Object> data);
    
    public String data() {
        HashMap<Object, Object> data = new HashMap<Object, Object>();
        data.put("packetType", packetType.ordinal());
        data.put("boardName", boardName);
        data.put("senderName", senderName);
        addPayloadToData(data);
        
        // Convert data to JSON format.
        Gson gson = new Gson();
        return gson.toJson(data);
    }
    
    public PacketType packetType() {
        return packetType;
    }
    
    public BoardName boardName() {
        return boardName;
    }
    
    public User senderName() {
    	return senderName;
    }

    protected static BoardName getBoardName(JsonObject data) {
        JsonObject board = data.get("boardName").getAsJsonObject();
        int boardId = board.get("id").getAsInt();
        String boardName = board.get("name").getAsString();
        return new BoardName(boardId, boardName);
    }

    protected static User getSenderName(JsonObject data) {
        JsonObject sender = data.get("senderName").getAsJsonObject();
        int senderId = sender.get("id").getAsInt();
        String senderName = sender.get("name").getAsString();
        return new User(senderId, senderName);
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
