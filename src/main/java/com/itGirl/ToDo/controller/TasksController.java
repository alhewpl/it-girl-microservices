package com.itGirl.ToDo.controller;

import com.itGirl.ToDo.entity.Task;
import com.itGirl.ToDo.service.taskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Api(value="To Do Tasks", description="Operations pertaining to tasks in users calendar")
@FeignClient(name="ToDoService" )
@RibbonClient(name="ToDoService")
@RestController
@RequestMapping(path="/toDo")
public class TasksController {

    @Autowired
    taskService taskService;
    private int userEmailId;
    private Date fromDate;
    private Date toDate;

    @ApiOperation(value = "View a list of all tasks", response = List.class)
    @GetMapping("/all_tasks")
    private List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    // read a specific event from a provided user. searchable by taskId, priority or bookedAt date.
    //if no search criteria provided, display all events from a user. -- !! to change the response type into a list
    @ApiOperation(value = "View a list of each user's tasks", response = List.class)
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
    @ApiOperation(value = "Delete single task or all tasks of a particular user, given either the user's email id or the task id", response = String.class)
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
    @ApiOperation(value = "Create a new task", response = Task.class)
    @PostMapping("/create_task")
    @Transactional
    private Task saveTask(@RequestBody Task task) {
        taskService.saveOrUpdate(task);
        return task;
    }

    // update an existing event. Event id is must input, no other fields required
    @ApiOperation(value = "Update an existing task", response = Task.class)
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
    @ApiOperation(value = "Checks if a user is available in a certain day")
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
