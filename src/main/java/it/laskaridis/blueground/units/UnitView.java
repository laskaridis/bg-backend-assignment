package it.laskaridis.blueground.units;

import it.laskaridis.blueground.common.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitView {

    public static UnitView fromModel(Unit model) {
        return new UnitViewBuilder()
                .uuid(model.getUuid())
                .title(model.getTitle())
                .region(model.getRegion())
                .description(model.getDescription())
                .priceAmount(model.getPrice().getAmount())
                .priceCurrency(model.getPrice().getCurrency().toString())
                .cancellationPolicy(model.getCancellationPolicy())
                .createdAt(model.getCreatedAt())
                .build();
    }

    public Unit toModel() {
        return new Unit.UnitBuilder()
                .uuid(getUuid())
                .title(getTitle())
                .region(getRegion())
                .description(getDescription())
                .price(Money.of(getPriceAmount(), getPriceCurrency()))
                .cancellationPolicy(getCancellationPolicy())
                .createdAt(getCreatedAt())
                .build();
    }

    private String uuid;
    private String title;
    private String region;
    private String description;
    private BigDecimal priceAmount;
    private String priceCurrency;
    private String cancellationPolicy;
    private LocalDateTime createdAt;
}
