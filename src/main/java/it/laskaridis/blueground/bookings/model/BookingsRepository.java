package it.laskaridis.blueground.bookings.model;

import it.laskaridis.blueground.units.model.Unit;
import it.laskaridis.blueground.users.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface BookingsRepository extends PagingAndSortingRepository<Booking, Long> {

    List<Booking> findAllByUser(User user);

    List<Booking> findAllByUnit(Unit unit);

    Optional<Booking> findByUuid(String uuid);
}
