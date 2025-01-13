package com.gege.ideas.messenger.image.controller;

import com.gege.ideas.messenger.image.entity.ImageEntry;
import com.gege.ideas.messenger.image.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

@Controller
@RequestMapping("/api/image")
public class ImageController {

   private ImageService imageService;

   @Autowired
   public ImageController(ImageService imageService) {
      this.imageService = imageService;
   }

   @PostMapping("/upload")
   public String handleImageUpload(
      @RequestPart("image") MultipartFile file,
      @RequestPart("imageEntry") ImageEntry imageEntry
   ) {
      imageService.addImage(file, imageEntry);
      return "redirect:/";
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
   public ResponseEntity<Resource> getImage(@PathVariable Long userId) {
      try {

         Resource file = imageService.getImageAsResourceByUserID(userId);

         if (file.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)  // or MediaType.IMAGE_PNG depending on your image type
                    .body(file);
         } else {
            return ResponseEntity.notFound().build();
         }
      } catch (Exception e) {
         return ResponseEntity.badRequest().build();
      }
   }

   @GetMapping("/uuid/{uuid}")
   public ResponseEntity<Resource> getImage(@PathVariable String uuid) {
      try {

         Resource file = imageService.getImageAsResourceByUUID(uuid);

         if (file.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)  // or MediaType.IMAGE_PNG depending on your image type
                    .body(file);
         } else {
            return ResponseEntity.notFound().build();
         }
      } catch (Exception e) {
         return ResponseEntity.badRequest().build();
      }
   }
}
