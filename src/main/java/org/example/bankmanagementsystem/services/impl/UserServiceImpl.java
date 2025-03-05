package org.example.bankmanagementsystem.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.bankmanagementsystem.Entities.Account;
import org.example.bankmanagementsystem.Entities.AccountType;
import org.example.bankmanagementsystem.Entities.Role;
import org.example.bankmanagementsystem.Entities.User;
import org.example.bankmanagementsystem.dto.requests.UserDTO;
import org.example.bankmanagementsystem.dto.requests.UserEditRequest;
import org.example.bankmanagementsystem.dto.responses.UserResponse;
import org.example.bankmanagementsystem.exceptions.UserAlreadyExistsException;
import org.example.bankmanagementsystem.repositories.AccountRepository;
import org.example.bankmanagementsystem.repositories.UserRepository;
import org.example.bankmanagementsystem.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final EmailServiceImpl emailService;
    private final AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = getUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    @Transactional
    @Override
    public void registerNewUser(UserDTO userDTO) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("A user with that email already exists");
        }
        User user = convertToUser(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsVerified(true);
        user.setGender("Любой");
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
        Account account = new Account();
        account.setCustomer(user);
        account.setBalance(1000.0);
        account.setType(AccountType.CHECKING);
        account.setAccountNumber(generateUniqueAccountNumber());
        accountRepository.save(account);
    }

    @Transactional
    @Override
    public void update(User user){
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateProfile(User user){
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void saveUserConfirmationCode(Long id, String code) {
        User user = userRepository.getUserById(id);
        user.setConfirmationCode(code);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updatePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private String generateCode() {
        return Integer.toString((int)(Math.random() * 900000) + 100000);
    }

    public UserDetails getCurrentUser() {
        // Получаем текущую аутентификацию
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Если пользователь аутентифицирован
        if (authentication != null && authentication.isAuthenticated()) {
            // Возвращаем объект UserDetails (который был установлен ранее в фильтре)
            return (UserDetails) authentication.getPrincipal();
        }

        return null;
    }

    @Override
    @Transactional
    public void resentCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        String code = generateCode();
        saveUserConfirmationCode(user.getId(), code);
        emailService.sendEmail(email, "Shanyraq Resend Code", "Your code is: " + code);
    }

    @Override
    @Transactional
    public UserResponse editUser(UserEditRequest userEditRequest) {
        User user = userRepository.findById(userEditRequest.getId()).get();
        user.setFirstName(userEditRequest.getFirstName());
        user.setLastName(userEditRequest.getLastName());
        userRepository.save(user);
        return modelMapper.map(user, UserResponse.class);
    }
    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = generateRandomAccountNumber();
        } while(accountRepository.findByAccountNumber(accountNumber).isPresent());
        return accountNumber;
    }
    private String generateRandomAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
