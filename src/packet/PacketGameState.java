package packet;

import java.awt.Color;
import java.util.HashMap;

import pixel.Pixel;
import util.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class PacketGameState extends Packet {
    private final int width;
    private final int height;
    private final User[] clients;
    private final Pixel[] pixels;
    
    /**
     * 
     * @param data JSON representation of the data.
     * @return the constructed Packet
     */
    protected static Packet createPacketWithDataInternal(JsonObject data) {
        int width = data.get("width").getAsInt();
        int height = data.get("height").getAsInt();
        
        JsonObject board = data.get("boardName").getAsJsonObject();
        int boardId = board.get("id").getAsInt();
        String boardName = board.get("name").getAsString();
        
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
            int r = jobject.get("r").getAsInt();
            int g = jobject.get("g").getAsInt();
            int b = jobject.get("b").getAsInt();
            pixels[i] = new Pixel(x, y, new Color(r/255.0f, g/255.0f, b/255.0f));
        }
        
        return new PacketGameState(new BoardName(boardId, boardName), width, height, clients, pixels);
    }
    
    public PacketGameState(BoardName boardName, int width, int height, User[] clients, Pixel[] pixels) {
        super(PacketType.PacketTypeGameState, boardName);
        this.width = width;
        this.height = height;
        this.clients = clients;
        this.pixels = pixels;
    }
    
    /**
     * See specification in superclass Packet.
     */
    @Override
    protected void addPayloadToData(HashMap<Object, Object> data) {
        data.put("width", width);
        data.put("height", height);
        data.put("clients", clients);
        data.put("pixels", pixels);
    }
    
    public int width() {
        return width;
    }
    
    public int height() {
        return height;
    }
    
    public User[] clients() {
        return clients.clone();
    }
    
    public Pixel[] pixels() {
        return pixels.clone();
    }
}
