package com.electronic.store.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "id")
   private String productId;

    @Column(nullable = false)
   private String title;

    @Column(length = 10000)
   private String description;

    @Column(nullable = false)
   private long price;

    private String productImageName;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date addedDate;
    private boolean live;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

}
