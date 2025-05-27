package org.ataraxii.wishlist.unit;

import org.ataraxii.wishlist.database.entity.User;
import org.ataraxii.wishlist.database.repository.SessionRepository;
import org.ataraxii.wishlist.database.repository.UserRepository;
import org.ataraxii.wishlist.dto.authentication.AuthRequestDto;
import org.ataraxii.wishlist.dto.authentication.AuthResponseDto;
import org.ataraxii.wishlist.exception.ConflictException;
import org.ataraxii.wishlist.exception.NotFoundException;
import org.ataraxii.wishlist.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;


    @Test
    void registerUser_valid_saveUser() {
        AuthRequestDto request = new AuthRequestDto();

        request.setUsername("testuser");
        request.setPassword("testpassword");

        Mockito.when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");

        AuthResponseDto response = authService.registerUser(request);

        Mockito.verify(userRepository).save(Mockito.any());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getUser());
        Assertions.assertEquals("testuser", response.getUser().getUsername());
    }

    @Test
    void registerUser_alreadyExist_throwException() {
        AuthRequestDto request = new AuthRequestDto();

        request.setUsername("testuser");
        request.setPassword("testpassword");

        Mockito.when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);
        Assertions.assertThrows(ConflictException.class, () -> authService.registerUser(request));
    }

    @Test
    void loginUser_valid_openSession() {
        AuthRequestDto request = new AuthRequestDto();

        request.setUsername("testuser");
        request.setPassword("testpassword");

        User mockUser = User.builder()
                .id(UUID.fromString("ea7c7381-a19c-49a9-92cd-d32c3db25092"))
                .username("testuser")
                .password("hashedpassword")
                .build();

        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        Mockito.when(passwordEncoder.matches("testpassword", "hashedpassword")).thenReturn(true);

        AuthResponseDto response = authService.loginUser(request);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getUser());
        Assertions.assertNotNull(response.getSession());
        Assertions.assertEquals("testuser", response.getUser().getUsername());
    }

    @Test
    void loginUser_userNotFound_throwException() {
        AuthRequestDto request = new AuthRequestDto();

        request.setUsername("testuser");
        request.setPassword("testpassword");

        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> authService.loginUser(request));
    }


}
