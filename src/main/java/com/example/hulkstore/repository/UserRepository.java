package com.example.hulkstore.repository;

import com.example.hulkstore.domain.User;
import com.example.hulkstore.utils.Encryptor;
import org.springframework.stereotype.Component;

@Component
public class UserRepository {
    private final UserRepositoryInterface userRepositoryInterface;
    private final Encryptor encryptor;

    public UserRepository(UserRepositoryInterface userRepositoryInterface, Encryptor encryptor) {
        this.userRepositoryInterface = userRepositoryInterface;
        this.encryptor = encryptor;
    }

    public void init() throws Exception {
        User user = new User();
        user.setFirstName("admin");
        user.setLastName("admin");
        user.setEmail("admin@gmail.com");
        user.setPassword(new String(encryptor.encrypt("admin123")));

        userRepositoryInterface.save(user);
    }

    public User getUser(String email) {
        return userRepositoryInterface.findByEmail(email).get();
    }

    public boolean exists(String email) {
        return userRepositoryInterface.findByEmail(email).isPresent();
    }

    public synchronized void add(User user) throws Exception {
        String encryptedPassword = encryptor.encrypt(user.getPassword());
        user.setPassword(encryptedPassword);
        userRepositoryInterface.save(user);
    }
}
