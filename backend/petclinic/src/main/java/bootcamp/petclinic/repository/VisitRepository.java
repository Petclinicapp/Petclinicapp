package bootcamp.petclinic.repository;

import bootcamp.petclinic.model.Visit;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

@Repository
public class VisitRepository {

    private final DynamoDbTable<Visit> visitTable;

    public VisitRepository(DynamoDbEnhancedClient enhancedClient) {
        this.visitTable = enhancedClient.table("Visits", TableSchema.fromBean(Visit.class));
    }

    public void save(Visit visit) {
        visitTable.putItem(visit);
    }

    public Optional<Visit> findById(String visitId) {
        return Optional.ofNullable(visitTable.getItem(r -> r.key(k -> k.partitionValue(visitId))));
    }
}