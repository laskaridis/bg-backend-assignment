package it.laskaridis.blueground.reminders.view;

import it.laskaridis.blueground.commons.emails.Email;
import it.laskaridis.blueground.reminders.model.Reminder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode
public class ReminderEmail extends Email {

    private final Reminder reminder;

    public ReminderEmail(Reminder reminder) {
        this.reminder = reminder;
    }

    @Override
    public String getRecipient() {
        return this.reminder.getBooking().getUser().getEmail();
    }

    @Override
    public String getSubject() {
        // TODO: localize this base on the user locale
        return "Get ready for your Mars travel!";
    }

    @Override
    public String getBody() {
        // TODO: localize this base on the user locale
        return "You are 30 days away from your scheduled trip to mars. Start packing!";
    }

    public Reminder getReminder() {
        return reminder;
    }
}
