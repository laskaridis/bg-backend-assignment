package it.laskaridis.blueground.bookings.model;

import it.laskaridis.blueground.reminders.model.Reminder;
import it.laskaridis.blueground.units.model.Unit;
import it.laskaridis.blueground.users.model.User;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Entity @Table(name = "bookings") @EntityListeners(AuditingEntityListener.class)
@Data @Builder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class Booking {

    private @Id @GeneratedValue(strategy = IDENTITY) Long id;
    private @EqualsAndHashCode.Include String uuid;
    private @Version Long version;
    private @ManyToOne @NotNull @JoinColumn(name = "user_id") User user;
    private @ManyToOne @NotNull @JoinColumn(name = "unit_id") Unit unit;
    private @NotNull @Column(name = "booked_from") LocalDate bookedFrom;
    private @NotNull @Column(name = "booked_until") LocalDate bookedUntil;
    private @CreatedDate @Column(name = "created_at") LocalDateTime createdAt;
    private @LastModifiedDate @Column(name = "last_modified_at") LocalDateTime lastModifiedAt;
    private @CreatedBy @ManyToOne @JoinColumn(name = "created_by_id") User createdBy;
    private @LastModifiedBy @ManyToOne @JoinColumn(name = "last_modified_by_id") User lastModifiedBy;
    private @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    Reminder reminder;

    @PrePersist
    private void setUuid() {
        this.uuid = UUID.randomUUID().toString();
    }

    public Optional<User> getCreatedBy() {
        return Optional.ofNullable(this.createdBy);
    }

    public Optional<User> getLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }
}
