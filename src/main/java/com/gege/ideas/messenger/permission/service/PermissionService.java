package com.gege.ideas.messenger.permission.service;

import com.gege.ideas.messenger.config.SystemUserInitializer;
import com.gege.ideas.messenger.contacts.entity.Contact;
import com.gege.ideas.messenger.contacts.service.ContactsService;
import com.gege.ideas.messenger.conversation.service.ConversationParticipantsService;
import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.repository.MessageRepository;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

   private final MessageRepository messageRepository;
   private final ContactsService contactsService;
   private final UserService userService;
   private final ConversationParticipantsService conversationParticipantsService;

   @Autowired
   public PermissionService(
      MessageRepository messageRepository,
      ContactsService contactsService,
      UserService userService,
      ConversationParticipantsService conversationParticipantsService
   ) {
      this.messageRepository = messageRepository;
      this.contactsService = contactsService;
      this.userService = userService;
      this.conversationParticipantsService = conversationParticipantsService;
   }

   public boolean hasPermissionToAddContact(String authToken, Long ownerId) {
      User user = userService.getUserByToken(authToken);
      return ownerId.equals(user.getUserId());
   }

   public boolean hasPermissionToDeleteContact(
      String authToken,
      Long contactUserId
   ) {
      User user = userService.getUserByToken(authToken);
      Contact contact = contactsService.getContactByOwnerIDAndContactUserId(
         user.getUserId(),
         contactUserId
      );
      Long userId = user.getUserId();
      return userId.equals(contact.getOwnerId());
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
      if (user != null) {
         return userToken.equals(authToken);
      }
      return false;
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
      if (user != null) {
         for (User participant : participants) {
            if (user.getUserId() == participant.getUserId()) {
               return true;
            }
         }
      }
      return false;
   }

   public boolean hasPermissionToMessages(
      String authToken,
      List<Message> messages
   ) {
      for (Message message : messages) {
         if (hasPermissionToMessage(message, authToken)) {
            return false;
         }
      }
      return true;
   }

   public boolean isInParticipantsIds(
      List<Long> participantsId,
      String authToken
   ) {
      return isInParticipants(
         userService.getUsersByIds(participantsId),
         authToken
      );
   }

   public boolean hasPermissionToMessage(Message message, String authToken) {
      if (hasPermissionToConversation(authToken, message.getConversationId())) {
         return true;
      }
      return false;
   }

   public boolean isUserTestUser(String authToken) {
      return authToken.equals(SystemUserInitializer.TEST_UUID);
   }
}
