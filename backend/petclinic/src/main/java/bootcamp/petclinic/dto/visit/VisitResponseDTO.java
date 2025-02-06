package bootcamp.petclinic.dto.visit;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VisitResponseDTO {
    private String visitId;
    private String petId;
    private LocalDateTime visitDateTime;
    private String reason;
    private String examinationResults;
    private String performedTests;
    private String diagnosis;
    private String prescribedTreatment;
}