package it.laskaridis.blueground.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserView {

    public static UserView fromModel(User model) {
        return new UserViewBuilder()
                .email(model.getEmail())
                .build();
    }

    public User toModel() {
        return new User.UserBuilder()
                .email(getEmail())
                .password(getPassword())
                .uuid(getUuid())
                .createdAt(getCreatedAt())
                .build();
    }

    private String email;
    private String password;
    private String uuid;
    private LocalDateTime createdAt;
}
