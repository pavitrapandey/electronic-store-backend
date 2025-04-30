package com.electronic.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Roles {

    @Id
    private String roleId;
    private String name;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private List<User> users=new ArrayList<>();
}
