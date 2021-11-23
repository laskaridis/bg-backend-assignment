package it.laskaridis.blueground.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
        User admin = new User.UserBuilder()
                .email("admin@localhost")
                .password(passwordEncoder.encode("changeme"))
                .role(Role.ADMINISTRATOR)
                .build();
        users.save(admin);
        User colonist = new User.UserBuilder()
                .email("colonist@localhost")
                .password(passwordEncoder.encode("changeme"))
                .role(Role.COLONIST)
                .build();
        users.save(colonist);
    }
}
