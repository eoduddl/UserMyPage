package com.example.project02.repository;

import com.example.project02.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findFirstByEmailAndPasswordOrderByIdDesc(String email, String password);

    Optional<User> findByEmailAndStatus(String email, String status);

}
