package it.laskaridis.blueground.bookings.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.laskaridis.blueground.bookings.model.Booking;
import it.laskaridis.blueground.bookings.model.BookingService;
import it.laskaridis.blueground.bookings.model.BookingsRepository;
import it.laskaridis.blueground.bookings.view.CreateBookingParams;
import it.laskaridis.blueground.bookings.view.ShowBookingView;
import it.laskaridis.blueground.bookings.view.UpdateBookingParams;
import it.laskaridis.blueground.units.controller.UnitNotFoundException;
import it.laskaridis.blueground.units.model.Unit;
import it.laskaridis.blueground.units.model.UnitsRepository;
import it.laskaridis.blueground.users.controller.UserNotFoundException;
import it.laskaridis.blueground.users.model.User;
import it.laskaridis.blueground.users.model.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

import static it.laskaridis.blueground.bookings.controller.ResourceUriFactories.createResourceUriForBooking;
import static it.laskaridis.blueground.users.model.Role.ADMINISTRATOR;
import static java.util.stream.Collectors.toList;

/**
 * Controller managing {@link Booking} resources.
 */

@Tag(name = "Booking")
@RestController
@RequestMapping("/api/v1")
@RolesAllowed({ ADMINISTRATOR })
public class BookingsController {

    private final UsersRepository users;
    private final UnitsRepository units;
    private final BookingsRepository bookings;
    private final BookingService bookingService;

    @Autowired
    public BookingsController(
            UsersRepository users,
            UnitsRepository units,
            BookingsRepository bookings,
            BookingService bookingService) {

        this.users = users;
        this.units = units;
        this.bookings = bookings;
        this.bookingService = bookingService;
    }

    /**
     * Retrieve a {@link User}'s {@link Booking}s
     */
    @GetMapping("/users/{userId}/bookings")
    public List<ShowBookingView> getUserBookings(@PathVariable String userId) {
        User user = this.users.findByUuid(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<Booking> model = this.bookings.findAllByUser(user);
        return model.stream().map(ShowBookingView::fromModel).collect(toList());
    }

    /**
     * Retrieve a {@link Unit}'s {@link Booking}s
     */
    @GetMapping("/units/{unitId}/bookings")
    public List<ShowBookingView> getUnitBookings(@PathVariable String unitId) {
        Unit unit = this.units.findByUuid(unitId).orElseThrow(() -> new UnitNotFoundException(unitId));
        List<Booking> model = this.bookings.findAllByUnit(unit);
        return model.stream().map(ShowBookingView::fromModel).collect(toList());
    }

    /**
     * Create a {@link Booking}
     */
    @PostMapping("/units/{unitId}/bookings")
    public ResponseEntity createUnitBooking(@PathVariable String unitId, @RequestBody CreateBookingParams form) {
        Unit unit = this.units.findByUuid(unitId).orElseThrow(() -> new UnitNotFoundException(unitId));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Booking booking = form.toModel(unit, user);
        booking = this.bookingService.create(booking);
        ShowBookingView view = ShowBookingView.fromModel(booking);
        return ResponseEntity.created(createResourceUriForBooking(booking)).body(view);
    }

    /**
     * Retrieve a {@link Booking}
     */
    @GetMapping("/bookings/{bookingId}")
    @RolesAllowed({ ADMINISTRATOR })
    public ResponseEntity<ShowBookingView> getBooking(@PathVariable String bookingId) {
        Booking booking = this.bookings.findByUuid(bookingId).orElseThrow(() -> new BookingNotFoundException(bookingId));
        ShowBookingView view = ShowBookingView.fromModel(booking);
        String version = Long.toString(booking.getVersion());
        return ResponseEntity.status(HttpStatus.OK).eTag(version).body(view);
    }

    /**
     * Update a {@link Booking}
     */
    @PutMapping("/bookings/{bookingId}")
    @RolesAllowed({ ADMINISTRATOR })
    public ResponseEntity<ShowBookingView> updateBooking(
            @PathVariable String bookingId,
            @RequestBody UpdateBookingParams form) {

        Booking booking = this.bookings.findByUuid(bookingId).orElseThrow(() -> new BookingNotFoundException(bookingId));
        booking.setBookedFrom(form.getBookedFrom());
        booking.setBookedUntil(form.getBookedUntil());
        booking = this.bookings.save(booking);
        ShowBookingView view = ShowBookingView.fromModel(booking);
        return ResponseEntity.ok(view);
    }

    /**
     * Partially update a {@link Booking}
     */
    @PatchMapping("/bookings/{bookingId}")
    public ResponseEntity<ShowBookingView> updateBookingPartially(@PathVariable String bookingId) {
        // TODO: partial update would help writing the client app more cleanly and easily
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Delete a {@link Booking}
     */
    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable String bookingId) {
        Optional<Booking> booking = this.bookings.findByUuid(bookingId);
        if (booking.isPresent())
            this.bookings.delete(booking.get());
        return ResponseEntity.noContent().build();
    }
}
