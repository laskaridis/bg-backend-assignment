package it.laskaridis.blueground.bookings.controller;

import it.laskaridis.blueground.errors.model.ResourceNotFoundException;

import static java.lang.String.format;

public class BookingNotFoundException extends ResourceNotFoundException {

    public BookingNotFoundException(String uuid) {
        super("Booking", format("booking `%s` does not exist", uuid));
    }
}
