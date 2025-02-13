package bootcamp.petclinic.repository;

import bootcamp.petclinic.model.DoctorSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

@Repository
public class DoctorScheduleRepository {
    private final DynamoDbTable<DoctorSchedule> doctorScheduleTable;

    public DoctorScheduleRepository(DynamoDbEnhancedClient enhancedClient) {
        this.doctorScheduleTable = enhancedClient.table("DoctorSchedule", TableSchema.fromBean(DoctorSchedule.class));
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
