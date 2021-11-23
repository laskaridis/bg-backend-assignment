package it.laskaridis.blueground.units.model;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Stores images of units.
 */
@Service
public interface ImageStorageService {

    Unit storeImageForUnit(Unit unit, MultipartFile file);

    Optional<Resource> loadUnitImage(Unit unit);

    void deleteUnitImage(Unit unit);
}
