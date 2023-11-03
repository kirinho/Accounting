package com.petProject.Accounting.repositories;
import com.petProject.Accounting.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}

