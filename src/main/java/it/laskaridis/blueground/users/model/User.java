package it.laskaridis.blueground.users.model;

import it.laskaridis.blueground.commons.validation.constraints.TimeZone;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity @Table(name = "users") @EntityListeners(AuditingEntityListener.class)
@Data @Builder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class User implements UserDetails {

    private @Id @GeneratedValue(strategy = IDENTITY) Long id;
    private @EqualsAndHashCode.Include String uuid;
    private @Version Long version;
    private @NotBlank @Email String email;
    private @NotBlank String password;
    private @NotBlank String role;
    private @TimeZone String timezone;
    private @CreatedDate @Column(name = "created_at") LocalDateTime createdAt;
    private @LastModifiedDate @Column(name = "last_modified_at") LocalDateTime lastModifiedAt;
    private @CreatedBy @ManyToOne @JoinColumn(name = "created_by_id") User createdBy;
    private @LastModifiedBy @ManyToOne @JoinColumn(name = "last_modified_by_id") User lastModifiedBy;

    @PrePersist
    private void beforeCreate() {
        this.uuid = UUID.randomUUID().toString();
        getDefaultTimezone();
    }

    private void getDefaultTimezone() {
        if (this.timezone == null)
            this.timezone = ZoneId.systemDefault().toString();
    }

    public Optional<User> getCreatedBy() {
        return Optional.ofNullable(this.createdBy);
    }

    public Optional<User> getLastModifiedBy() {
        return Optional.ofNullable(this.lastModifiedBy);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getRole()));
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
