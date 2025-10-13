package com.gege.ideas.messenger.devices.controller;

import com.gege.ideas.messenger.devices.entity.Device;
import com.gege.ideas.messenger.devices.service.DeviceService;
import com.gege.ideas.messenger.security.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

   private DeviceService deviceService;

   private final PermissionService permissionService;

   @Autowired
   public DeviceController(
      DeviceService deviceService,
      PermissionService permissionService
   ) {
      this.deviceService = deviceService;
      this.permissionService = permissionService;
   }

   @PostMapping("/add-device")
   public ResponseEntity<?> addContact(
      @RequestBody Device device,
      @RequestHeader("Authorization") String authToken
   ) {
      return ResponseEntity.ok().body(deviceService.addDevice(device));
   }

   @GetMapping("/get-devices/{user-id}")
   public ResponseEntity<?> searchContacts(
      @PathVariable long userId,
      @RequestHeader("Authorization") String authToken
   ) {
      return ResponseEntity.ok().body(deviceService.getDevices(userId));
   }
}
