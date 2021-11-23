package it.laskaridis.blueground.reminders.model;

/**
 * Applies a backoff strategy based on a maximum number of dispatch attempts.
 */
public class CountBasedDispatchBackoffStrategy implements ReminderDispatchBackoffStrategy {

    private final int threshold;

    /**
     * @param threshold maximum number of allowed notification dispatch attempts
     */
    public CountBasedDispatchBackoffStrategy(int threshold) {
        this.threshold = threshold;
    }

    /**
     * @return the maximum number of allowed notification dispatch attempts for this instance.
     */
    public int getThreshold() {
        return this.threshold;
    }

    /**
     * Returns true if the {@link Reminder#getDispatchAttemptsCount()} of the
     * specified {@link Reminder} is less than or equal to the configured threshold
     * of dispatch attempts.
     *
     * @param reminder the reminder to be sent
     * @return <code>true</code> if the reminder can be sent, <code>false</code> otherwise
     */
    @Override
    public boolean isEligibleForSending(Reminder reminder) {
        return reminder.getDispatchAttemptsCount() <= this.threshold;
    }
}
