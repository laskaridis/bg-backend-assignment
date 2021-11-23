package it.laskaridis.blueground.units;

import it.laskaridis.blueground.common.Money;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "units")
public class Unit {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String uuid;
    @NotBlank
    private String title;
    @NotBlank
    private String region;
    private String description;
    @NotBlank
    private String cancellationPolicy;
    @Embedded @Valid
    private Money price;
    private LocalDateTime createdAt;

    @PrePersist
    private void beforeCreate() {
        this.uuid = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return uuid.equals(unit.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
