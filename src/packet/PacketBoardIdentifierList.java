package packet;

import java.util.Arrays;
import java.util.HashMap;

import name.BoardIdentifier;
import name.ClientIdentifier;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class PacketBoardIdentifierList extends Packet {
    private final BoardIdentifier[] boards;
    
    /**
     * 
     * @param data JSON representation of the data.
     * @return the constructed Packet
     */
    protected static Packet createPacketWithDataInternal(JsonObject data) {
        JsonArray jboards = data.get("boards").getAsJsonArray();
        
        BoardIdentifier[] boards = new BoardIdentifier[jboards.size()];
        
        for (int i = 0; i < jboards.size(); i++) {
            JsonObject jobject = jboards.get(i).getAsJsonObject();

            int boardId = jobject.get("id").getAsInt();
            String boardName = jobject.get("name").getAsString();
            boards[i] = new BoardIdentifier(boardId, boardName);
        }

        BoardIdentifier boardName = getBoardName(data);
        assert boardName.equals(BoardIdentifier.NULL_BOARD);
        
        ClientIdentifier senderName = getSenderName(data);
        return new PacketBoardIdentifierList(boards, senderName);
    }
    
    public PacketBoardIdentifierList(BoardIdentifier[] boards, ClientIdentifier senderName) {
        super(PacketType.PacketTypeBoardIdentifierList, senderName);
        this.boards = boards;
    }
    
    /**
     * See specification in superclass Packet.
     */
    @Override
    protected void addPayloadToData(HashMap<Object, Object> data) {
        data.put("boards", boards);
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
