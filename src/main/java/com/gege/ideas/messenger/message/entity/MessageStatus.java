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

   @Column
   private  boolean delivered;

   private final int type = MessageConstans.MESSAGE_STATUS;

   // getters/setters


   public MessageStatus(Long messageStatusId, String uuid, Map<Long, MessageStatusType> userStatuses, boolean delivered) {
      this.messageStatusId = messageStatusId;
      this.uuid = uuid;
      this.userStatuses = userStatuses;
      this.delivered = delivered;
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

   public boolean isDelivered() {
      return delivered;
   }

   public void setDelivered(boolean delivered) {
      this.delivered = delivered;
   }

   public int getType() {
      return type;
   }
}
