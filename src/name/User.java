package name;

public class User extends Name {
    public User(int id, String name) {
        super(id, name);
    }
    
    public User clone() {
        return new User(id(), name());
    }
    
}
