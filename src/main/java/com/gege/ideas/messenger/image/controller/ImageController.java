package com.gege.ideas.messenger.image.controller;

import com.gege.ideas.messenger.image.entity.ImageEntry;
import com.gege.ideas.messenger.image.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
   }
}