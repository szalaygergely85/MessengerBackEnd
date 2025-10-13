package com.gege.ideas.messenger.audit;

import jakarta.persistence.*;

@Entity
@Table(
   name = "Changes",
   uniqueConstraints = @UniqueConstraint(
      columnNames = { "user_id", "entity_type" }
   )
)
public class ChangeRecord {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "user_id", nullable = false)
   private Long userId;

   @Column(name = "entity_type", nullable = false)
   private Long entityType;

   @Column(name = "last_modified", nullable = false)
   private Long lastModified;

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   public Long getEntityType() {
      return entityType;
   }

   public void setEntityType(Long entityType) {
      this.entityType = entityType;
   }

   public Long getLastModified() {
      return lastModified;
   }

   public void setLastModified(Long lastModified) {
      this.lastModified = lastModified;
   }

   public ChangeRecord() {}
}
