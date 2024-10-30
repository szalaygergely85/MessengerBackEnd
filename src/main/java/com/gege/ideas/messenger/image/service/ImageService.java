package com.gege.ideas.messenger.image.service;

import com.gege.ideas.messenger.file.service.FileUploadService;
import com.gege.ideas.messenger.image.constans.ImageConstans;
import com.gege.ideas.messenger.image.entity.ImageEntry;
import com.gege.ideas.messenger.image.repository.ImageRepository;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {
   private final Path rootLocation = Paths.get("images");

   private final ImageRepository imageRepository;
   private final FileUploadService fileUploadService;

   private final UserService userService;

   @Autowired
   public ImageService(ImageRepository imageRepository, FileUploadService fileUploadService, UserService userService) {
      this.imageRepository = imageRepository;
      this.fileUploadService = fileUploadService;
      this.userService = userService;
   }


   public ImageEntry addImage(MultipartFile file, ImageEntry imageEntry) {
      Long userId = imageEntry.getUserId();
      fileUploadService.saveFile(file, userId.toString());

      if(ImageConstans.TAG_PROFILE.equals(imageEntry.getTags())){
         User user = userService.getUserById(userId);
         user.setProfilePictureUuid(imageEntry.getUuid());
         userService.updateUser(user);
      }
      return imageRepository.save(imageEntry);
   }
   public ImageEntry getImageByUUID(String uuid){

      return imageRepository.findByUuid(uuid);
   }

   public Resource getImageAsResource(String uuid) {
      ImageEntry imageEntry= getImageByUUID(uuid);
      String imageName = imageEntry.getFileName();
      try {
         Path userFolderPath = rootLocation.resolve(imageEntry.getUserId().toString()).normalize();
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

}
