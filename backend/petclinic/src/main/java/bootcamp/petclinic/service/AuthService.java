package bootcamp.petclinic.service;

import bootcamp.petclinic.dto.login.LoginRequestDTO;
import bootcamp.petclinic.dto.login.LoginResponseDTO;
import bootcamp.petclinic.dto.register.RegisterRequestDTO;
import bootcamp.petclinic.dto.register.RegisterResponseDTO;
import bootcamp.petclinic.enums.Roles;
import bootcamp.petclinic.exceptions.UserAlreadyExistsException;
import bootcamp.petclinic.exceptions.UserAlreadyLoggedInException;
import bootcamp.petclinic.model.User;
import bootcamp.petclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final Argon2PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;


    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) throws UserAlreadyExistsException {
        if (userRepository.existsUserByEmail(registerRequestDTO.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists!");
        }
        if (userRepository.existsUserByUsername(registerRequestDTO.getUsername())) {
            throw new UserAlreadyExistsException("This username already exists!");
        }

        User user = User.builder()
                .username(registerRequestDTO.getUsername())
                .email(registerRequestDTO.getEmail())
                .firstname(registerRequestDTO.getFirstname())
                .lastname(registerRequestDTO.getLastname())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .role(Roles.ROLE_USER)
                .build();
        userRepository.save(user);

        return new RegisterResponseDTO(user.getId(), "User registered successfully!");
    }


    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));

        User user = userRepository.findUserByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var validUserTokens = tokenService.getAllValidUserTokens(user);
        if (!validUserTokens.isEmpty()) {
            throw new UserAlreadyLoggedInException("You are already logged in");
        }

        tokenService.deleteAllUserTokens(user);
        String jwtToken = jwtService.generateToken(user);
        tokenService.saveUserToken(user, jwtToken);

        return LoginResponseDTO.builder()
                .token(jwtToken)
                .message("User logged in successfully")
                .build();
    }

    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findUserByUsername(username);
        }
        return Optional.empty();
    }
}
