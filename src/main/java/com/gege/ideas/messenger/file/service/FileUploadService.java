package com.gege.ideas.messenger.file.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

   private final Path rootLocation = Paths.get("images");

   // Allowed file extensions for security
   private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
      "jpg", "jpeg", "png", "gif", "bmp", "webp",  // Images
      "pdf", "doc", "docx", "txt", "xls", "xlsx",  // Documents
      "mp4", "avi", "mov", "mkv", "webm",          // Videos
      "mp3", "wav", "ogg", "m4a"                   // Audio
   );

   @Value("${spring.servlet.multipart.max-file-size:50MB}")
   private String maxFileSize;

   public FileUploadService() {
      try {
         Files.createDirectories(rootLocation);
      } catch (IOException e) {
         throw new RuntimeException("Could not initialize storage", e);
      }
   }

   /**
    * Validates filename to prevent path traversal attacks
    */
   private void validateFilename(String filename) {
      if (filename == null || filename.trim().isEmpty()) {
         throw new IllegalArgumentException("Filename cannot be empty");
      }

      // Check for path traversal attempts
      if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
         throw new SecurityException("Invalid filename: path traversal attempt detected");
      }

      // Check for null bytes
      if (filename.contains("\0")) {
         throw new SecurityException("Invalid filename: null byte detected");
      }

      // Validate file extension
      String extension = getFileExtension(filename).toLowerCase();
      if (!ALLOWED_EXTENSIONS.contains(extension)) {
         throw new IllegalArgumentException(
            "File type not allowed. Allowed types: " + String.join(", ", ALLOWED_EXTENSIONS)
         );
      }
   }

   /**
    * Gets file extension from filename
    */
   private String getFileExtension(String filename) {
      int lastDot = filename.lastIndexOf('.');
      if (lastDot == -1 || lastDot == filename.length() - 1) {
         return "";
      }
      return filename.substring(lastDot + 1);
   }

   public void saveFile(MultipartFile file, String userFolder) {
      try {
         if (file.isEmpty()) {
            throw new IllegalArgumentException("Failed to store empty file.");
         }

         // Validate file size (Spring already enforces max-file-size, but double check)
         if (file.getSize() > 50 * 1024 * 1024) { // 50MB
            throw new IllegalArgumentException("File size exceeds maximum allowed size of 50MB");
         }

         // Validate filename
         String originalFilename = file.getOriginalFilename();
         validateFilename(originalFilename);

         // Validate content type
         String contentType = file.getContentType();
         if (contentType == null || (!contentType.startsWith("image/") &&
             !contentType.startsWith("video/") &&
             !contentType.startsWith("audio/") &&
             !contentType.equals("application/pdf") &&
             !contentType.startsWith("application/vnd.") &&
             !contentType.equals("text/plain"))) {
            throw new IllegalArgumentException("Invalid content type: " + contentType);
         }

         Path userFolderPath =
            this.rootLocation.resolve(userFolder).normalize();
         Path destinationFile = userFolderPath
            .resolve(Paths.get(originalFilename))
            .normalize()
            .toAbsolutePath();

         if (
            !destinationFile.getParent().equals(userFolderPath.toAbsolutePath())
         ) {
            throw new SecurityException(
               "Cannot store file outside current directory."
            );
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

   public Resource loadAsResource(String filename, String userFolder) {
      try {
         // Validate filename to prevent path traversal
         validateFilename(filename);

         Path userFolderPath = rootLocation.resolve(userFolder).normalize();
         Path file = userFolderPath.resolve(filename).normalize().toAbsolutePath();

         // Ensure the resolved path is still within the allowed directory
         if (!file.startsWith(userFolderPath.toAbsolutePath())) {
            throw new SecurityException("Cannot access file outside user directory");
         }

         Resource resource = new UrlResource(file.toUri());
         if (resource.exists() && resource.isReadable()) {
            return resource;
         } else {
            return null;
         }
      } catch (MalformedURLException e) {
         throw new RuntimeException("Could not read file: " + filename, e);
      }
   }
}
