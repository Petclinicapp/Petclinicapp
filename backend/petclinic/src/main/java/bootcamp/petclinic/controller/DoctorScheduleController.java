package bootcamp.petclinic.controller;

import bootcamp.petclinic.dto.doctorSchedule.DoctorScheduleRequest;
import bootcamp.petclinic.dto.doctorSchedule.RemoveSlotRequest;
import bootcamp.petclinic.model.DoctorSchedule;
import bootcamp.petclinic.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;

    @PostMapping("/create")
    public ResponseEntity<DoctorSchedule> createSchedule(@RequestBody DoctorScheduleRequest request) {
        DoctorSchedule schedule = doctorScheduleService.createSchedule(
                request.getDoctorId(),
                request.getStartDate(),
                request.getEndDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getIntervalMinutes()
        );

        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorSchedule> getDoctorSchedule(@PathVariable String doctorId) {
        Optional<DoctorSchedule> schedule = doctorScheduleService.getDoctorSchedule(doctorId);
        return schedule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{doctorId}/remove")
    public ResponseEntity<String> removeSlot(
            @PathVariable String doctorId,
            @RequestBody RemoveSlotRequest request) {

        boolean removed = doctorScheduleService.removeSlot(doctorId, request.getDate(), request.getTime());

        if (removed) {
            return ResponseEntity.ok("Slot removed successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
