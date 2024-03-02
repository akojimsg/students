package com.akojimsg.students.data.repositories;

import com.akojimsg.students.data.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserManagementRepository extends JpaRepository<UserAccount, Long> {
}
