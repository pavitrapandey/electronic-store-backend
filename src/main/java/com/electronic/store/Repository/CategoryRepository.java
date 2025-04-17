package com.electronic.store.Repository;

import com.electronic.store.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    // find by categoryTitle

    Category findByTitle(String Title);
}
