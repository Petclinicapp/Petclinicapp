package bootcamp.petclinic.model;

import bootcamp.petclinic.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Set;

@DynamoDbBean
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String userId;
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
    private Roles role;
    private Set<Token> tokens;

    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }

    @DynamoDbIgnore
    public Set<Token> getTokens() {
        return tokens;
    }
}
