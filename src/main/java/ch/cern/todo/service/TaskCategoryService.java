package ch.cern.todo.service;

import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.repository.TaskCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskCategoryService {

    private final TaskCategoryRepository taskCategoryRepository;


    public TaskCategoryService(TaskCategoryRepository taskCategoryRepository) {
        this.taskCategoryRepository = taskCategoryRepository;
    }

    public List<TaskCategory> findAll() {
        return taskCategoryRepository.findAll();
    }

    public Optional<TaskCategory> findById(int id) {
        return taskCategoryRepository.findById(id);
    }

    public Optional<TaskCategory> findByName(String name) {
        return taskCategoryRepository.findByCategoryName(name);
    }

    public TaskCategory save(TaskCategory taskCategory) {
        return this.taskCategoryRepository.save(taskCategory);
    }

    public TaskCategory update(TaskCategory taskCategory) {
        return this.taskCategoryRepository.save(taskCategory);
    }

    public void deleteById(int id) {
        this.taskCategoryRepository.deleteById(id);
    }
}
