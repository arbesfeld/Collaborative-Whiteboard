package packet;

import static org.junit.Assert.*;

import org.junit.Test;

public class PacketTest {
    
    @Test
    public void newClientPacketTest() {
        // Create a NewClientPacket, convert it to data, and ensure that
        // the reconstructed packet is identical to the original packet.
        
        Packet packet = new PacketNewClient("name", "id");
        String data = packet.data();
        assertEquals(packet, Packet.createPacketWithData(data));
    }
}
