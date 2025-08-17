package com.gege.ideas.messenger.contacts.repository;

import com.gege.ideas.messenger.contacts.entity.Contact;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactsRepository extends JpaRepository<Contact, Long> {
   List<Contact> findByOwnerId(long id);

   Contact findByOwnerIdAndContactUserId(Long ownerId, Long ContactId);
}
