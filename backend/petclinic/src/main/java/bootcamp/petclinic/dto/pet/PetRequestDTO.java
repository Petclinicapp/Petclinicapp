package bootcamp.petclinic.dto.pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetRequestDTO {
    @NotBlank(message = "Pet name is required")
    private String petName;

    @NotBlank(message = "Species is required")
    private String species;

    private String breed;
    private String gender;

    @Positive(message = "Age must be positive")
    private int age;

    @Positive(message = "Weight must be positive")
    private double weight;

    private String userId;
}
