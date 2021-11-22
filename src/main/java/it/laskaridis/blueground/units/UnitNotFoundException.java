package it.laskaridis.blueground.units;

import it.laskaridis.blueground.common.NotFoundException;

public class UnitNotFoundException extends NotFoundException {
    private final String uuid;
    public UnitNotFoundException(String uuid) {
        super("NotFoundError.Unit", String.format("Unit with uuid `%s` was not found", uuid));
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
