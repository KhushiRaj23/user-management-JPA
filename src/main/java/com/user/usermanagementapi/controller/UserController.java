package com.user.usermanagementapi.controller;

import com.user.usermanagementapi.model.User;
import com.user.usermanagementapi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

//mark this class as a REST controller, handling incoming HTTP requests
@CrossOrigin(origins = {
        "http://localhost:8080",   // Spring‑Boot static pages
        "http://127.0.0.1:5500"    // VS‑Code Live‑Server, etc.
})
@RestController
@RequestMapping("/api/users")  //Base path for all the endpoints in this controller
public class UserController {
    @Autowired //inject the UserRepository dependency
    private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;


    //name=admin password=admin123 email=admin@gmail.com
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409: email taken
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of("ROLE_USER"));
        User saved = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping
    public ResponseEntity<List<User>> createUsers(@RequestBody List<@Valid User> users) {
        users.forEach(u -> {
            u.setPassword(passwordEncoder.encode(u.getPassword()));
            if (u.getRoles() == null || u.getRoles().isEmpty()) {
                u.setRoles(Set.of("ROLE_USER"));
            }
        });
        List<User> savedUsers = userRepository.saveAll(users);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsers);
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userRepository.findAll(); // Retrieves all the users from the db
    }
    @GetMapping("/{id}")   //...api/users/{id}
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        Optional<User> user=userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @Valid @RequestBody User userDetails) {
        return userRepository.findById(id).map(existing -> {
            existing.setName(userDetails.getName());
            existing.setEmail(userDetails.getEmail());
            if (!userDetails.getPassword().isBlank()) {
                existing.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }
            existing.setRoles(userDetails.getRoles()); // optional: validate roles
            User updated = userRepository.save(existing);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id); //Delete the user
            return ResponseEntity.noContent().build(); //return 204 No content
        }else{
            return ResponseEntity.notFound().build(); //if user not found return 404 not found
        }
    }
    // Retrieves all users with pagination and sorting capabilities.
    // HTTP METHOD :GET
    // Endpoint:/api/users//QueryParameters:
    //-page
    //-size
    //-sort

    @GetMapping("/page")
    public Page<User> getUsers(Pageable pageable){
        return  userRepository.findAll(pageable);
    }


}
