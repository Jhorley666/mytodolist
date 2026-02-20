package com.bibavix.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

class RoleTest {

    @Test
    void testRoleFieldsAndGettersSetters() {
        UUID roleId = UUID.randomUUID();
        Role role = new Role();
        role.setRoleId(roleId);
        role.setName("ROLE_USER");

        assertEquals(roleId, role.getRoleId());
        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    void testDefaultConstructor() {
        Role role = new Role();
        assertNull(role.getRoleId());
        assertNull(role.getName());
    }

    @Test
    void testToStringDoesNotThrow() {
        UUID roleId = UUID.randomUUID();
        Role role = new Role();
        role.setRoleId(roleId);
        role.setName("ROLE_ADMIN");
        assertDoesNotThrow(role::toString);
    }
}