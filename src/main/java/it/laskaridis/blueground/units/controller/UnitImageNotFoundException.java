package it.laskaridis.blueground.units.controller;

import it.laskaridis.blueground.errors.model.ResourceNotFoundException;

import static java.lang.String.format;

public class UnitImageNotFoundException extends ResourceNotFoundException {
    public UnitImageNotFoundException(String unitId) {
        super("UnitImage", format("no image found for unit `%s`", unitId));
    }
}
