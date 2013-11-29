package packet;

import java.util.Arrays;

import name.BoardIdentifier;

public final class PacketBoardIdentifierList extends Packet {
    private static final long serialVersionUID = 3621326113552483907L;
    private final BoardIdentifier[] boards;
    
    public PacketBoardIdentifierList(BoardIdentifier[] boards) {
        this.boards = boards;
    }
    
    public BoardIdentifier[] boards() {
        return boards.clone();
    }

	@Override
	public void process(PacketHandler handler) {
		handler.receivedBoardIdentifierListPacket(this);
	}
}
