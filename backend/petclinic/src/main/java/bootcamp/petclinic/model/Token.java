package bootcamp.petclinic.model;

import bootcamp.petclinic.enums.TokenType;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class Token {

    private String tokenId;
    private String token;
    private TokenType tokenType;
    private boolean isExpired;
    private boolean isRevoked;
    private String userId;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("tokenId")
    public String getTokenId() {
        return tokenId;
    }

    @DynamoDbAttribute("token")
    public String getToken() {
        return token;
    }

    @DynamoDbAttribute("tokenType")
    public TokenType getTokenType() {
        return tokenType;
    }

    @DynamoDbAttribute("isExpired")
    public boolean isExpired() {
        return isExpired;
    }
    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    @DynamoDbAttribute("isRevoked")
    public boolean isRevoked() {
        return isRevoked;
    }
    public void setRevoked(boolean revoked) {
        isRevoked = revoked;
    }

    @DynamoDbAttribute("userId")
    public String getUserId() {
        return userId;
    }

}
