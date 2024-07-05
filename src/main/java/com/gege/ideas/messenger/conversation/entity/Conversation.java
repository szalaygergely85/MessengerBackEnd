package com.gege.ideas.messenger.conversation.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "conversations")
public class Conversation {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long conversationId;

   private Long timeStamp;

   private boolean hasNewMessage;

   public Long getTimeStamp() {
      return timeStamp;
   }

   public void setTimeStamp(Long timeStamp) {
      this.timeStamp = timeStamp;
   }

   public boolean isHasNewMessage() {
      return hasNewMessage;
   }

   public void setHasNewMessage(boolean hasNewMessage) {
      this.hasNewMessage = hasNewMessage;
   }

   public Conversation(Long timeStamp, boolean hasNewMessage) {
      this.timeStamp = timeStamp;
      this.hasNewMessage = hasNewMessage;
   }

   public Long getConversationId() {
      return conversationId;
   }

   public void setConversationId(Long conversationId) {
      this.conversationId = conversationId;
   }

   public Conversation() {}
}
