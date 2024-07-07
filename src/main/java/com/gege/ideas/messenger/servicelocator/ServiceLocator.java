package com.gege.ideas.messenger.servicelocator;

import com.gege.ideas.messenger.conversation.service.ConversationService;
import com.gege.ideas.messenger.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ServiceLocator {

   @Autowired
   private ApplicationContext applicationContext;

   public ConversationService getConversationService() {
      return applicationContext.getBean(ConversationService.class);
   }

   public MessageService getMessageService() {
      return applicationContext.getBean(MessageService.class);
   }
}
