package it.laskaridis.blueground.reminders.model;

import it.laskaridis.blueground.bookings.model.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Entity @Table(name = "reminders") @EntityListeners(AuditingEntityListener.class)
@Data @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class Reminder {

    private @Id @GeneratedValue(strategy = IDENTITY) Long id;
    private @EqualsAndHashCode.Include @NotNull String uuid;
    private @Version Long version;
    private @CreatedDate @NotNull @Column(name = "created_at") LocalDateTime createdAt;
    private @NotNull @Column(name = "scheduled_at") LocalDateTime scheduledAt;
    private @Column(name = "sent_at") LocalDateTime sentAt;
    private @OneToOne @JoinColumn(name = "booking_id") @NotNull Booking booking;
    private @NotNull @Column(name = "dispatch_attempts_count") Integer dispatchAttemptsCount;

    @PrePersist
    public void setUuid() {
        this.uuid = UUID.randomUUID().toString();
        this.dispatchAttemptsCount = 0;
    }

    public Optional<LocalDateTime> getSentAt() {
        return Optional.ofNullable(this.sentAt);
    }

    public boolean isSent() {
        return this.getSentAt().isPresent();
    }

    /**
     * Booking has already stated, so a reminder is functionally useless.
     *
     * @return <code>true</code>if the booking's date is in the past, <code>false</code> otherwise.
     */
    public boolean isExpired() {
        LocalDate now = LocalDate.now();
        return now.isAfter(getBooking().getBookedFrom());
    }

    /**
     * Number of times the reminder has been attempted to be sent to the user. Meant to count all
     * kinds of attempts, successful and unsuccessful. This implies that reminders that have not
     * been sent yet would have a value of <code>0</code>, successfully sent reminders on the first
     * try a value of <code>1</code>, etc. Meant to apply some sort of backoff strategy to avoid
     * poison reminders (i.e. problematic reminders that will always fail for some reason).
     *
     * @return the number of times we attempted to notify the user about his booking.
     */
    public Integer getAvailableDispatchAttempts() {
        return 3 - getDispatchAttemptsCount();
    }
}
