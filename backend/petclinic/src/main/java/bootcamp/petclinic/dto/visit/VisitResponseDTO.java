package bootcamp.petclinic.dto.visit;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VisitResponseDTO {
    private String petId;
    private String userId;
    private LocalDateTime visitDateTime;
    private String reason;
}