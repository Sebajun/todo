package ch.cern.todo.controller;

import ch.cern.todo.exception.CategoryNotFoundException;
import ch.cern.todo.model.Task;
import ch.cern.todo.service.TaskService;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(final TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getTasks() {
        return taskService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Task> getTask(@PathVariable int id) {
        return taskService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestParam String name,
                                           @RequestParam String description,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date deadline,
                                           @RequestParam String categoryName) {
        try {
            return ResponseEntity.ok(taskService.save(name, description, deadline, categoryName));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public Task updateTask(@RequestBody Task task) {
        return taskService.update(task);
    }

    @DeleteMapping("{id}")
    public void deleteTask(@PathVariable int id) {
        taskService.deleteById(id);
    }
}
