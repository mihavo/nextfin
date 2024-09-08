package com.michaelvol.bankingapp.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.holder.entity.Holder;
import com.michaelvol.bankingapp.users.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@DynamicUpdate
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "username", nullable = false, unique = true)
	@Size(min = 7, max = 28, message = "Username must be between 7 and 28 characters")
	private String username;

	@Column(name = "hashed_password", nullable = false)
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

	@Column(name = "social-security-number")
	@Size(min = 9, max = 9, message = "Social security number must be 9 digits")
	private String socialSecurityNumber;

	@Column(name = "role", nullable = false)
	@Enumerated
	@Builder.Default
	private Role role = Role.USER;

	@Column(name = "isExpired", nullable = false)
	@Builder.Default
	private boolean isExpired = false;

	@Column(name = "isLocked", nullable = false)
	@Builder.Default
	private boolean isLocked = false;

	@Column(name = "auth_provider", nullable = false)
	@Builder.Default
	private String authProvider = "local";

	@Column(name = "auth_provider_id")
	private String authProviderId;

	@OneToOne
	private Holder holder;

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
	public boolean isEnabled() {
		return true;
	}
}
