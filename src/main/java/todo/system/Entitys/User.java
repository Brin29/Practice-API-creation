package todo.system.Entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class User {

    private @Id @GeneratedValue Long id;

    private String name;

    private String email;

    public  User(String name, String email){
        this.name = name;
        this.email = email;
    }

    public  User(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    @Override
    public String toString(){
        return "User [id=" + id + ", name=" + name + ", email=" + email + "]";
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.id,  this.name, this.email);
    }

    @Override
    public boolean equals(Object o){
        // Si esta clase es igual al objeto o
        if (this == o) return true;
        // Si es diferente
        if (!(o instanceof User)) return false;
        // casteo del objeto
        User user = (User) o;

        return Objects.equals(this.id, user.id) &&
                Objects.equals(this.name, user.name) &&
                Objects.equals(this.email, user.email);
    }
}
