package bootcamp.petclinic.model;

import bootcamp.petclinic.enums.VisitStatus;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@DynamoDbBean
public class Visit {

    private String visitId;
    private String petId;
    private String userId;
    private String doctorId;
    private LocalDate visitDate;
    private LocalTime visitTime;
    private String reason;
    private VisitStatus status;

    private String examinationResults;
    private String performedTests;
    private String diagnosis;
    private String prescribedTreatment;

    @DynamoDbPartitionKey
    public String getVisitId() {
        return visitId;
    }
}
