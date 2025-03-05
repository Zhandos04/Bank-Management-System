package org.example.bankmanagementsystem.controllers;

import lombok.RequiredArgsConstructor;
import org.example.bankmanagementsystem.Entities.User;
import org.example.bankmanagementsystem.dto.requests.UserEditRequest;
import org.example.bankmanagementsystem.dto.responses.UserResponse;
import org.example.bankmanagementsystem.repositories.UserRepository;
import org.example.bankmanagementsystem.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    @GetMapping("/allCustomers")
    public ResponseEntity<List<UserResponse>> customers() {
        return ResponseEntity.ok(userRepository.findAll().stream().map(this::convertToUserResponse).collect(Collectors.toList()));
    }
    @GetMapping("/customerDetail/{id}")
    public ResponseEntity<UserResponse> customerDetail(@PathVariable Long id) {
        return ResponseEntity.ok(convertToUserResponse(userRepository.findById(id).get()));
    }
    @PutMapping("/editCustomer")
    public ResponseEntity<UserResponse> editCustomer(@RequestBody UserEditRequest userEditRequest) {
        return ResponseEntity.ok(userService.editUser(userEditRequest));
    }
    private UserResponse convertToUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }
}
