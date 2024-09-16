package todo.system.Controllers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.system.Assembler.TaskModelAssembler;
import todo.system.Entitys.Task;
import todo.system.Entitys.User;
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
    public ResponseEntity<?> createTask(@RequestBody Task task) {

        EntityModel<Task> entityModel = assembler.toModel(repository.save(task));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/tasks/{id}")
    public EntityModel<?> getTask(@PathVariable Long id){
        Task task  = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));

        return assembler.toModel(task);
    }

    @GetMapping("/tasks")
    public CollectionModel<EntityModel<Task>> getTasks() {

       List<EntityModel<Task>> tasks = repository.findAll()
               .stream()
               .map(assembler::toModel)
               .collect(Collectors.toList());

       return CollectionModel.of(tasks,
               linkTo(methodOn(TasksController.class).getTasks()).withSelfRel());
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
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task newTask){
        Task updateTask =  repository.findById(id).map(task -> {
            task.setTitle(newTask.getTitle());
            task.setDescription(newTask.getDescription());
            task.setStatus(newTask.getStatus());
            task.setUser(newTask.getUser());

            return repository.save(task);
        }).orElseGet(() -> repository.save(newTask));

        EntityModel<Task> entityModel = assembler.toModel(updateTask);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id){

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
