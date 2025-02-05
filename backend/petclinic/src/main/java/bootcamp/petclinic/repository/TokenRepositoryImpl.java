package bootcamp.petclinic.repository;

import bootcamp.petclinic.model.Token;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TokenRepositoryImpl implements TokenRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public TokenRepositoryImpl(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }

    private DynamoDbTable<Token> getTokenTable() {
        return dynamoDbEnhancedClient.table("Tokens", TableSchema.fromBean(Token.class));
    }

    @Override
    public List<Token> findAllValidTokensByUser(String userId) {
        return getTokenTable().scan()
                .items()
                .stream()
                .filter(token -> token.getUserId().equals(userId) && !token.isExpired() && !token.isRevoked())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Token> findByToken(String tokenValue) {
        return getTokenTable().scan()
                .items()
                .stream()
                .filter(token -> token.getToken().equals(tokenValue))
                .findFirst();
    }

    @Override
    public List<Token> findAllByUserId(String userId) {
        return getTokenTable().scan()
                .items()
                .stream()
                .filter(token -> token.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void save(Token token) {
        getTokenTable().putItem(token);
    }

    @Override
    public void saveAll(List<Token> tokens) {
        tokens.forEach(this::save);
    }

    @Override
    public void deleteAll(List<Token> tokens) {
        tokens.forEach(token -> getTokenTable().deleteItem(token));
    }

    @Override
    public void delete(Token storedToken) {
        getTokenTable().deleteItem(storedToken);
    }

    @Override
    public List<Token> findAllValidTokensByUserId(String userId) {
        return getTokenTable().scan()
                .items()
                .stream()
                .filter(token -> token.getUserId().equals(userId) && !token.isExpired() && !token.isRevoked())
                .collect(Collectors.toList());
    }
}
