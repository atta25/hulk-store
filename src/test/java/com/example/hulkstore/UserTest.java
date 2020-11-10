package com.example.hulkstore;

import com.example.hulkstore.domain.User;
import com.example.hulkstore.dto.UserDTO;
import com.example.hulkstore.exception.InvalidUserException;
import com.example.hulkstore.repository.UserRepository;
import com.example.hulkstore.service.UserService;
import com.example.hulkstore.utils.Encryptor;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class UserTest {
    private UserRepository userRepositoryMock;
    private UserService userService;

    @Before
    public void setUp() {
        userRepositoryMock = mock(UserRepository.class);
        userService = new UserService(userRepositoryMock, new Encryptor());
    }

    @Test
    public void whenTheUserDoesNotExistShouldAddIt() throws Exception {
        User user = new User();
        user.setEmail("abc@gmail.com");
        when(userRepositoryMock.exists("abc@gmail.com")).thenReturn(false);

        userService.add(user);

        verify(userRepositoryMock).add(user);
    }

    @Test(expected = InvalidUserException.class)
    public void whenTheUserExistShouldThrowAnException() throws Exception {
        User user = new User();
        user.setEmail("abc@gmail.com");
        when(userRepositoryMock.exists("abc@gmail.com")).thenReturn(true);

        userService.add(user);

        verify(userRepositoryMock).add(user);
    }

    @Test(expected = InvalidUserException.class)
    public void whenThePasswordsAreDifferentItShouldThrowAnException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("abc@gmail.com");
        userDTO.setPassword("123");
        User userMock = mock(User.class);
        when(userMock.getEmail()).thenReturn("abc@gmail.com");
        when(userMock.getPassword()).thenReturn("456");
        when(userRepositoryMock.getUser("abc@gmail.com")).thenReturn(userMock);

        userService.authenticate(userDTO);
    }
}
