package it.laskaridis.blueground.units.model;

import it.laskaridis.blueground.users.model.User;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Entity @Table(name = "units") @EntityListeners(AuditingEntityListener.class)
@Data @Builder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class Unit implements Serializable {

    private @Id @GeneratedValue(strategy = IDENTITY) Long id;
    private @EqualsAndHashCode.Include String uuid;
    private @Version Long version;
    private @NotBlank String title;
    private @NotBlank String region;
    private String description;
    private @NotBlank @Column(name = "cancellation_policy") String cancellationPolicy;
    private @Embedded @Valid Money price;
    private @CreatedDate @Column(name = "created_at") LocalDateTime createdAt;
    private @LastModifiedDate @Column(name = "last_modified_at") LocalDateTime lastModifiedAt;
    private @CreatedBy @ManyToOne @JoinColumn(name = "created_by_id") User createdBy;
    private @LastModifiedBy @ManyToOne @JoinColumn(name = "last_modified_by_id") User lastModifiedBy;

    // For simplicity a single image per unit is assumed here. However, the
    // API has been designed to be able to support multiple images per unit,
    // if need to.
    private @Column(name = "image_file") String imageFile;

    @PrePersist
    private void setUuid() {
        this.uuid = UUID.randomUUID().toString();
    }

    public Optional<User> getCreatedBy() {
        return Optional.ofNullable(this.createdBy);
    }

    public Optional<User> getLastModifiedBy() {
        return Optional.ofNullable(this.lastModifiedBy);
    }
}
