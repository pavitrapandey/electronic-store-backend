package com.electronic.store.Repository;

import com.electronic.store.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, String> {
   Optional<Roles> findByName(String name);
}
