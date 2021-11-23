package it.laskaridis.blueground.units.view;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.laskaridis.blueground.units.model.Unit;
import it.laskaridis.blueground.users.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;

import static it.laskaridis.blueground.units.controller.ResourceUriFactories.newResourceUriForUnitImage;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowUnitView implements Serializable {

    public static ShowUnitView fromModel(Unit model) {
        return ShowUnitView.builder()
                .id(model.getUuid())
                .title(model.getTitle())
                .region(model.getRegion())
                .description(model.getDescription())
                .priceAmount(model.getPrice().getAmount())
                .priceCurrency(model.getPrice().getCurrency().toString())
                .cancellationPolicy(model.getCancellationPolicy())
                .imageFile(newResourceUriForUnitImage(model).map(URI::toString).orElse(null))
                .createdAt(model.getCreatedAt())
                .lastModifiedAt(model.getLastModifiedAt())
                .createdBy(model.getCreatedBy().map(User::getEmail).orElse(null))
                .lastModifiedBy(model.getLastModifiedBy().map(User::getEmail).orElse(null))
                .build();
    }

    private String id;
    private String title;
    private String region;
    private String description;
    private BigDecimal priceAmount;
    private String priceCurrency;
    private String cancellationPolicy;
    private String imageFile;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String createdBy;
    private String lastModifiedBy;
}
