package com.akojimsg.students.utils.configs;

import com.akojimsg.students.data.entities.UserAccount;
import com.akojimsg.students.data.repositories.UserManagementRepository;
import com.akojimsg.students.data.repositories.UserRepository;
import com.akojimsg.students.services.UserManagementService;
import com.akojimsg.students.services.impl.UserManagementServiceImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class AppConfig {
  @Bean
  public UserDetailsService userDetailsService(UserRepository repository) {
    return username -> repository.findByUsername(username).asUser();
  }

  @Bean
  @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
  public UserManagementService userManagementService(){
    return new UserManagementServiceImpl();
  }

  @Bean
  CommandLineRunner initUsers(UserManagementRepository umRepository) {
    return args -> {
      umRepository.save(new UserAccount("test", "password", "ROLE_USER"));
      umRepository.save(new UserAccount("admin", "password", "ROLE_ADMIN"));
    };
  }
}
