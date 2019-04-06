package com.itGirl.ToDo.service;

import com.itGirl.ToDo.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class taskService {
    @Autowired
    TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<Task>();
        taskRepository.findAll().forEach(task -> taskList.add(task));
        return taskList;
    }

    public Task getTaskById(int userEmailId) {
        return taskRepository.findById(userEmailId).get();
    }

    public Task saveOrUpdate(Task task) {
        taskRepository.save(task);
        return task;
    }

    public void delete(int userEmailId) {
        taskRepository.deleteById(userEmailId);
    }
}
