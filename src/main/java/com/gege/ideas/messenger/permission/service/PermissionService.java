package com.gege.ideas.messenger.permission.service;

import com.gege.ideas.messenger.DTO.MessageBoard;
import com.gege.ideas.messenger.conversation.service.ConversationParticipantsService;
import com.gege.ideas.messenger.message.repository.MessageRepository;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import com.gege.ideas.messenger.user.service.UserTokenService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

   private final MessageRepository messageRepository;
   private final UserTokenService userTokenService;
   private final UserService userService;
   private final ConversationParticipantsService conversationParticipantsService;

   @Autowired
   public PermissionService(
      MessageRepository messageRepository,
      UserTokenService userTokenService,
      UserService userService,
      ConversationParticipantsService conversationParticipantsService
   ) {
      this.messageRepository = messageRepository;
      this.userTokenService = userTokenService;
      this.userService = userService;
      this.conversationParticipantsService = conversationParticipantsService;
   }

   public boolean hasPermissionToAddContact(String authToken, Long ownerId) {
      User user = userService.getUserByToken(authToken);
      return ownerId == user.getUserId();
   }

   public boolean hasPermissionToConversation(
      String userToken,
      Long conversationId
   ) {
      List<User> participants =
         conversationParticipantsService.getUsersByConversationId(
            conversationId
         );

      return isInParticipants(participants, userToken);
   }

   public boolean hasPermissionToUser(String userToken, String authToken) {
      User user = userService.getUserByToken(authToken);
      return (user != null && userToken == authToken);
   }

   public boolean isUserRegistered(String authToken) {
      User user = userService.getUserByToken(authToken);
      if (user != null) {
         return true;
      }
      return false;
   }

   public boolean isInParticipants(List<User> participants, String authToken) {
      User user = userService.getUserByToken(authToken);
      for (User participant : participants) {
         if (user.getUserId() == participant.getUserId()) {
            return true;
         }
      }
      return false;
   }

   public boolean hasPermissionToMessageBoards(
      String authToken,
      List<MessageBoard> messages
   ) {
      for (MessageBoard message : messages) {
         if (
            !hasPermissionToConversation(authToken, message.getConversationId())
         ) {
            return false;
         }
      }
      return true;
   }

   public boolean isInParticipantsIds(
      List<Long> participantsId,
      String authToken
   ) {
      return true;
   }
}
