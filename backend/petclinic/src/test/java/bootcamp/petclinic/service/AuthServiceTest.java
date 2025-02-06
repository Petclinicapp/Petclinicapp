package bootcamp.petclinic.service;

import bootcamp.petclinic.dto.login.LoginRequestDTO;
import bootcamp.petclinic.dto.login.LoginResponseDTO;
import bootcamp.petclinic.dto.register.RegisterRequestDTO;
import bootcamp.petclinic.dto.register.RegisterResponseDTO;
import bootcamp.petclinic.enums.Roles;
import bootcamp.petclinic.enums.TokenType;
import bootcamp.petclinic.exceptions.UserAlreadyExistsException;
import bootcamp.petclinic.exceptions.UserAlreadyLoggedInException;
import bootcamp.petclinic.model.Token;
import bootcamp.petclinic.model.User;
import bootcamp.petclinic.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequestDTO registerRequestDTO;
    private LoginRequestDTO loginRequestDTO;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registerRequestDTO = RegisterRequestDTO.builder()
                .username("testUser")
                .email("test@example.com")
                .firstname("Test")
                .lastname("User")
                .password("#TestTest123")
                .build();

        loginRequestDTO = LoginRequestDTO.builder()
                .username("testUser")
                .password("#TestTest123")
                .build();

        user = User.builder()
                .userId("test-uuid")
                .username("testUser")
                .email("test@example.com")
                .firstname("Test")
                .lastname("User")
                .password("encodedPassword")
                .role(Roles.ROLE_USER)
                .build();
    }


    @Test
    @DisplayName("register_WhenUserDoesNotExist_ShouldRegisterSuccessfully")
    void registerWhenUserDoesNotExistShouldRegisterSuccessfully() throws UserAlreadyExistsException {

        when(userRepository.existsUserByEmail(registerRequestDTO.getEmail())).thenReturn(false);
        when(userRepository.existsUserByUsername(registerRequestDTO.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(registerRequestDTO.getPassword())).thenReturn("encodedPassword");

        RegisterResponseDTO response = authService.register(registerRequestDTO);

        assertNotNull(response);
        assertEquals("User registered successfully!", response.getMessage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("resister_WhenEmailExists_ShouldThrowException")
    void resisterWhenEmailExistsShouldThrowException() {

        when(userRepository.existsUserByEmail(registerRequestDTO.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(registerRequestDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("register_WhenUsernameExists_ShouldThrowException")
    void registerWhenUsernameExistsShouldThrowException() {

        when(userRepository.existsUserByEmail(registerRequestDTO.getEmail())).thenReturn(false);
        when(userRepository.existsUserByUsername(registerRequestDTO.getUsername())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(registerRequestDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("login_WhenValidCredentials_ShouldLoginSuccessfully")
    void loginWhenValidCredentialsShouldLoginSuccessfully() {

        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(tokenService.getAllValidUserTokens(any(User.class))).thenReturn(new ArrayList<>());
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        LoginResponseDTO response = authService.login(loginRequestDTO);

        assertNotNull(response);
        assertEquals("User logged in successfully", response.getMessage());
        assertEquals("jwt-token", response.getToken());
        verify(tokenService).saveUserToken(any(User.class), anyString());
    }

    @Test
    @DisplayName("login_WhenUserAlreadyLoggedIn_ShouldThrowException")
    void loginWhenUserAlreadyLoggedInShouldThrowException() {

        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

        List<Token> tokens = List.of(
                Token.builder()
                        .tokenId(UUID.randomUUID().toString())
                        .userId(user.getUserId())
                        .token("existing-token")
                        .tokenType(TokenType.BEARER)
                        .isExpired(false)
                        .isRevoked(false)
                        .build()
        );

        when(tokenService.getAllValidUserTokens(any(User.class))).thenReturn(tokens);
        assertThrows(UserAlreadyLoggedInException.class, () -> authService.login(loginRequestDTO));
        verify(tokenService, never()).saveUserToken(any(User.class), anyString());
    }


    @Test
    @DisplayName("login_whenUserNotFound_ShouldThrowException")
    void loginWhenUserNotFoundShouldThrowException() {

        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authService.login(loginRequestDTO));
    }

    @Test
    @DisplayName("getCurrentUser_WhenUserIsAuthenticated_ShouldReturnUser")
    void getCurrentUserWhenUserIsAuthenticatedShouldReturnUser() {

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findUserByUsername("testUser")).thenReturn(Optional.of(user));

        Optional<User> result = authService.getCurrentUser();

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("getCurrentUser_WhenUserNotAuthenticated_ShouldReturnEmpty")
    void getCurrentUserWhenUserNotAuthenticatedShouldReturnEmpty() {

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        Optional<User> result = authService.getCurrentUser();

        assertTrue(result.isEmpty());
    }
}