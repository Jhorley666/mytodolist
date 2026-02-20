package com.bibavix.model;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskStatusTest {

    @Test
    void testTaskStatusFieldsAndGettersSetters() {
        UUID statusId = UUID.randomUUID();
        TaskStatus status = new TaskStatus();
        status.setStatusId(statusId);
        status.setName("In Progress");

        assertEquals(statusId, Optional.ofNullable(status.getStatusId()).get());
        assertEquals("In Progress", status.getName());
    }

    @Test
    void testDefaultConstructor() {
        TaskStatus status = new TaskStatus();
        assertNull(status.getStatusId());
        assertNull(status.getName());
    }
}