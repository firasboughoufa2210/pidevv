package com.example.pidev.repository;

import com.example.pidev.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    void deleteByEmail(String email);

    @Query(value = "select * from users u " +
            "where concat(u.first_name, ' ', u.last_name) like %:name% " +
            "order by u.first_name asc, u.last_name asc",
           nativeQuery = true)
    List<User> findUsersByName(String name, Pageable pageable);
}
