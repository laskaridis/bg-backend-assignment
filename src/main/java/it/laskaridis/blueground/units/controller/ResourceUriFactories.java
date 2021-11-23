package it.laskaridis.blueground.units.controller;

import it.laskaridis.blueground.units.model.Unit;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

public final class ResourceUriFactories {

    private ResourceUriFactories() { }

    /**
     * @return a URI of the form "/api/v1/units/{uuid}
     */
    public static URI newResourceUriForUnit(Unit unit) {
        Assert.notNull(unit, "unit can't be null");
        return UriComponentsBuilder.newInstance()
                .pathSegment("api", "v1", "units", "{uuid}")
                .buildAndExpand(unit.getUuid())
                .toUri();
    }

    /**
     * @return a uri of the form "/api/v1/units/{uuid}/images/{file}" or {@link Optional#empty()} if not applicable
     */
    public static Optional<URI> newResourceUriForUnitImage(Unit unit) {
        Assert.notNull(unit, "unit can't be null");
        if (unit.getImageFile() == null)
            return Optional.empty();
        else {
            URI uri = UriComponentsBuilder.newInstance()
                    .pathSegment("api", "v1", "units", "{uuid}", "images", "{file}")
                    .buildAndExpand(unit.getUuid(), unit.getImageFile())
                    .toUri();
            return Optional.ofNullable(uri);
        }
    }
}
