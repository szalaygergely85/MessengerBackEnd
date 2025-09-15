package com.gege.ideas.messenger.devices.repository;

import com.gege.ideas.messenger.devices.entity.Device;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
   List<Device> findDevicesByUserId(long userId);

   Device findDeviceByUserId(long userId);

   List<Device> findDevicesByDeviceToken(String deviceToken);
}
