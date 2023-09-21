package ch.cern.todo.controller;

import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.service.TaskCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/task-category")
public class TaskCategoryController {

    private final TaskCategoryService taskCategoryService;

    public TaskCategoryController(TaskCategoryService taskCategoryService) {
        this.taskCategoryService = taskCategoryService;
    }

    @GetMapping
    public List<TaskCategory> getTaskCategories() {
        return taskCategoryService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<TaskCategory> getTaskCategory(@PathVariable(required = true) int id) {
        return taskCategoryService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TaskCategory createTaskCategory(@RequestParam String categoryName, @RequestParam String categoryDescription) {
        return taskCategoryService.save(new TaskCategory(categoryName, categoryDescription));
    }

    @PutMapping
    public TaskCategory updateTaskCategory(@RequestBody TaskCategory taskCategory) {
        return taskCategoryService.update(taskCategory);
    }

    @DeleteMapping("{id}")
    public void deleteTask(@PathVariable int id) {
        taskCategoryService.deleteById(id);
    }
}
