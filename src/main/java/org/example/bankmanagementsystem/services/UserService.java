package org.example.bankmanagementsystem.services;

import org.example.bankmanagementsystem.Entities.User;
import org.example.bankmanagementsystem.dto.requests.UserDTO;
import org.example.bankmanagementsystem.dto.requests.UserEditRequest;
import org.example.bankmanagementsystem.dto.responses.UserResponse;
import org.example.bankmanagementsystem.exceptions.UserAlreadyExistsException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {
    UserDetails loadUserByUsername(String username);
    void registerNewUser(UserDTO userDTO) throws UserAlreadyExistsException;
    void update(User user);
    void updateProfile(User user);
    void saveUserConfirmationCode(Long id, String code);
    void updatePassword(User user);
    Optional<User> getUserByEmail(String email);
    UserDetails getCurrentUser();
    void resentCode(String email);
    UserResponse editUser(UserEditRequest userEditRequest);
}
