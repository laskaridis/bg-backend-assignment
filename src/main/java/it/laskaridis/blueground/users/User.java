package it.laskaridis.blueground.users;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class User {

    @Id @GeneratedValue private Long id;
    @EqualsAndHashCode.Include private String uuid;
    @NotBlank private String email;
    @NotBlank private String password;
    @CreatedDate private LocalDateTime createdAt;

    @PrePersist
    private void beforeCreate() {
        this.uuid = UUID.randomUUID().toString();
    }
}
