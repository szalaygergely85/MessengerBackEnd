package com.gege.ideas.messenger.conversation.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "conversations")
public class Conversations {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long conversationId;

   private Long timeStamp;

   public Conversations(Long timeStamp) {
      this.timeStamp = timeStamp;
   }

   public Long getConversationId() {
      return conversationId;
   }

   public void setConversationId(Long conversationId) {
      this.conversationId = conversationId;
   }

   public Conversations() {}
}
