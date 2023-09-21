package ch.cern.todo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class TaskCategory {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private int id;

    @NotNull
    @Column(name = "category_name", unique = true)
    @Length(max = 100)
    private String categoryName;

    @Length(max = 200)
    @Column(name = "category_desc")
    private String categoryDescription;

    @OneToMany(targetEntity = Task.class, mappedBy = "taskCategory", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Task> taskList;

    public TaskCategory(String categoryName, String categoryDescription) {
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
    }
}
