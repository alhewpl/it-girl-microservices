package com.itGirl.ToDo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;



@Entity
@Getter
@Setter
@ApiModel(description = "All details about the Tasks ")
@Table(name="TASKS")
public class Task {
    @ApiModelProperty(notes = "What the task is")
    @Column(name="TASK_NAME", nullable = false, length = 100)
    private String taskName;

    @ApiModelProperty(notes = "The database generated task ID")
    @Id
    //@Generated(GenerationTime.ALWAYS)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="TASK_ID", nullable = false, insertable = false)
    private int taskId;

    @ApiModelProperty(notes = "Timestamp of when the task was added")
    @Column(name="BOOKED_AT", nullable = false)
    private Date bookedAt;

    @ApiModelProperty(notes = "Priority of a task to be completed")
    @Column(name="PRIORITY", nullable = false, length = 10)
    private String priority;

    @ApiModelProperty(notes = "Email id of the user the task belongs to")
    @Column(name="USER_EMAIL_ID", nullable = false)
    private int userEmailId;
}
