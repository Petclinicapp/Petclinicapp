package bootcamp.petclinic.dto.visit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitRequestDTO {
    private String petId;
    private LocalDateTime visitDateTime;
    private String reason;

    public VisitRequestDTO(LocalDateTime visitDateTime, String reason) {
        this.visitDateTime = visitDateTime;
        this.reason = reason;
    }
}