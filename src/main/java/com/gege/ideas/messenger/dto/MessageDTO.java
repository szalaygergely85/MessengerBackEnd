package com.gege.ideas.messenger.dto;

import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.entity.MessageStatus;

public class MessageDTO {

   private Message message;

   private MessageStatus status;

   private long timestamp;

   public MessageDTO(Message message, MessageStatus status, long timestamp) {
      this.message = message;
      this.status = status;
      this.timestamp = timestamp;
   }

   public long getTimestamp() {
      return timestamp;
   }

   public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
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
