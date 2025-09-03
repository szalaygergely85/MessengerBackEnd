package com.gege.ideas.messenger.message.entity;

import com.gege.ideas.messenger.message.constans.MessageConstans;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "message_status")
public class MessageStatus implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long messageStatusId;

   @Column
   private String uuid;

   @ElementCollection(fetch = FetchType.EAGER)
   @CollectionTable(
           name = "message_status_user",
           joinColumns = @JoinColumn(name = "message_status_id")
   )
   @MapKeyColumn(name = "user_id")
   @Column(name = "status")
   @Enumerated(EnumType.STRING)  // Store enum as string
   private Map<Long, MessageStatusType> userStatuses = new HashMap<>();

   @ElementCollection(fetch = FetchType.EAGER)
   @CollectionTable(
           name = "message_status_delivered",
           joinColumns = @JoinColumn(name = "message_status_id")
   )
   @MapKeyColumn(name = "user_id")
   @Column(name = "delivered")
   private Map<Long, Boolean> deliveredStatuses = new HashMap<>();

   private final int type = MessageConstans.MESSAGE_STATUS;

   // getters/setters


   public MessageStatus(Long messageStatusId, String uuid, Map<Long, MessageStatusType> userStatuses, Map<Long, Boolean> deliveredStatuses) {
      this.messageStatusId = messageStatusId;
      this.uuid = uuid;
      this.userStatuses = userStatuses;
      this.deliveredStatuses = deliveredStatuses;
   }

   public MessageStatus() {}

   public Long getMessageStatusId() {
      return messageStatusId;
   }

   public void setMessageStatusId(Long messageStatusId) {
      this.messageStatusId = messageStatusId;
   }

   public String getUuid() {
      return uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public Map<Long, MessageStatusType> getUserStatuses() {
      return userStatuses;
   }

   public void setUserStatuses(Map<Long, MessageStatusType> userStatuses) {
      this.userStatuses = userStatuses;
   }

   public Map<Long, Boolean> getDeliveredStatuses() {
      return deliveredStatuses;
   }

   public void setDeliveredStatuses(Map<Long, Boolean> deliveredStatuses) {
      this.deliveredStatuses = deliveredStatuses;
   }

   public int getType() {
      return type;
   }
}
