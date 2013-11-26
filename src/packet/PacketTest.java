package packet;

import static org.junit.Assert.*;

import org.junit.Test;

import pixel.Pixel;
import util.Pair;

public class PacketTest {
    
    @Test
    public void newClientPacketTest() {
        // Create a NewClientPacket, convert it to data, and ensure that
        // the reconstructed packet is identical to the original packet.
        
        Packet packet = new PacketNewClient(new BoardName(2, "myBoard"), new User(3, "name"));
        String data = packet.data();
        PacketNewClient newPacket = (PacketNewClient) Packet.createPacketWithData(data);
        
        assertEquals(packet, newPacket);
        assertEquals(packet.hashCode(), newPacket.hashCode());
        
        assertTrue(newPacket.user().id() == 3);
        assertTrue(newPacket.user().name().equals("name"));
        assertTrue(newPacket.boardName().id() == 2);
        assertTrue(newPacket.boardName().name().equals("myBoard"));
    }
    
    @Test
    public void disconnectClientPacketTest() {
        // Create a DisconnectClientPacket, convert it to data, and ensure that
        // the reconstructed packet is identical to the original packet.
        
        Packet packet = new PacketDisconnectClient(new BoardName(4, "board"), new User(20, "name"));
        String data = packet.data();
        PacketDisconnectClient newPacket = (PacketDisconnectClient) Packet.createPacketWithData(data);
        
        assertEquals(packet, newPacket);
        assertEquals(packet.hashCode(), newPacket.hashCode());
        
        assertTrue(newPacket.user().id() == 20);
        assertTrue(newPacket.user().name().equals("name"));
        assertTrue(newPacket.boardName().id() == 4);
        assertTrue(newPacket.boardName().name().equals("board"));
    }
    
    @Test
    public void gameStatePacketTest() {
        User[] clients = new User[2];
        
        clients[0] = new User(4, "name1");
        clients[1] = new User(7, "name2");
        
        Pixel[] pixels = new Pixel[2];
        pixels[0] = new Pixel(0, 0, "white");
        pixels[1] = new Pixel(1, 2, "black");
        
        Packet packet = new PacketGameState(new BoardName(100, "board"), clients, pixels);
        String data = packet.data();
        PacketGameState newPacket = (PacketGameState) Packet.createPacketWithData(data);
        
        assertEquals(packet, newPacket);
        assertEquals(packet.hashCode(), newPacket.hashCode());
        assertTrue(packet.boardName().id() == 100);
        assertTrue(packet.boardName().name().equals("board"));
        assertArrayEquals(newPacket.clients(), clients);
        assertArrayEquals(newPacket.pixels(), pixels);
    }
    
    @Test
    public void boardStatePacketTest() {
        BoardName[] boards = new BoardName[2];
        
        boards[0] = new BoardName(4, "name1");
        boards[1] = new BoardName(7, "name2");
        
        Packet packet = new PacketBoardState(boards);
        String data = packet.data();
        PacketBoardState newPacket = (PacketBoardState) Packet.createPacketWithData(data);
        
        assertEquals(packet, newPacket);
        assertEquals(packet.hashCode(), newPacket.hashCode());
        assertArrayEquals(newPacket.boards(), boards);
    }

    @Test
    public void drawPixelPacketTest() {
        Pixel whitePixel = new Pixel(0, 0, "white");
        
        Packet packet = new PacketDrawPixel(new BoardName(13, "BOARD"), whitePixel);
        String data = packet.data();
        PacketDrawPixel newPacket = (PacketDrawPixel) Packet.createPacketWithData(data);
        
        assertEquals(packet, newPacket);
        assertEquals(packet.hashCode(), newPacket.hashCode());
        assertEquals(newPacket.pixel(), whitePixel);
    }
    
    @Test
    public void differentPacketTest() {
        // Assert that different packets produce different data.
        
        Packet packet1 = new PacketNewClient(new BoardName(2, "board"), new User(10, "name1"));
        Packet packet2 = new PacketDisconnectClient(new BoardName(2, "board"), new User(10, "name1"));
        Packet packet3 = new PacketNewClient(new BoardName(2, "board"), new User(15, "name2"));
        
        assertFalse(packet1.data().equals(packet2.data()));
        assertFalse(packet1.data().equals(packet3.data()));
    }
}
