package com.gege.ideas.messenger.contacts.repository;

import com.gege.ideas.messenger.contacts.entity.Contacts;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactsRepository extends JpaRepository<Contacts, Long> {
   List<Contacts> findByOwnerId(Long id);

   Contacts findByOwnerIdAndContactUserId(Long ownerId, Long ContactId);
}
