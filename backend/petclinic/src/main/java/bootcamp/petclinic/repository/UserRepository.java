package bootcamp.petclinic.repository;

import bootcamp.petclinic.model.User;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {

    private final DynamoDbTable<User> userTable;

    public UserRepository(DynamoDbEnhancedClient enhancedClient) {
        this.userTable = enhancedClient.table("Users", TableSchema.fromBean(User.class));
    }

    public void save(User user) {
        userTable.putItem(user);
    }

    public Optional<User> findUserByUsername(String username) {
        return userTable.scan()
                .items()
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public Optional<User> findUserByEmail(String email) {
        return userTable.scan()
                .items()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public Optional<User> findUserById(UUID uuid) {
        return Optional.ofNullable(userTable.getItem(r -> r.key(k -> k.partitionValue(uuid.toString()))));
    }

    public boolean existsUserByEmail(String email) {
        return findUserByEmail(email).isPresent();
    }

    public boolean existsUserByUsername(String username) {
        return findUserByUsername(username).isPresent();
    }

    public boolean existsByAccountNumber(String accountNumber) {
        return userTable.scan()
                .items()
                .stream()
                .anyMatch(user -> accountNumber.equals(user.getUsername()));
    }
}
