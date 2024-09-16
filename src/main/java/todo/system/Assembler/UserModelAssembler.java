package todo.system.Assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import todo.system.Controllers.TasksController;
import todo.system.Controllers.UsersController;
import todo.system.Entitys.User;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// EntityModel convierte los Usuarios en un entity que contiene los enlaces
@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user){

        return EntityModel.of(user,
                linkTo(methodOn(UsersController.class).getUser(user.getId())).withSelfRel(),
                linkTo(methodOn(UsersController.class).getUsers()).withRel("users"),
                // Comunicacion entre ambas
                linkTo(methodOn(TasksController.class).getTask(user.getId())).withRel("tasks"));
    }
}
