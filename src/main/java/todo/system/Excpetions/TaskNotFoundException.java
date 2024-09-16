package todo.system.Excpetions;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("We could find task with id " + id);
    }
}
