package bootcamp.petclinic.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDateTime;

@Data
@DynamoDbBean
public class Visit {

    private String visitId;
    private String petId;
    private String userId;
    private LocalDateTime visitDateTime;
    private String reason;

    @DynamoDbPartitionKey
    public String getVisitId() {
        return visitId;
    }
}