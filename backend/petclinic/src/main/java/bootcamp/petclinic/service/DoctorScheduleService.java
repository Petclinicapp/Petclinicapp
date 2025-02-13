package bootcamp.petclinic.service;

import bootcamp.petclinic.model.Availability;
import bootcamp.petclinic.model.DoctorSchedule;
import bootcamp.petclinic.repository.DoctorScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorScheduleService {
    private final DoctorScheduleRepository doctorScheduleRepository;

    public DoctorSchedule createSchedule(String doctorId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int intervalMinutes) {
        List<Availability> slots = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalTime time = startTime;
            while (time.isBefore(endTime)) {
                slots.add(new Availability(doctorId, date, time, false));
                time = time.plusMinutes(intervalMinutes);
            }
        }

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctorId(doctorId);
        schedule.setAvailableSlots(slots);
        doctorScheduleRepository.save(schedule);

        return schedule;
    }

    public void updateSchedule(DoctorSchedule schedule) {
        doctorScheduleRepository.save(schedule);
    }

    public Optional<DoctorSchedule> getDoctorSchedule(String doctorId) {
        return doctorScheduleRepository.findById(doctorId);
    }

    public boolean removeSlot(String doctorId, LocalDate date, LocalTime time) {
        Optional<DoctorSchedule> optionalSchedule = doctorScheduleRepository.findById(doctorId);
        if (optionalSchedule.isPresent()) {
            DoctorSchedule schedule = optionalSchedule.get();
            List<Availability> updatedSlots = new ArrayList<>(schedule.getAvailableSlots());
            boolean removed = updatedSlots.removeIf(slot ->
                    slot.getAvailableDate().equals(date) && slot.getAvailableTime().equals(time)
            );

            if (removed) {
                schedule.setAvailableSlots(updatedSlots);
                doctorScheduleRepository.save(schedule);
                return true;
            }
        }
        return false;
    }
}
