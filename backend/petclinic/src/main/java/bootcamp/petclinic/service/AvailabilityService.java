package bootcamp.petclinic.service;

import bootcamp.petclinic.model.Availability;
import bootcamp.petclinic.model.DoctorSchedule;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class AvailabilityService {

    public Optional<Availability> findAvailableSlot(String doctorId, LocalDate date, LocalTime time, List<DoctorSchedule> schedules) {
        return schedules.stream()
                .filter(schedule -> schedule.getDoctorId().equals(doctorId))
                .flatMap(schedule -> schedule.getAvailableSlots().stream())
                .filter(slot -> slot.getAvailableDate().equals(date) && slot.getAvailableTime().equals(time) && !slot.isBooked())
                .findFirst();
    }

    public void bookSlot(Availability slot) {
        slot.setBooked(true);
    }
}