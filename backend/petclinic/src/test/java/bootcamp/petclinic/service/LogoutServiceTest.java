package bootcamp.petclinic.service;

import bootcamp.petclinic.model.Token;
import bootcamp.petclinic.repository.TokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private LogoutService logoutService;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private Token token;
    private static final String VALID_JWT = "valid.jwt.token";

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        token = Token.builder()
                .tokenId("test-token-id")
                .token(VALID_JWT)
                .isExpired(false)
                .isRevoked(false)
                .build();
    }

    @Test
    @DisplayName("logout_WithValidToken_ShouldLogoutSuccessfully")
    void logoutWithValidTokenShouldLogoutSuccessfully() throws UnsupportedEncodingException {

        request.addHeader("Authorization", "Bearer " + VALID_JWT);
        when(tokenRepository.findByToken(VALID_JWT)).thenReturn(Optional.of(token));

        logoutService.logout(request, response, authentication);

        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertEquals("Logout successful", response.getContentAsString());
        verify(tokenRepository).delete(token);
        assertTrue(token.isExpired());
        assertTrue(token.isRevoked());
    }

    @Test
    @DisplayName("logout_WithNoAuthHeader_ShouldReturnBadRequest")
    void logoutWithNoAuthHeaderShouldReturnBadRequest() throws UnsupportedEncodingException {

        logoutService.logout(request, response, authentication);

        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        assertEquals("No JWT token found in the request headers", response.getContentAsString());
        verify(tokenRepository, never()).delete(any());
    }

    @Test
    @DisplayName("logout_WithInvalidAuthHeaderFormat_ShouldReturnBadRequest")
    void logoutWithInvalidAuthHeaderFormatShouldReturnBadRequest() throws UnsupportedEncodingException {

        request.addHeader("Authorization", "Invalid Format");

        logoutService.logout(request, response, authentication);

        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        assertEquals("No JWT token found in the request headers", response.getContentAsString());
        verify(tokenRepository, never()).delete(any());
    }

    @Test
    @DisplayName("logout_WithNonExistentToken_ShouldReturnBadRequest")
    void logoutWithNonExistentTokenShouldReturnBadRequest() throws UnsupportedEncodingException {

        request.addHeader("Authorization", "Bearer " + VALID_JWT);
        when(tokenRepository.findByToken(VALID_JWT)).thenReturn(Optional.empty());

        logoutService.logout(request, response, authentication);

        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        assertEquals("Invalid JWT token", response.getContentAsString());
        verify(tokenRepository, never()).delete(any());
    }

    @Test
    @DisplayName("logout_WithIOException_ShouldThrowRuntimeException")
    void logoutWithIoExceptionShouldThrowRuntimeException() throws IOException {

        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockResponse.getWriter()).thenThrow(new IOException("Test IO Exception"));
        request.addHeader("Authorization", "Bearer " + VALID_JWT);
        when(tokenRepository.findByToken(VALID_JWT)).thenReturn(Optional.of(token));

        assertThrows(RuntimeException.class, () -> logoutService.logout(request, mockResponse, authentication));
    }
}