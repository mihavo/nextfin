package com.nextfin.onboarding.entity;

import com.nextfin.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tos_acceptances")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TosAcceptance {
    
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "tos_version", nullable = false)
    private String tosVersion;

    @Column(name = "acceptance_date", nullable = false)
    private LocalDateTime acceptanceDate;
}
