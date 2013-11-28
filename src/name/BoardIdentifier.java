package name;

public final class BoardIdentifier extends Identifier {
	private static final long serialVersionUID = -7136909813853854025L;
	
	public static final BoardIdentifier NULL_BOARD = new BoardIdentifier(0, "");
    public BoardIdentifier(int id, String name) {
        super(id, name);
    }

}
