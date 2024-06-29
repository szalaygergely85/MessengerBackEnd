package com.gege.ideas.messenger.permission.service;

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
      User user = userService.getUserByToken(userToken);
      List<Long> participantIds =
         conversationParticipantsService.getParticipantIds(conversationId);
      for (Long participantId : participantIds) {
         if (participantId == user.getUserId()) {
            return true;
         }
      }
      return false;
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
}
