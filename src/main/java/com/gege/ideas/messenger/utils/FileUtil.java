package com.gege.ideas.messenger.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class FileUtil {

   public static Resource loadAsResource(String filename) {
      try {
         Path file = new File(filename).toPath();
         Resource resource = (Resource) new UrlResource(file.toUri());
         if (resource != null) {
            return resource;
         }
      } catch (MalformedURLException e) {
         throw new RuntimeException(e);
      }
      return null;
   }
}
