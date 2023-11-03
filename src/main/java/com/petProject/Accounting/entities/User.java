package com.petProject.Accounting.entities;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String surname;
    private String name;
    private LocalDate dateOfBirth;

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setSurname(String surname){this.surname = surname; }
    public void setName(String name){this.name = name; }
    public void setDateOfBirth(LocalDate dateOfBirth){this.dateOfBirth = dateOfBirth; }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getSurname() {
        return surname;
    }
    public String getName() {
        return name;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
}



