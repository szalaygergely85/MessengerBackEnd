package com.gege.ideas.messenger.image.service;

import com.gege.ideas.messenger.file.service.FileUploadService;
import com.gege.ideas.messenger.image.constants.ImageConstans;
import com.gege.ideas.messenger.image.entity.ImageEntry;
import com.gege.ideas.messenger.image.repository.ImageRepository;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

   private final Path rootLocation = Paths.get("images");

   private final ImageRepository imageRepository;
   private final FileUploadService fileUploadService;

   private final UserService userService;

   @Autowired
   public ImageService(
      ImageRepository imageRepository,
      FileUploadService fileUploadService,
      UserService userService
   ) {
      this.imageRepository = imageRepository;
      this.fileUploadService = fileUploadService;
      this.userService = userService;
   }

   public Resource addImage(MultipartFile file, ImageEntry imageEntry) {
      Long userId = imageEntry.getUserId();
      saveImage(file, imageEntry);

      if (ImageConstans.TAG_PROFILE.equals(imageEntry.getTags())) {
         User user = userService.getUserById(userId);
         user.setProfilePictureUuid(imageEntry.getUuid());
         userService.updateUser(user);
      }
      ImageEntry localImage = imageRepository.save(imageEntry);
      return getImageAsResourceByUUID(localImage.getUuid());
   }

   public Resource getImageAsResource(ImageEntry imageEntry) {
      String imageName = imageEntry.getFileName();
      try {
         String folder = getFolder(imageEntry);

         Path userFolderPath = this.rootLocation.resolve(folder).normalize();

         Path file = userFolderPath.resolve(imageName).normalize();
         Resource resource = new UrlResource(file.toUri());
         if (resource.exists() || resource.isReadable()) {
            return resource;
         } else {
            throw new RuntimeException("Could not read file: " + imageName);
         }
      } catch (MalformedURLException e) {
         throw new RuntimeException("Could not read file: " + imageName, e);
      }
   }

   public Resource getImageAsResourceByUserID(Long userId) {
      User user = userService.getUserById(userId);
      String uuid = user.getProfilePictureUuid();
      return getImageAsResourceByUUID(uuid);
   }

   public Resource getImageAsResourceByUUID(String uuid) {
      ImageEntry imageEntry = imageRepository.findByUuid(uuid);
      return getImageAsResource(imageEntry);
   }

   private void saveImage(MultipartFile file, ImageEntry imageEntry) {
      try {
         if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
         }

         String userFolder = getFolder(imageEntry);
         // Determine target folder based on image tag

         Path userFolderPath =
            this.rootLocation.resolve(userFolder).normalize();

         Path destinationFile = userFolderPath
            .resolve(Paths.get(imageEntry.getFileName()))
            .normalize()
            .toAbsolutePath();

         if (
            !destinationFile.getParent().equals(userFolderPath.toAbsolutePath())
         ) {
            throw new RuntimeException(
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

   private String getFolder(ImageEntry imageEntry) {
      if (ImageConstans.TAG_MESSAGE.equals(imageEntry.getTags())) {
         // Store in /images/messages/{conversationId}
         return Paths
            .get("messages", String.valueOf(imageEntry.getConversationId()))
            .toString();
      }
      if (ImageConstans.TAG_PROFILE.equals(imageEntry.getTags())) {
         // Store in /images/profile/{userId}
         return Paths
            .get("profile", String.valueOf(imageEntry.getUserId()))
            .toString();
      }

      return null;
   }
}
