package bootcamp.petclinic.dto.visit;

import bootcamp.petclinic.model.VisitDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VisitResponseDTO {
    private String visitId;
    private String petId;
    private String userId;
    private LocalDateTime visitDateTime;
    private String reason;
    private VisitDetails visitDetails;
}
