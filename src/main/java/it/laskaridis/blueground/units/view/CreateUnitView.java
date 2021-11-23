package it.laskaridis.blueground.units.view;

import it.laskaridis.blueground.units.model.Unit;
import it.laskaridis.blueground.units.model.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class CreateUnitView {

    public Unit toModel() {
        return Unit.builder()
                .title(getTitle())
                .region(getRegion())
                .description(getDescription())
                .price(Money.of(getPriceAmount(), getPriceCurrency()))
                .cancellationPolicy(getCancellationPolicy())
                .build();
    }

    private String title;
    private String region;
    private String description;
    private BigDecimal priceAmount;
    private String priceCurrency;
    private String cancellationPolicy;
}
