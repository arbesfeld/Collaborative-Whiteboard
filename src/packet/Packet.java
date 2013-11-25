package packet;

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class Packet {
    
    private final PacketType packetType;
    
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
        
        switch (packetType) {
        
        case PacketTypeNewClient:
            return PacketNewClient.createPacketWithDataInternal(jobject);
            
        default:
            throw new InvalidPacketTypeException();
            
        }
    }
    
    protected Packet(PacketType packetType) {
        this.packetType = packetType;
    }
    
    protected Packet() {
        throw new UnsupportedOperationException();
    }
    
    protected abstract void addPayloadToData(HashMap<Object, Object> data);
    
    public String data() {
        HashMap<Object, Object> data = new HashMap<Object, Object>();
        data.put("packetType", packetType.ordinal());
        addPayloadToData(data);
        
        // Convert data to JSON format.
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        if (packetType != other.packetType)
            return false;
        return true;
    }

}
