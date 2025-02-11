package bootcamp.petclinic.dto.visit;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VisitRequestDTO {
    private String visitId;
    private String petId;
    private String userId;
    private LocalDateTime visitDateTime;
    private String reason;

}