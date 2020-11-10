package com.example.hulkstore.repository;

import com.example.hulkstore.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepositoryInterface extends CrudRepository<User, String> {
    Optional<User> findByEmail(String email);
}
