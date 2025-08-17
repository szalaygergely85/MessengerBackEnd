package com.gege.ideas.messenger.notifcation;

import java.util.Map;

public interface NotificationService {
   String sendNotification(long userId, Map<String, String> data);
}
