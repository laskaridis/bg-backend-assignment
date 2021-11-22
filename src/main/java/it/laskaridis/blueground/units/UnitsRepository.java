package it.laskaridis.blueground.units;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UnitsRepository extends PagingAndSortingRepository<Unit, Long> {

    Optional<Unit> findByUuid(String uuid);
}
