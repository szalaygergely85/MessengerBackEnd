package com.gege.ideas.messenger.image.controller;

import com.gege.ideas.messenger.image.entity.ImageEntry;
import com.gege.ideas.messenger.image.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
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
      @RequestPart("file") MultipartFile file,
      @RequestPart("imageEntry") ImageEntry imageEntry
   ) {
      imageService.addImage(file, imageEntry);
      return "redirect:/";
   }
}
