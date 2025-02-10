package bootcamp.petclinic.repository;

import bootcamp.petclinic.model.Pet;
import bootcamp.petclinic.model.Visit;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PetRepository {

    private final DynamoDbTable<Pet> petTable;
    private final DynamoDbTable<Visit> visitTable;

    public PetRepository(DynamoDbEnhancedClient enhancedClient) {
        this.petTable = enhancedClient.table("Pets", TableSchema.fromBean(Pet.class));
        this.visitTable = enhancedClient.table("Visits", TableSchema.fromBean(Visit.class));
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

    public List<Pet> findPetsByUserId(String userId) {
        return petTable.scan()
                .items()
                .stream()
                .filter(pet -> userId.equals(pet.getUserId()))
                .collect(Collectors.toList());
    }
}
