package com.gege.ideas.messenger.file.controller;

import com.gege.ideas.messenger.file.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/files")
public class FileUploadController {

   private FileUploadService _fileUploadService;

   @Autowired
   public FileUploadController(FileUploadService _fileUploadService) {
      this._fileUploadService = _fileUploadService;
   }

   @GetMapping("/{filename:.+}")
   @ResponseBody
   public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
      Resource file = _fileUploadService.loadAsResource(filename);

      if (file == null) return ResponseEntity.notFound().build();

      return ResponseEntity
         .ok()
         .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\""
         )
         .body(file);
   }

   @PostMapping("/upload")
   public String handleFileUpload(
      @RequestParam("file") MultipartFile file,
      @RequestParam("details") String details
   ) {
      _fileUploadService.saveFile(file);

      return "redirect:/";
   }
}
