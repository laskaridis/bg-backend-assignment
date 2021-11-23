package it.laskaridis.blueground.commons.emails;

/**
 * Interface for services managing application emails.
 */
public interface ApplicationMailer {

    /**
     * Send an <code>email</code> to a recipient's <code>recipientEmail</code> address.
     *
     * @param recipientEmail the recipient's email
     * @param message the email message
     * @throws EmailDispatchException if anything goes wrong
     */
    void send(String recipientEmail, Email message);
}
