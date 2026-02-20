package com.bibavix.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTaskFieldsAndGettersSetters() {
        UUID taskId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();
        UUID priorityId = UUID.randomUUID();

        Task task = new Task();
        task.setTaskId(taskId);
        task.setUserId(userId);
        task.setCategoryId(categoryId);
        task.setStatusId(statusId);
        task.setTitle("Test Title");
        task.setDescription("Test Description");
        task.setPriorityId(priorityId);
        LocalDate dueDate = LocalDate.of(2025, 7, 18);
        task.setDueDate(dueDate);
        LocalDateTime now = LocalDateTime.now();
        task.setCreatedAt(now);
        task.setUpdatedAt(now);

        assertEquals(taskId, task.getTaskId());
        assertEquals(userId, task.getUserId());
        assertEquals(categoryId, task.getCategoryId());
        assertEquals(statusId, Optional.ofNullable(task.getStatusId()).get());
        assertEquals("Test Title", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertEquals(priorityId, task.getPriorityId());
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
    void testToStringDoesNotThrow() {
        Task task = new Task();
        task.setTaskId(UUID.randomUUID());
        task.setTitle("Sample");
        assertDoesNotThrow(task::toString);
    }
}