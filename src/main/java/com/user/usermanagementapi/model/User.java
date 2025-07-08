package com.user.usermanagementapi.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.PropertyValues;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


//marks this class as a JPA entity
@Entity
@Table(name="users")
//@Getter  //THis annotation is used to make Getter function automatically
//@Setter  //This annotation is used to make Setter function automatically
@Data     //This annotation is used to make Getter + Setter + NoArgsConstructor + AllArgsConstructor all together at compilation
@NoArgsConstructor       //This annotation is used for default constructor
@RequiredArgsConstructor  //This annotation is used for nonNull field Constructor
public class User {

    @Id  //specifies the PRIMARY KEY of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = "Name is required")
    @Size(min = 2,max = 100,message = "Name must be between 2 and 100 characters")
    private String name;

    @NonNull
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name="created_at",updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @NonNull
    @NotBlank
    private String password;


    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name = "user_roles",joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles=new HashSet<>();



}
