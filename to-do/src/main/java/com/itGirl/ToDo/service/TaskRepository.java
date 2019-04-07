package com.itGirl.ToDo.service;

import com.itGirl.ToDo.entity.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Integer> {
}
