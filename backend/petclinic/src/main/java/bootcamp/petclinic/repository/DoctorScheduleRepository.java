package bootcamp.petclinic.repository;

import bootcamp.petclinic.model.DoctorSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DoctorScheduleRepository {

    private final DynamoDbClient dynamoDbClient;
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    private final DynamoDbTable<DoctorSchedule> doctorScheduleTable;

    public DoctorScheduleRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
        this.dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        this.doctorScheduleTable = dynamoDbEnhancedClient.table("DoctorSchedule", TableSchema.fromBean(DoctorSchedule.class));
    }

    public void save(DoctorSchedule doctorSchedule) {
        doctorScheduleTable.putItem(doctorSchedule);
    }

    public Optional<DoctorSchedule> findById(String doctorId) {
        return Optional.ofNullable(doctorScheduleTable.getItem(r -> r.key(k -> k.partitionValue(doctorId))));
    }

    public void deleteById(String doctorId) {
        doctorScheduleTable.deleteItem(r -> r.key(k -> k.partitionValue(doctorId)));
    }
}
