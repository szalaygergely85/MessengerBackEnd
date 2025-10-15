package com.gege.ideas.messenger.image.controller;

import com.gege.ideas.messenger.exception.UnauthorizedException;
import com.gege.ideas.messenger.image.constans.ImageConstans;
import com.gege.ideas.messenger.image.entity.ImageEntry;
import com.gege.ideas.messenger.image.service.ImageService;
import com.gege.ideas.messenger.permission.service.PermissionService;
import com.gege.ideas.messenger.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/image")
public class ImageController {

   private ImageService imageService;

   private PermissionService _permissionService;

   private UserService _userService;

   @Autowired
   public ImageController(
      ImageService imageService,
      PermissionService permissionService,
      UserService userService
   ) {
      this.imageService = imageService;
      this._permissionService = permissionService;
      this._userService = userService;
   }

   @PostMapping(
      value = "/upload",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
   )
   public ResponseEntity<?> handleImageUpload(
      @RequestPart("image") MultipartFile file,
      @RequestPart("imageEntry") ImageEntry imageEntry,
      @RequestHeader("Authorization") String token
   ) {
      switch (imageEntry.getTags()) {
         case ImageConstans.TAG_PROFILE:
            if (
               !_permissionService.hasPermissionToUser(
                  token,
                  _userService.getTokenById(imageEntry.getUserId())
               )
            ) {
               throw new UnauthorizedException();
            }
            Resource resourceFile = imageService.addImage(file, imageEntry);
            return ResponseEntity
               .ok()
               .contentType(MediaType.IMAGE_JPEG)
               .body(resourceFile);
         case ImageConstans.TAG_MESSAGE:
            if (
               !_permissionService.hasPermissionToConversation(
                  token,
                  imageEntry.getConversationId()
               )
            ) {
               throw new UnauthorizedException();
            }
            Resource resourceFile2 = imageService.addImage(file, imageEntry);
            return ResponseEntity
               .ok()
               .contentType(MediaType.IMAGE_JPEG)
               .body(resourceFile2);
         default:
            throw new IllegalArgumentException("Unknown image tag");
      }
   }

   /*
@GetMapping("/{uuid}")
@ResponseBody
public ResponseEntity<Resource> getImage(@PathVariable String uuid) {
	Resource file = imageService.getImageAsResource(uuid);

	if (file == null) return ResponseEntity.notFound().build();

	return ResponseEntity
		.ok()
		.header(
			HttpHeaders.CONTENT_DISPOSITION,
			"attachment; filename=\"" + file.getFilename() + "\""
		)
		.body(file);
} */

   @GetMapping("/userid/{userId}")
   public ResponseEntity<?> getImage(
      @PathVariable Long userId,
      @RequestHeader("Authorization") String token
   ) {
      if (!_permissionService.isUserRegistered(token)) {
         throw new UnauthorizedException();
      }
      Resource file = imageService.getImageAsResourceByUserID(userId);

      if (!file.exists()) {
         return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
   }

   @GetMapping("/uuid/{uuid}")
   public ResponseEntity<?> getImage(
      @PathVariable String uuid,
      @RequestHeader("Authorization") String token
   ) {
      if (!_permissionService.isUserRegistered(token)) {
         throw new UnauthorizedException();
      }
      Resource file = imageService.getImageAsResourceByUUID(uuid);

      if (!file.exists()) {
         return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
   }
}
