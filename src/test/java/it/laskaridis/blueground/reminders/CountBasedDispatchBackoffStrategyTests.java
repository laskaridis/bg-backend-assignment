package it.laskaridis.blueground.reminders;

import it.laskaridis.blueground.reminders.model.CountBasedDispatchBackoffStrategy;
import it.laskaridis.blueground.reminders.model.Reminder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CountBasedDispatchBackoffStrategyTests {

    private CountBasedDispatchBackoffStrategy subject;

    @Test
    public void isEligibleForSending() {
        this.subject = new CountBasedDispatchBackoffStrategy(3);
        Reminder reminder = new Reminder();

        reminder.setDispatchAttemptsCount(this.subject.getThreshold() - 1);
        assertTrue(this.subject.isEligibleForSending(reminder));

        reminder.setDispatchAttemptsCount(this.subject.getThreshold());
        assertTrue(this.subject.isEligibleForSending(reminder));

        reminder.setDispatchAttemptsCount(this.subject.getThreshold() + 1);
        assertFalse(this.subject.isEligibleForSending(reminder));
    }
}
