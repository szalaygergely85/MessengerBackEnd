package com.gege.ideas.messenger.message.service;

import com.gege.ideas.messenger.DTO.MessageBoard;
import com.gege.ideas.messenger.conversation.service.ConversationParticipantsService;
import com.gege.ideas.messenger.conversation.service.ConversationService;
import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.repository.MessageRepository;
import com.gege.ideas.messenger.notifcation.entity.Notification;
import com.gege.ideas.messenger.notifcation.service.NotificationService;
import com.gege.ideas.messenger.permission.service.PermissionService;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import com.gege.ideas.messenger.user.service.UserTokenService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

   private final MessageRepository messageRepository;
   private final UserTokenService userTokenService;
   private final UserService userService;
   private final ConversationParticipantsService conversationParticipantsService;
   private final PermissionService permissionService;
   private final ConversationService conversationService;
   private final NotificationService notificationService;

   @Autowired
   public MessageService(
      MessageRepository messageRepository,
      UserTokenService userTokenService,
      UserService userService,
      ConversationParticipantsService conversationParticipantsService,
      PermissionService permissionService,
      ConversationService conversationService,
      NotificationService notificationService
   ) {
      this.messageRepository = messageRepository;
      this.userTokenService = userTokenService;
      this.userService = userService;
      this.conversationParticipantsService = conversationParticipantsService;
      this.permissionService = permissionService;
      this.conversationService = conversationService;
      this.notificationService = notificationService;
   }

   public Message createMessage(Message message) {
      message.setRead(false);

      List<User> recipientUsers =
         conversationParticipantsService.getUsersByConversationId(
            message.getConversationId()
         );
      for (User recipientUser : recipientUsers) {
         if (recipientUser.getUserId() != message.getSenderId()) {
            String title = "Message from: " + recipientUser.getDisplayName();
            notificationService.addNotification(
               new Notification(
                  message.getContent(),
                  message.getConversationId(),
                  true,
                  message.getTimestamp(),
                  title,
                  recipientUser.getUserId()
               )
            );
         }
      }
      return messageRepository.save(message);
   }

   public List<Message> getMessagesByConversationIdOrderedByTimestamp(Long id) {
      return messageRepository.findByConversationIdOrderByTimestampAsc(id);
   }

   public List<MessageBoard> getMessagesBoardEntriesOrderedByTimestamp(
      String token
   ) {
      User user = userService.getUserByToken(token);
      if (user != null) {
         List<MessageBoard> messageBoard = new ArrayList<>();
         List<Long> conversationIds =
            conversationParticipantsService.getConversationIdsByUserId(
               user.getUserId()
            );
         for (int i = 0; i < conversationIds.size(); i++) {
            List<User> participants =
               conversationParticipantsService.getUsersByConversationId(
                  conversationIds.get(i)
               );
            Message message =
               messageRepository.findTopByConversationIdOrderByTimestampDesc(
                  conversationIds.get(i)
               );
            if (message != null) {
               messageBoard.add(
                  new MessageBoard(
                     conversationIds.get(i),
                     message,
                     participants
                  )
               );
            }
         }
         return messageBoard;
      }
      return null;
   }

   public Message getLatestMassageByConversationId(Long conversationId) {
      return messageRepository.findTopByConversationIdOrderByTimestampDesc(
         conversationId
      );
   }

   @Deprecated
   public List<MessageBoard> getNewMessagesByUserToken(String authToken) {
      User user = userService.getUserByToken(authToken);
      List<Long> conversationIds =
         conversationParticipantsService.getConversationIdsByUserId(
            user.getUserId()
         );
      List<Long> filteredIds =
         conversationService.getConversationsWithNewMessage(conversationIds);
      if (filteredIds != null) {
         List<MessageBoard> messageBoards = new ArrayList<>();
         for (Long conversationId : filteredIds) {
            messageBoards.add(
               new MessageBoard(
                  conversationId,
                  getLatestMassageByConversationId(conversationId),
                  conversationParticipantsService.getUsersByConversationId(
                     conversationId
                  )
               )
            );
         }
         return messageBoards;
      } else {
         return null;
      }
   }

   public void deleteMessage(Message message) {
      messageRepository.delete(message);
   }

   public Object getMessagesAndCompareWithLocal(String token, int count) {
      User user = userService.getUserByToken(token);
      List<Long> conversationIds =
         conversationParticipantsService.getConversationIdsByUserId(
            user.getUserId()
         );

      List<Message> messages = new ArrayList<>();
      for (Long conversationId : conversationIds) {
         List<Message> messagesOfConversation =
            messageRepository.findByConversationId(conversationId);
         if (messagesOfConversation != null) {
            messages.addAll(messagesOfConversation);
         }
      }

      if (messages.size() == count) {
         return null;
      }

      return messages;
   }
}
