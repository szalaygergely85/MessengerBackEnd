package com.gege.ideas.messenger.devices.service;

import com.gege.ideas.messenger.devices.entity.Device;
import com.gege.ideas.messenger.devices.repository.DeviceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

   private DeviceRepository deviceRepository;

   @Autowired
   public DeviceService(DeviceRepository deviceRepository) {
      this.deviceRepository = deviceRepository;
   }

   public Object addDevice(Device device) {
      List<Device> localDevices = deviceRepository.findDevicesByDeviceToken(
         device.getDeviceToken()
      );
      if (!localDevices.isEmpty()) {
         for (Device localDevice : localDevices) {
            if (localDevice.getUserId() == device.getUserId()) {
               return localDevice;
            }
         }
      }
      return deviceRepository.save(device);
   }

   public List<Device> getDevices(long userId) {
      return deviceRepository.findDevicesByUserId(userId);
   }
}
