package com.gege.ideas.messenger.DTO;

import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.entity.MessageStatus;

public class MessageDTO {

   private Message message;

   private MessageStatus status;

   public MessageDTO(Message message, MessageStatus status) {
      this.message = message;
      this.status = status;
   }

   public MessageDTO() {}

   public Message getMessage() {
      return message;
   }

   public void setMessage(Message message) {
      this.message = message;
   }

   public MessageStatus getStatus() {
      return status;
   }

   public void setStatus(MessageStatus status) {
      this.status = status;
   }
}
