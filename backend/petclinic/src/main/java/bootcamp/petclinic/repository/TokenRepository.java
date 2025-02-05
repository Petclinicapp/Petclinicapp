package bootcamp.petclinic.repository;

import bootcamp.petclinic.model.Token;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository {
    List<Token> findAllValidTokensByUser(String userId);
    Optional<Token> findByToken(String token);
    List<Token> findAllByUserId(String userId);
    void save(Token token);
    void saveAll(List<Token> validUserTokens);
    void deleteAll(List<Token> allUserTokens);
    void delete(Token storedToken);
    List<Token> findAllValidTokensByUserId(String userId);
}
