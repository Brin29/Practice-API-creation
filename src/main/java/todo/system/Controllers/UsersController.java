package todo.system.Controllers;

import com.fasterxml.jackson.datatype.jsr310.ser.YearSerializer;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.system.Assembler.UserModelAssembler;
import todo.system.Entitys.User;
import todo.system.Excpetions.UserNotFoundException;
import todo.system.Repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UsersController {

    // Inyeccion de la dependencia
    private final UserRepository repository;

    private final UserModelAssembler assembler;

    public UsersController(UserRepository repository, UserModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody User user){

        EntityModel<User> entityModel = assembler.toModel(repository.save(user));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    @GetMapping("/users/{id}")
    public EntityModel<User> getUser(@PathVariable Long id){

        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return assembler.toModel(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> putUser(@PathVariable Long id,
    @RequestBody User newUser){

        User updateUser = repository.findById(id).map(user -> {

            user.setName(newUser.getName());
            user.setEmail(newUser.getEmail());

            return repository.save(user);
        })
        //En caso de que no lo encuentre lo va a crear
        .orElseGet(() -> {
            return repository.save(newUser);
        });

        EntityModel<User> userEntityModel = assembler.toModel(updateUser);

        return ResponseEntity.created(userEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(userEntityModel);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> getUsers(){

        List<EntityModel<User>> users = repository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(users,
                linkTo(methodOn(UsersController.class).getUsers()).withSelfRel());

    }
}
