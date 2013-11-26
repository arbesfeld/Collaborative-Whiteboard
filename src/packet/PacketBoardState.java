package packet;

import java.util.HashMap;

import pixel.Pixel;
import util.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class PacketBoardState extends Packet {
    private final BoardName[] boards;
    
    /**
     * 
     * @param data JSON representation of the data.
     * @return the constructed Packet
     */
    protected static Packet createPacketWithDataInternal(JsonObject data) {
        JsonArray jboards = data.get("boards").getAsJsonArray();
        
        BoardName[] boards = new BoardName[jboards.size()];
        
        for (int i = 0; i < jboards.size(); i++) {
            JsonObject jobject = jboards.get(i).getAsJsonObject();
     
            int id = jobject.get("id").getAsInt();
            String name = jobject.get("name").getAsString();
            
            boards[i] = new BoardName(id, name);
        }
        
        return new PacketBoardState(boards);
    }
    
    public PacketBoardState(BoardName[] boards) {
        super(PacketType.PacketTypeBoardState);
        this.boards = boards;
    }
    
    /**
     * See specification in superclass Packet.
     */
    @Override
    protected void addPayloadToData(HashMap<Object, Object> data) {
        data.put("boards", boards);
    }
    
    public BoardName[] boards() {
        return boards.clone();
    }
}
