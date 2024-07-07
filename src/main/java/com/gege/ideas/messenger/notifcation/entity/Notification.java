package com.gege.ideas.messenger.notifcation.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "notification")
public class Notification {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long notificationId;

   @Lob
   @Column(columnDefinition = "LONGTEXT")
   private String content;
   private Long conversationId;
   private boolean isActive;
   private Long timeStamp;
   private String title;
   private Long userId;

   public Long getNotificationId() {
      return notificationId;
   }

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public Long getConversationId() {
      return conversationId;
   }

   public void setConversationId(Long conversationId) {
      this.conversationId = conversationId;
   }

   public boolean isActive() {
      return isActive;
   }

   public void setActive(boolean active) {
      isActive = active;
   }

   public Long getTimeStamp() {
      return timeStamp;
   }

   public void setTimeStamp(Long timeStamp) {
      this.timeStamp = timeStamp;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   public Notification() {}

   public Notification(
      String content,
      Long conversationId,
      boolean isActive,
      Long timeStamp,
      String title,
      Long userId
   ) {
      this.content = content;
      this.conversationId = conversationId;
      this.isActive = isActive;
      this.timeStamp = timeStamp;
      this.title = title;
      this.userId = userId;
   }
}
