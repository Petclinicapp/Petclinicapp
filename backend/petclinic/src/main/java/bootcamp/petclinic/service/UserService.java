package bootcamp.petclinic.service;

import bootcamp.petclinic.model.User;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

@Repository
public class UserService {

    private final DynamoDbTable<User> userTable;

    public UserService(DynamoDbEnhancedClient enhancedClient) {
        this.userTable = enhancedClient.table("Users", TableSchema.fromBean(User.class));
    }

    public void save(User user) {
        userTable.putItem(user);
    }

    public Optional<User> findByUsername(String username) {
        return userTable.scan()
                .items()
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return userTable.scan()
                .items()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}

