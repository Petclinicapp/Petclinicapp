package bootcamp.petclinic.controller;

import bootcamp.petclinic.dto.login.LoginRequestDTO;
import bootcamp.petclinic.dto.login.LoginResponseDTO;
import bootcamp.petclinic.exceptions.UserAlreadyLoggedInException;
import bootcamp.petclinic.exceptions.UserNotFoundException;
import bootcamp.petclinic.exceptions.UsernameOrPasswordInvalidException;
import bootcamp.petclinic.model.User;
import bootcamp.petclinic.service.AuthService;
import bootcamp.petclinic.service.JwtService;
import bootcamp.petclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findUserByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username is already registered.");
        }

        Optional<User> existingEmail = userRepository.findUserByEmail(user.getEmail());
        if (existingEmail.isPresent()) {
            throw new RuntimeException("Email is already in use.");
        }

        user.setUserId(UUID.randomUUID().toString());
        userRepository.save(user);

        return "Registration successful!";
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            LoginResponseDTO loggedUser = authService.login(loginRequestDTO);
            return ResponseEntity.ok(loggedUser);
        } catch (UserAlreadyLoggedInException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResponseDTO(e.getMessage()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponseDTO(e.getMessage()));
        } catch (UsernameOrPasswordInvalidException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponseDTO(e.getMessage()));
        }
    }
}
