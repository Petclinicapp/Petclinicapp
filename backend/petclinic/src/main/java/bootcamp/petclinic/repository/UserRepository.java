package bootcamp.petclinic.repository;

import bootcamp.petclinic.model.User;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

@Repository
public class UserRepository {

    private final DynamoDbTable<User> userTable;

    public UserRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.userTable = dynamoDbEnhancedClient.table("Users", TableSchema.fromBean(User.class));
    }

    public void save(User user) {
        userTable.putItem(user);
    }

    public Optional<User> findUserById(String userId) {
        return Optional.ofNullable(userTable.getItem(r -> r.key(k -> k.partitionValue(userId))));
    }

    public Optional<User> findUserByUsername(String username) {
        return userTable.scan()
                .items()
                .stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst();
    }

    public Optional<User> findUserByEmail(String email) {
        return userTable.scan()
                .items()
                .stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }

    public boolean existsUserByUsername(String username) {
        return findUserByUsername(username).isPresent();
    }

    public boolean existsUserByEmail(String email) {
        return findUserByEmail(email).isPresent();
    }

    public void deleteUserById(String userId) {
        userTable.deleteItem(r -> r.key(k -> k.partitionValue(userId)));
    }
}