package com.bibavix.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user_timer", catalog = "todo_list", schema = "todo_list")
public class UserTimer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_timer")
    private Integer idUserTimer;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "total_seconds_accumulated")
    private Integer totalSecondsAccumulated;

    @Column(name = "started_at")
    private Timestamp startedAt;

    @Column(name = "is_running")
    private Boolean isRunning;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
