package bootcamp.petclinic.dto.visit;

import bootcamp.petclinic.enums.VisitStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitUpdateResponseDTO {

    private VisitStatus status;
    private String message;

    public VisitUpdateResponseDTO(String message) {
        this.message = message;
    }
}
