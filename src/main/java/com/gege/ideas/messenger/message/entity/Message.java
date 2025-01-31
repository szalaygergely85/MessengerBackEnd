package com.gege.ideas.messenger.message.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "message")
public class Message implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long messageId;

   @Column(nullable = false)
   private long conversationId;

   @Column(nullable = false)
   private long senderId;

   @Column(nullable = false)
   private long timestamp;

   @Lob
   @Column(columnDefinition = "LONGTEXT")
   private String contentEncrypted;

   @Column
   private int type;

   @Column(columnDefinition = "LONGTEXT")
   private String uuid;

   public Long getMessageId() {
      return messageId;
   }

   public void setMessageId(Long messageId) {
      this.messageId = messageId;
   }

   public long getConversationId() {
      return conversationId;
   }

   public void setConversationId(long conversationId) {
      this.conversationId = conversationId;
   }

   public long getSenderId() {
      return senderId;
   }

   public void setSenderId(long senderId) {
      this.senderId = senderId;
   }

   public long getTimestamp() {
      return timestamp;
   }

   public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
   }

   public String getContentEncrypted() {
      return contentEncrypted;
   }

   public void setContentEncrypted(String content) {
      this.contentEncrypted = content;
   }

   public int getType() {
      return type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public String getUuid() {
      return uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public Message() {}

   public Message(
      long conversationId,
      long senderId,
      long timestamp,
      String contentEncrypted,
      int type,
      String uuid
   ) {
      this.messageId = messageId;
      this.conversationId = conversationId;
      this.senderId = senderId;
      this.timestamp = timestamp;
      this.contentEncrypted = contentEncrypted;

      this.type = type;

      this.uuid = uuid;
   }
}
