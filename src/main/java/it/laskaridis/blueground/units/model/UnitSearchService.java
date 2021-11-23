package it.laskaridis.blueground.units.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UnitSearchService {

    Page<Unit> search(Pageable pageable);

    Page<Unit> search(Optional<String> query, Pageable pageable);

}
