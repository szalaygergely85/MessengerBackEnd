package com.gege.ideas.messenger.conversation.controller;

import com.gege.ideas.messenger.DTO.ConversationContent;
import com.gege.ideas.messenger.conversation.service.ConversationsService;
import com.gege.ideas.messenger.user.entity.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conversation")
public class ConversationsController {

   private final ConversationsService conversationsService;

   @Autowired
   public ConversationsController(ConversationsService conversationsService) {
      this.conversationsService = conversationsService;
   }

   @PostMapping
   public Long addConversation(@RequestBody List<User> participants) {
      return conversationsService.addConversation(participants);
   }

   @GetMapping("id/{id}")
   public ConversationContent getConversationAndContentById(
      @PathVariable Long id
   ) {
      return conversationsService.getConversationContent(id);
   }
}
