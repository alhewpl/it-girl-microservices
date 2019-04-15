package com.itGirl.ToDo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;



@Entity
@Getter
@Setter
@Table(name="TASKS")
public class Task {
    @Column(name="TASK_NAME", nullable = false, length = 100)
    private String taskName;

    @Id
    //@Generated(GenerationTime.ALWAYS)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="TASK_ID", nullable = false, insertable = false)
    private int taskId;

    @Column(name="BOOKED_AT", nullable = false)
    private Date bookedAt;

    @Column(name="PRIORITY", nullable = false, length = 10)
    private String priority;

    @Column(name="USER_EMAIL_ID", nullable = false)
    private int userEmailId;
}
