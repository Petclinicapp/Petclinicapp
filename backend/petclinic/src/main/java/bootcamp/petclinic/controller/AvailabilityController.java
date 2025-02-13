package bootcamp.petclinic.controller;

import bootcamp.petclinic.model.Availability;
import bootcamp.petclinic.model.DoctorSchedule;
import bootcamp.petclinic.service.AvailabilityService;
import bootcamp.petclinic.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;
    private final DoctorScheduleService doctorScheduleService;

    @GetMapping("/all")
    public ResponseEntity<List<Availability>> getAllAvailableSlots(@RequestParam String doctorId) {
        List<DoctorSchedule> schedules = doctorScheduleService.getDoctorSchedule(doctorId)
                .map(List::of)
                .orElse(List.of());

        List<Availability> availableSlots = availabilityService.getAvailableTimeSlots(doctorId, schedules);

        if (availableSlots.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(availableSlots);
    }
}
