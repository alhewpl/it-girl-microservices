package com.itGirl.ToDo.controller;

import com.itGirl.ToDo.entity.Task;
import com.itGirl.ToDo.service.taskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    private int userEmailId;
    private Date fromDate;
    private Date toDate;

    @GetMapping("/tasks")
    private List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }


    @RequestMapping(path = "/tasks/{userEmailId}", method = RequestMethod.GET)
    private Task getTask(@PathVariable int userEmailId,
                         @RequestParam(value="priority") Optional<String> priority,
                         @RequestParam(value="bookedAt") @DateTimeFormat(pattern="yyyy-MM-dd") Optional<Date> bookedAt){
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

    @PutMapping("/tasks/{userEmailId}")
    private Task updateTask(@PathVariable int userEmailId, @Valid @RequestBody Task taskDetails) {
        Task taskItem = taskService.getTaskById(userEmailId);
        taskItem.setTaskName(taskDetails.getTaskName());
        taskItem.setPriority(taskDetails.getPriority());

        Task updatedTask = taskService.saveOrUpdate(taskItem);
        return updatedTask;
    }


    @RequestMapping(path = "/isUserAvailable/{userEmailId}", method = RequestMethod.GET)
    public boolean isAvailable(@PathVariable int userEmailId,
                               @RequestParam (value="fromDate")     @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
                               @RequestParam(value="toDate")     @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate) {
        this.userEmailId = userEmailId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        return true;
    }
}
