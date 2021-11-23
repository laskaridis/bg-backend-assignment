package it.laskaridis.blueground.bookings.controller;

import it.laskaridis.blueground.bookings.model.Booking;
import it.laskaridis.blueground.units.model.Unit;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public final class ResourceUriFactories {

    private ResourceUriFactories() { }

    /**
     * @returns a URI of the form "/api/v1/bookings/{uuid}
     */
    public static URI createResourceUriForBooking(Booking booking) {
        return UriComponentsBuilder.newInstance()
                .pathSegment("api", "v1", "bookings", "{uuid}")
                .buildAndExpand(booking.getUuid())
                .toUri();
    }

    /**
     * @returns a URI of the form "/api/v1/units/{uuid}/bookings
     */
    public static URI createResourceUriForUnitBookings(Unit unit) {
        return UriComponentsBuilder.newInstance()
                .pathSegment("api", "v1", "units", "{uuid}", "bookings")
                .buildAndExpand(unit.getUuid())
                .toUri();
    }

}
