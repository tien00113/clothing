package com.tasc.clothing.model;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;

import com.tasc.clothing.utils.CustomGrantedAuthority;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank
    @Size(min = 8, message = "Mật khẩu phải từ 8 ký tự trở lên")
    @Column(nullable = false)
    private String password;

    @Email(message = "Email không hợp lệ")
    private String email;

    private String firstName;
    private String lastName;

    private String phoneNumber;

    // @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    // @Enumerated(EnumType.STRING)
    // @CollectionTable(name = "user_roles")
    // @Convert(converter = RoleListConverter.class)
    private Collection<Role> roles;

    @Override
    public java.util.Collection<? extends CustomGrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new CustomGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
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

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public enum Role {
        USER,
        ADMIN
    }
}
