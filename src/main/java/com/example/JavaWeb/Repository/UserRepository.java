package com.example.JavaWeb.Repository;

import com.example.JavaWeb.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Integer> {
}
