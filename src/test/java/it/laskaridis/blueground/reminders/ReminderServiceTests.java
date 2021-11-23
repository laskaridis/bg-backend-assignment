package it.laskaridis.blueground.reminders;

import it.laskaridis.blueground.bookings.model.Booking;
import it.laskaridis.blueground.commons.emails.ApplicationMailer;
import it.laskaridis.blueground.commons.emails.EmailDispatchException;
import it.laskaridis.blueground.reminders.model.Reminder;
import it.laskaridis.blueground.reminders.model.ReminderDispatchBackoffStrategy;
import it.laskaridis.blueground.reminders.model.ReminderService;
import it.laskaridis.blueground.reminders.model.RemindersRepository;
import it.laskaridis.blueground.reminders.view.ReminderEmail;
import it.laskaridis.blueground.users.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
public class ReminderServiceTests {

    private ReminderService subject;

    private @MockBean RemindersRepository reminders;
    private @MockBean ApplicationMailer mailer;
    private @MockBean ReminderDispatchBackoffStrategy backoffStrategy;
    private @Captor ArgumentCaptor<Reminder> reminderArgumentCaptor;

    @BeforeEach
    public void setUp() {
        this.subject = new ReminderService(
                this.reminders,
                this.backoffStrategy,
                this.mailer
        );
    }

    @Test
    public void dispatch_shouldNotSendRemindersAlreadySent() {
        // Given
        Reminder anAlreadySentReminder = createNewAlreadySentReminder();

        // Then
        verify(this.mailer, never()).send(any(), any());

        // When
        this.subject.dispatch(anAlreadySentReminder);
    }

    @Test
    public void dispatch_shouldNotSendRemindersThatExpired() {
        // Given
        Reminder anExpiredReminder = createNewExpiredReminder();

        // Then
        verify(this.mailer, never()).send(any(), any());

        // When
        this.subject.dispatch(anExpiredReminder);
    }

    @Test
    public void dispatch_shouldNotSendRemindersWhichNeedToBackoff() {
        // Given
        Reminder aValidReminder = createNewValidReminder();
        doReturn(false).when(this.backoffStrategy).isEligibleForSending(aValidReminder);

        // Then
        verify(this.mailer, never()).send(any(), any());

        // When
        this.subject.dispatch(aValidReminder);
    }

    @Test
    public void dispatch_shouldSendEligibleReminders() {
        // Given
        Reminder aValidReminder = createNewValidReminder();
        String expectedRecipient = aValidReminder.getBooking().getUser().getEmail();
        ReminderEmail expectedEmail = new ReminderEmail(aValidReminder);
        assertThat(aValidReminder.getDispatchAttemptsCount()).isEqualTo(0);
        doReturn(true).when(this.backoffStrategy).isEligibleForSending(aValidReminder);
        doReturn(aValidReminder).when(this.reminders).save(aValidReminder);

        // Then
        verify(this.mailer, atMostOnce()).send(expectedRecipient, expectedEmail);

        // When
        this.subject.dispatch(aValidReminder);
        assertThat(aValidReminder.getDispatchAttemptsCount()).isEqualTo(1);
    }

    @Test
    public void dispatch_shouldIncrementAttemptsCountOnError() {
        // Given
        Reminder aValidReminder = createNewValidReminder();
        String expectedRecipient = aValidReminder.getBooking().getUser().getEmail();
        ReminderEmail expectedEmail = new ReminderEmail(aValidReminder);
        assertThat(aValidReminder.getDispatchAttemptsCount()).isEqualTo(0);
        doReturn(true).when(this.backoffStrategy).isEligibleForSending(aValidReminder);
        EmailDispatchException exception = new EmailDispatchException("code", "description");
        doThrow(exception).when(this.mailer).send(expectedRecipient, expectedEmail);
        doReturn(aValidReminder).when(this.reminders).save(aValidReminder);

        // When
        this.subject.dispatch(aValidReminder);

        // Then
        assertThat(aValidReminder.getDispatchAttemptsCount()).isEqualTo(1);
    }

    private Reminder createNewAlreadySentReminder() {
        Reminder reminder = new Reminder();
        reminder.setSentAt(LocalDateTime.now());
        return reminder;
    }

    private Reminder createNewExpiredReminder() {
        Reminder reminder = new Reminder();
        Booking booking = new Booking();
        booking.setBookedFrom(LocalDate.now().minusDays(1));
        reminder.setBooking(booking);
        return reminder;
    }

    private Reminder createNewValidReminder() {
        User user = new User();
        user.setEmail("user@localhost");
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBookedFrom(LocalDate.now().plusDays(1));
        Reminder reminder = new Reminder();
        reminder.setBooking(booking);
        reminder.setDispatchAttemptsCount(0);
        return reminder;
    }
}
