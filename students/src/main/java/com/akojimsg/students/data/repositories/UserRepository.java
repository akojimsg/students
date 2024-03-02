package com.akojimsg.students.data.repositories;

import com.akojimsg.students.data.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <UserAccount, Long> {
  UserAccount findByUsername(String username);

  Boolean existsByUsername(String username);
}
