package com.gege.ideas.messenger.user.repository;

import com.gege.ideas.messenger.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   User findByEmail(String email);
   User findByUserTokenId(Long userTokenId);

   @Query(
      "SELECT u FROM User u WHERE u.fullName LIKE %:searchText% OR u.email LIKE %:searchText% OR u.displayName LIKE %:searchText%"
   )
   List<User> searchUsers(@Param("searchText") String searchText);
}
