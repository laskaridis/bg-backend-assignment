package it.laskaridis.blueground.units.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.laskaridis.blueground.units.model.ImageStorageService;
import it.laskaridis.blueground.units.model.Unit;
import it.laskaridis.blueground.units.model.UnitsRepository;
import it.laskaridis.blueground.users.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Tag(name = "Unit")
@RestController
@RequestMapping("/api/v1")
@RolesAllowed({Role.ADMINISTRATOR })
public class UnitImagesController {

    private final UnitsRepository units;
    private final ImageStorageService imageStorageService;

    @Autowired
    public UnitImagesController(UnitsRepository unitRepository, ImageStorageService imageStorageService) {
        this.units = unitRepository;
        this.imageStorageService = imageStorageService;
    }

    @PostMapping("/units/{unitId}/images")
    public ResponseEntity updateUnitImage(@PathVariable String unitId, @RequestParam MultipartFile file) {
        Unit unit = this.units.findByUuid(unitId).orElseThrow(() -> new UnitNotFoundException(unitId));
        this.imageStorageService.storeImageForUnit(unit, file);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/units/{unitId}/images/{filename:.+}")
    public ResponseEntity loadImage(@PathVariable String unitId, @PathVariable String filename) {
        Unit unit = this.units.findByUuid(unitId)
                .orElseThrow(() -> new UnitNotFoundException(unitId));
        Resource image = this.imageStorageService.loadUnitImage(unit)
                .orElseThrow(() -> new UnitImageNotFoundException(unitId));
        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, getContentDispositionHeaderFor(filename))
                .body(image);
    }

    private String getContentDispositionHeaderFor(String filename) {
        return format("attachment; filename=\"%s\"", filename);
    }

    @DeleteMapping("/units/{unitId}/images/{filename:.+}")
    public ResponseEntity<Void> deleteImage(@PathVariable String unitId, @PathVariable String filename) {
        Unit unit = this.units.findByUuid(unitId).orElseThrow(() -> new UnitNotFoundException(unitId));
        if (unit.getImageFile() == null)
            throw new UnitImageNotFoundException(unitId);
        this.imageStorageService.deleteUnitImage(unit);
        return ResponseEntity.noContent().build();
    }
}
