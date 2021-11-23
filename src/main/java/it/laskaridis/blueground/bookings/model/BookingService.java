package it.laskaridis.blueground.bookings.model;

import it.laskaridis.blueground.reminders.model.Reminder;
import it.laskaridis.blueground.reminders.model.RemindersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.print.Book;
import java.time.*;
import java.util.TimeZone;

import static org.springframework.util.Assert.notNull;

@Service
public class BookingService {

    private final BookingsRepository bookings;
    private final RemindersRepository reminders;

    @Autowired
    public BookingService(BookingsRepository bookings, RemindersRepository reminders) {
        this.bookings = bookings;
        this.reminders = reminders;
    }

    @Transactional
    public Booking create(Booking booking) {
        notNull(booking, "booking should not be null");
        verifyBookingSlotIsAvailable(booking);
        validateAndPersist(booking);
        return createBookingReminder(booking);
    }

    private void verifyBookingSlotIsAvailable(Booking booking) {
        // TODO: A production-grade application would probably check against
        // overlapping bookings at this point. Hope this implementation suffices
        // for the purposes of this demo.
    }

    private Booking validateAndPersist(Booking booking) {
        return this.bookings.save(booking);
    }

    private Booking createBookingReminder(Booking booking) {
        LocalDateTime dispatchTimeAtLocalZone = getReminderScheduledDispatchTimestamp(booking);
        Reminder reminder = new Reminder();
        reminder.setBooking(booking);
        reminder.setScheduledAt(dispatchTimeAtLocalZone);
        booking.setReminder(reminder);
        return this.bookings.save(booking);
    }

    /**
     * Converts 30 days prior to booking at 11:00 in the customer's local time,
     * into local time.
     */
    private LocalDateTime getReminderScheduledDispatchTimestamp(Booking booking) {
        // FIXME: specify reminder days and time in configuration instead of hard coding them
        LocalDate reminderDate = booking.getBookedFrom().minusDays(30);
        LocalTime reminderTime = LocalTime.of(11, 00);
        ZoneId userZone = TimeZone.getTimeZone(booking.getUser().getTimezone()).toZoneId();
        ZonedDateTime dispatchTimeAtUserZone = ZonedDateTime.of(reminderDate, reminderTime, userZone);
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime dispatchTimeAtLocalZone = dispatchTimeAtUserZone.withZoneSameInstant(localZone);
        return dispatchTimeAtLocalZone.toLocalDateTime();
    }

}
