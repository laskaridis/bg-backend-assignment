package it.laskaridis.blueground.users.view;

import it.laskaridis.blueground.users.model.User;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.Optional;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class CreateUserView implements Serializable {

    private String email;
    private String password;
    private String role;
    private String timezone;

    public User toModel() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return User.builder()
                .email(getEmail())
                .password(Optional.ofNullable(getPassword()).map(encoder::encode).orElse(null))
                .role(getRole())
                .timezone(getTimezone())
                .build();
    }
}
