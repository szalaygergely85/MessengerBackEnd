package com.gege.ideas.messenger.conversation.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "conversation_participants")
public class ConversationParticipant {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long conversationParticipantId;

   @Column(nullable = false)
   private Long conversationId;

   @Column(nullable = false)
   private Long userId;


   public Long getConversationParticipantId() {
      return conversationParticipantId;
   }

   public void setConversationParticipantId(Long conversationParticipantId) {
      this.conversationParticipantId = conversationParticipantId;
   }

   public Long getConversationId() {
      return conversationId;
   }

   public void setConversationId(Long conversationId) {
      this.conversationId = conversationId;
   }

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }


   public ConversationParticipant(Long conversationId, Long userId) {
      this.conversationId = conversationId;
      this.userId = userId;
   }

   public ConversationParticipant() {}
}
