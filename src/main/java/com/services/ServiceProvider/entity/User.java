package com.services.ServiceProvider.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    public User(User user) {
        this.id = user.id;
        this.password = user.password;
        this.username = user.username;
        this.contactNumber = user.contactNumber;
        this.name = user.name;
        this.isBlocked = user.isBlocked;
        this.address = user.address;
        this.isEmailVerified = user.isEmailVerified;
        this.isPasswordCreated = user.isPasswordCreated;
        this.createdAt = user.createdAt;
        this.updatedAt = user.updatedAt;
        this.isDummyUser = user.isDummyUser;
        this.roles = user.roles != null ? new HashSet<>(user.roles) : null;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 150)
    @Column(name = "email", unique = true, nullable = false)
    private String username;

    @Size(max = 10)
    private String contactNumber;

    @Size(max = 50, message = "Name must not exceed 50 characters")
    private String name;


    private Boolean isBlocked;

    private String address;

    private Boolean isEmailVerified = false;

    private Boolean isPasswordCreated = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    private Boolean isDummyUser;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

}