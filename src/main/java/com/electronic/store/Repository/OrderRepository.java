package com.electronic.store.Repository;

import com.electronic.store.entities.Order;
import com.electronic.store.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
  List<Order> findByUser(User user);
  Page<Order> findAll(Pageable page);
}