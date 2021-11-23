package it.laskaridis.blueground.units;

import it.laskaridis.blueground.common.Money;
import it.laskaridis.blueground.users.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class UnitsController {

    private final UnitsRepository unitRepository;

    @Autowired
    public UnitsController(UnitsRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    /**
     * GET /units
     */
    @GetMapping("/units")
    @RolesAllowed({ Role.ADMINISTRATOR, Role.COLONIST })
    public List<UnitView> getUnits(Pageable pageable) {
        return this.unitRepository.findAll(pageable)
                .stream()
                .map(UnitView::fromModel)
                .collect(toList());
    }

    /**
     * GET /units/{uuid}
     */
    @GetMapping("/units/{uuid}")
    @RolesAllowed({ Role.ADMINISTRATOR, Role.COLONIST })
    public UnitView getUnit(@PathVariable String uuid) {
        Unit entity = this.unitRepository.findByUuid(uuid).orElseThrow(() -> new UnitNotFoundException(uuid));
        return UnitView.fromModel(entity);
    }

    /**
     * POST /units
     */
    @PostMapping("/units")
    @RolesAllowed({ Role.ADMINISTRATOR })
    public ResponseEntity<Void> createUnit(@RequestBody UnitView form) {
        Unit unit = unitRepository.save(form.toModel());
        URI uri = createResourceUriFor(unit);
        return ResponseEntity.created(uri).build();
    }

    private URI createResourceUriFor(Unit unit) {
        return UriComponentsBuilder.newInstance()
                .pathSegment("units", "{uuid}")
                .buildAndExpand(unit.getUuid())
                .toUri();
    }

    /**
     * PUT /units/{uuid}
     */
    @PutMapping("/units/{uuid}")
    @RolesAllowed({ Role.ADMINISTRATOR })
    public ResponseEntity<UnitView> updateUnit(@PathVariable String uuid, @RequestBody UnitView form) {
        Unit existingUnit = this.unitRepository.findByUuid(uuid).orElseThrow(() -> new UnitNotFoundException(uuid));
        existingUnit.setPrice(Money.of(form.getPriceAmount(), form.getPriceCurrency()));
        existingUnit.setDescription(form.getDescription());
        existingUnit.setTitle(form.getTitle());
        existingUnit.setCancellationPolicy(form.getCancellationPolicy());
        Unit updatedUnit = this.unitRepository.save(existingUnit);
        return ResponseEntity.ok(UnitView.fromModel(updatedUnit));
    }

    /**
     * PATCH /units/{uuid}
     */
    @PatchMapping("/units/{uuid}")
    @RolesAllowed({ Role.ADMINISTRATOR })
    public ResponseEntity<UnitView> partiallyUpdateUnit(@PathVariable String uuid, @RequestBody UnitView form) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * DELETE /units/{uuid}
     */
    @DeleteMapping("/units/{uuid}")
    @RolesAllowed({ Role.ADMINISTRATOR })
    public ResponseEntity<Void> deleteUnit(@PathVariable String uuid) {
        this.unitRepository.findByUuid(uuid).ifPresent(this.unitRepository::delete);
        return ResponseEntity.ok().build();
    }
}
