package packet;

import java.util.HashMap;

import com.google.gson.JsonObject;

public final class PacketNewClient extends Packet {
    private final String name;
    private final String id;
    
    /**
     * 
     * @param data JSON representation of the data.
     * @return
     */
    protected static PacketNewClient createPacketWithDataInternal(JsonObject data) {
        String name = data.get("name").getAsString();
        String id = data.get("id").getAsString();
        return new PacketNewClient(id, name);
    }
    
    public PacketNewClient(String id, String name) {
        super(PacketType.PacketTypeNewClient);
        this.id = id;
        this.name = name;
    }
    
    @Override
    protected void addPayloadToData(HashMap<Object, Object> data) {
        data.put("name", name);
        data.put("id", id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        PacketNewClient other = (PacketNewClient) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    
}
