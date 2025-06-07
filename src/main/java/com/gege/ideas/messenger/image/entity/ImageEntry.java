package com.gege.ideas.messenger.image.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "image")
public class ImageEntry {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   private String fileName;

   @Column(nullable = false)
   private Long userId;

   @Column(nullable = false)
   private String imageUri;

   @Column(nullable = false)
   private String mimeType;

   @Column(nullable = false)
   private int width;

   @Column(nullable = false)
   private int height;

   @Column(nullable = false)
   private long size;

   @Column(nullable = false)
   private long dateAdded;

   @Column
   private String status;

   @Column
   private String tags;

   @Column
   private String uuid;

   @Column
   private Long conversationId;

   public ImageEntry() {}

   public ImageEntry(
      Long id,
      String fileName,
      Long userId,
      String imageUri,
      String mimeType,
      int width,
      int height,
      long size,
      long dateAdded,
      String status,
      String tags,
      String uuid,
      Long conversationId
   ) {
      this.id = id;
      this.fileName = fileName;
      this.userId = userId;
      this.imageUri = imageUri;
      this.mimeType = mimeType;
      this.width = width;
      this.height = height;
      this.size = size;
      this.dateAdded = dateAdded;
      this.status = status;
      this.tags = tags;
      this.uuid = uuid;
      this.conversationId = conversationId;
   }

   public Long getConversationId() {
      return conversationId;
   }

   public void setConversationId(Long conversationId) {
      this.conversationId = conversationId;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getFileName() {
      return fileName;
   }

   public void setFileName(String fileName) {
      this.fileName = fileName;
   }

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   public String getImageUri() {
      return imageUri;
   }

   public void setImageUri(String imageUri) {
      this.imageUri = imageUri;
   }

   public String getMimeType() {
      return mimeType;
   }

   public void setMimeType(String mimeType) {
      this.mimeType = mimeType;
   }

   public int getWidth() {
      return width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public int getHeight() {
      return height;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public long getSize() {
      return size;
   }

   public void setSize(long size) {
      this.size = size;
   }

   public long getDateAdded() {
      return dateAdded;
   }

   public void setDateAdded(long dateAdded) {
      this.dateAdded = dateAdded;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getTags() {
      return tags;
   }

   public void setTags(String tags) {
      this.tags = tags;
   }

   public String getUuid() {
      return uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }
}
