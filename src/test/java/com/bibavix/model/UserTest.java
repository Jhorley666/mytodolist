package com.bibavix.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserFieldsAndGettersSetters() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setUserId(userId);
        user.setUsername("alice");
        user.setEmail("alice@example.com");
        user.setPassword("secret");
        user.setEnabled(true);

        UUID roleId = UUID.randomUUID();
        Role role = new Role();
        role.setRoleId(roleId);
        role.setName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        assertEquals(userId, user.getUserId());
        assertEquals("alice", user.getUsername());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals("secret", user.getPassword());
        assertTrue(user.isEnabled());
        assertNotNull(user.getRoles());
        assertEquals(1, user.getRoles().size());
        assertEquals("ROLE_USER", user.getRoles().iterator().next().getName());
    }

    @Test
    void testDefaultConstructor() {
        User user = new User();
        assertNull(user.getUserId());
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertFalse(user.isEnabled());
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
    }

    @Test
    void testToStringDoesNotThrow() {
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setUsername("bob");
        assertDoesNotThrow(user::toString);
    }
}