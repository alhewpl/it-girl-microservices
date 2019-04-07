package com.itGirl.ToDo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name="TASKS")
public class Task {
    @Column(name="TASK_NAME")
    private String taskName;

    @Id
    @GeneratedValue
    @Column(name="TASK_ID")
    private int taskId;

    @Column(name="BOOKED_AT")
    private Date bookedAt;

    @Column(name="PRIORITY")
    private String priority;

    @Column(name="USER_EMAIL_ID")
    private int userEmailId;
}
