package bootcamp.petclinic.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetResponseDTO {
    private String petId;
    private String petName;
    private String species;
    private String breed;
    private String gender;
    private int age;
    private double weight;
    private String userId;
}
