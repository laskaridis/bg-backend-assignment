package it.laskaridis.blueground.units.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.laskaridis.blueground.units.model.Unit;
import it.laskaridis.blueground.units.model.UnitSearchService;
import it.laskaridis.blueground.units.model.UnitsRepository;
import it.laskaridis.blueground.units.view.CreateUnitView;
import it.laskaridis.blueground.units.view.ShowUnitView;
import it.laskaridis.blueground.units.view.UpdateUnitView;
import it.laskaridis.blueground.units.model.Money;
import it.laskaridis.blueground.users.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.laskaridis.blueground.units.controller.ResourceUriFactories.newResourceUriForUnit;
import static java.util.stream.Collectors.toList;

@Tag(name = "Unit")
@RestController
@RequestMapping("/api/v1")
public class UnitsController {

    private final UnitsRepository units;
    private final UnitSearchService unitSearchService;

    @Autowired
    public UnitsController(UnitsRepository unitRepository, UnitSearchService unitSearchService) {
        this.units = unitRepository;
        this.unitSearchService = unitSearchService;
    }

    @GetMapping("/units")
    @RolesAllowed({ Role.ADMINISTRATOR, Role.COLONIST })
    public List<ShowUnitView> getUnits(@RequestParam Optional<String> query, Pageable pageable) {
        return this.unitSearchService.search(query, pageable)
                .stream()
                .map(ShowUnitView::fromModel)
                .collect(toList());
    }

    @GetMapping("/units/{unitId}")
    @RolesAllowed({ Role.ADMINISTRATOR, Role.COLONIST })
    public ResponseEntity<ShowUnitView> getUnit(@PathVariable String unitId) {
        Unit unit = this.units.findByUuid(unitId).orElseThrow(() -> new UnitNotFoundException(unitId));
        ShowUnitView view = ShowUnitView.fromModel(unit);
        String version = Long.toString(unit.getVersion());
        return ResponseEntity.status(HttpStatus.OK).eTag(version).body(view);
    }

    @PostMapping("/units")
    @RolesAllowed({ Role.ADMINISTRATOR })
    public ResponseEntity<ShowUnitView> createUnit(@RequestBody CreateUnitView form) {
        Unit unit = units.save(form.toModel());
        URI uri = newResourceUriForUnit(unit);
        ShowUnitView view = ShowUnitView.fromModel(unit);
        return ResponseEntity.created(uri).body(view);
    }

    @PutMapping("/units/{unitId}")
    @RolesAllowed({ Role.ADMINISTRATOR })
    public ResponseEntity<ShowUnitView> updateUnit(@PathVariable String unitId, @RequestBody UpdateUnitView form) {
        Unit unit = this.units.findByUuid(unitId).orElseThrow(() -> new UnitNotFoundException(unitId));
        unit.setPrice(Money.of(form.getPriceAmount(), form.getPriceCurrency()));
        unit.setDescription(form.getDescription());
        unit.setTitle(form.getTitle());
        unit.setCancellationPolicy(form.getCancellationPolicy());
        unit = this.units.save(unit);
        ShowUnitView view = ShowUnitView.fromModel(unit);
        return ResponseEntity.ok(view);
    }

    @PatchMapping("/units/{unitId}")
    @RolesAllowed({ Role.ADMINISTRATOR })
    public ResponseEntity<ShowUnitView> partiallyUpdateUnit(@PathVariable String unitId, @RequestBody ShowUnitView form) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @DeleteMapping("/units/{unitId}")
    @RolesAllowed({ Role.ADMINISTRATOR })
    public ResponseEntity<Void> deleteUnit(@PathVariable String unitId) {
        this.units.findByUuid(unitId).ifPresent(this.units::delete);
        return ResponseEntity.noContent().build();
    }
}
