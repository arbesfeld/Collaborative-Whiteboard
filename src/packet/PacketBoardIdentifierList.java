package packet;

import java.util.Arrays;

import name.BoardIdentifier;
import name.ClientIdentifier;

public final class PacketBoardIdentifierList extends Packet {
    private static final long serialVersionUID = 3621326113552483907L;
    private final BoardIdentifier[] boards;
    
    public PacketBoardIdentifierList(BoardIdentifier[] boards, ClientIdentifier senderName) {
        super(PacketType.BOARD_IDENTIFIER_LIST, senderName);
        this.boards = boards;
    }
    
    public BoardIdentifier[] boards() {
        return boards.clone();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(boards);
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
		PacketBoardIdentifierList other = (PacketBoardIdentifierList) obj;
		if (!Arrays.equals(boards, other.boards))
			return false;
		return true;
	}
    
    
}
