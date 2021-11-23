package it.laskaridis.blueground.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowUserView implements Serializable {

    public static ShowUserView fromModel(User model) {
        return new ShowUserViewBuilder()
                .uuid(model.getUuid())
                .email(model.getEmail())
                .role(model.getRole())
                .createdAt(model.getCreatedAt())
                .build();
    }

    private String uuid;
    private String email;
    private String role;
    private LocalDateTime createdAt;
}
