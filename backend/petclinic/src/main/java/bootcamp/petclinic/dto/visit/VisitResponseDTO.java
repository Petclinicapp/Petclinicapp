package bootcamp.petclinic.dto.visit;

import bootcamp.petclinic.enums.VisitStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisitResponseDTO {
    private String visitId;
    private String petId;
    private String userId;
    private LocalDateTime visitDateTime;
    private String reason;
    private String message;
    private String visitDetailsId;
    private VisitStatus status;

    public VisitResponseDTO(String visitId, String petId, String userId, LocalDateTime visitDateTime, String reason, VisitStatus status, String visitDetailsId) {
        this.visitId = visitId;
        this.petId = petId;
        this.userId = userId;
        this.visitDateTime = visitDateTime;
        this.reason = reason;
        this.status = status;
        this.visitDetailsId = visitDetailsId;
    }

    public VisitResponseDTO(String message) {
        this.message = message;
    }
}