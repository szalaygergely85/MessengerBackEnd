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
      Device localDevice = deviceRepository.findDeviceByUserId(
         device.getUserId()
      );
      if (localDevice != null) {
         localDevice.setDeviceToken(device.getDeviceToken());
         return deviceRepository.save(localDevice);
      }

      return deviceRepository.save(device);
   }

   public List<Device> getDevices(long userId) {
      return deviceRepository.findDevicesByUserId(userId);
   }
}
