package com.bibavix.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories", catalog = "todo_list", schema = "todo_list")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "user_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private Integer userId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;
}
