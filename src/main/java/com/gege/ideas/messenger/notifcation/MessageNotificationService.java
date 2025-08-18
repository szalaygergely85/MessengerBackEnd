package com.gege.ideas.messenger.notifcation;

import com.gege.ideas.messenger.devices.entity.Device;
import com.gege.ideas.messenger.devices.service.DeviceService;
import com.gege.ideas.messenger.firebase.FirebaseMessageService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("messageNotificationService")
public class MessageNotificationService implements NotificationService {

   private final FirebaseMessageService firebaseService;
   private final DeviceService deviceService;

   @Autowired
   public MessageNotificationService(
      FirebaseMessageService firebaseService,
      DeviceService deviceService
   ) {
      this.firebaseService = firebaseService;
      this.deviceService = deviceService;
   }

   @Override
   public String sendNotification(long userId, Map<String, String> data) {
      data.put("type", "message");
      if (data.get("title") == null) {
         data.put("title", "Unknown");
      }

      List<Device> deviceList = deviceService.getDevices(userId);
      for (Device device : deviceList) {
         firebaseService.sendDataMessage(device.getDeviceToken(), data);
      }

      return "OK";
   }
}
