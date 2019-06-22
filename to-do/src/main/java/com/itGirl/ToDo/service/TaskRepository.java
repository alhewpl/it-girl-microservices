package com.itGirl.ToDo.service;

import com.itGirl.ToDo.entity.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Integer> {
    void deleteByUserEmailId(int userEmailId);

    List<Object> findByUserEmailId(int userEmailId);
}
