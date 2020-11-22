package de.haw.eborrow.services;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
    public void init();

    public void save(MultipartFile file, String fileName);

    public Resource load(String filename);

    public void deleteAll();

    public Stream<Path> loadAll();
}