package com.itGirl.ToDo.controller;

import com.itGirl.ToDo.entity.Task;
import com.itGirl.ToDo.service.taskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path="/v1")
public class TasksController {

    /*@RequestMapping(path="/tasks", method = RequestMethod.GET)
    public @ResponseBody List<Task> tasks(){
        List<Task> tasks = new ArrayList<>();
        return tasks;
    }


    @RequestMapping(path="/tasks/{userEmailId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Task taskItem (@PathVariable int userEmailId,
              @RequestParam String param){
        System.out.println("id =  [" + userEmailId + "], param = [" + param + "]");
        return new Task();
    }*/

    @Autowired
    taskService taskService;

    @GetMapping("/tasks")
    private List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/tasks/{userEmailId}")
    private Task getTask(@PathVariable int userEmailId) {
        return taskService.getTaskById(userEmailId);
    }

    @DeleteMapping("/tasks/{userEmailId}")
    private void deleteTask(@PathVariable int userEmailId) {
        taskService.delete(userEmailId);
    }

    @PostMapping("/tasks")
    @Transactional
    private Task saveTask(@RequestBody Task task) {
        taskService.saveOrUpdate(task);
        return task;
    }

    @PutMapping("/tasks/{taskId}")
    private Task updateTask(@PathVariable int taskId, @Valid @RequestBody Task taskDetails) {
        Task taskItem = taskService.getTaskById(taskId);
        taskItem.setTaskName(taskDetails.getTaskName());
        taskItem.setPriority(taskDetails.getPriority());

        Task updatedTask = taskService.saveOrUpdate(taskItem);
        return updatedTask;
    }
}
