package com.gege.ideas.messenger.message.service;

import com.gege.ideas.messenger.DTO.MessageDTO;
import com.gege.ideas.messenger.conversation.entity.Conversation;
import com.gege.ideas.messenger.conversation.service.ConversationParticipantsService;
import com.gege.ideas.messenger.conversation.service.ConversationService;
import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.entity.MessageStatus;
import com.gege.ideas.messenger.message.entity.MessageStatusType;
import com.gege.ideas.messenger.message.repository.MessageRepository;
import com.gege.ideas.messenger.notifcation.NotificationService;
import com.gege.ideas.messenger.permission.service.PermissionService;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

   private final MessageRepository messageRepository;

   private NotificationService notificationService;
   private final UserService userService;
   private final ConversationParticipantsService conversationParticipantsService;
   private final PermissionService permissionService;
   private final ConversationService conversationService;
   private final MessageStatusService messageStatusService;

   @Autowired
   public MessageService(
      MessageRepository messageRepository,
      UserService userService,
      ConversationParticipantsService conversationParticipantsService,
      PermissionService permissionService,
      ConversationService conversationService,
      MessageStatusService messageStatusService,
      @Qualifier(
         "messageNotificationService"
      ) NotificationService notificationService
   ) {
      this.messageRepository = messageRepository;

      this.userService = userService;
      this.conversationParticipantsService = conversationParticipantsService;
      this.permissionService = permissionService;
      this.conversationService = conversationService;
      this.messageStatusService = messageStatusService;
      this.notificationService = notificationService;
   }

   public Message addMessage(Message message) {
      List<User> recipientUsers =
         conversationParticipantsService.getUsersByConversationId(
            message.getConversationId()
         );

      Map<Long, MessageStatusType> userStatuses = new HashMap<>();

      Map<Long, Boolean> deliveredStatuses = new HashMap<>();

      for (User recipientUser : recipientUsers) {
         if (recipientUser.getUserId() != message.getSenderId()) {
            _sendNotification(recipientUser, message);
            userStatuses.put(
               recipientUser.getUserId(),
               MessageStatusType.PENDING
            );
            deliveredStatuses.put(recipientUser.getUserId(), false);
         } else {
            deliveredStatuses.put(recipientUser.getUserId(), true);
            userStatuses.put(recipientUser.getUserId(), MessageStatusType.READ);
         }
      }

      messageStatusService.createPendingMessage(
         new MessageStatus(
            null,
            message.getUuid(),
            userStatuses,
            deliveredStatuses
         )
      );
      return messageRepository.save(message);
   }

   public void deleteMessage(Message message) {
      messageRepository.delete(message);
   }

   public Object deleteMessage(String uuid) {
      deleteMessage(messageRepository.findByUuid(uuid));
      return true;
   }

   public Message getLatestMessageByConversationId(Long conversationId) {
      return messageRepository.findTopByConversationIdOrderByTimestampDesc(
         conversationId
      );
   }

   public List<MessageDTO> getMessagesByConversationIdOrderedByTimestamp(
      Long id,
      Long timestamp,
      String token
   ) {
      List<MessageDTO> messageDTOS = new ArrayList<>();
      List<Message> messages = new ArrayList<>();
      if (timestamp != null) {
         messages =
         messageRepository.findByConversationIdAndTimestampAfterOrderByTimestampAsc(
            id,
            timestamp
         );
      } else {
         messages =
         messageRepository.findByConversationIdOrderByTimestampAsc(id);
      }
      if (!messages.isEmpty()) {
         for (Message message : messages) {
            messageDTOS.add(
               new MessageDTO(
                  message,
                  messageStatusService.getMessageStatus(
                     message.getUuid(),
                     userService.getUserIdByToken(token)
                  ),
                  message.getTimestamp()
               )
            );
         }
         return messageDTOS;
      }
      return null;
   }

   public Message getMessageByID(Long messageId) {
      return messageRepository.findById(messageId).orElse(null);
   }

   public Message getMessageByUUID(String uuid) {
      return messageRepository.findByUuid(uuid);
   }

   public List<Message> getNewMessagesByConversationId(Long conversationId) {
      /* return messageRepository.findByConversationIdAndIsDownloaded(
		conversationId,
		false
	);*/return null;
   }

   public List<Message> getNewMessagesByUserToken(String authToken) {
      User user = userService.getUserByToken(authToken);

      List<Long> conversationIds =
         conversationParticipantsService.getConversationIdsByUserId(
            user.getUserId()
         );

      if (conversationIds != null) {
         List<Message> messages = new ArrayList<>();
         for (Long conversationId : conversationIds) {
            messages.addAll(getNewMessagesByConversationId(conversationId));
         }
         return messages;
      } else {
         return null;
      }
   }

   public Object getNotDeliveredMessages(String authToken) {
      List<Message> messages = new ArrayList<>();
      List<MessageStatus> messageStatuses =
         messageStatusService.getMessagesStatusNotDelivered(authToken);
      if (!messageStatuses.isEmpty()) {
         for (MessageStatus messageStatus : messageStatuses) {
            Message message = messageRepository.findByUuid(
               messageStatus.getUuid()
            );
            if (message != null) {
               messages.add(message);
            }
         }
      }
      return messages;
   }

   public Boolean markMessagesAsDownloaded(
      List<String> messageUuids,
      String token
   ) {
      for (String uuid : messageUuids) {
         messageStatusService.markMessageAsDelivered(uuid, token);
      }
      return true;
   }

   private void _sendNotification(User recipientUser, Message message) {
      Conversation conversation = conversationService.getConversationById(
         message.getConversationId()
      );
      Map<String, String> data = new HashMap<>();
      data.put("content", message.getContent());
      String title = conversation.getConversationName();
      if (title == null) {
         User user = userService.getUserById(message.getSenderId());
         data.put("title", user.getDisplayName());
      } else {
         data.put("title", title);
      }
      notificationService.sendNotification(recipientUser.getUserId(), data);
   }
}
