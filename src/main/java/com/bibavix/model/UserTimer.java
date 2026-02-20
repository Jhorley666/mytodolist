package com.bibavix.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user_timer", catalog = "todo_list", schema = "todo_list")
public class UserTimer {
    @Id
    @GeneratedValue
    @Column(name = "id_user_timer", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    @JdbcTypeCode(Types.BINARY)
    private UUID idUserTimer;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "total_seconds_accumulated")
    private Integer totalSecondsAccumulated;

    @Column(name = "started_at")
    private Timestamp startedAt;

    @Column(name = "is_running")
    private Boolean isRunning;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
