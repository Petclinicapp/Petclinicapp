package bootcamp.petclinic.service;

import bootcamp.petclinic.dto.visit.*;
import bootcamp.petclinic.enums.VisitStatus;
import bootcamp.petclinic.exceptions.UnauthorizedAccessException;
import bootcamp.petclinic.exceptions.VisitNotFoundException;
import bootcamp.petclinic.model.Availability;
import bootcamp.petclinic.model.DoctorSchedule;
import bootcamp.petclinic.model.Visit;
import bootcamp.petclinic.model.VisitDetails;
import bootcamp.petclinic.repository.VisitDetailsRepository;
import bootcamp.petclinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final VisitDetailsRepository visitDetailsRepository;
    private final AuthService authService;
    private final AvailabilityService availabilityService;
    private final DoctorScheduleService doctorScheduleService;

    public VisitResponseDTO createVisit(VisitRequestDTO visitRequestDTO) {
        String userId = authService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"))
                .getUserId();

        List<DoctorSchedule> schedules = doctorScheduleService.getDoctorSchedule(visitRequestDTO.getDoctorId())
                .map(List::of)
                .orElse(List.of());


        Optional<Availability> availableSlot = availabilityService.findAndReserveTimeSlot(
                visitRequestDTO.getDoctorId(),
                visitRequestDTO.getVisitDateTime().toLocalDate(),
                visitRequestDTO.getVisitDateTime().toLocalTime(),
                schedules
        );

        if (availableSlot.isEmpty()) {
            throw new RuntimeException("Selected time slot is not available");
        }

        availabilityService.bookSlot(availableSlot.get());
        doctorScheduleService.updateSchedule(schedules.get(0));

        Visit visit = new Visit();
        visit.setVisitId(UUID.randomUUID().toString());
        visit.setPetId(visitRequestDTO.getPetId());
        visit.setUserId(userId);
        visit.setVisitDateTime(visitRequestDTO.getVisitDateTime());
        visit.setReason(visitRequestDTO.getReason());
        visit.setStatus(VisitStatus.PENDING);

        VisitDetails visitDetails = createVisitDetails(visit.getVisitId());
        visit.setVisitDetailsId(visitDetails.getVisitDetailsId());

        visitRepository.save(visit);

        return new VisitResponseDTO(
                visit.getVisitId(),
                visit.getPetId(),
                visit.getUserId(),
                visit.getVisitDateTime(),
                visit.getReason(),
                visit.getStatus(),
                visit.getVisitDetailsId()
        );
    }

    public VisitDetails createVisitDetails(String visitId) {
        VisitDetails visitDetails = new VisitDetails();
        visitDetails.setVisitDetailsId(UUID.randomUUID().toString());
        visitDetails.setVisitId(visitId);
        visitDetailsRepository.save(visitDetails);
        return visitDetails;
    }

    public Optional<VisitResponseDTO> getVisitById(String visitId) {
        return visitRepository.findById(visitId)
                .map(visit -> new VisitResponseDTO(
                        visit.getVisitId(),
                        visit.getPetId(),
                        visit.getUserId(),
                        visit.getVisitDateTime(),
                        visit.getReason(),
                        visit.getStatus(),
                        visit.getVisitDetailsId()
                ));
    }

    public List<VisitResponseDTO> getAllUserVisits() {
        List<Visit> visits = visitRepository.findAllVisits();

        return visits.stream()
                .map(visit -> new VisitResponseDTO(
                        visit.getVisitId(),
                        visit.getPetId(),
                        visit.getUserId(),
                        visit.getVisitDateTime(),
                        visit.getReason(),
                        visit.getStatus(),
                        visit.getVisitDetailsId()
                )).collect(Collectors.toList());
    }

    public List<VisitResponseDTO> getVisitsByUserId(String userId) {
        return visitRepository.findByUserId(userId)
                .stream()
                .map(visit -> new VisitResponseDTO(
                        visit.getVisitId(),
                        visit.getPetId(),
                        visit.getUserId(),
                        visit.getVisitDateTime(),
                        visit.getReason(),
                        visit.getStatus(),
                        visit.getVisitDetailsId()
                ))
                .collect(Collectors.toList());
    }

    public VisitResponseDTO updateVisit(String visitId, VisitRequestDTO visitRequestDTO) {
        Visit existingVisit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found"));

        boolean isUpdated = false;

        if (visitRequestDTO.getVisitDateTime() != null && !visitRequestDTO.getVisitDateTime().equals(existingVisit.getVisitDateTime())) {
            existingVisit.setVisitDateTime(visitRequestDTO.getVisitDateTime());
            isUpdated = true;
        }

        if (visitRequestDTO.getReason() != null && !visitRequestDTO.getReason().equals(existingVisit.getReason())) {
            existingVisit.setReason(visitRequestDTO.getReason());
            isUpdated = true;
        }

        if (isUpdated) {
            visitRepository.save(existingVisit);
            return new VisitResponseDTO(
                    existingVisit.getVisitId(),
                    existingVisit.getPetId(),
                    existingVisit.getUserId(),
                    existingVisit.getVisitDateTime(),
                    existingVisit.getReason(),
                    existingVisit.getStatus(),
                    existingVisit.getVisitDetailsId()
            );
        } else {
            return new VisitResponseDTO("No changes were detected");
        }
    }

    public Optional<VisitResponseDTO> updateVisitDetails(String visitId, VisitDetailsUpdateDTO visitDetailsUpdateDTO) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found"));

        VisitDetails visitDetails = visitDetailsRepository.findById(visit.getVisitDetailsId())
                .orElseThrow(() -> new VisitNotFoundException("Visit details not found"));

        visitDetails.setExaminationResults(visitDetailsUpdateDTO.getExaminationResults());
        visitDetails.setPerformedTests(visitDetailsUpdateDTO.getPerformedTests());
        visitDetails.setDiagnosis(visitDetailsUpdateDTO.getDiagnosis());
        visitDetails.setPrescribedTreatment(visitDetailsUpdateDTO.getPrescribedTreatment());

        visitDetailsRepository.save(visitDetails);

        return Optional.of(new VisitResponseDTO(
                visit.getVisitId(),
                visit.getPetId(),
                visit.getUserId(),
                visit.getVisitDateTime(),
                visit.getReason(),
                visit.getStatus(),
                visit.getVisitDetailsId()
        ));
    }

    public VisitUpdateResponseDTO updateVisitStatus(String visitId, VisitUpdateRequestDTO visitUpdateRequestDTO) {

        String userRole = String.valueOf(authService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"))
                .getRole());

        if (!userRole.equals("ROLE_DOCTOR")) {
            throw new UnauthorizedAccessException("Only doctors can update the visit status");
        }

        Visit existingVisit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found"));

        if (visitUpdateRequestDTO.getStatus() == null) {
            throw new IllegalArgumentException("New status cannot be null");
        }

        if (!existingVisit.getStatus().equals(VisitStatus.PENDING)) {
            throw new IllegalStateException("Only PENDING visits can be updated");
        }

        if (!(visitUpdateRequestDTO.getStatus().equals(VisitStatus.CONFIRMED) || visitUpdateRequestDTO.getStatus().equals(VisitStatus.CANCELLED))) {
            throw new IllegalArgumentException("Visit can only be updated to ACCEPTED or CANCELLED status");
        }

        existingVisit.setStatus(visitUpdateRequestDTO.getStatus());
        visitRepository.save(existingVisit);

        return new VisitUpdateResponseDTO(
                existingVisit.getStatus(),
                "Status updated"
        );
    }


    public void deleteVisit(String visitId) {
        visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found"));

        visitRepository.deleteVisitById(visitId);
    }
}
