package it.laskaridis.blueground.reminders.model;

/**
 * Strategy to determine if a {@link Reminder} can sent or not. Used to
 * apply some sort of a backoff strategy for notification reminder
 * dispatches.
 *
 * @see CountBasedDispatchBackoffStrategy
 * @see NoDispatchBackoffStrategy
 */
public interface ReminderDispatchBackoffStrategy {

    /**
     * Determine if the specified {@link Reminder} is eligible for dispatch.
     *
     * @param reminder the reminder to be sent
     * @return <code>true</code> if the reminder can be sent, <code>false</code> otherwise
     */
    boolean isEligibleForSending(Reminder reminder);

}
