package it.laskaridis.blueground.units;

import it.laskaridis.blueground.units.view.CreateUnitView;

import java.math.BigDecimal;

class Fixtures {

    private Fixtures() { }

    static CreateUnitView newValidCreateUnitForm() {
        CreateUnitView form = CreateUnitView.builder()
                .title("test title")
                .description("test description")
                .cancellationPolicy("none")
                .region("Europe")
                .priceAmount(BigDecimal.ONE)
                .priceCurrency("EUR")
                .build();
        return form;
    }

    static CreateUnitView newCreateUnitFormWithNoTitle() {
        CreateUnitView form = CreateUnitView.builder()
                .description("test description")
                .cancellationPolicy("none")
                .region("Europe")
                .priceAmount(BigDecimal.ONE)
                .priceCurrency("EUR")
                .build();
        return form;
    }

    static CreateUnitView newCreateUnitFormWithNoCancellationPolicy() {
        CreateUnitView form = CreateUnitView.builder()
                .title("test title")
                .description("test description")
                .region("Europe")
                .priceAmount(BigDecimal.ONE)
                .priceCurrency("EUR")
                .build();
        return form;
    }

    static CreateUnitView newCreateUnitFormWithNoRegion() {
        CreateUnitView form = CreateUnitView.builder()
                .title("test title")
                .description("test description")
                .cancellationPolicy("none")
                .priceAmount(BigDecimal.ONE)
                .priceCurrency("EUR")
                .build();
        return form;
    }

    static CreateUnitView newCreateUnitFormWithNoPriceAmount() {
        CreateUnitView form = CreateUnitView.builder()
                .title("test title")
                .description("test description")
                .cancellationPolicy("none")
                .region("Europe")
                .priceCurrency("EUR")
                .build();
        return form;
    }

    static CreateUnitView newCreateUnitFormWithNoPriceCurrency() {
        CreateUnitView form = CreateUnitView.builder()
                .title("test title")
                .description("test description")
                .cancellationPolicy("none")
                .region("Europe")
                .priceAmount(BigDecimal.ONE)
                .build();
        return form;
    }
}
