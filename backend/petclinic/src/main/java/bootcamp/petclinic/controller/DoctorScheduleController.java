package bootcamp.petclinic.controller;

import bootcamp.petclinic.model.DoctorSchedule;
import bootcamp.petclinic.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<DoctorSchedule> createSchedule(
            @RequestParam String doctorId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam int intervalMinutes) {

        DoctorSchedule schedule = doctorScheduleService.createSchedule(
                doctorId,
                LocalDate.parse(startDate),
                LocalDate.parse(endDate),
                LocalTime.parse(startTime),
                LocalTime.parse(endTime),
                intervalMinutes
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
            @RequestParam String date,
            @RequestParam String time) {

        boolean removed = doctorScheduleService.removeSlot(doctorId, LocalDate.parse(date), LocalTime.parse(time));

        if (removed) {
            return ResponseEntity.ok("Slot removed successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
