package com.example.Modeme.User.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Modeme.User.UserEntity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByCreatedAt(LocalDateTime date);
    Optional<User> findByNameAndEmail(String name, String email);
    Optional<User> findByNameAndPhone(String name, String phone);
    Optional<User> findByUsernameAndNameAndEmail(String username, String name, String email);
    Optional<User> findByUsernameAndNameAndPhone(String username, String name, String phone);
    List<User> findAllByNameAndPhone(String name, String phone);
    List<User> findAllByNameAndEmail(String name, String email);

    // 아이디로 검색 (부분 일치)
    Page<User> findByUsernameContaining(String username, Pageable pageable);
    // 회원 유형으로 검색 (부분 일치)
    Page<User> findByRoleContaining(String role, Pageable pageable);
}
