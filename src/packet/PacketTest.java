package packet;

import static org.junit.Assert.*;

import org.junit.Test;

import canvas.Pixel;
import name.BoardIdentifier;
import name.ClientIdentifier;

public class PacketTest {
    
      @Test
          public void joinBoardPacketConstTest() {
              // test creating a JoinBoardPacket
          PacketJoinBoard packet = new PacketJoinBoard(new BoardIdentifier(4, "board"));
          assertTrue(packet.boardName().id() == 4);
          assertTrue(packet.boardName().name().equals("board"));
      }
  
    @Test
    public void boardStatePacketConstTest() {
        //Test constructing a boardStatePacket
        BoardIdentifier[] boards = new BoardIdentifier[2];
        
        boards[0] = new BoardIdentifier(4, "name1");
        boards[1] = new BoardIdentifier(7, "name2");
        
        PacketBoardIdentifierList packet = new PacketBoardIdentifierList(boards);   
        assertArrayEquals(packet.boards(), boards);
    }
}
