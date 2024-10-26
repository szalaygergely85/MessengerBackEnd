package com.gege.ideas.messenger.image.service;

import com.gege.ideas.messenger.file.service.FileUploadService;
import com.gege.ideas.messenger.image.entity.ImageEntry;
import com.gege.ideas.messenger.image.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    private final FileUploadService fileUploadService;

    @Autowired
    public ImageService(ImageRepository imageRepository, FileUploadService fileUploadService) {
        this.imageRepository = imageRepository;
        this.fileUploadService = fileUploadService;
    }

    public ImageEntry addImage(MultipartFile file, ImageEntry imageEntry){
        fileUploadService.saveFile(file, imageEntry.getUserId().toString());
        return imageRepository.save(imageEntry);
    }
}
