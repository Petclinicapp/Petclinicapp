package bootcamp.petclinic.service;

import bootcamp.petclinic.enums.TokenType;
import bootcamp.petclinic.model.Token;
import bootcamp.petclinic.model.User;
import bootcamp.petclinic.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {


    private final TokenRepository tokenRepository;
    private final HttpServletRequest request;


    public void saveUserToken(User user, String jwtToken) {
        String tokenId = UUID.randomUUID().toString();

        Token token = Token.builder()
                .tokenId(tokenId)
                .userId(user.getUserId())
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .isExpired(false)
                .isRevoked(false)
                .build();

        tokenRepository.save(token);
    }


    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getUserId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void deleteAllUserTokens(User user) {
        var allUserTokens = tokenRepository.findAllByUserId(user.getUserId());
        if (!allUserTokens.isEmpty()) {
            tokenRepository.deleteAll(allUserTokens);
        }
    }

    public List<Token> getAllValidUserTokens(User user) {
        return tokenRepository.findAllValidTokensByUserId(user.getUserId());
    }

    public String getCurrentToken() {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        } else {
            throw new IllegalArgumentException("No JWT token found in the request headers");
        }
    }

    public void clearToken() {
        SecurityContextHolder.clearContext();
    }
}
