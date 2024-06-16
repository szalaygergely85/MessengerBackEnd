package com.gege.ideas.messenger.DTO;

import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.user.entity.User;
import java.util.List;

public class ConversationContent {

   private Long conversationId;
   private List<User> participants;
   private List<Message> messages;
   private String PublicKey;

   public Long getConversationId() {
      return conversationId;
   }

   public void setConversationId(Long conversationId) {
      this.conversationId = conversationId;
   }

   public List<User> getParticipants() {
      return participants;
   }

   public void setParticipants(List<User> participants) {
      this.participants = participants;
   }

   public List<Message> getMessages() {
      return messages;
   }

   public void setMessages(List<Message> messages) {
      this.messages = messages;
   }

   public String getPublicKey() {
      return PublicKey;
   }

   public void setPublicKey(String publicKey) {
      PublicKey = publicKey;
   }

   public ConversationContent(Long conversationId) {
      this.conversationId = conversationId;
   }
}
