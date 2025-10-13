package com.gege.ideas.messenger.dto;

import com.gege.ideas.messenger.conversation.entity.Conversation;
import com.gege.ideas.messenger.conversation.entity.ConversationParticipant;
import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.user.entity.User;
import java.io.Serializable;
import java.util.List;

public class ConversationDTO implements Serializable {

   private Conversation conversation;
   private List<ConversationParticipant> participants;
   private List<User> users;

   private Message messageEntry;

   public ConversationDTO(
      Conversation conversation,
      List<ConversationParticipant> participants,
      List<User> users,
      Message messageEntry
   ) {
      this.conversation = conversation;
      this.participants = participants;
      this.users = users;
      this.messageEntry = messageEntry;
   }

   public Conversation getConversation() {
      return conversation;
   }

   public void setConversation(Conversation conversation) {
      this.conversation = conversation;
   }

   public List<ConversationParticipant> getParticipants() {
      return participants;
   }

   public void setParticipants(List<ConversationParticipant> participants) {
      this.participants = participants;
   }

   public List<User> getUsers() {
      return users;
   }

   public void setUsers(List<User> users) {
      this.users = users;
   }

   public Message getMessageEntry() {
      return messageEntry;
   }

   public void setMessageEntry(Message messageEntry) {
      this.messageEntry = messageEntry;
   }
}
