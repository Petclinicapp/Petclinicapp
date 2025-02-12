package bootcamp.petclinic.dto.visit;

import bootcamp.petclinic.enums.VisitStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitUpdateRequestDTO {

    private VisitStatus status;
}
