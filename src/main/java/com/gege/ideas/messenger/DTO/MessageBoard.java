package com.gege.ideas.messenger.DTO;

import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.user.entity.User;
import java.util.List;

public class MessageBoard {

   private Long conversationId;

   private Message message;

   private List<User> participants;

   public MessageBoard(
      Long conversationId,
      Message message,
      List<User> participants
   ) {
      this.conversationId = conversationId;
      this.message = message;
      this.participants = participants;
   }

   public Long getConversationId() {
      return conversationId;
   }

   public void setConversationId(Long conversationId) {
      this.conversationId = conversationId;
   }

   public Message getMessage() {
      return message;
   }

   public void setMessage(Message message) {
      this.message = message;
   }

   public List<User> getParticipants() {
      return participants;
   }

   public void setParticipants(List<User> participants) {
      this.participants = participants;
   }
}
