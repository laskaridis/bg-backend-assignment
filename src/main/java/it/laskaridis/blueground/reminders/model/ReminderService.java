package it.laskaridis.blueground.reminders.model;

import it.laskaridis.blueground.reminders.view.ReminderEmail;
import it.laskaridis.blueground.commons.emails.ApplicationMailer;
import it.laskaridis.blueground.commons.emails.Email;
import it.laskaridis.blueground.commons.emails.EmailDispatchException;
import it.laskaridis.blueground.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReminderService {

    private final RemindersRepository reminders;
    private final ReminderDispatchBackoffStrategy dispatchBackoffStrategy;
    private final ApplicationMailer mailer;

    @Autowired
    public ReminderService(RemindersRepository reminders, ReminderDispatchBackoffStrategy dispatchBackoffStrategy, ApplicationMailer mailer) {
        this.reminders = reminders;
        this.dispatchBackoffStrategy = dispatchBackoffStrategy;
        this.mailer = mailer;
    }

    // FIXME: specify this in configuration
    @Scheduled(fixedDelay = 1000 * 30)
    public void sendBookingReminders() {
        LocalDateTime window = getCurrentReminderDispatchWindow();
        List<Reminder> reminders = this.reminders.findAllByScheduledAtBeforeAndSentAtIsNull(window);
        for (Reminder reminder : reminders)
            dispatch(reminder);
    }

    /**
     * Returns the upper bound of the current notification dispatch window. It is assumed
     * that messages are dispatched hourly. So if the time when this method is invoked is
     * 13:15, the value 14:00 will be returned.
     */
    private LocalDateTime getCurrentReminderDispatchWindow() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.of(LocalTime.now().plusHours(1).getHour(), 0, 0);
        return LocalDateTime.of(date, time);
    }

    @Async
    public void dispatch(Reminder reminder) {
        // make absolutely sure we don't send reminders already sent, or after the fact:
        if (reminder.isSent() || reminder.isExpired())
            return;
        if (!this.dispatchBackoffStrategy.isEligibleForSending(reminder))
            return;
        try {
            sendMailFor(reminder);
            reminder.setSentAt(LocalDateTime.now());
            this.reminders.save(reminder);
        } catch (EmailDispatchException e) {
            return;
        } finally {
            incrementDispatchAttemptsCount(reminder);
        }
    }

    private void sendMailFor(Reminder reminder) {
        User user = reminder.getBooking().getUser();
        Email email = new ReminderEmail(reminder);
        this.mailer.send(user.getEmail(), email);
    }

    private void incrementDispatchAttemptsCount(Reminder reminder) {
        int attempts = reminder.getDispatchAttemptsCount();
        reminder.setDispatchAttemptsCount(++attempts);
        this.reminders.save(reminder);
    }
}
