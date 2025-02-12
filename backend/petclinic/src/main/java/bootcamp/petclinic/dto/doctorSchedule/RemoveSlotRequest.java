package bootcamp.petclinic.dto.doctorSchedule;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class RemoveSlotRequest {
    private LocalDate date;
    private LocalTime time;
}
