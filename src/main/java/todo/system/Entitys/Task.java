package todo.system.Entitys;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import todo.system.StatusEnum.Status;

import java.util.Objects;

public class Task {

    private @Id @GeneratedValue Long id;

    private  String title;

    private String description;

    private Enum<Status> status;

    // Un usuario puede tener muchas tareas
    @ManyToOne
    private User user;

    public Task(String title, String description, User user) {}

    public Task(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Enum<Status> getStatus() {
        return status;
    }

    public void setStatus(Enum<Status> status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.id, this.title, this.description, this.status, this.user);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;

        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        return Objects.equals(this.id, task.id)
                && Objects.equals(this.title, task.title)
                && Objects.equals(this.description, task.description)
                && Objects.equals(this.status, task.status)
                && Objects.equals(this.user, task.user);
    }

    @Override
    public String toString(){
        return "Task [id=" + id + ", title=" + title + ", description=" + description + ", " +
                "status=" + status + ", " +
                "user=" + user + "]";
    }
}

