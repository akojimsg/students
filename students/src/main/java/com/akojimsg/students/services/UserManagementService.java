package com.akojimsg.students.services;

import com.akojimsg.students.data.dto.SignInDTO;
import com.akojimsg.students.data.dto.SignUpDTO;
import com.akojimsg.students.data.entities.UserAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserManagementService {
  UserAccount createUserAccount(SignUpDTO dto);
}
