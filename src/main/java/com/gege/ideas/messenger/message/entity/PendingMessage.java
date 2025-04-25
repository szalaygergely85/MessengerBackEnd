package com.gege.ideas.messenger.message.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pending_message")
public class PendingMessage implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long messageToSendId;

   @Column(nullable = false)
   private String uuid;

   @Column(nullable = false)
   private Long userId;

   @Column
   private boolean delivered;

   public PendingMessage(String uuid, Long userId, boolean delivered) {
      this.uuid = uuid;
      this.userId = userId;
      this.delivered = delivered;
   }

   public PendingMessage() {}

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

   public boolean isDelivered() {
      return delivered;
   }

   public void setDelivered(boolean delivered) {
      this.delivered = delivered;
   }
}
