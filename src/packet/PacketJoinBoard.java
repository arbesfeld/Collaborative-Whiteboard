package packet;

import name.BoardIdentifier;
import name.ClientIdentifier;

public final class PacketJoinBoard extends Packet {
    private static final long serialVersionUID = 3502327190158127229L;

    private final BoardIdentifier boardName;
    
    public PacketJoinBoard(BoardIdentifier boardName) {
    	this.boardName = boardName;
    }
    
    public BoardIdentifier boardName() {
    	return boardName;
    }

	@Override
	public void process(PacketHandler handler) {
		handler.receivedJoinBoardPacket(this);
	}
}
