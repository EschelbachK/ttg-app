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

    // ================= REGISTER =================

    @Test
    void register_success() {
        when(repo.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        when(encoder.encode("123456")).thenReturn("hashed");
        when(repo.save(any())).thenAnswer(i -> i.getArguments()[0]);

        User user = service.register("TEST@mail.com", "user", "123456");

        assertEquals("test@mail.com", user.getEmail());
        assertEquals("hashed", user.getPassword());
    }

    @Test
    void register_emailExists() {
        when(repo.findByEmail("test@mail.com")).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistsException.class,
                () -> service.register("test@mail.com", "user", "123456"));
    }

    // ================= LOGIN =================

    @Test
    void login_success() {
        User user = new User();
        user.setPassword("hashed");

        when(repo.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(encoder.matches("123456", "hashed")).thenReturn(true);

        User result = service.login("TEST@mail.com", "123456");

        assertNotNull(result);
    }

    @Test
    void login_userNotFound() {
        when(repo.findByEmail("test@mail.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> service.login("test@mail.com", "123456"));
    }

    @Test
    void login_wrongPassword() {
        User user = new User();
        user.setPassword("hashed");

        when(repo.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(encoder.matches("wrong", "hashed")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> service.login("test@mail.com", "wrong"));
    }

    // ================= GET =================

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

    // ================= UPDATE =================

    @Test
    void updateProfile_success() {
        User user = mock(User.class);
        when(user.getId()).thenReturn("id");

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
        User other = new User();

        when(user.getId()).thenReturn("id");
        when(other.getId()).thenReturn("other");

        when(repo.findById("id")).thenReturn(Optional.of(user));
        when(repo.findByEmail("mail@test.com")).thenReturn(Optional.of(other));

        assertThrows(EmailAlreadyExistsException.class,
                () -> service.updateProfile("id", "mail@test.com", null));
    }

    // ================= PASSWORD =================

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

    // ================= DELETE =================

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