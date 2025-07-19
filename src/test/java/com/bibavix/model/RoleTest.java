package com.bibavix.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void testRoleFieldsAndGettersSetters() {
        Role role = new Role();
        role.setRoleId(1);
        role.setName("ROLE_USER");

        assertEquals(1, role.getRoleId());
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
        Role role = new Role();
        role.setRoleId(2);
        role.setName("ROLE_ADMIN");
        assertDoesNotThrow(role::toString);
    }
}