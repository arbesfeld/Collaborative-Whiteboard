package packet;

import java.util.HashMap;

import pixel.Pixel;
import user.User;
import util.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class PacketGameState extends Packet {
    private final User[] clients;
    private final Pixel[] pixels;
    
    /**
     * 
     * @param data JSON representation of the data.
     * @return the constructed Packet
     */
    protected static Packet createPacketWithDataInternal(JsonObject data) {
        int boardID = data.get("boardID").getAsInt();
        
        JsonArray jclients = data.get("clients").getAsJsonArray();
        
        User[] clients = new User[jclients.size()];
        
        for (int i = 0; i < jclients.size(); i++) {
            JsonObject jobject = jclients.get(i).getAsJsonObject();
     
            int id = jobject.get("id").getAsInt();
            String name = jobject.get("name").getAsString();
            
            clients[i] = new User(id, name);
        }
        
        JsonArray jpixels = data.get("pixels").getAsJsonArray();
        Pixel[] pixels = new Pixel[jpixels.size()];
        
        for (int i = 0; i < jpixels.size(); i++) {
            JsonObject jobject = jpixels.get(i).getAsJsonObject();
            int x = jobject.get("x").getAsInt();
            int y = jobject.get("y").getAsInt();
            int rgb = jobject.get("rgb").getAsInt();
            pixels[i] = new Pixel(x, y, rgb);
        }
        
        return new PacketGameState(boardID, clients, pixels);
    }
    
    public PacketGameState(int boardID, User[] clients, Pixel[] pixels) {
        super(PacketType.PacketTypeGameState, boardID);
        this.clients = clients;
        this.pixels = pixels;
    }
    
    /**
     * See specification in superclass Packet.
     */
    @Override
    protected void addPayloadToData(HashMap<Object, Object> data) {
        data.put("clients", clients);
        data.put("pixels", pixels);
    }
    
    public User[] clients() {
        return clients.clone();
    }
    
    public Pixel[] pixels() {
        return pixels.clone();
    }
}
