package com.bibavix.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "permissions", catalog = "todo_list", schema = "todo_list")
public class Permission {
    @Id
    @GeneratedValue
    @Column(name = "permission_id", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    @JdbcTypeCode(Types.BINARY)
    private UUID permissionId;

    @Column(name = "permission_name")
    private String permissionName;
}
