package packet;

import java.util.HashMap;

import pixel.Pixel;

import com.google.gson.JsonObject;

public final class PacketDrawPixel extends Packet {
    private final Pixel pixel;
    
    /**
     * 
     * @param data JSON representation of the data.
     * @return the constructed Packet
     */
    protected static Packet createPacketWithDataInternal(JsonObject data) {
        int boardID = data.get("boardID").getAsInt();
        
        JsonObject jobject = data.get("pixel").getAsJsonObject();
        int x = jobject.get("x").getAsInt();
        int y = jobject.get("y").getAsInt();
        int rgb = jobject.get("rgb").getAsInt();
        Pixel pixel = new Pixel(x, y, rgb);
        
        return new PacketDrawPixel(boardID, pixel);
    }
    
    public PacketDrawPixel(int boardID, Pixel pixel) {
        super(PacketType.PacketTypeDrawPixel, boardID);
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
    