package bootcamp.petclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
public class Availability {
    private String doctorId;
    private LocalDate availableDate;
    private LocalTime availableTime;
    private boolean isBooked;
}