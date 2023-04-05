package com.javatechie.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    String saveFileWithSpecificFileNameV(String fileName, MultipartFile file, String folderName);
    List<String>saveMultipleFileWithSpecificFileNameV(String module, MultipartFile[] files,String folderName);
}
