package it.laskaridis.blueground.users;

import it.laskaridis.blueground.users.model.Role;
import it.laskaridis.blueground.users.model.User;
import it.laskaridis.blueground.users.model.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

/**
 * Loads the database with fixture {@link User} data, on application
 * startup.
 */
@Component
public class UserFixturesLoader implements ApplicationRunner {

    private final UsersRepository users;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserFixturesLoader(UsersRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // an admin user, for testing purposes
        User admin = User.builder()
                .email("admin@localhost")
                .password(passwordEncoder.encode("changeme"))
                .role(Role.ADMINISTRATOR)
                .timezone("America/Los_Angeles")
                .build();
        users.save(admin);

        // a colonist user, for testing purposes
        User colonist = User.builder()
                .email("colonist@localhost")
                .password(passwordEncoder.encode("changeme"))
                .role(Role.COLONIST)
                .build();
        users.save(colonist);
    }
}
