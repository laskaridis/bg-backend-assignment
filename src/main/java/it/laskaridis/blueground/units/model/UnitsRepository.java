package it.laskaridis.blueground.units.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UnitsRepository extends PagingAndSortingRepository<Unit, Long> {

    Optional<Unit> findByUuid(String uuid);

     Page<Unit> findAllByTitleContainingOrRegionContainingIgnoreCase(String title, String region, Pageable pageable);
}
