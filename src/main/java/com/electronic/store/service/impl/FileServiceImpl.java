package com.electronic.store.service.impl;

import com.electronic.store.Exception.BadRequestApiException;
import com.electronic.store.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new BadRequestApiException("File name is invalid");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        if (extension.equalsIgnoreCase(".png") ||
                extension.equalsIgnoreCase(".jpg") ||
                extension.equalsIgnoreCase(".jpeg")) {

            String randomName = UUID.randomUUID().toString();
            String newFileName = randomName + extension;

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fullPath = path + File.separator + newFileName;
            Files.copy(file.getInputStream(), Paths.get(fullPath), StandardCopyOption.REPLACE_EXISTING);

            return newFileName;

        } else {
            throw new BadRequestApiException("Invalid file format");
        }
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath = path + File.separator + fileName;
        InputStream inputStream=new FileInputStream(fullPath);
        return inputStream;
    }
}
