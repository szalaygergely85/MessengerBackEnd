package com.gege.ideas.messenger.message.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "message")
public class Message {

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
   private String content;

   @Column
   private boolean isRead;

   @Column
   private int type;

   @Lob
   @Column(columnDefinition = "LONGTEXT")
   private String contentSenderVersion;

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

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public boolean isRead() {
      return isRead;
   }

   public int getType() {
      return type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public void setRead(boolean read) {
      isRead = read;
   }

   public String getContentSenderVersion() {
      return contentSenderVersion;
   }

   public void setContentSenderVersion(String contentSenderVersion) {
      this.contentSenderVersion = contentSenderVersion;
   }
}
