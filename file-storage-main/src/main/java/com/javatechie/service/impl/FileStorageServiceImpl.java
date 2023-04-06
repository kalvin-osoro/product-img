package com.javatechie.service.impl;

import com.javatechie.service.FileStorageService;
import com.javatechie.util.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    public static String uploadDirectory = "upload";

    private final Path root = Paths.get(uploadDirectory);


    @Override
    public String saveFileWithSpecificFileNameV(String fileName, MultipartFile file, String folderName) {
        try {

            Path subDirectory = Paths.get(uploadDirectory + "/" + folderName);
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }
            if (!Files.exists(subDirectory)) {
                Files.createDirectories(subDirectory);
            }
            fileName = new File(subDirectory + "/" + fileName).getName();
            Path targetLocation = subDirectory.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("Path is " + fileName);
            return fileName;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public List<String> saveMultipleFileWithSpecificFileNameV(String module, MultipartFile[] files, String folderName) {
        List<String> listFilePath = new ArrayList<>();
        try {
            Arrays.stream(files).forEach(file -> {
                String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
                String fileName = module.concat(ImageUtils.generateUniqueNoByDate()).
                        concat(".").concat(fileExtension);
                byte[] bytes = new byte[0];
                Path path = null;
                try {
                    bytes = file.getBytes();
                    Path subDirectory = Paths.get(uploadDirectory + "/" + folderName);
                    if (!Files.exists(subDirectory)) {
                        Files.createDirectories(subDirectory);
                    }
                    path = Files.write(Paths.get(subDirectory + "/" + fileName), bytes);
                } catch (IOException e) {
                    log.info("Error is ");
                }
                String filePath = path.toString();
                listFilePath.add(filePath);
//                log.info("Path is " + filePath);

            });
            return listFilePath;
        } catch (Exception e) {
            log.info("Could not store the file. Error in saveFileWithSpecificFileName: "
                    + e.getMessage());
            return listFilePath;

        }
    }


    @Override
    public Resource loadFileAsResourceByName(String fileName) {
        try {
            Path filePath = Paths.get(uploadDirectory + "/" + ImageUtils.getSubFolder() + "/").resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }

    @Override
    public List<Resource> loadAllFilesAsResources() {
        List<Resource> resources = new ArrayList<>();
        try {
            Path directory = Paths.get(uploadDirectory + "/" + ImageUtils.getSubFolder() +"/");
            Files.walk(directory)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Resource resource = new UrlResource(path.toUri());
                            if (resource.exists()) {
                                resources.add(resource);
                            } else {
                                throw new RuntimeException("File not found " + path);
                            }
                        } catch (MalformedURLException ex) {
                            throw new RuntimeException("File not found " + path, ex);
                        }
                    });
        } catch (IOException ex) {
            throw new RuntimeException("Error reading files from directory " + uploadDirectory, ex);
        }
        return resources;
    }



}
