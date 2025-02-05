package bootcamp.petclinic.service;

import bootcamp.petclinic.dto.login.LoginRequestDTO;
import bootcamp.petclinic.dto.login.LoginResponseDTO;
import bootcamp.petclinic.dto.register.RegisterRequestDTO;
import bootcamp.petclinic.exceptions.UserAlreadyLoggedInException;
import bootcamp.petclinic.model.User;
import bootcamp.petclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;

    public String register(RegisterRequestDTO request) {

        Optional<User> existingUser = userRepository.findUserByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username already registered.");
        }

        Optional<User> existingEmail = userRepository.findUserByEmail(request.getEmail());
        if (existingEmail.isPresent()) {
            throw new RuntimeException("Email already in use.");
        }

        User user = User.builder()
                .userId(UUID.randomUUID().toString())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        return "Registration successful";
    }



    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
        );

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

        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
            return userRepository.findUserByUsername(username);
        }
        return Optional.empty();
    }
}
