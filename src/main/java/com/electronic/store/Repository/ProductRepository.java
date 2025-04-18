package com.electronic.store.Repository;

import com.electronic.store.entities.Category;
import com.electronic.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,String>{

    Page<Product> findByLiveTrue(Pageable pageable);

    Page<Product> findByPriceBetween(long min, long max, Pageable pageable);

    Page<Product> findByCategory(Category category,Pageable pageable);

}
