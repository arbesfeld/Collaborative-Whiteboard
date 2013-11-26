package packet;

import java.util.HashMap;

import pixel.Pixel;
import util.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class PacketGameState extends Packet {
    private final Pair<String, String>[] clients;
    private final Pixel[] pixels;
    
    /**
     * 
     * @param data JSON representation of the data.
     * @return the constructed Packet
     */
    protected static Packet createPacketWithDataInternal(JsonObject data) {
        int boardID = data.get("boardID").getAsInt();
        
        JsonArray jclients = data.get("clients").getAsJsonArray();
        
        @SuppressWarnings("unchecked")
        Pair<String, String>[] clients = new Pair[jclients.size()];
        
        for (int i = 0; i < jclients.size(); i++) {
            JsonObject jobject = jclients.get(i).getAsJsonObject();
            String id = jobject.get("first").getAsString();
            String name = jobject.get("second").getAsString();
            clients[i] = new Pair<String, String>(id, name);
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
    
    public PacketGameState(int boardID, Pair<String, String>[] clients, Pixel[] pixels) {
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
    
    public Pair<String, String>[] clients() {
        return clients.clone();
    }
    
    public Pixel[] pixels() {
        return pixels.clone();
    }
}
