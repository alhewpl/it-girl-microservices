package com.itGirl.ToDo.service;

import com.itGirl.ToDo.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public List<Task> getTaskByEmailId(int userEmailId) {
        List<Task> taskList = new ArrayList<>();
        taskRepository.findByUserEmailId(userEmailId).forEach(task -> taskList.add((Task) task));
        return taskList;
    }

    public Task getTaskById(int taskId) {
        return taskRepository.findById(taskId).get();
    }

    public Task saveOrUpdate(Task task) {
        taskRepository.save(task);
        return task;
    }

    public void delete(int taskId) {

        taskRepository.deleteById(taskId);
    }

    @Transactional
    public void deleteByUserEmailId(int userEmailId) {
        taskRepository.deleteByUserEmailId(userEmailId);
    }

}
