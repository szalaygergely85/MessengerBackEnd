package com.gege.ideas.messenger.utils.test;

import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.service.MessageService;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.utils.RandomUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageTestUtil {

   private MessageService messageService;

   @Autowired
   public MessageTestUtil(MessageService messageService) {
      this.messageService = messageService;
   }

   public Message addTestMessage(User user) {
      return addTestMessage(user, RandomUtil.getRandomLong());
   }

   public Message addTestMessage(User user, Long conversationId) {
      return messageService.createMessage(
         new Message(
            conversationId,
            user.getUserId(),
            RandomUtil.getRandomLong(),
            RandomUtil.getRandomString(10),
            RandomUtil.getRandomBoolean(),
            RandomUtil.getRandomInt(),
            RandomUtil.getRandomString(10),
            RandomUtil.getRandomString(10),
            false
         )
      );
   }

   public void deleteMessages(List<Message> messages) {
      for (Message message : messages) {
         messageService.deleteMessage(message);
      }
   }
}
