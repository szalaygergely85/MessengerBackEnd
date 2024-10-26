package com.gege.ideas.messenger.image.repository;

import com.gege.ideas.messenger.image.entity.ImageEntry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ImageRepository extends JpaRepository<ImageEntry, Long> {

}
