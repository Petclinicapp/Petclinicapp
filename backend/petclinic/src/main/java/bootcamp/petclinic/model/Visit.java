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
    private LocalDateTime visitDateTime;
    private String reason;
    private String examinationResults;
    private String performedTests;
    private String diagnosis;
    private String prescribedTreatment;

    @DynamoDbPartitionKey
    public String getVisitId() {
        return visitId;
    }
}