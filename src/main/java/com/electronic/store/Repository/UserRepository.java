package com.electronic.store.Repository;

import com.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,String> {



    Optional<User> findByEmail(String email);
    List<User> findByKeyword(String keyword);
}
