package bootcamp.petclinic.repository;

import bootcamp.petclinic.model.VisitDetails;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.Optional;

@Repository
public class VisitDetailsRepository {

    private final DynamoDbTable<VisitDetails> visitDetailsTable;

    public VisitDetailsRepository(DynamoDbEnhancedClient enhancedClient) {
        this.visitDetailsTable = enhancedClient.table("VisitDetails",
                software.amazon.awssdk.enhanced.dynamodb.TableSchema.fromBean(VisitDetails.class));
    }

    public void save(VisitDetails visitDetails) {
        visitDetailsTable.putItem(visitDetails);
    }

    public Optional<VisitDetails> findById(String visitDetailsId) {
        VisitDetails visitDetails = visitDetailsTable.getItem(r -> r.key(k -> k.partitionValue(visitDetailsId)));
        return Optional.ofNullable(visitDetails);
    }

    public void deleteById(String visitDetailsId) {
        visitDetailsTable.deleteItem(r -> r.key(k -> k.partitionValue(visitDetailsId)));
    }
}
