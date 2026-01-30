package com.bibavix.dto;

import com.bibavix.model.Permission;
import com.bibavix.model.Role;
import com.bibavix.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsImplTest {

    @Test
    void build_shouldMapUserToUserDetailsImpl() {
        User user = new User();
        user.setUsername("alice");
        user.setPassword("secret");
        Role role = new Role();
        role.setName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        assertEquals("alice", userDetails.getUsername());
        assertEquals("secret", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        GrantedAuthority authority = userDetails.getAuthorities().iterator().next();
        assertEquals("ROLE_USER", authority.getAuthority());
        assertEquals("ROLE_USER", authority.getAuthority());
    }

    @Test
    void build_shouldIncludeDirectUserPermissions() {
        User user = new User();
        user.setUsername("bob");
        user.setPassword("password");
        user.setRoles(Collections.emptySet());

        Permission permission = new Permission();
        permission.setPermissionName("task:read");
        permission.setPermissionId(1);
        Set<Permission> permissions = new HashSet<>();
        permissions.add(permission);
        user.setPermissions(permissions);

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("task:read")));
    }

    @Test
    void getAuthorities_returnsEmptyListIfNoRoles() {
        User user = new User();
        user.setUsername("bob");
        user.setPassword("pass");
        user.setRoles(Collections.emptySet());

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        assertNotNull(userDetails.getAuthorities());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void defaultAccountStatusMethods_returnTrue() {
        UserDetailsImpl userDetails = new UserDetailsImpl("user", "pw", Collections.emptyList());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }
}