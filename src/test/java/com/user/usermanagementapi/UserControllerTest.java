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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        //ACT
        mockMvc.perform(get("/api/users/{id}", user1.getId()).contentType(MediaType.APPLICATION_JSON))
                //asserting
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user1.getId().intValue())))
                .andExpect(jsonPath("$.name",is(user1.getName())))
                .andExpect(jsonPath("$.email",is(user1.getEmail())));
    }

    @Test
    void testCreateUsers()throws Exception{
        //---ARRANGE---
        User newUser1=new User("utkarsh","utkarsh@gmail.com");
        User newUser2=new User("Khushi","khushi@gmail.com");
        List<User> newUsers=Arrays.asList(newUser1,newUser2);
        //---ACT---
        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(newUsers)))
        //---ASSERT---
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].id",notNullValue()))
                .andExpect(jsonPath("$[0].name",is("utkarsh")))
                .andExpect(jsonPath("$[0].email",is("utkarsh@gmail.com")))
                .andExpect(jsonPath("$[1].id",notNullValue()))
                .andExpect(jsonPath("$[1].name",is("Khushi")))
                .andExpect(jsonPath("$[1].email",is("khushi@gmail.com")))
        ;
    }
    //Implement these test methods
    @Test
    void testDeleteUserFound() throws Exception {
        mockMvc.perform(delete("/api/users/{id}",user1.getId())).andExpect(status().isNoContent());
        assertFalse(userRepository.existsById(user1.getId()));
        assertEquals(initialUserCount-1,userRepository.count());

    }

    @Test
    void testDeleteUserNotFound() throws Exception{
        Long nonExistedId=user1.getId()+101;  //any value which does not exist in database
        mockMvc.perform(delete("/api/users/{id}",nonExistedId)).andExpect(status().isNotFound());
        assertEquals(initialUserCount,userRepository.count());
    }

}
