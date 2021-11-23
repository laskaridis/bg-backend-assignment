package it.laskaridis.blueground.reminders.model;

/**
 * Does not apply any kind of backoff strategy.
 */
public class NoDispatchBackoffStrategy implements ReminderDispatchBackoffStrategy {

    /**
     * Allows any number of dispatches (i.e. no backoff is applied).
     *
     * @param reminder the reminder to be sent
     * @return <code>true</code> if the reminder can be sent, <code>false</code> otherwise
     */
    @Override
    public boolean isEligibleForSending(Reminder reminder) {
        return true;
    }
}
