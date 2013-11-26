package models;

import java.util.Arrays;
import java.util.HashSet;

import packet.BoardName;
import packet.User;
import pixel.Pixel;

public abstract class BoardModel {
    private final BoardName boardName;
    private final HashSet<User> users;
    
    protected BoardModel(BoardName boardName) {
        this.boardName = boardName;
        this.users = new HashSet<User>();
    }
    
    // Only to be used by ClientBoardModel.
    protected BoardModel(BoardName boardName, User[] initUsers) {
        this.boardName = boardName;
        this.users = new HashSet<User>(Arrays.asList(initUsers));
    }
    
    public synchronized void addUser(User user) {
        this.users.add(user);
    }
    
    public synchronized void removeUser(User user) {
        if (!this.users.contains(user))
            throw new IllegalArgumentException("User " + user.toString() + " does not exist.");
        this.users.remove(user);
    }
    
    public synchronized BoardName boardName() {
        return boardName;
    }
    
    public synchronized User[] users() {
        return users.toArray(new User[users.size()]);
    }
    
    public abstract void putPixel(Pixel pixel);

    @Override
    public synchronized int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((boardName == null) ? 0 : boardName.hashCode());
        result = prime * result + ((users == null) ? 0 : users.hashCode());
        return result;
    }

    @Override
    public synchronized boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BoardModel other = (BoardModel) obj;
        if (boardName == null) {
            if (other.boardName != null)
                return false;
        } else if (!boardName.equals(other.boardName))
            return false;
        if (users == null) {
            if (other.users != null)
                return false;
        } else if (!users.equals(other.users))
            return false;
        return true;
    }
}
