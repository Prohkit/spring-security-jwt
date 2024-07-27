package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void saveUser() {
        User user = new User();

        userService.saveUser(user);

        verify(userRepository).save(user);
    }

    @Test
    void findByUsername() {
        User user = new User();
        User returnedUser = new User();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(returnedUser));

        userService.findByUsername(user.getUsername());

        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    void findByUsernameThrowsExceptionWhenUserNotFound() {
        User user = new User();

        assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername(user.getUsername()));
    }

    @Test
    void findById() {
        User user = new User();
        User returnedUser = new User();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(returnedUser));

        userService.findById(user.getId());

        verify(userRepository).findById(user.getId());
    }

    @Test
    void findByIdThrowsExceptionWhenUserNotFound() {
        User user = new User();

        assertThrows(UsernameNotFoundException.class, () -> userService.findById(user.getId()));
    }
}