package it.laskaridis.blueground.units.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.lang.String.format;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.util.Assert.notNull;

/**
 * A very simple storage service that uses the local filesystem to store
 * images. In a production-like environment a more reliable, resilient
 * and scalable implementation should be used, like the S3 service from
 * Amazon.
 */
@Service
public class FilesystemImageStorageService implements ImageStorageService {

    private final Path imageStorageRoot;
    private final UnitsRepository units;

    @Autowired
    public FilesystemImageStorageService(ImageStorageProperties properties, UnitsRepository units) {
        imageStorageRoot = Paths.get(properties.getLocation());
        this.units = units;
    }

    /**
     * Method runs within a transaction to rollback any updates on the model if
     * the new image fails to be stored or the old one to be deleted.
     */
    @Override @Transactional
    public Unit storeImageForUnit(Unit unit, MultipartFile file) {
        if (file.isEmpty())
            throw new ImageStorageException("file can't be empty");
        String existingImageFile = unit.getImageFile();
        unit.setImageFile(file.getOriginalFilename());
        unit = this.units.save(unit);
        saveImageFile(unit.getUuid(), file);
        if (!isEmpty(existingImageFile))
            deleteImageFile(unit.getUuid(), existingImageFile);
        return unit;
    }

    @Override
    public Optional<Resource> loadUnitImage(Unit unit) {
        if (unit.getImageFile() == null)
            return Optional.empty();
        Path path = getDestinationPath(unit.getUuid(), unit.getImageFile());
        if (!Files.exists(path))
            return Optional.empty();
        else {
            try {
                Resource resource = new UrlResource(path.toUri());
                return Optional.of(resource);
            } catch (MalformedURLException e) {
                throw new ImageStorageException(format("failed to load file `%s`", path.getFileName().toString()));
            }
        }
    }

    /**
     * Method runs within a transaction to rollback any updates on the model if
     * the image fails to be deleted.
     */
    @Override @Transactional
    public void deleteUnitImage(Unit unit) {
        if (unit.getImageFile() == null)
            return;
        String filename = unit.getImageFile();
        unit.setImageFile(null);
        this.units.save(unit);
        this.deleteImageFile(unit.getUuid(), filename);
    }

    private void deleteImageFile(String bucket, String filename) {
        notNull(bucket, "bucket can't be null");
        notNull(filename, "filename can't be null");

        Path path = getDestinationPath(bucket, filename);
        if (!Files.exists(path))
            return;
        else {
            try {
                Files.delete(path);
            } catch (IOException ignored) {
                // FIXME: logging
            }
        }
    }

    private void saveImageFile(String bucket, MultipartFile file) {
        notNull(bucket, "bucket can't be null");
        notNull(file, "file can't be null");

        try (InputStream in = file.getInputStream()) {
            Path destinationBucket = getDestinationPath(bucket);
            if (!Files.exists(destinationBucket))
                Files.createDirectories(destinationBucket);
            Path destinationFile = getDestinationPath(bucket, file.getOriginalFilename());
            Files.copy(in, destinationFile, REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ImageStorageException(format("failed to store file `%s`", file.getOriginalFilename()));
        }
    }

    private Path getDestinationPath(String bucket, String file) {
        return this.imageStorageRoot.resolve(Paths.get(bucket, file)).normalize().toAbsolutePath();
    }

    private Path getDestinationPath(String bucket) {
        return this.imageStorageRoot.resolve(Paths.get(bucket)).normalize().toAbsolutePath();
    }
}
