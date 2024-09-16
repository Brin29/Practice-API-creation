package todo.system.Controllers;

import org.springframework.web.bind.annotation.*;
import todo.system.Entitys.User;
import todo.system.Repository.UserRepository;

import java.util.List;

@RestController
public class UsersController {

    // Inyeccion de la dependencia
    private final UserRepository repository;

    public UsersController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/users")
    public  User addUser(@RequestBody User user){
        return repository.save(user);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id){
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException);
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
    public List<User> getUsers(){
        return repository.findAll();
    }
}
