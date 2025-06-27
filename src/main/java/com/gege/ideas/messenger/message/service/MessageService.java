package com.gege.ideas.messenger.message.service;

import com.gege.ideas.messenger.DTO.MessageBoard;
import com.gege.ideas.messenger.conversation.service.ConversationParticipantsService;
import com.gege.ideas.messenger.conversation.service.ConversationService;
import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.entity.PendingMessage;
import com.gege.ideas.messenger.message.repository.MessageRepository;
import com.gege.ideas.messenger.notifcation.service.NotificationService;
import com.gege.ideas.messenger.permission.service.PermissionService;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

   private final MessageRepository messageRepository;

   private final UserService userService;
   private final ConversationParticipantsService conversationParticipantsService;
   private final PermissionService permissionService;
   private final ConversationService conversationService;
   private final PendingMessageService pendingMessageService;

   @Autowired
   public MessageService(
      MessageRepository messageRepository,
      UserService userService,
      ConversationParticipantsService conversationParticipantsService,
      PermissionService permissionService,
      ConversationService conversationService,
      PendingMessageService pendingMessageService
   ) {
      this.messageRepository = messageRepository;

      this.userService = userService;
      this.conversationParticipantsService = conversationParticipantsService;
      this.permissionService = permissionService;
      this.conversationService = conversationService;
      this.pendingMessageService = pendingMessageService;
   }

   public Message createMessage(Message message) {
      List<User> recipientUsers =
         conversationParticipantsService.getUsersByConversationId(
            message.getConversationId()
         );

	for (User recipientUser : recipientUsers) {
		if (recipientUser.getUserId() != message.getSenderId()) {
			pendingMessageService.createPendingMessage(new PendingMessage(message.getUuid(), recipientUser.getUserId(), false));
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

   public Boolean markMessagesAsDownloaded(List<String> messageUuids, String token) {
      for (String uuid : messageUuids) {
       pendingMessageService.markMessageAsDelivered(uuid, token);
      }
      return true;

   }

   public Message getLatestMessageByConversationId(Long conversationId) {
      return messageRepository.findTopByConversationIdOrderByTimestampDesc(
         conversationId
      );
   }

   public Message getMessageByID(Long messageId) {
      return messageRepository.findById(messageId).orElse(null);
   }

   public Message getMessageByUUID(String uuid) {
      return messageRepository.findByUuid(uuid);
   }

   public Object deleteMessage(String uuid) {
      messageRepository.delete(messageRepository.findByUuid(uuid));
      return true;
   }

   public Object getNotDeliveredMessages(String authToken) {
      List<Message> messages = new ArrayList<>();
      List<PendingMessage> pendingMessages = pendingMessageService.getNotDeliveredMessages(authToken);
      if (!pendingMessages.isEmpty()) {
         for (PendingMessage pendingMessage : pendingMessages){
            Message message = messageRepository.findByUuid(pendingMessage.getUuid());
            if (message!=null){
               messages.add(message);
            }
         }
      }
      return messages;
   }
}
