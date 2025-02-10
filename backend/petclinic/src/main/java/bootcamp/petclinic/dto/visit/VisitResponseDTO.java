package bootcamp.petclinic.dto.visit;

import bootcamp.petclinic.enums.VisitStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class VisitResponseDTO {
    private String visitId;
    private String petId;
    private String userId;
    private String doctorId;
    private LocalDate visitDate;
    private LocalTime visitTime;
    private String reason;
    private VisitStatus status;
    private String examinationResults;
    private String performedTests;
    private String diagnosis;
    private String prescribedTreatment;
}
