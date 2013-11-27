package packet;

import java.awt.Color;
import java.util.HashMap;

import name.BoardIdentifier;
import name.ClientIdentifier;
import canvas.Pixel;

import com.google.gson.JsonObject;

public final class PacketDrawPixel extends Packet {
    private final Pixel pixel;
    
    /**
     * 
     * @param data JSON representation of the data.
     * @return the constructed Packet
     */
    protected static Packet createPacketWithDataInternal(JsonObject data) {
        BoardIdentifier boardName = getBoardName(data);
        ClientIdentifier senderName = getSenderName(data);
        
        JsonObject jobject = data.get("pixel").getAsJsonObject();
        int x = jobject.get("x").getAsInt();
        int y = jobject.get("y").getAsInt();
        float r = jobject.get("r").getAsInt();
        float g = jobject.get("g").getAsInt();
        float b = jobject.get("b").getAsInt();
        Pixel pixel = new Pixel(x, y, new Color(r/255.0f,g/255.0f,b/255.0f));
        
        return new PacketDrawPixel(boardName, senderName, pixel);
    }
    
    public PacketDrawPixel(BoardIdentifier boardName, ClientIdentifier senderName, Pixel pixel) {
        super(PacketType.PacketTypeDrawPixel, boardName, senderName);
        this.pixel = pixel;
    }
    
    /**
     * See specification in superclass Packet.
     */
    @Override
    protected void addPayloadToData(HashMap<Object, Object> data) {
        data.put("pixel", pixel);
    }
    
    public Pixel pixel() {
        return pixel.clone();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((pixel == null) ? 0 : pixel.hashCode());
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
        PacketDrawPixel other = (PacketDrawPixel) obj;
        if (pixel == null) {
            if (other.pixel != null)
                return false;
        } else if (!pixel.equals(other.pixel))
            return false;
        return true;
    }
    
}
    