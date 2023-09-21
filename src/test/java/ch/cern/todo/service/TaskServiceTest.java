package ch.cern.todo.service;

import static org.junit.jupiter.api.Assertions.*;

import ch.cern.todo.exception.CategoryNotFoundException;
import ch.cern.todo.model.Task;
import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaskServiceTest {

    private static final String TaskName = "task name";
    private static final String TaskDescription = "desc";

    private static final String TaskCategoryName = "category name";
    private static final String TaskCategoryDescription = "desc";

    @Autowired
    TaskService taskService;
    @Autowired
    TaskCategoryService taskCategoryService;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskCategoryRepository taskCategoryRepository;

    @BeforeEach
    public void clear() {
        taskRepository.deleteAll();
        taskCategoryRepository.deleteAll();
    }

    @Test
    void canCreateTaskWithExistingCategory() throws Exception{
        TaskCategory category = taskCategoryService.save(new TaskCategory(TaskCategoryName, TaskCategoryDescription));
        taskCategoryRepository.save(category);
        taskService.save(TaskName, TaskDescription, new Date(), category.getCategoryName());
        Task foundTask = taskService.findAll().get(0);
        assertAll(
                () -> assertEquals(TaskCategoryName, foundTask.getTaskCategory().getCategoryName()),
                () -> assertEquals(TaskCategoryDescription, foundTask.getTaskCategory().getCategoryDescription()),
                () -> assertEquals(TaskName, foundTask.getTaskName()),
                () -> assertEquals(TaskDescription, foundTask.getTaskDescription())
        );
    }

    @Test
    void cannotCreteTaskWithoutCategory() throws Exception {
        assertThrows(CategoryNotFoundException.class, ()->taskService.save(TaskName, TaskDescription, new Date(), null));
    }

    @Test
    void successfulUpdateCase() throws Exception{
        TaskCategory category = taskCategoryService.save(new TaskCategory(TaskCategoryName, TaskCategoryDescription));
        taskCategoryRepository.save(category);
        Task saved = taskService.save(TaskName, TaskDescription, new Date(), category.getCategoryName());

        saved.setTaskName("updated");
        saved.setTaskDescription("updated");

        taskService.update(saved);

        Task foundTask = taskService.findById(saved.getId()).orElseGet(Assertions::fail);
        assertAll(
                () -> assertEquals("updated", foundTask.getTaskName()),
                () -> assertEquals("updated", foundTask.getTaskDescription())
        );
    }

    @Test
    void shouldNotAddTaskIfAlreadyInCategory() throws Exception{
        TaskCategory taskCategory = new TaskCategory(TaskCategoryName, TaskCategoryDescription);
        taskCategoryRepository.save(taskCategory);
        taskService.save(TaskName, TaskDescription, new Date(), taskCategory.getCategoryName());
        assertThrows(Exception.class,
                ()-> taskService.save(TaskName, TaskDescription, new Date(), taskCategory.getCategoryName()));
    }

}