package com.gege.ideas.messenger.logging.controller;

import com.gege.ideas.messenger.logging.entity.CrashLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LogsController {

   private static final Logger crashLogger = LoggerFactory.getLogger(
      "CrashLogger"
   );

   @PostMapping("/crash-report")
   public ResponseEntity<Void> crashReport(@RequestBody CrashLog crashLog) {
      crashLogger.error(
         "Crash reported:\nMessage: {}\nStackTrace: {}\nAndroidVersion: {}\nDeviceInfo: {}",
         crashLog.getMessage(),
         crashLog.getStackTrace(),
         crashLog.getAndroidVersion(),
         crashLog.getDevice()
      );
      return ResponseEntity.ok().build();
   }
}
