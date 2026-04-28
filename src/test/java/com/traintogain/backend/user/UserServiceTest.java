package com.traintogain.backend.user;

import com.traintogain.backend.auth.refreshtoken.RefreshTokenService;
import com.traintogain.backend.exception.*;
import com.traintogain.backend.mail.MailService;
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

    @Mock
    private MailService mailService;

    @InjectMocks
    private UserService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_success() {
        when(repo.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        when(encoder.encode("Password1")).thenReturn("hashed");
        when(repo.save(any())).thenAnswer(i -> i.getArguments()[0]);
        doNothing().when(mailService).sendVerificationEmail(anyString(), anyString());

        User user = service.register("TEST@mail.com", "user", "Password1");

        assertEquals("test@mail.com", user.getEmail());
        assertEquals("hashed", user.getPassword());
        assertFalse(user.isEnabled());
        assertNotNull(user.getVerificationToken());
    }

    @Test
    void register_emailExists() {
        when(repo.findByEmail("test@mail.com")).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistsException.class,
                () -> service.register("test@mail.com", "user", "Password1"));
    }

    @Test
    void login_success() {
        User user = new User();
        user.setPassword("hashed");
        user.setEnabled(true);

        when(repo.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(encoder.matches("Password1", "hashed")).thenReturn(true);

        User result = service.login("TEST@mail.com", "Password1");

        assertNotNull(result);
    }

    @Test
    void login_notEnabled() {
        User user = new User();
        user.setPassword("hashed");
        user.setEnabled(false);

        when(repo.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialsException.class,
                () -> service.login("test@mail.com", "Password1"));
    }

    @Test
    void login_wrongPassword() {
        User user = new User();
        user.setPassword("hashed");
        user.setEnabled(true);

        when(repo.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(encoder.matches("WrongPass1", "hashed")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> service.login("test@mail.com", "WrongPass1"));
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

        User result = service.updateProfile("id", "new@mail.com", "newUser");

        assertEquals("new@mail.com", result.getEmail());
        assertEquals("newUser", result.getUsername());
    }

    @Test
    void updateProfile_emailTaken() {
        User user = new User();
        user.setEmail("old@mail.com");

        User other = new User();
        other.setEmail("other@mail.com");
        other.setId("otherId");

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
        when(encoder.matches("OldPass1", "oldHash")).thenReturn(true);
        when(encoder.encode("NewPass1")).thenReturn("newHash");

        service.changePassword("id", "OldPass1", "NewPass1");

        verify(repo).save(user);
        verify(refresh).deleteTokensForUser("id");
    }

    @Test
    void changePassword_wrongOld() {
        User user = new User();
        user.setPassword("oldHash");

        when(repo.findById("id")).thenReturn(Optional.of(user));
        when(encoder.matches("WrongOld1", "oldHash")).thenReturn(false);

        assertThrows(InvalidPasswordException.class,
                () -> service.changePassword("id", "WrongOld1", "NewPass1"));
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