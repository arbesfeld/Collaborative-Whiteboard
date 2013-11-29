package packet;

import name.BoardIdentifier;


public final class PacketNewBoard extends Packet {
    private static final long serialVersionUID = -3720287069109037134L;
    private final BoardIdentifier boardName;
    private final int width;
    private final int height;
    
    public PacketNewBoard(BoardIdentifier boardName, int width, int height) {
    	this.boardName = boardName;
        this.width = width;
        this.height = height;
    }
    
    public BoardIdentifier boardName() {
    	return boardName;
    }
    
    public int width() {
        return width;
    }
    
    public int height() {
        return height;
    }
    
	@Override
	public void process(PacketHandler handler) {
		handler.receivedNewBoardPacket(this);
	}
}
