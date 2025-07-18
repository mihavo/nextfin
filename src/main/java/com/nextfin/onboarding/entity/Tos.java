package com.nextfin.onboarding.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tos {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^\\d+\\.\\d+$", message = "Version must be in the format x.x")
    private String version;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt;

}
