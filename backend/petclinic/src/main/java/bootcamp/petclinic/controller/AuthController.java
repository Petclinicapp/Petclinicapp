package bootcamp.petclinic.controller;

import bootcamp.petclinic.dto.login.LoginRequestDTO;
import bootcamp.petclinic.dto.login.LoginResponseDTO;
import bootcamp.petclinic.dto.register.RegisterRequestDTO;
import bootcamp.petclinic.dto.register.RegisterResponseDTO;
import bootcamp.petclinic.exceptions.UserAlreadyExistsException;
import bootcamp.petclinic.exceptions.UserAlreadyLoggedInException;
import bootcamp.petclinic.exceptions.UserNotFoundException;
import bootcamp.petclinic.exceptions.UsernameOrPasswordInvalidException;
import bootcamp.petclinic.repository.UserRepository;
import bootcamp.petclinic.service.AuthService;
import bootcamp.petclinic.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            RegisterResponseDTO response = authService.register(registerRequestDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new RegisterResponseDTO(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponseDTO(e.getMessage()));
        }
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
