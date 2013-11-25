package packet;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import pixel.Pixel;
import util.Pair;

public class PacketTest {
    
    @Test
    public void newClientPacketTest() {
        // Create a NewClientPacket, convert it to data, and ensure that
        // the reconstructed packet is identical to the original packet.
        
        Packet packet = new PacketNewClient("id", "name");
        String data = packet.data();
        PacketNewClient newPacket = (PacketNewClient) Packet.createPacketWithData(data);
        
        assertEquals(packet, newPacket);
        assertEquals(packet.hashCode(), newPacket.hashCode());
        
        assertTrue(newPacket.id().equals("id"));
        assertTrue(newPacket.name().equals("name"));
    }
    
    @Test
    public void disconnectClientPacketTest() {
        // Create a DisconnectClientPacket, convert it to data, and ensure that
        // the reconstructed packet is identical to the original packet.
        
        Packet packet = new PacketDisconnectClient("id", "name");
        String data = packet.data();
        PacketDisconnectClient newPacket = (PacketDisconnectClient) Packet.createPacketWithData(data);
        
        assertEquals(packet, newPacket);
        assertEquals(packet.hashCode(), newPacket.hashCode());
        
        assertTrue(newPacket.id().equals("id"));
        assertTrue(newPacket.name().equals("name"));
    }
    
    @Test
    public void differentPacketTest() {
        // Assert that different packets produce different data.
        
        Packet packet1 = new PacketNewClient("id", "name");
        Packet packet2 = new PacketDisconnectClient("id", "name");
        Packet packet3 = new PacketNewClient("id2", "name2");
        
        assertFalse(packet1.data().equals(packet2.data()));
        assertFalse(packet1.data().equals(packet3.data()));
    }
    
    @Test
    public void gameStatePacketTest() {
        @SuppressWarnings("unchecked")
        Pair<String, String>[] clients = new Pair[2];
        
        clients[0] = new Pair<String, String>("id1", "name1");
        clients[1] = new Pair<String, String>("id1", "name1");
        
        Pixel[] pixels = new Pixel[2];
        pixels[0] = new Pixel(0, 0, "white");
        pixels[1] = new Pixel(1, 2, "black");
        
        Packet packet = new PacketGameState(clients, pixels);
        String data = packet.data();
        PacketGameState newPacket = (PacketGameState) Packet.createPacketWithData(data);
        
        assertEquals(packet, newPacket);
        assertEquals(packet.hashCode(), newPacket.hashCode());
        assertArrayEquals(newPacket.clients(), clients);
        assertArrayEquals(newPacket.pixels(), pixels);
    }
}
