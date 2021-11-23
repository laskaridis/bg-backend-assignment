package it.laskaridis.blueground.users.view;

import it.laskaridis.blueground.users.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class ShowUserView implements Serializable {

    public static ShowUserView fromModel(User model) {
        return new ShowUserViewBuilder()
                .id(model.getUuid())
                .email(model.getEmail())
                .role(model.getRole())
                .timezone(model.getTimezone())
                .createdAt(model.getCreatedAt())
                .lastUpdatedAt(model.getLastModifiedAt())
                .createdBy(model.getCreatedBy().map(User::getEmail).orElse(null))
                .lastUpdatedBy(model.getLastModifiedBy().map(User::getEmail).orElse(null))
                .build();
    }

    private String id;
    private String email;
    private String role;
    private String timezone;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private String createdBy;
    private String lastUpdatedBy;
}
