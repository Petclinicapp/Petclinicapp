package bootcamp.petclinic.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import java.util.List;

@Data
@DynamoDbBean
public class DoctorSchedule {
    private String doctorId;
    private List<Availability> availableSlots;
}