package com.itGirl.ToDo.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itGirl.ToDo.entity.Task;
import com.itGirl.ToDo.service.taskService;

@RestController
@RequestMapping(path="/toDo")
public class TasksController {

    @Autowired
    taskService taskService;
    private int userEmailId;
    private Date fromDate;
    private Date toDate;

    @GetMapping("/all_tasks")
    private List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    // read a specific event from a provided user. searchable by taskId, priority or bookedAt date.
    //if no search criteria provided, display all events from a user. -- !! to change the response type into a list
    @RequestMapping(path = "/fetch_task/{userEmailId}", method = RequestMethod.GET)
    private List<Task> getTask(@PathVariable int userEmailId,
                         @RequestParam(value="taskId") Optional<Integer> taskId,
                         @RequestParam(value="priority") Optional<String> priority,
                         @RequestParam(value="bookedAt") @DateTimeFormat(pattern="yyyy-MM-dd") Optional<Date> bookedAt){
        return taskService.getTaskByEmailId(userEmailId);
    }


    // delete operation takes in userEmailId as a path variable and taskId as an optional param.
    // if the userEmailId has any task associated with the that taskId, delete the task. If the optional param not
    // passed, delete all the tasks associated with that particular email id.
    @RequestMapping(path = "/delete_task/{userEmailId}", method = RequestMethod.DELETE)
    private String deleteTask(@PathVariable int userEmailId,
                              @RequestParam(value="taskId") Optional<Integer> taskId) {
         List<Task> tasksToDelete = taskService.getTaskByEmailId(userEmailId);
         Integer b = taskId.isPresent() ? taskId.get() : 0;
         System.out.println(b);
         for (Task taskToDelete: tasksToDelete) {
            if (taskToDelete.getTaskId() == b) {
                taskService.delete(b);
                return "task deleted successfully";
            } else {
                taskService.deleteByUserEmailId(userEmailId);
                return "all tasks associated with this user have been deleted successfully";
            }
        }

        return "";
    }

    // generate a unique eventid for user. all fields are must - emailid, event date and time, event priority
    @PostMapping("/create_task")
    @Transactional
    private Task saveTask(@RequestBody Task task) {
        taskService.saveOrUpdate(task);
        return task;
    }

    // update an existing event. Event id is must input, no other fields required
    @PutMapping("/update_task/{taskId}")
    private Task updateTask(@PathVariable int taskId,
                            @Valid @RequestBody Task taskDetails) {
        Task taskItem = taskService.getTaskById(taskId);
        taskItem.setTaskName(taskDetails.getTaskName());
        taskItem.setPriority(taskDetails.getPriority());
        taskItem.setBookedAt(taskDetails.getBookedAt());

        Task updatedTask = taskService.saveOrUpdate(taskItem);
        return updatedTask;
    }

    // takes email id as input and date/time range. check if a particular user
    // is available for the date range that has been provided
    @RequestMapping(path = "/isUserAvailable/{userEmailId}", method = RequestMethod.GET)
    public boolean isAvailable(@PathVariable int userEmailId,
                               @RequestParam (value="fromDate")     @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
                               @RequestParam(value="toDate")     @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate) {
        this.userEmailId = userEmailId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        Task taskItem = taskService.getTaskById(userEmailId);
        return (fromDate.after(taskItem.getBookedAt()) && toDate.after(taskItem.getBookedAt())
                || fromDate.before(taskItem.getBookedAt()) && toDate.before(taskItem.getBookedAt()));
    }
}
