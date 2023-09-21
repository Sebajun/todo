package ch.cern.todo.service;

import ch.cern.todo.exception.CategoryNotFoundException;
import ch.cern.todo.model.Task;
import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.repository.TaskRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

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

    public Task save(String taskName, String taskDescription, Date deadline, String categoryName) throws CategoryNotFoundException {
        Optional<TaskCategory> category = taskCategoryService.findByName(categoryName);
        return category
                .map(taskCategory -> taskRepository.save(new Task(taskName, taskDescription, deadline, taskCategory)))
                .orElseThrow(()-> new CategoryNotFoundException());
    }

    public Task update(Task task) {
        return taskRepository.save(task);
    }

    public void deleteById(int id) {
        taskRepository.deleteById(id);
    }
}