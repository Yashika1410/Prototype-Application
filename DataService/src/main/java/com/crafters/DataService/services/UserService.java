package com.crafters.DataService.services;
import com.crafters.DataService.entities.User;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User getUserById(String userId);
}
