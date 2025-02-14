package bootcamp.petclinic.service;

import bootcamp.petclinic.model.User;
import bootcamp.petclinic.enums.Roles;
import bootcamp.petclinic.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private final String secretKey = "YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXoxMjM0NTY=";
    private final long jwtExpiration = 3600000L;      // 1 valanda
    private final long refreshExpiration = 7200000L;  // 2 valandos

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", jwtExpiration);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", refreshExpiration);
    }

    @Test
    void generateToken_WhenUserIsValid_ShouldGenerateTokenWithClaims() {
        User user = new User();
        user.setUserId("user-123");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setRole(Roles.ROLE_USER); // naudojame Roles enum
        user.setFirstname("Test");
        user.setLastname("User");

        String token = jwtService.generateToken(user);
        assertNotNull(token);

        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);

        Claims claims = jwtService.extractAllClaims(token);
        assertEquals("user-123", claims.get("id", String.class));
        assertEquals("testuser", claims.get("username", String.class));
        assertEquals("test@example.com", claims.get("email", String.class));
        assertEquals(Roles.ROLE_USER.name(), claims.get("role", String.class));
        assertEquals("Test", claims.get("firstname", String.class));
        assertEquals("User", claims.get("lastname", String.class));
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        User user = new User();
        user.setUserId("user-123");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setRole(Roles.ROLE_USER);
        user.setFirstname("Test");
        user.setLastname("User");

        String token = jwtService.generateToken(user);
        String extractedUsername = jwtService.extractUsername(token);
        assertEquals("testuser", extractedUsername);
    }

    @Test
    void generateRefreshToken_ShouldGenerateTokenWithSubject() {
        User user = new User();
        user.setUserId("user-123");
        user.setUsername("refreshUser");
        user.setEmail("refresh@example.com");
        user.setRole(Roles.ROLE_ADMIN);
        user.setFirstname("Refresh");
        user.setLastname("User");

        UserDetails userDetails = new CustomUserDetails(user);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        assertNotNull(refreshToken);

        String subject = jwtService.extractUsername(refreshToken);
        assertEquals("refreshUser", subject);
    }

    @Test
    void isTokenValid_WhenTokenIsValid_ShouldReturnTrue() {
        User user = new User();
        user.setUserId("user-123");
        user.setUsername("validUser");
        user.setEmail("valid@example.com");
        user.setRole(Roles.ROLE_USER);
        user.setFirstname("Valid");
        user.setLastname("User");

        String token = jwtService.generateToken(user);
        UserDetails userDetails = new CustomUserDetails(user);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenValid_WhenTokenUsernameMismatch_ShouldReturnFalse() {
        User user = new User();
        user.setUserId("user-123");
        user.setUsername("userOne");
        user.setEmail("one@example.com");
        user.setRole(Roles.ROLE_USER);
        user.setFirstname("User");
        user.setLastname("One");

        String token = jwtService.generateToken(user);

        User anotherUser = new User();
        anotherUser.setUserId("user-456");
        anotherUser.setUsername("userTwo");
        anotherUser.setEmail("two@example.com");
        anotherUser.setRole(Roles.ROLE_USER);
        anotherUser.setFirstname("User");
        anotherUser.setLastname("Two");

        UserDetails anotherUserDetails = new CustomUserDetails(anotherUser);
        assertFalse(jwtService.isTokenValid(token, anotherUserDetails));
    }

    @Test
    void isTokenExpired_WhenTokenIsExpired_ShouldThrowExpiredJwtException() {
        User user = new User();
        user.setUserId("user-123");
        user.setUsername("expiredUser");
        user.setEmail("expired@example.com");
        user.setRole(Roles.ROLE_USER);
        user.setFirstname("Expired");
        user.setLastname("User");

        CustomUserDetails userDetails = new CustomUserDetails(user);

        String expiredToken = jwtService.buildToken(new HashMap<>(), userDetails, -1000);

        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenExpired(expiredToken));
    }

    @Test
    void extractExpiration_ShouldReturnFutureDateForValidToken() {
        User user = new User();
        user.setUserId("user-123");
        user.setUsername("futureUser");
        user.setEmail("future@example.com");
        user.setRole(Roles.ROLE_USER);
        user.setFirstname("Future");
        user.setLastname("User");

        String token = jwtService.generateToken(user);
        Date expirationDate = jwtService.extractExpiration(token);
        assertTrue(expirationDate.after(new Date()));
    }

    @Test
    void extractClaim_ShouldReturnCorrectClaimValue() {
        User user = new User();
        user.setUserId("user-123");
        user.setUsername("claimUser");
        user.setEmail("claim@example.com");
        user.setRole(Roles.ROLE_USER);
        user.setFirstname("Claim");
        user.setLastname("User");

        String token = jwtService.generateToken(user);
        String email = jwtService.extractClaim(token, claims -> claims.get("email", String.class));
        assertEquals("claim@example.com", email);
    }

    @Test
    void getSignInKey_ShouldReturnNonNullKey() {
        Key key = jwtService.getSignInKey();
        assertNotNull(key);
    }
}
