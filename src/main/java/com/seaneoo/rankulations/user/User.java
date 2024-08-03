package com.seaneoo.rankulations.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "regulation_users")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String username;

    @Column(name = "profile_pic")
    @JsonProperty("profile_pic")
    String profilePic;

    @Enumerated(value = STRING)
    @Builder.Default
    UserRole role = UserRole.USER;

    @Column(nullable = false)
    @Builder.Default
    @JsonIgnore
    Boolean enabled = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    @JsonProperty("created_at")
    Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    @JsonProperty("updated_at")
    Instant updatedAt;

    @Enumerated(value = STRING)
    @Column(name = "auth_provider", nullable = false)
    @JsonProperty("auth_provider")
    AuthProvider authProvider;

    @Column(name = "provider_id", nullable = false, unique = true)
    @JsonProperty("provider_id")
    String providerId;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return null;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return enabled;
    }
}
