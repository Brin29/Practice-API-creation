package todo.system.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.system.Entitys.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
