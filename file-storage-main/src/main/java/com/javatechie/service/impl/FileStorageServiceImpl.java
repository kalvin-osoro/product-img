package com.javatechie.service.impl;

import com.javatechie.service.FileStorageService;
import com.javatechie.util.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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


}
