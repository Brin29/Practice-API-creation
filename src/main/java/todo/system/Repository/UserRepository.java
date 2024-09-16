package todo.system.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.system.Entitys.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
