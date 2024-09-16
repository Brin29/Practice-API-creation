package todo.system.Controllers;

import org.springframework.web.bind.annotation.*;
import todo.system.Entitys.Task;
import todo.system.Repository.TaskRepository;

@RestController
public class TasksController {

    // Inyeccion de dependencias
    private final TaskRepository repository;

    public TasksController(TaskRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/tasks")
    public Task createTask(@RequestBody Task task) {
        return repository.save(task);
    }

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundTaskException);
    }

    @PutMapping("/tasks/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task newTask){
        return repository.findById(id).map(task -> {
            task.setTitle(newTask.getTitle());
            task.setDescription(newTask.getDescription());
            task.setStatus(newTask.getStatus());
            task.setUser(newTask.getUser());

            return repository.save(task);
        }).orElseGet(() -> repository.save(newTask));
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Long id){
        repository.deleteById(id);
    }

}
