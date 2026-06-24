package com.HiredAI.heiredAi.Service;

import com.HiredAI.heiredAi.Entity.UserEntity;
import com.HiredAI.heiredAi.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;
    private JwtUtil jwtUtil;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        emailService = mock(EmailService.class);
        jwtUtil = mock(JwtUtil.class);
        userService = new UserService(userRepository, passwordEncoder, emailService, jwtUtil, true);
    }

    @Test
    void register_persistsUserWithAcquisitionSource() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");

        String result = userService.Register(
                "john@example.com",
                "password123",
                "John",
                "Doe",
                "1234567890",
                "referral_source"
        );

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository, times(1)).save(captor.capture());

        UserEntity savedUser = captor.getValue();
        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals("hashed_password", savedUser.getPassword());
        assertEquals("John", savedUser.getFirstName());
        assertEquals("Doe", savedUser.getLastName());
        assertEquals("1234567890", savedUser.getMobile());
        assertEquals("referral_source", savedUser.getAcquisitionSource());
    }
}
