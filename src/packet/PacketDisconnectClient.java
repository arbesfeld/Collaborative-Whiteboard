package packet;

import java.util.HashMap;

import user.User;

import com.google.gson.JsonObject;

public final class PacketDisconnectClient extends Packet {
    private final User user;
    
    /**
     * 
     * @param data JSON representation of the data.
     * @return the constructed Packet
     */
    protected static Packet createPacketWithDataInternal(JsonObject data) {
        int boardID = data.get("boardID").getAsInt();
        JsonObject juser = data.get("user").getAsJsonObject();
        int id = juser.get("id").getAsInt();
        String name = juser.get("name").getAsString();
        return new PacketDisconnectClient(boardID, new User(id, name));
    }
    
    public PacketDisconnectClient(int boardID, User user) {
        super(PacketType.PacketTypeDisconnectClient, boardID);
        this.user = user;
    }
    
    /**
     * See specification in superclass Packet.
     */
    @Override
    protected void addPayloadToData(HashMap<Object, Object> data) {
        data.put("user", user);
    }
    
    public User user() {
        return user;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        PacketDisconnectClient other = (PacketDisconnectClient) obj;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }
    
}