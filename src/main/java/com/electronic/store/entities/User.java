package com.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    private String userId;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String gender;

    private String about;

    private String imageName;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Roles> roles=new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private List<Order> orders=new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority( role.getName()))  // Adding ROLE_ prefix
                .collect(Collectors.toSet());
        System.out.println("authorities: "+authorities);
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
