package de.haw.eborrow.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    public void init();

    public void save(MultipartFile file, String fileName,String directory);

    public Resource load(String directory,String filename);

    public void deleteAll();

    public Stream<Path> loadAll();
}