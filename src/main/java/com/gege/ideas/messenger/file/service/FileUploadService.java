package com.gege.ideas.messenger.file.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

   private final Path rootLocation = Paths.get("images");

   public FileUploadService() {
      try {
         Files.createDirectories(rootLocation);
      } catch (IOException e) {
         throw new RuntimeException("Could not initialize storage", e);
      }
   }

   public void saveFile(MultipartFile file, String userFolder) {
      try {
         if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
         }


         Path userFolderPath = this.rootLocation.resolve(userFolder).normalize();
         Path destinationFile = userFolderPath.resolve(Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();


         if (!destinationFile.getParent().equals(userFolderPath.toAbsolutePath())) {
            throw new RuntimeException("Cannot store file outside current directory.");
         }

         Files.createDirectories(userFolderPath);

         try (var inputStream = file.getInputStream()) {
            Files.copy(
                    inputStream,
                    destinationFile,
                    StandardCopyOption.REPLACE_EXISTING
            );
         }
      } catch (IOException e) {
         throw new RuntimeException("Failed to store file.", e);
      }
   }
   public Resource loadAsResource(String filename) {
      try {
         Path file = rootLocation.resolve(filename);
         Resource resource = new UrlResource(file.toUri());
         if (resource.exists() || resource.isReadable()) {
            return resource;
         } else {
            throw new RuntimeException("Could not read file: " + filename);
         }
      } catch (MalformedURLException e) {
         throw new RuntimeException("Could not read file: " + filename, e);
      }
   }
}
