package name;

public class BoardName extends Name {
	public final static BoardName NULL_BOARD = new BoardName(0, "");
	
    public BoardName(int id, String name) {
        super(id, name);
    }
    
    public BoardName clone() {
        return new BoardName(id(), name());
    }
}
