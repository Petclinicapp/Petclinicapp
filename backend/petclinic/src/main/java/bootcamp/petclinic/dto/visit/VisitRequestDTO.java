package bootcamp.petclinic.dto.visit;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VisitRequestDTO {
    private String petId;
    private LocalDateTime visitDateTime;
    private String reason;
}