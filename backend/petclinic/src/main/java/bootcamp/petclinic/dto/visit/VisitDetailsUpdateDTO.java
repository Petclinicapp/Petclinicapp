package bootcamp.petclinic.dto.visit;

import lombok.Data;

@Data
public class VisitDetailsUpdateDTO {
    private String visitId;
    private String examinationResults;
    private String performedTests;
    private String diagnosis;
    private String prescribedTreatment;
}