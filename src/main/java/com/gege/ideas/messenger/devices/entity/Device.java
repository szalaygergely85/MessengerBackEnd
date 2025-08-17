package com.gege.ideas.messenger.devices.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "device")
public class Device {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long deviceId;

   @Column(nullable = false)
   private long userId;

   @Column(nullable = false)
   private String deviceToken;

   @Column(name = "last_active")
   private LocalDateTime lastActive = LocalDateTime.now();

   @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt = LocalDateTime.now();

   @Column(name = "updated_at")
   private LocalDateTime updatedAt = LocalDateTime.now();

   @PreUpdate
   public void preUpdate() {
      updatedAt = LocalDateTime.now();
   }

   public Device() {}

   public Device(long deviceId, long userId, String deviceToken) {
      this.deviceId = deviceId;
      this.userId = userId;
      this.deviceToken = deviceToken;
   }

   public long getDeviceId() {
      return deviceId;
   }

   public long getUserId() {
      return userId;
   }

   public void setUserId(long userId) {
      this.userId = userId;
   }

   public String getDeviceToken() {
      return deviceToken;
   }

   public void setDeviceToken(String deviceToken) {
      this.deviceToken = deviceToken;
   }
}
