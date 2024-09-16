package todo.system.Excpetions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("We could find the user with the id " + id);
    }

}
