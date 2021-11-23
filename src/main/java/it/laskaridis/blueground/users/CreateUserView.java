package it.laskaridis.blueground.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserView implements Serializable {

    @JsonIgnore private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User toModel() {
        return new User.UserBuilder()
                .uuid(getUuid())
                .email(getEmail())
                .password(passwordEncoder.encode(getPassword()))
                .role(getRole())
                .createdAt(getCreatedAt())
                .build();
    }

    private String uuid;
    private String email;
    private String password;
    private String role;
    private LocalDateTime createdAt;
}
