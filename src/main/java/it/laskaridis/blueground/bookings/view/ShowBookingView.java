package it.laskaridis.blueground.bookings.view;

import it.laskaridis.blueground.bookings.model.Booking;
import it.laskaridis.blueground.users.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ShowBookingView {

    public static ShowBookingView fromModel(Booking model) {
        return ShowBookingView.builder()
                .bookingId(model.getUuid())
                .unitId(model.getUnit().getUuid())
                .unitTitle(model.getUnit().getTitle())
                .unitRegion(model.getUnit().getRegion())
                .bookedFrom(model.getBookedFrom())
                .bookedUntil(model.getBookedUntil())
                .bookedAt(model.getCreatedAt())
                .lastModifiedAt(model.getLastModifiedAt())
                .bookedBy(model.getCreatedBy().map(User::getEmail).orElse(null))
                .lastUpdatedBy(model.getLastModifiedBy().map(User::getEmail).orElse(null))
                .reminderScheduledAt(model.getReminder().getScheduledAt())
                .reminderSentAt(model.getReminder().getSentAt().orElse(null))
                .build();
    }

    private String bookingId;
    private String unitId;
    private String unitTitle;
    private String unitRegion;
    private LocalDate bookedFrom;
    private LocalDate bookedUntil;
    private LocalDateTime bookedAt;
    private String bookedBy;
    private LocalDateTime lastModifiedAt;
    private String lastUpdatedBy;
    private LocalDateTime reminderScheduledAt;
    private LocalDateTime reminderSentAt;

}
