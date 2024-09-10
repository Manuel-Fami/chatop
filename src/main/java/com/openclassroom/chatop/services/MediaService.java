package com.openclassroom.chatop.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MediaService {
	
		@Value("${image.upload-dir}")
		private String uploadDir;

		@SuppressWarnings("null")
        public String storePicture(MultipartFile picture) throws IOException {
		
            String filename = StringUtils.cleanPath(picture.getOriginalFilename());
            String uniqueFilename = generateUniqueFilename(filename);
            
            Path uploadPath = Paths.get(uploadDir); 
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        
            Path targetLocation = uploadPath.resolve(uniqueFilename);
            Files.copy(picture.getInputStream(), targetLocation);

            return targetLocation.toString();		
		}
		
    private String generateUniqueFilename(String filename) {
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timestamp = now.format(formatter);
        
        String extension = filename.substring(filename.lastIndexOf('.'));
        String nameWithoutExtension = filename.substring(0, filename.lastIndexOf('.'));

        return nameWithoutExtension + "_" + timestamp + extension;
    }
}
