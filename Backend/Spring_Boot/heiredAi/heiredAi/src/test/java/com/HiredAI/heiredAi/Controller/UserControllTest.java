package com.HiredAI.heiredAi.Controller;

import com.HiredAI.heiredAi.Entity.UserEntity;
import com.HiredAI.heiredAi.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllTest {

    private UserService userService;
    private UserControll userControll;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userControll = new UserControll(userService, "AUTH_TOKEN", 86400, false, "Lax");
    }

    @Test
    void userRegister_delegatesToUserServiceWithAcquisitionSource() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMobile("1234567890");
        user.setAcquisitionSource("organic_search");

        when(userService.Register(
                eq("test@example.com"),
                eq("password123"),
                eq("John"),
                eq("Doe"),
                eq("1234567890"),
                eq("organic_search")
        )).thenReturn("Registered successfully");

        String result = userControll.userRegister(user);

        assertEquals("Registered successfully", result);
        verify(userService, times(1)).Register(
                eq("test@example.com"),
                eq("password123"),
                eq("John"),
                eq("Doe"),
                eq("1234567890"),
                eq("organic_search")
        );
    }
}
