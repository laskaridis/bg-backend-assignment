package it.laskaridis.blueground.commons.emails;

import org.springframework.stereotype.Component;

/**
 * Email management service that uses https://www.mailinator.com/
 * to dispatch application emails.
 */
@Component
public class MailinatorMailer implements ApplicationMailer {

    @Override
    public void send(String email, Email message) {
        // TODO: sorry, but i had no time to implement this :-(
    }
}
