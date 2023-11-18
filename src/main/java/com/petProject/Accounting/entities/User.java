package com.petProject.Accounting.entities;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;
    private String quotes;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Budget> budgets = new ArrayList<>();



    public List<Budget> getBudgets() {
        return budgets;
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    public void addBudget(Budget budget) {
        budgets.add(budget);
        budget.setUser(this);
    }

    public void removeBudget(Budget budget) {
        budgets.remove(budget);
        budget.setUser(null);
    }
    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void setQuotes(String quotes) {
        this.quotes = quotes;
    }

    public String getQuotes() {
        return quotes;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public Status getStatus() {
        return status;
    }

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



