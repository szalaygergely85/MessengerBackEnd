package com.gege.ideas.messenger.notifcation;

import com.gege.ideas.messenger.devices.entity.Device;
import com.gege.ideas.messenger.devices.service.DeviceService;
import com.gege.ideas.messenger.firebase.FirebaseMessageService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("callNotificationService")
public class CallNotificationService implements NotificationService {

   private final FirebaseMessageService firebaseService;
   private final DeviceService deviceService;

   @Autowired
   public CallNotificationService(
      FirebaseMessageService firebaseService,
      DeviceService deviceService
   ) {
      this.firebaseService = firebaseService;
      this.deviceService = deviceService;
   }

   @Override
   public String sendNotification(long userId, Map<String, String> data) {
      List<Device> devices = deviceService.getDevices(userId);
      for (Device device : devices) {
         firebaseService.sendHighPriorityDataMessage(
            device.getDeviceToken(),
            data
         );
      }
      return "OK";
   }
}
