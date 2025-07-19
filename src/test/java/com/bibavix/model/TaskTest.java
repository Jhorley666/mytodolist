package com.bibavix.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTaskFieldsAndGettersSetters() {
        Task task = new Task();
        task.setTaskId(1);
        task.setUserId(2);
        task.setCategoryId(3);
        task.setStatusId((short) 1);
        task.setTitle("Test Title");
        task.setDescription("Test Description");
        task.setPriority(Task.Priority.High);
        LocalDate dueDate = LocalDate.of(2025, 7, 18);
        task.setDueDate(dueDate);
        LocalDateTime now = LocalDateTime.now();
        task.setCreatedAt(now);
        task.setUpdatedAt(now);

        assertEquals(1, task.getTaskId());
        assertEquals(2, task.getUserId());
        assertEquals(3, task.getCategoryId());
        assertEquals((short) 1, Optional.ofNullable(task.getStatusId()).get());
        assertEquals("Test Title", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertEquals(Task.Priority.High, task.getPriority());
        assertEquals(dueDate, task.getDueDate());
        assertEquals(now, task.getCreatedAt());
        assertEquals(now, task.getUpdatedAt());
    }

    @Test
    void testOnCreateSetsTimestamps() {
        Task task = new Task();
        task.onCreate();
        assertNotNull(task.getCreatedAt());
        assertNotNull(task.getUpdatedAt());
        assertEquals(task.getCreatedAt(), task.getUpdatedAt());
    }

    @Test
    void testOnUpdateSetsUpdatedAt() throws InterruptedException {
        Task task = new Task();
        task.onCreate();
        LocalDateTime created = task.getCreatedAt();
        Thread.sleep(10); // Ensure updatedAt will be different
        task.onUpdate();
        assertEquals(created, task.getCreatedAt());
        assertTrue(task.getUpdatedAt().isAfter(created));
    }

    @Test
    void testPriorityEnumValues() {
        assertEquals(Task.Priority.Low, Task.Priority.valueOf("Low"));
        assertEquals(Task.Priority.Medium, Task.Priority.valueOf("Medium"));
        assertEquals(Task.Priority.High, Task.Priority.valueOf("High"));
    }

    @Test
    void testToStringDoesNotThrow() {
        Task task = new Task();
        task.setTaskId(1);
        task.setTitle("Sample");
        assertDoesNotThrow(task::toString);
    }
}