package com.example.margocoursework.Repositories;

import com.example.margocoursework.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
