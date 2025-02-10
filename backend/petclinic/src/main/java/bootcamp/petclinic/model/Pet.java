package bootcamp.petclinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    private String petId;
    private String petName;
    private String species;
    private String breed;
    private String gender;
    private int age;
    private double weight;
    private String userId;

    @DynamoDbPartitionKey
    public String getPetId() {
        return petId;
    }

}
