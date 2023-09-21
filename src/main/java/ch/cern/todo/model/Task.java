package ch.cern.todo.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue
    @Column(name = "task_id")
    private int id;

    @NotNull
    @Length(max = 100)
    @Column(name = "task_name")
    private String taskName;

    @Length(max = 200)
    @Column(name = "task_description")
    private String taskDescription;

    @NotNull
    private Date deadline;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "task_category_id")
    @NotNull
    private TaskCategory taskCategory;

    public Task(final String taskName, final String taskDescription, final Date deadline, final TaskCategory taskCategory) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.deadline = deadline;
        this.taskCategory = taskCategory;
    }
}
