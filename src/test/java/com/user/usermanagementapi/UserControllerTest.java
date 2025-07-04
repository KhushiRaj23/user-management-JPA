package com.user.usermanagementapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.user.usermanagementapi.model.User;
import com.user.usermanagementapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;  //mock our model,view,controller layer

    @Autowired
    private ObjectMapper objectMapper; //helps to convert an object data into JSON and vice versa

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private List<User> manyUsers;
    private long initialUserCount;
    @BeforeEach
    void setUp(){
        userRepository.deleteAll(); //to maintain isolation
        objectMapper.registerModule(new JavaTimeModule());
        User initialUser1=new User("John Doe","John.doe@example.com");
        User initialUser2=new User("Jane Smith","Jane.smith@example.com");

        List<User> savedInitialUsers=userRepository.saveAll(Arrays.asList(initialUser1,initialUser2));
        this.user1=savedInitialUsers.get(0);
        this.user2=savedInitialUsers.get(1);

        manyUsers= IntStream.rangeClosed(1,25).mapToObj(i->new User("User "+i,"user"+ i +"@example.com")).collect(Collectors.toList());
        userRepository.saveAll(manyUsers);

        initialUserCount=userRepository.count();
    }

    @Test
    void testGetUserByIdFound() throws Exception {
        //used to mock http request like GET,PUT,POST.....
        mockMvc.perform(get("/api/users/{id}",user1.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }




}
