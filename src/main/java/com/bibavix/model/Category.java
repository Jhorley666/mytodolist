package com.bibavix.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "categories", catalog = "todo_list", schema = "todo_list")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    @JdbcTypeCode(Types.BINARY)
    private UUID categoryId;

    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "created_at")
    private LocalDate createdAt;
}
