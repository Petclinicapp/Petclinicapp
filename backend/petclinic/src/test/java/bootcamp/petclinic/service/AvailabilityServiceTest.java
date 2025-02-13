package bootcamp.petclinic.service;

import bootcamp.petclinic.model.Availability;
import bootcamp.petclinic.model.DoctorSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AvailabilityServiceTest {

    private AvailabilityService availabilityService;

    @BeforeEach
    void setUp() {
        availabilityService = new AvailabilityService();
    }

    // ----- Tests for getAvailableTimeSlots() -----

    @Test
    void getAvailableTimeSlots_WhenSlotsAvailable_ShouldReturnOnlyNotBookedSlots() {
        String doctorId = "doc-1";
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctorId(doctorId);

        Availability slot1 = new Availability(doctorId, LocalDate.now(), LocalTime.of(10, 0), false);
        Availability slot2 = new Availability(doctorId, LocalDate.now(), LocalTime.of(10, 30), true);
        schedule.setAvailableSlots(Arrays.asList(slot1, slot2));

        List<DoctorSchedule> schedules = Collections.singletonList(schedule);
        List<Availability> availableSlots = availabilityService.getAvailableTimeSlots(doctorId, schedules);

        assertNotNull(availableSlots);
        assertEquals(1, availableSlots.size());
        assertEquals(slot1, availableSlots.get(0));
    }

    @Test
    void getAvailableTimeSlots_WhenNoSchedulesProvided_ShouldReturnEmptyList() {
        String doctorId = "doc-1";
        List<DoctorSchedule> schedules = Collections.emptyList();
        List<Availability> availableSlots = availabilityService.getAvailableTimeSlots(doctorId, schedules);

        assertNotNull(availableSlots);
        assertTrue(availableSlots.isEmpty());
    }

    @Test
    void getAvailableTimeSlots_WhenDoctorIdDoesNotMatch_ShouldReturnEmptyList() {
        String doctorId = "doc-1";
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctorId("doc-2");
        Availability slot = new Availability("doc-2", LocalDate.now(), LocalTime.of(9, 0), false);
        schedule.setAvailableSlots(Collections.singletonList(slot));

        List<DoctorSchedule> schedules = Collections.singletonList(schedule);
        List<Availability> availableSlots = availabilityService.getAvailableTimeSlots(doctorId, schedules);

        assertNotNull(availableSlots);
        assertTrue(availableSlots.isEmpty());
    }

    // ----- Tests for findAndReserveTimeSlot() -----

    @Test
    void findAndReserveTimeSlot_WhenMatchingSlotExists_ShouldReturnSlot() {
        String doctorId = "doc-1";
        LocalDate targetDate = LocalDate.of(2025, 2, 13);
        LocalTime targetTime = LocalTime.of(10, 0);

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctorId(doctorId);
        Availability matchingSlot = new Availability(doctorId, targetDate, targetTime, false);
        Availability nonMatchingSlot = new Availability(doctorId, targetDate, LocalTime.of(10, 30), false);
        schedule.setAvailableSlots(Arrays.asList(matchingSlot, nonMatchingSlot));

        List<DoctorSchedule> schedules = Collections.singletonList(schedule);
        Optional<Availability> result = availabilityService.findAndReserveTimeSlot(doctorId, targetDate, targetTime, schedules);

        assertTrue(result.isPresent());
        assertEquals(matchingSlot, result.get());
    }

    @Test
    void findAndReserveTimeSlot_WhenSlotIsBooked_ShouldReturnEmpty() {
        String doctorId = "doc-1";
        LocalDate targetDate = LocalDate.of(2025, 2, 13);
        LocalTime targetTime = LocalTime.of(10, 0);

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctorId(doctorId);

        Availability bookedSlot = new Availability(doctorId, targetDate, targetTime, true);
        schedule.setAvailableSlots(Collections.singletonList(bookedSlot));

        List<DoctorSchedule> schedules = Collections.singletonList(schedule);
        Optional<Availability> result = availabilityService.findAndReserveTimeSlot(doctorId, targetDate, targetTime, schedules);

        assertFalse(result.isPresent());
    }

    @Test
    void findAndReserveTimeSlot_WhenNoMatchingSlotFound_ShouldReturnEmpty() {
        String doctorId = "doc-1";
        LocalDate targetDate = LocalDate.of(2025, 2, 13);
        LocalTime targetTime = LocalTime.of(10, 0);

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctorId(doctorId);

        Availability slot = new Availability(doctorId, targetDate, LocalTime.of(9, 0), false);
        schedule.setAvailableSlots(Collections.singletonList(slot));

        List<DoctorSchedule> schedules = Collections.singletonList(schedule);
        Optional<Availability> result = availabilityService.findAndReserveTimeSlot(doctorId, targetDate, targetTime, schedules);

        assertFalse(result.isPresent());
    }

    // ----- Tests for bookSlot() -----

    @Test
    void bookSlot_ShouldSetSlotAsBooked() {
        String doctorId = "doc-1";
        Availability slot = new Availability(doctorId, LocalDate.now(), LocalTime.of(10, 0), false);

        availabilityService.bookSlot(slot);

        assertTrue(slot.isBooked());
    }

    @Test
    void bookSlot_WhenSlotAlreadyBooked_ShouldRemainBooked() {
        String doctorId = "doc-1";
        Availability slot = new Availability(doctorId, LocalDate.now(), LocalTime.of(10, 0), true);

        availabilityService.bookSlot(slot);

        assertTrue(slot.isBooked());
    }
}
