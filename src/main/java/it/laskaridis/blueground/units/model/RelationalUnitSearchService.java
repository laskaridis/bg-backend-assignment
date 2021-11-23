package it.laskaridis.blueground.units.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * A rudimentary unit search service that is implemented using SQL. In production
 * systems a more robust implementation that would index entities in a search
 * engine (such as elasticsearch) would likely be used. In that respect, each
 * entity would typically be indexed asynchronously (for example, posted to a
 * queue or event stream which is monitored by an indexer service) and looked up
 * against the search engine.
 */
@Service
public class RelationalUnitSearchService implements UnitSearchService {

    private final UnitsRepository units;

    @Autowired
    public RelationalUnitSearchService(UnitsRepository units) {
        this.units = units;
    }

    @Override
    public Page<Unit> search(Pageable pageable) {
        return this.search(Optional.empty(), pageable);
    }

    @Override
    public Page<Unit> search(Optional<String> query, Pageable pageable) {
        if (query.isPresent()) {
            return this.units.findAllByTitleContainingOrRegionContainingIgnoreCase(
                    query.get(), query.get(), pageable);
        } else
            return this.units.findAll(pageable);
    }
}
