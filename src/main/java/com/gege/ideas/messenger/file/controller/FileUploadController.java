package com.gege.ideas.messenger.file.controller;

import com.gege.ideas.messenger.file.service.FileUploadService;
import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.service.MessageService;
import com.gege.ideas.messenger.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
      try {
         // Verify user is authenticated
         if (!_permissionService.isUserRegistered(token)) {
            return ResponseEntity
               .status(HttpStatus.UNAUTHORIZED)
               .body("Authentication required");
         }

         // Option 1: User can download their own files
         if (_permissionService.hasPermissionToSendAsUser(token, userId)) {
            Resource file = _fileUploadService.loadAsResource(filename, userId.toString());
            if (file != null) {
               return ResponseEntity
                  .ok()
                  .header(
                     HttpHeaders.CONTENT_DISPOSITION,
                     "attachment; filename=\"" + file.getFilename() + "\""
                  )
                  .body(file);
            } else {
               return ResponseEntity.notFound().build();
            }
         }

         // Option 2: User can download if they share a conversation with the file owner
         // This requires checking if both users are in any common conversation
         // For now, we'll be restrictive and only allow users to download their own files
         // To allow conversation participants to download, you'd need to pass conversationId
         // or check all conversations both users share

         return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body("You do not have permission to access this file");
      } catch (SecurityException e) {
         return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(e.getMessage());
      } catch (Exception e) {
         return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Failed to retrieve file");
      }
   }

   @PostMapping("/upload")
   public ResponseEntity<?> handleFileUpload(
      @RequestPart("file") MultipartFile file,
      @RequestPart("messageEntry") Message message,
      @RequestHeader("Authorization") String token
   ) {
      try {
         // Verify user is authenticated and matches the sender
         if (!_permissionService.hasPermissionToSendAsUser(token, message.getSenderId())) {
            return ResponseEntity
               .status(HttpStatus.FORBIDDEN)
               .body("You cannot send messages as another user");
         }

         // Verify user has permission to send to this conversation
         if (!_permissionService.hasPermissionToConversation(token, message.getConversationId())) {
            return ResponseEntity
               .status(HttpStatus.FORBIDDEN)
               .body("You are not a participant in this conversation");
         }

         Long id = message.getSenderId();
         _fileUploadService.saveFile(file, id.toString());
         _messageService.addMessage(message);
         return ResponseEntity.ok().build();
      } catch (IllegalArgumentException | SecurityException e) {
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
      } catch (Exception e) {
         return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Failed to upload file");
      }
   }
}
