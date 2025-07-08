package com.user.usermanagementapi;


import com.user.usermanagementapi.service.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import  static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class JwtServiceTesting {
    @Autowired
    private JwtService jwtService;

    //created a user for testing the features of jwtservice
    private final UserDetails user= User.withUsername("TestUser")
            .password("testuser123")
            .authorities("ROLE_USER")
            .build();

    @Test
//    Test 1 – Did we get *some* token back?
    void testGenerateTokenNotNull(){
        String token=jwtService.generateToken(user);
        assertThat(token).as("Token should not be null/blank").isNotBlank();
    }

    @Test
//    Test 2 – Does extractUsername() give us the same username we put in?
    void testExtractUsername(){
        String token=jwtService.generateToken(user);
        String extracted=jwtService.extractUsername(token);
        assertThat(extracted).as("Username parsed from JWT should match the principal")
                .isEqualTo(user.getUsername());
    }

    @Test
//    Test 3 – A freshly generated token must pass validation.
    void testValidateTokenSuccess(){
        String token=jwtService.generateToken(user);
        boolean valid=jwtService.validateToken(token,user);
        assertThat(valid).as("Fresh token should be valid").isTrue();
    }

    @Test
//    Test 4 – If we temper the token string, validation must fail.
    void testValidateTokenFailure(){
        String token=jwtService.generateToken(user)+"tampertbgladbvjHDFCJS";
        boolean valid=jwtService.validateToken(token,user);
        assertThat(valid).as("Corrupted token must be rejected").isFalse();
    }

}
