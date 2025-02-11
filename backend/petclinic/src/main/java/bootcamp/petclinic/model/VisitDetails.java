package bootcamp.petclinic.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@DynamoDbBean
public class VisitDetails {

    private String visitDetailsId;
    private String examinationResults;
    private String performedTests;
    private String diagnosis;
    private String prescribedTreatment;

    @DynamoDbPartitionKey
    public String getVisitDetailsId() {
        return visitDetailsId;
    }
}
