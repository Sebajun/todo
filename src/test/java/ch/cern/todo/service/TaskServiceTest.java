package ch.cern.todo.service;

import ch.cern.todo.model.Task;
import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

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
    void canCreateTaskWithNotExistingCategory() {
        TaskCategory taskCategory = new TaskCategory(TaskCategoryName, TaskCategoryDescription);
        taskService.save(TaskName, TaskDescription, new Date(), taskCategory);
        Task foundTask = taskService.findAll().get(0);
        assertAll(
                () -> assertEquals(TaskCategoryName, foundTask.getTaskCategory().getCategoryName()),
                () -> assertEquals(TaskCategoryDescription, foundTask.getTaskCategory().getCategoryDescription()),
                () -> assertEquals(TaskName, foundTask.getTaskName()),
                () -> assertEquals(TaskDescription, foundTask.getTaskDescription())
        );
    }

    @Test
    void canCreateTaskWithExistingCategory() {
        TaskCategory category = taskCategoryService.save(new TaskCategory(TaskCategoryName, TaskCategoryDescription));
        taskCategoryRepository.save(category);
        taskService.save(TaskName, TaskDescription, new Date(), category);
        Task foundTask = taskService.findAll().get(0);
        assertAll(
                () -> assertEquals(TaskCategoryName, foundTask.getTaskCategory().getCategoryName()),
                () -> assertEquals(TaskCategoryDescription, foundTask.getTaskCategory().getCategoryDescription()),
                () -> assertEquals(TaskName, foundTask.getTaskName()),
                () -> assertEquals(TaskDescription, foundTask.getTaskDescription())
        );
    }

    @Test
    void successfulUpdateCase() {
        TaskCategory category = taskCategoryService.save(new TaskCategory(TaskCategoryName, TaskCategoryDescription));
        Task saved = taskService.save(TaskName, TaskDescription, new Date(), category);

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
    void shouldNotAddTaskIfAlreadyInCategory() {
        TaskCategory taskCategory = new TaskCategory(TaskCategoryName, TaskCategoryDescription);
        taskService.save(TaskName, TaskDescription, new Date(), taskCategory);
        assertThrows(Exception.class, () -> taskService.save(TaskName, TaskDescription, new Date(), taskCategory));
    }

}