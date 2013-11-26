package packet;

import static org.junit.Assert.*;

import java.awt.Color;

import name.BoardName;
import name.User;

import org.junit.Test;

import pixel.Pixel;

public class PacketTest {
    
    @Test
    public void newClientPacketTest() {
        // Create a NewClientPacket, convert it to data, and ensure that
        // the reconstructed packet is identical to the original packet.
        
        Packet packet = new PacketNewClient(new User(3, "name"));
        String data = packet.data();
        PacketNewClient newPacket = (PacketNewClient) Packet.createPacketWithData(data);
        
        assertEquals(packet, newPacket);
        assertEquals(packet.hashCode(), newPacket.hashCode());
        
        assertTrue(newPacket.senderName().id() == 3);
        assertTrue(newPacket.senderName().name().equals("name"));
        assertTrue(newPacket.boardName().id() == 0);
        assertTrue(newPacket.boardName().name().equals(""));
    }
    
    @Test
    public void joinBoardPacketTest() {
        // Create a JoinBoardPacket, convert it to data, and ensure that
        // the reconstructed packet is identical to the original packet.
        
        Packet packet = new PacketJoinBoard(new BoardName(4, "board"), new User(20, "name"));
        String data = packet.data();
        PacketJoinBoard newPacket = (PacketJoinBoard) Packet.createPacketWithData(data);
        
        assertEquals(packet, newPacket);
        assertEquals(packet.hashCode(), newPacket.hashCode());
        
        assertTrue(newPacket.senderName().id() == 20);
        assertTrue(newPacket.senderName().name().equals("name"));
        assertTrue(newPacket.boardName().id() == 4);
        assertTrue(newPacket.boardName().name().equals("board"));
    }
    
    @Test
    public void exitBoardPacketTest() {
        // Create an ExitClientPacket, convert it to data, and ensure that
        // the reconstructed packet is identical to the original packet.
        
        Packet packet = new PacketExitBoard(new BoardName(4, "board"), new User(20, "name"));
        String data = packet.data();
        PacketExitBoard newPacket = (PacketExitBoard) Packet.createPacketWithData(data);
        
        assertEquals(packet, newPacket);
        assertEquals(packet.hashCode(), newPacket.hashCode());
        
        assertTrue(newPacket.senderName().id() == 20);
        assertTrue(newPacket.senderName().name().equals("name"));
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
        
        Packet packet = new PacketGameState(new BoardName(100, "board"), new User(13, "matt"), 256, 256, clients, pixels);
        String data = packet.data();
        PacketGameState newPacket = (PacketGameState) Packet.createPacketWithData(data);
        
        assertEquals(packet, newPacket);
        assertEquals(packet.hashCode(), newPacket.hashCode());
        assertTrue(packet.boardName().id() == 100);
        assertTrue(packet.boardName().name().equals("board"));
        assertTrue(packet.senderName().id() == 13);
        assertTrue(packet.senderName().name().equals("matt"));
        assertArrayEquals(newPacket.clients(), clients);
        assertArrayEquals(newPacket.pixels(), pixels);
    }
    
    @Test
    public void boardStatePacketTest() {
        BoardName[] boards = new BoardName[2];
        
        boards[0] = new BoardName(4, "name1");
        boards[1] = new BoardName(7, "name2");
        
        Packet packet = new PacketBoardState(boards, new User(1341, "jon"));
        String data = packet.data();
        PacketBoardState newPacket = (PacketBoardState) Packet.createPacketWithData(data);
        
        assertEquals(packet, newPacket);
        assertEquals(packet.hashCode(), newPacket.hashCode());
        assertArrayEquals(newPacket.boards(), boards);
    }

    @Test
    public void drawPixelPacketTest() {
        Pixel whitePixel = new Pixel(0, 0, "white");
        
        Packet packet = new PacketDrawPixel(new BoardName(13, "BOARD"), new User(124, "server"), whitePixel);
        String data = packet.data();
        PacketDrawPixel newPacket = (PacketDrawPixel) Packet.createPacketWithData(data);
        
        assertEquals(packet, newPacket);
        assertEquals(packet.hashCode(), newPacket.hashCode());
        assertEquals(newPacket.pixel(), whitePixel);

        Pixel rgbPixel = new Pixel(0, 0, new Color(10, 15, 13));
        packet = new PacketDrawPixel(new BoardName(13, "BOARD"), new User(12414, "server"), rgbPixel);
        data = packet.data();
        newPacket = (PacketDrawPixel) Packet.createPacketWithData(data);
        
        assertEquals(packet, newPacket);
        assertEquals(packet.hashCode(), newPacket.hashCode());
        assertEquals(newPacket.pixel(), rgbPixel);
    }
    
    @Test
    public void differentPacketTest() {
        // Assert that different packets produce different data.
        
        Packet packet1 = new PacketNewClient(new User(10, "name1"));
        Packet packet2 = new PacketExitBoard(new BoardName(2, "board"), new User(10, "name1"));
        Packet packet3 = new PacketNewClient(new User(15, "name2"));
        
        assertFalse(packet1.data().equals(packet2.data()));
        assertFalse(packet1.data().equals(packet3.data()));
    }
}
