package com.petProject.Accounting.repositories;
import com.petProject.Accounting.entities.UserAuthentication;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserAuthentication, Long> {
    UserAuthentication findByUsername(String username);
}

