package com.gege.ideas.messenger.message.controller;

import com.gege.ideas.messenger.DTO.MessageBoard;
import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.service.MessageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message")
public class MessageController {

   private final MessageService messageService;

   @Autowired
   MessageController(MessageService messageService) {
      this.messageService = messageService;
   }

   @GetMapping("/{token}/messageboardentries")
   public List<MessageBoard> getLatestMessage(@PathVariable String token) {
      return messageService.getLatestMessage(token);
   }

   @PostMapping
   public Message addMessage(@RequestBody Message message) {
      return messageService.createMessage(message);
   }

   @GetMapping("/byconversationid/{id}")
   public List<Message> getConversationMessages(@PathVariable long id) {
      return messageService.getConversationMessages(id);
   }
}
