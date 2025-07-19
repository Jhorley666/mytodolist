package com.bibavix.model;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskStatusTest {

    @Test
    void testTaskStatusFieldsAndGettersSetters() {
        TaskStatus status = new TaskStatus();
        status.setStatusId((short) 1);
        status.setName("In Progress");

        assertEquals((short) 1, Optional.ofNullable(status.getStatusId()).get());
        assertEquals("In Progress", status.getName());
    }

    @Test
    void testDefaultConstructor() {
        TaskStatus status = new TaskStatus();
        assertNull(status.getStatusId());
        assertNull(status.getName());
    }
}