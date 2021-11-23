package it.laskaridis.blueground.units.controller;

import it.laskaridis.blueground.errors.model.ResourceNotFoundException;

import static java.lang.String.format;

public class UnitNotFoundException extends ResourceNotFoundException {

    public UnitNotFoundException(String uuid) {
        super("Unit", format("unit `%s` does not exist", uuid));
    }
}
