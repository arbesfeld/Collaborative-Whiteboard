package packet;

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class Packet {
    
    private final PacketType packetType;
    private final int boardID;
    
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
            
        case PacketTypeDrawPixel:
            packet = PacketDrawPixel.createPacketWithDataInternal(jobject);
            break;
            
        default:
            throw new InvalidPacketTypeException();
            
        }
        
        return packet;
    }
    
    protected Packet(PacketType packetType, int boardID) {
        if (boardID < 0) {
            throw new IllegalArgumentException("BoardID must be non-negative.");
        }
        
        this.packetType = packetType;
        this.boardID = boardID;
    }
    
    protected abstract void addPayloadToData(HashMap<Object, Object> data);
    
    public String data() {
        HashMap<Object, Object> data = new HashMap<Object, Object>();
        data.put("packetType", packetType.ordinal());
        data.put("boardID", boardID);
        addPayloadToData(data);
        
        // Convert data to JSON format.
        Gson gson = new Gson();
        return gson.toJson(data);
    }
    
    public PacketType packetType() {
        return packetType;
    }
    
    public int boardID() {
        return boardID;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + boardID;
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
        if (boardID != other.boardID)
            return false;
        if (packetType != other.packetType)
            return false;
        return true;
    }

}
