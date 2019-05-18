package com.itGirl.ToDo.service;

import com.itGirl.ToDo.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUserName(String userName);
}
