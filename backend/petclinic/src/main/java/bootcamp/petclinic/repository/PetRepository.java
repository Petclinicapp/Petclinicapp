package bootcamp.petclinic.repository;

import bootcamp.petclinic.model.Pet;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

@Repository
public class PetRepository {

    private final DynamoDbTable<Pet> petTable;

    public PetRepository(DynamoDbEnhancedClient enhancedClient) {
        this.petTable = enhancedClient.table("Pets", TableSchema.fromBean(Pet.class));
    }

    public void save(Pet pet) {
        petTable.putItem(pet);
    }

    public Optional<Pet> findById(String petId) {
        return Optional.ofNullable(petTable.getItem(r -> r.key(k -> k.partitionValue(petId))));
    }

    public void deleteById(String petId) {
        petTable.deleteItem(r -> r.key(k -> k.partitionValue(petId)));
    }
}
