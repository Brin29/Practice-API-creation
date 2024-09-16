package todo.system.Assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import todo.system.Controllers.TasksController;
import todo.system.Controllers.UsersController;
import todo.system.Entitys.Task;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TaskModelAssembler implements RepresentationModelAssembler<Task, EntityModel<Task>> {

    @Override
    public EntityModel<Task> toModel(Task task){

        return EntityModel.of(task,
                linkTo(methodOn(TasksController.class).getTask(task.getId())).withSelfRel(),
                linkTo(methodOn(TasksController.class).getTasks()).withRel("tasks"),
                linkTo(methodOn(UsersController.class).getUser(task.getUser().getId())).withRel("users"));

    }

}
