package com.nextfin.onboarding.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private String version;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt;

}
