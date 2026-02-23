package com.personalized_learning_hub.service.system;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RequiredArgsConstructor
@Service
public class FileUtil {


    public static String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadDirectory = Paths.get("Files-Upload");

        String fileCode = RandomStringUtils.randomAlphanumeric(8);

        if (!Files.exists(uploadDirectory)) {
            Files.createDirectories(uploadDirectory);  // ✅ eksikse oluştur
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadDirectory.resolve(fileCode + "-" + fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Error saving uploaded file: " + fileName, ioe);
        }
        return fileCode;
    }

    private Path foundFile;
    public Resource getFileAsResource(String fileCode) throws IOException {
        Path uploadDirectory = Paths.get("Files-Upload");

        Files.list(uploadDirectory).forEach(file -> {
            if (file.getFileName().toString().startsWith(fileCode)) {
                foundFile = file;
                return;
            }
        });
        if (foundFile != null){
            return new UrlResource(foundFile.toUri());
        }
        return null;

    }
}