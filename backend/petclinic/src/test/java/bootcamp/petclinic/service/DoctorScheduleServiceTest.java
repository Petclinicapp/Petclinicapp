package bootcamp.petclinic.service;

import bootcamp.petclinic.model.Availability;
import bootcamp.petclinic.model.DoctorSchedule;
import bootcamp.petclinic.repository.DoctorScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorScheduleServiceTest {

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @InjectMocks
    private DoctorScheduleService doctorScheduleService;

    private final String doctorId = "doc-1";


    // ----- Tests for createSchedule() -----

    @Test
    void createSchedule_WhenValidInput_ShouldCreateScheduleSuccessfully() {

        LocalDate startDate = LocalDate.of(2025, 2, 13);
        LocalDate endDate = LocalDate.of(2025, 2, 13);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);
        int intervalMinutes = 30;

        DoctorSchedule schedule = doctorScheduleService.createSchedule(doctorId, startDate, endDate, startTime, endTime, intervalMinutes);

        assertNotNull(schedule);
        assertEquals(doctorId, schedule.getDoctorId());
        assertNotNull(schedule.getAvailableSlots());
        assertEquals(2, schedule.getAvailableSlots().size());

        verify(doctorScheduleRepository, times(1)).save(schedule);
    }

    @Test
    void createSchedule_WhenRepositoryThrowsException_ShouldPropagateException() {

        LocalDate startDate = LocalDate.of(2025, 2, 13);
        LocalDate endDate = LocalDate.of(2025, 2, 13);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);
        int intervalMinutes = 30;

        doThrow(new RuntimeException("DB error"))
                .when(doctorScheduleRepository).save(any(DoctorSchedule.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                doctorScheduleService.createSchedule(doctorId, startDate, endDate, startTime, endTime, intervalMinutes));
        assertTrue(exception.getMessage().contains("DB error"));
    }

    // ----- Tests for updateSchedule() -----

    @Test
    void updateSchedule_WhenValidSchedule_ShouldUpdateSuccessfully() {
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctorId(doctorId);
        schedule.setAvailableSlots(Collections.emptyList());

        doctorScheduleService.updateSchedule(schedule);

        verify(doctorScheduleRepository, times(1)).save(schedule);
    }

    @Test
    void updateSchedule_WhenRepositoryThrowsException_ShouldPropagateException() {

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctorId(doctorId);
        schedule.setAvailableSlots(Collections.emptyList());

        doThrow(new RuntimeException("DB error"))
                .when(doctorScheduleRepository).save(schedule);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            doctorScheduleService.updateSchedule(schedule);
        });
        assertTrue(exception.getMessage().contains("DB error"));
    }

    // ----- Tests for getDoctorSchedule() -----

    @Test
    void getDoctorSchedule_WhenScheduleExists_ShouldReturnSchedule() {

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctorId(doctorId);

        when(doctorScheduleRepository.findById(doctorId)).thenReturn(Optional.of(schedule));

        Optional<DoctorSchedule> result = doctorScheduleService.getDoctorSchedule(doctorId);

        assertTrue(result.isPresent());
        assertEquals(doctorId, result.get().getDoctorId());
    }

    @Test
    void getDoctorSchedule_WhenScheduleDoesNotExist_ShouldReturnEmpty() {

        when(doctorScheduleRepository.findById(doctorId)).thenReturn(Optional.empty());

        Optional<DoctorSchedule> result = doctorScheduleService.getDoctorSchedule(doctorId);

        assertFalse(result.isPresent());
    }

    // ----- Tests for removeSlot() -----

    @Test
    void removeSlot_WhenSlotExists_ShouldRemoveSlotAndReturnTrue() {

        LocalDate date = LocalDate.of(2025, 2, 13);
        LocalTime time = LocalTime.of(10, 0);

        Availability matchingSlot = new Availability(doctorId, date, time, false);

        Availability nonMatchingSlot = new Availability(doctorId, date, LocalTime.of(10, 30), false);
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctorId(doctorId);
        schedule.setAvailableSlots(Arrays.asList(matchingSlot, nonMatchingSlot));

        when(doctorScheduleRepository.findById(doctorId)).thenReturn(Optional.of(schedule));

        boolean result = doctorScheduleService.removeSlot(doctorId, date, time);

        assertTrue(result);

        assertEquals(1, schedule.getAvailableSlots().size());
        assertFalse(schedule.getAvailableSlots().contains(matchingSlot));
        verify(doctorScheduleRepository, times(1)).save(schedule);
    }

    @Test
    void removeSlot_WhenSlotDoesNotExist_ShouldReturnFalse() {

        LocalDate date = LocalDate.of(2025, 2, 13);
        LocalTime time = LocalTime.of(9, 0); // Nėra slot'o šiam laikui.
        Availability slot = new Availability(doctorId, date, LocalTime.of(10, 0), false);
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctorId(doctorId);
        schedule.setAvailableSlots(Collections.singletonList(slot));

        when(doctorScheduleRepository.findById(doctorId)).thenReturn(Optional.of(schedule));

        boolean result = doctorScheduleService.removeSlot(doctorId, date, time);

        assertFalse(result);
        verify(doctorScheduleRepository, never()).save(any(DoctorSchedule.class));
    }

    @Test
    void removeSlot_WhenScheduleDoesNotExist_ShouldReturnFalse() {

        LocalDate date = LocalDate.of(2025, 2, 13);
        LocalTime time = LocalTime.of(10, 0);

        when(doctorScheduleRepository.findById(doctorId)).thenReturn(Optional.empty());

        boolean result = doctorScheduleService.removeSlot(doctorId, date, time);

        assertFalse(result);
        verify(doctorScheduleRepository, never()).save(any(DoctorSchedule.class));
    }
}
