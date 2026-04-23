package com.traintogain.backend.user;

import com.traintogain.backend.auth.refreshtoken.RefreshTokenService;
import com.traintogain.backend.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository repo;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private RefreshTokenService refresh;

    @InjectMocks
    private UserService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_success() {
        when(repo.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        when(encoder.encode("123456")).thenReturn("hashed");
        when(repo.save(any())).thenAnswer(i -> i.getArguments()[0]);

        User user = service.register("TEST@mail.com", "user", "123456");

        assertEquals("test@mail.com", user.getEmail());
        assertEquals("hashed", user.getPassword());
        assertFalse(user.isEnabled());
        assertNotNull(user.getVerificationToken());
    }

    @Test
    void register_emailExists() {
        when(repo.findByEmail("test@mail.com")).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistsException.class,
                () -> service.register("test@mail.com", "user", "123456"));
    }

    @Test
    void login_success() {
        User user = new User();
        user.setPassword("hashed");
        user.setEnabled(true);

        when(repo.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(encoder.matches("123456", "hashed")).thenReturn(true);

        User result = service.login("TEST@mail.com", "123456");

        assertNotNull(result);
    }

    @Test
    void login_notEnabled() {
        User user = new User();
        user.setPassword("hashed");
        user.setEnabled(false);

        when(repo.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialsException.class,
                () -> service.login("test@mail.com", "123456"));
    }

    @Test
    void login_wrongPassword() {
        User user = new User();
        user.setPassword("hashed");
        user.setEnabled(true);

        when(repo.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(encoder.matches("wrong", "hashed")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> service.login("test@mail.com", "wrong"));
    }

    @Test
    void getById_success() {
        User user = new User();

        when(repo.findById("id")).thenReturn(Optional.of(user));

        User result = service.getById("id");

        assertNotNull(result);
    }

    @Test
    void getById_notFound() {
        when(repo.findById("id")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.getById("id"));
    }

    @Test
    void updateProfile_success() {
        User user = new User();
        user.setEmail("old@mail.com");
        user.setUsername("old");

        when(repo.findById("id")).thenReturn(Optional.of(user));
        when(repo.findByEmail("new@mail.com")).thenReturn(Optional.empty());
        when(repo.save(any())).thenAnswer(i -> i.getArguments()[0]);

        User result = service.updateProfile("id", "NEW@mail.com", "newUser");

        assertEquals("new@mail.com", result.getEmail());
        assertEquals("newUser", result.getUsername());
    }

    @Test
    void updateProfile_emailTaken() {
        User user = new User();
        user.setEmail("old@mail.com");

        User other = new User();
        other.setEmail("other@mail.com");

        when(repo.findById("id")).thenReturn(Optional.of(user));
        when(repo.findByEmail("mail@test.com")).thenReturn(Optional.of(other));

        assertThrows(EmailAlreadyExistsException.class,
                () -> service.updateProfile("id", "mail@test.com", null));
    }

    @Test
    void changePassword_success() {
        User user = new User();
        user.setPassword("oldHash");

        when(repo.findById("id")).thenReturn(Optional.of(user));
        when(encoder.matches("old", "oldHash")).thenReturn(true);
        when(encoder.encode("new")).thenReturn("newHash");

        service.changePassword("id", "old", "new");

        verify(repo).save(user);
        verify(refresh).deleteTokensForUser("id");
    }

    @Test
    void changePassword_wrongOld() {
        User user = new User();
        user.setPassword("oldHash");

        when(repo.findById("id")).thenReturn(Optional.of(user));
        when(encoder.matches("wrong", "oldHash")).thenReturn(false);

        assertThrows(InvalidPasswordException.class,
                () -> service.changePassword("id", "wrong", "new"));
    }

    @Test
    void delete_success() {
        when(repo.existsById("id")).thenReturn(true);

        service.deleteById("id");

        verify(repo).deleteById("id");
    }

    @Test
    void delete_notFound() {
        when(repo.existsById("id")).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                () -> service.deleteById("id"));
    }
}