package com.itGirl.ToDo.service;

import com.itGirl.ToDo.entity.Task;
import com.itGirl.ToDo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class userService {
    @Autowired
    UserRepository userRepository;

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();
        userRepository.findAll().forEach(user -> userList.add(user));
        return userList;
    }

    public User getUserByName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User saveOrUpdate(User user) {
        userRepository.save(user);
        return user;
    }

    public void delete(int userId) {

        userRepository.deleteById(userId);
    }

}
