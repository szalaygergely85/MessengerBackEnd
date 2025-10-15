package com.gege.ideas.messenger.file.controller;

import com.gege.ideas.messenger.exception.UnauthorizedException;
import com.gege.ideas.messenger.file.service.FileUploadService;
import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.service.MessageService;
import com.gege.ideas.messenger.permission.service.PermissionService;
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
   private MessageService _messageService;
   private PermissionService _permissionService;

   @Autowired
   public FileUploadController(
      FileUploadService _fileUploadService,
      MessageService _messageService,
      PermissionService _permissionService
   ) {
      this._fileUploadService = _fileUploadService;
      this._messageService = _messageService;
      this._permissionService = _permissionService;
   }

   @GetMapping("/{userId}/{filename}")
   @ResponseBody
   public ResponseEntity<?> serveFile(
      @PathVariable Long userId,
      @PathVariable String filename,
      @RequestHeader("Authorization") String token
   ) {
      // Verify user is authenticated
      if (!_permissionService.isUserRegistered(token)) {
         throw new UnauthorizedException("Authentication required");
      }

      // User can download their own files
      if (!_permissionService.hasPermissionToSendAsUser(token, userId)) {
         throw new SecurityException(
            "You do not have permission to access this file"
         );
      }

      Resource file = _fileUploadService.loadAsResource(
         filename,
         userId.toString()
      );
      if (file == null) {
         return ResponseEntity.notFound().build();
      }

      return ResponseEntity
         .ok()
         .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\""
         )
         .body(file);
   }

   @PostMapping("/upload")
   public ResponseEntity<?> handleFileUpload(
      @RequestPart("file") MultipartFile file,
      @RequestPart("messageEntry") Message message,
      @RequestHeader("Authorization") String token
   ) {
      // Verify user is authenticated and matches the sender
      if (
         !_permissionService.hasPermissionToSendAsUser(
            token,
            message.getSenderId()
         )
      ) {
         throw new SecurityException(
            "You cannot send messages as another user"
         );
      }

      // Verify user has permission to send to this conversation
      if (
         !_permissionService.hasPermissionToConversation(
            token,
            message.getConversationId()
         )
      ) {
         throw new SecurityException(
            "You are not a participant in this conversation"
         );
      }

      Long id = message.getSenderId();
      _fileUploadService.saveFile(file, id.toString());
      _messageService.addMessage(message);
      return ResponseEntity.ok().build();
   }
}
