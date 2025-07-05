package com.user.usermanagementapi;

import com.user.usermanagementapi.model.User;
import com.user.usermanagementapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

@DataJpaTest //loads only a slice of the spring context relevant to JPA
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;
    private User user1;
    private User user2;

    @BeforeEach //run before every test
    void setUp(){
        userRepository.deleteAllInBatch();
        user1=new User("rhea sharma","rhea@example.com");
        user2=new User("priya gupta","priyagupta@gmail.com");
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();
        entityManager.clear();
    }
    @Test
    void testFindByEmailFound() throws Exception {
        Optional<User> foundUser=userRepository.findByEmail(user1.getEmail());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo(user1.getName());
        assertThat(foundUser.get().getEmail()).isEqualTo(user1.getEmail());
        assertThat(foundUser.get().getId()).isEqualTo(user1.getId());
    }

    @Test
    void testFindByEmailNotFound() throws Exception {
        Optional<User> result=userRepository.findByEmail("ghhhtyyy@example.com");
        assertThat(result).isNotPresent();
    }
}
