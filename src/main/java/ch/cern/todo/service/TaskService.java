package ch.cern.todo.service;

import ch.cern.todo.model.Task;
import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskCategoryService taskCategoryService;

    public TaskService(final TaskRepository taskRepository, TaskCategoryService taskCategoryService) {
        this.taskRepository = taskRepository;
        this.taskCategoryService = taskCategoryService;
    }

    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task save(String taskName, String taskDescription, Date deadline, String categoryName) {
        return taskCategoryService.findByName(categoryName)
                .map(taskCategory -> taskRepository.save(new Task(taskName, taskDescription, deadline, taskCategory)))
                .orElse(null);
    }

    public Task save(String taskName, String taskDescription, Date deadline, int categoryId) {
        return taskCategoryService.findById(categoryId)
                .map(taskCategory -> taskRepository.save(new Task(taskName, taskDescription, deadline, taskCategory)))
                .orElse(null);
    }

    public Task save(final String taskName, final String taskDescription, final Date deadline, TaskCategory category) {
        return taskCategoryService.findById(category.getId())
                .map(taskCategory -> taskRepository.save(new Task(taskName, taskDescription, deadline, taskCategory)))
                .orElseGet(() -> {
                    TaskCategory newCategory = taskCategoryService.save(
                            new TaskCategory(category.getCategoryName(), category.getCategoryDescription()));
                    return taskRepository.save(new Task(taskName, taskDescription, deadline, newCategory));
                });
    }

    public Task update(Task task) {
        return taskRepository.save(task);
    }

    public void deleteById(int id) {
        taskRepository.deleteById(id);
    }
}
