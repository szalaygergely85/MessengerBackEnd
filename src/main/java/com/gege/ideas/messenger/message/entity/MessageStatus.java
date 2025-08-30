package com.gege.ideas.messenger.message.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "message_status")
public class MessageStatus implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long messageStatusId;

   @Column(nullable = false)
   private String uuid;

   @Column(nullable = false)
   private Long userId;

   @Column
   MessageStatusType messageStatusType;

   public MessageStatus(
      String uuid,
      Long userId,
      MessageStatusType messageStatusType
   ) {
      this.uuid = uuid;
      this.userId = userId;

      this.messageStatusType = messageStatusType;
   }

   public MessageStatus() {}

   public String getUuid() {
      return uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   public Long getMessageStatusId() {
      return messageStatusId;
   }

   public void setMessageStatusId(Long messageToSendId) {
      this.messageStatusId = messageToSendId;
   }

   public MessageStatusType getMessageStatusType() {
      return messageStatusType;
   }

   public void setMessageStatusType(MessageStatusType messageStatusType) {
      this.messageStatusType = messageStatusType;
   }
}
