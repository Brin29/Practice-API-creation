package todo.system.Controllers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import todo.system.Assembler.TaskModelAssembler;
import todo.system.Entitys.Task;
import todo.system.Excpetions.TaskNotFoundException;
import todo.system.Repository.TaskRepository;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class TasksController {


    // Inyeccion de dependencias
    private final TaskRepository repository;

    private final TaskModelAssembler assembler;

    public TasksController(TaskRepository repository, TaskModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @PostMapping("/tasks")
    public Task createTask(@RequestBody Task task) {
        return repository.save(task);
    }

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable Long id){
        return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return repository.findAll();
    }

    @GetMapping("/users/{id}/tasks")
    public CollectionModel<EntityModel<Task>> allTasksByUserId(@PathVariable Long id){
        List<EntityModel<Task>> tasks = repository.findById(id)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(tasks,
                linkTo(methodOn(TasksController.class).allTasksByUserId(id)).withSelfRel());
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
