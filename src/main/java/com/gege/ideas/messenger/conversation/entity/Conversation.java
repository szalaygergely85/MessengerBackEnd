package com.gege.ideas.messenger.conversation.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "conversations")
public class Conversation {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long conversationId;

   @Column
   private String conversationName;

   @Column
   private Long timeStamp;

   @Column
   private Long creatorUserId;

   @Column
   private int numberOfParticipants;

   @Column
   private Long lastUpdated;

   public Long getLastUpdated() {
      return lastUpdated;
   }

   public void setLastUpdated(Long lastUpdated) {
      this.lastUpdated = lastUpdated;
   }

   public Long getTimeStamp() {
      return timeStamp;
   }

   public void setTimeStamp(Long timeStamp) {
      this.timeStamp = timeStamp;
   }

   public String getConversationName() {
      return conversationName;
   }

   public void setConversationName(String conversationName) {
      this.conversationName = conversationName;
   }

   public Conversation(Long timeStamp, boolean hasNewMessage) {
      this.timeStamp = timeStamp;
   }

   public Conversation(
      Long timeStamp,
      Long creatorUserId,
      int numberOfParticipants
   ) {
      this.timeStamp = timeStamp;
      this.creatorUserId = creatorUserId;
      this.numberOfParticipants = numberOfParticipants;
   }

   public Conversation(
      String conversationName,
      Long timeStamp,
      Long creatorUserId,
      int numberOfParticipants
   ) {
      this.conversationName = conversationName;
      this.timeStamp = timeStamp;
      this.creatorUserId = creatorUserId;
      this.numberOfParticipants = numberOfParticipants;
   }

   public Long getConversationId() {
      return conversationId;
   }

   public void setConversationId(Long conversationId) {
      this.conversationId = conversationId;
   }

   public Long getCreatorUserId() {
      return creatorUserId;
   }

   public void setCreatorUserId(Long creatorUserId) {
      this.creatorUserId = creatorUserId;
   }

   public int getNumberOfParticipants() {
      return numberOfParticipants;
   }

   public void setNumberOfParticipants(int numberOfParticipants) {
      this.numberOfParticipants = numberOfParticipants;
   }

   public Conversation() {}
}
