package bootcamp.petclinic.dto.visit;

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

    public VisitResponseDTO(String visitId, String petId, String userId, LocalDateTime visitDateTime, String reason) {
        this.visitId = visitId;
        this.petId = petId;
        this.userId = userId;
        this.visitDateTime = visitDateTime;
        this.reason = reason;
    }

    public VisitResponseDTO(String message) {
        this.message = message;
    }
}