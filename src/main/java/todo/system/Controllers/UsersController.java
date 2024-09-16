package todo.system.Controllers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
    public  User addUser(@RequestBody User user){
        return repository.save(user);
    }


    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id){
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @PutMapping("/users/{id}")
    public User putUser(@PathVariable Long id,
    @RequestBody User newUser){
        return repository.findById(id).map(user -> {

            user.setName(newUser.getName());
            user.setEmail(newUser.getEmail());

            return repository.save(user);
        })
        //En caso de que no lo encuentre lo va a crear
        .orElseGet(() -> {
            return repository.save(newUser);
        });
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id){
        repository.deleteById(id);
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
