package com.example.hulkstore.service;

import com.example.hulkstore.domain.User;
import com.example.hulkstore.dto.UserDTO;
import com.example.hulkstore.exception.InvalidUserException;
import com.example.hulkstore.repository.UserRepository;
import com.example.hulkstore.utils.Encryptor;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    private final UserRepository userRepository;
    private final Encryptor encryptor;

    public UserService(UserRepository userRepository, Encryptor encryptor) {
        this.userRepository = userRepository;
        this.encryptor = encryptor;
    }

    //A user is added only if it does not exist in the repository
    public void add(User user) {
        try {
            if (userRepository.exists(user.getEmail())) {
                throw new InvalidUserException();
            } else {
                userRepository.add(user);
            }
        } catch (Exception e) {
            throw new InvalidUserException(e);
        }
    }

    //It is considered a correct authentication if the email and password match
    public void authenticate(UserDTO userDTO) {
        try {
            User user = userRepository.getUser(userDTO.getEmail());
            String decrypted = encryptor.decrypt(user.getPassword());
            if (!user.getEmail().equals(userDTO.getEmail()) || !decrypted.equals(userDTO.getPassword())) {
                throw new InvalidUserException();
            }
        } catch (Exception e) {
            throw new InvalidUserException(e);
        }
    }
}
