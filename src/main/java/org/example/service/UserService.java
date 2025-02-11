package org.example.service;

import org.example.entity.User;

public interface UserService {

    void register(User user);

    User authenticate(String email, String password);

}
