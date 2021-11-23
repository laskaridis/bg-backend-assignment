package it.laskaridis.blueground.bookings.view;

import it.laskaridis.blueground.bookings.model.Booking;
import it.laskaridis.blueground.units.model.Unit;
import it.laskaridis.blueground.users.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class CreateBookingParams {

    private LocalDate bookedFrom;
    private LocalDate bookedUntil;

    public Booking toModel(Unit unit, User user) {
        return Booking.builder()
                .unit(unit)
                .user(user)
                .bookedFrom(bookedFrom)
                .bookedUntil(bookedUntil)
                .build();
    }
}
