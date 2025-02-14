package bootcamp.petclinic.service;

import bootcamp.petclinic.dto.visit.VisitRequestDTO;
import bootcamp.petclinic.dto.visit.VisitResponseDTO;
import bootcamp.petclinic.enums.VisitStatus;
import bootcamp.petclinic.model.Availability;
import bootcamp.petclinic.model.DoctorSchedule;
import bootcamp.petclinic.model.Visit;
import bootcamp.petclinic.model.User;
import bootcamp.petclinic.repository.VisitDetailsRepository;
import bootcamp.petclinic.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VisitServiceTest {

    @Mock
    private VisitRepository visitRepository;

    @Mock
    private VisitDetailsRepository visitDetailsRepository;

    @Mock
    private AvailabilityService availabilityService;

    @Mock
    private DoctorScheduleService doctorScheduleService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private VisitService visitService;

    private Visit visit;
    private VisitRequestDTO visitRequestDTO;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        visitRequestDTO = new VisitRequestDTO();
        setField(visitRequestDTO, "petId", "pet-1");
        setField(visitRequestDTO, "doctorId", "doctor-1");
        setField(visitRequestDTO, "visitDateTime", LocalDateTime.now());
        setField(visitRequestDTO, "reason", "Checkup");

        visit = new Visit(
                UUID.randomUUID().toString(),
                "pet-1",
                "user-1",
                LocalDateTime.now(),
                "Checkup",
                "visit-details-1",
                VisitStatus.PENDING
        );
        testUser = mock(User.class);
        when(testUser.getUserId()).thenReturn("user-1");

        when(doctorScheduleService.getDoctorSchedule(anyString()))
                .thenReturn(Optional.of(mock(DoctorSchedule.class)));
        when(availabilityService.findAndReserveTimeSlot(anyString(), any(), any(), anyList()))
                .thenReturn(Optional.of(mock(Availability.class)));
        doNothing().when(availabilityService).bookSlot(any());

        doNothing().when(visitDetailsRepository).save(any());



    }

    private void setField(Object object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Cannot set field: " + fieldName, e);
        }
    }

    // ---- Tests for createVisit ----

    @Test
    @DisplayName("createVisit_WhenUserAuthenticated_ShouldCreateSuccessfully")
    void createVisit_WhenUserAuthenticated_ShouldCreateSuccessfully() {
        // Teigiamas scenarijus: autentiškas vartotojas, gydytojo tvarkaraštis ir laisva laiko eilutė yra rasti.
        when(authService.getCurrentUser()).thenReturn(Optional.of(testUser));
        doNothing().when(visitRepository).save(any(Visit.class));

        VisitResponseDTO response = visitService.createVisit(visitRequestDTO);

        assertNotNull(response);
        assertEquals("Checkup", response.getReason());
        verify(visitRepository).save(any(Visit.class));
    }


    @Test
    @DisplayName("createVisit_WhenUserNotAuthenticated_ShouldThrowException")
    void createVisit_WhenUserNotAuthenticated_ShouldThrowException() {
        when(authService.getCurrentUser()).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> visitService.createVisit(visitRequestDTO));
    }

    @Test
    @DisplayName("createVisit_WhenDoctorScheduleNotFound_ShouldThrowException")
    void createVisit_WhenDoctorScheduleNotFound_ShouldThrowException() {
        when(authService.getCurrentUser()).thenReturn(Optional.of(testUser));
        when(doctorScheduleService.getDoctorSchedule(anyString())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> visitService.createVisit(visitRequestDTO));
    }

    @Test
    @DisplayName("createVisit_WhenAvailabilityNotFound_ShouldThrowException")
    void createVisit_WhenAvailabilityNotFound_ShouldThrowException() {
        when(authService.getCurrentUser()).thenReturn(Optional.of(testUser));
        when(availabilityService.findAndReserveTimeSlot(anyString(), any(), any(), anyList()))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> visitService.createVisit(visitRequestDTO));
    }

    // ---- Tests for getVisitById ----

    @Test
    @DisplayName("getVisitById_WhenVisitExists_ShouldReturnVisit")
    void getVisitById_WhenVisitExists_ShouldReturnVisit() {
        when(visitRepository.findById(anyString())).thenReturn(Optional.of(visit));

        Optional<VisitResponseDTO> response = visitService.getVisitById(visit.getVisitId());

        assertTrue(response.isPresent());
        assertEquals("Checkup", response.get().getReason());
    }

    @Test
    @DisplayName("getVisitById_WhenVisitNotFound_ShouldReturnEmpty")
    void getVisitById_WhenVisitNotFound_ShouldReturnEmpty() {
        when(visitRepository.findById(anyString())).thenReturn(Optional.empty());

        Optional<VisitResponseDTO> response = visitService.getVisitById("invalid-id");
        assertFalse(response.isPresent());
    }
}
