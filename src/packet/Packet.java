package packet;

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class Packet {
    
    private final PacketType packetType;
    private final BoardName boardName;
    
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
            
        case PacketTypeDisconnectClient:
            packet = PacketDisconnectClient.createPacketWithDataInternal(jobject);
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
    
    protected Packet(PacketType packetType) {
        // BoardName is not used
        this(packetType, new BoardName(0, ""));
    }
    
    protected Packet(PacketType packetType, BoardName boardName) {
        if (boardName.id() < 0) {
            throw new IllegalArgumentException("BoardID must be non-negative.");
        }
        
        this.packetType = packetType;
        this.boardName = boardName;
    }
    
    protected abstract void addPayloadToData(HashMap<Object, Object> data);
    
    public String data() {
        HashMap<Object, Object> data = new HashMap<Object, Object>();
        data.put("packetType", packetType.ordinal());
        data.put("boardName", boardName);
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((boardName == null) ? 0 : boardName.hashCode());
        result = prime * result
                + ((packetType == null) ? 0 : packetType.hashCode());
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
        return true;
    }
    
}
