package com.nextfin.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nextfin.AppConstants;
import com.nextfin.auth.enums.OnboardingStep;
import com.nextfin.generic.Auditable;
import com.nextfin.holder.entity.Holder;
import com.nextfin.users.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Check(constraints = "(auth_provider != 'local') OR (hashed_password IS NOT NULL)")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@DynamicUpdate
@ToString(exclude = "holder")
public class User extends Auditable implements NextfinUserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "username", nullable = false, unique = true)
	@Size(min = 7, max = 28, message = "Username must be between 7 and 28 characters")
	private String username;

	@Column(name = "hashed_password")
	@Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
	@Pattern(
			regexp = AppConstants.USER_PWD_REGEX,
			message = "Password must contain at least one digit, one lowercase, one uppercase letter, and one special character"
	)
	@JsonIgnore
	private String hashedPassword;

	@Column(name = "email", nullable = false, unique = true)
	@Email
	private String email;

	@Column(name = "preferred-phone-number", unique = true)
	@Size(min = 10, max = 15, message = "Phone number should be between 10 and 15 digits")
	private String preferredPhoneNumber;

	@Column(name = "social-security-number")
	@Size(min = 9, max = 9, message = "Social security number must be 9 digits")
	private String socialSecurityNumber;

	@Column(name = "role", nullable = false)
	@Enumerated
	@Builder.Default
	private Role role = Role.USER;

	@Column(name = "is_expired", nullable = false)
	@Builder.Default
	private boolean isExpired = false;

	@Column(name = "is_locked", nullable = false)
	@Builder.Default
	private boolean isLocked = false;

	@Column(name = "auth_provider")
	private String authProvider;

	@Column(name = "auth_client_name", nullable = false)
	@Builder.Default
	private String authClientName = "local";

	@Column(name = "auth_provider_id", columnDefinition = "TEXT")
	private String authProviderId;

	@Getter(onMethod = @__(@JsonIgnore))
	@OneToOne(mappedBy = "user")
	private Holder holder;

    @Column(name = "onboarding_step", nullable = false)
    @Builder.Default
    private OnboardingStep onboardingStep = OnboardingStep.HOLDER_CREATION;

    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return this.hashedPassword;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !isExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !isLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof User && ((User) obj).getId().equals(this.id);
	}
}
